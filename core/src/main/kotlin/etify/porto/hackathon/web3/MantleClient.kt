package etify.porto.hackathon.web3

import etify.porto.hackathon.dfns.DfnsService
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.web3j.abi.FunctionEncoder
import org.web3j.abi.datatypes.Function
import org.web3j.abi.datatypes.Utf8String
import org.web3j.crypto.Credentials
import org.web3j.crypto.RawTransaction
import org.web3j.crypto.TransactionEncoder
import org.web3j.protocol.Web3j
import org.web3j.protocol.core.DefaultBlockParameterName
import org.web3j.protocol.http.HttpService
import org.web3j.tx.RawTransactionManager
import org.web3j.tx.TransactionManager
import org.web3j.tx.gas.DefaultGasProvider
import org.web3j.tx.response.PollingTransactionReceiptProcessor
import org.web3j.utils.Numeric
import java.math.BigInteger

interface MantleClient {
    fun createProject(projectId: String)
    fun fund(address: String, amount: String, publicKey: String, sender: String)
}

@Service
class MantleClientImpl(
    @Value("\${mantle.rpc}") private val rpc: String,
    @Value("\${mantle.private-key}") private val privateKey: String,
    private val dfnsService: DfnsService
) : MantleClient {
    companion object {
        private const val PROJECT_ADDRESS = "0xe5431dfE8623622111424E0d42cAD8552E67F2D0"
        private const val CHAIN_ID = 5001L
    }

    private val client = initializeClient()
    private val wallet = initializeWallet()
    private val transactionManager = RawTransactionManager(client, wallet, CHAIN_ID)
    private val poller = PollingTransactionReceiptProcessor(
        client,
        TransactionManager.DEFAULT_POLLING_FREQUENCY,
        TransactionManager.DEFAULT_POLLING_ATTEMPTS_PER_TX_HASH
    )

    private fun initializeClient(): Web3j {
        val service = HttpService(rpc, false)
        return Web3j.build(service)
    }

    private fun initializeWallet(): Credentials {
        return Credentials.create(privateKey)
    }

    override fun createProject(projectId: String) {
        val function = Function(
            "createProject",
            listOf(Utf8String(projectId)),
            listOf()
        )

        val encoded = FunctionEncoder.encode(function)

        val transaction = transactionManager.sendTransaction(
            BigInteger.valueOf(1),
            DefaultGasProvider.GAS_LIMIT,
            PROJECT_ADDRESS,
            encoded,
            BigInteger.ZERO
        )

        poller.waitForTransactionReceipt(transaction.transactionHash)
    }

    override fun fund(address: String, amount: String, publicKey: String, sender: String) {
        val nonce = client.ethGetTransactionCount(sender, DefaultBlockParameterName.LATEST)
            .send()
            .transactionCount

        val function = Function(
            "fund",
            listOf(),
            listOf()
        )

        val encodedFunction = FunctionEncoder.encode(function)

        val transaction = RawTransaction.createTransaction(
            nonce,
            BigInteger.valueOf(1),
            DefaultGasProvider.GAS_LIMIT,
            address,
            BigInteger.ZERO,
            encodedFunction
        )

        val encodedTransaction = TransactionEncoder.encode(transaction)
        val signedTransaction = dfnsService.sign(publicKey, encodedTransaction)

        val tx = client.ethSendRawTransaction(Numeric.toHexString(signedTransaction)).send()
        poller.waitForTransactionReceipt(tx.transactionHash)
    }
}