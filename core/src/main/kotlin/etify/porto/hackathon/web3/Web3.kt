package etify.porto.hackathon.web3

import etify.porto.hackathon.dfns.DfnsService
import etify.porto.hackathon.lifi.TransactionRequest
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.web3j.abi.FunctionEncoder
import org.web3j.abi.FunctionReturnDecoder
import org.web3j.abi.TypeReference
import org.web3j.abi.datatypes.Address
import org.web3j.abi.datatypes.Function
import org.web3j.abi.datatypes.Type
import org.web3j.abi.datatypes.generated.Uint256
import org.web3j.crypto.Credentials
import org.web3j.crypto.RawTransaction
import org.web3j.crypto.TransactionEncoder
import org.web3j.protocol.Web3j
import org.web3j.protocol.core.DefaultBlockParameterName
import org.web3j.protocol.core.methods.request.Transaction
import org.web3j.protocol.http.HttpService
import org.web3j.tx.TransactionManager
import org.web3j.tx.response.PollingTransactionReceiptProcessor
import org.web3j.utils.Numeric
import java.lang.Exception
import java.math.BigInteger
import java.util.*

interface Web3 {
    fun sendTransaction(publicKey: String, transactionRequest: TransactionRequest)
    fun getBalance(contractAddress: String, owner: String): BigInteger
}

@Service
class Web3Impl(
    @Value("\${web3.rpc}") private val rpc: String,
    @Value("\${mantle.private-key}") private val privateKey: String,
    private val dfnsService: DfnsService
) : Web3 {
    private final val client = Web3j.build(HttpService(rpc, false))
    private val wallet = Credentials.create(privateKey)

    private val poller = PollingTransactionReceiptProcessor(
        client,
        TransactionManager.DEFAULT_POLLING_FREQUENCY,
        TransactionManager.DEFAULT_POLLING_ATTEMPTS_PER_TX_HASH
    )

    override fun sendTransaction(publicKey: String, transactionRequest: TransactionRequest) {
        val nonce = client.ethGetTransactionCount(transactionRequest.from, DefaultBlockParameterName.LATEST)
            .send()
            .transactionCount

        val transaction = RawTransaction.createTransaction(
            transactionRequest.chainId.toLong(),
            nonce,
            BigInteger(transactionRequest.gasLimit, 16),
            transactionRequest.to,
            BigInteger(transactionRequest.value, 16),
            transactionRequest.data,
            BigInteger(transactionRequest.gasPrice, 16),
            BigInteger(transactionRequest.gasLimit, 16)
        )

        val encodedTransaction = TransactionEncoder.encode(transaction)
        val signedTransaction = dfnsService.sign(publicKey, encodedTransaction)

        val tx = client.ethSendRawTransaction(Numeric.toHexString(signedTransaction)).send()
        poller.waitForTransactionReceipt(tx.transactionHash)
    }

    override fun getBalance(contractAddress: String, owner: String): BigInteger {
        val function = Function(
            "balanceOf",
            listOf<Type<*>>(Address(owner)),
            listOf<TypeReference<*>>(object : TypeReference<Uint256?>() {})
        )
        val encoded = FunctionEncoder.encode(function)
        val transaction = Transaction.createEthCallTransaction(wallet.address, contractAddress, encoded)
        val result = client.ethCall(transaction, DefaultBlockParameterName.LATEST).send()

        return try {
            FunctionReturnDecoder.decode(result.value, function.outputParameters).first().value as BigInteger
        } catch (ex: Exception) {
            BigInteger.ZERO
        }
    }
}
