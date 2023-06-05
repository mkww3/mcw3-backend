package etify.porto.hackathon.lifi

import etify.porto.hackathon.project.Token
import etify.porto.hackathon.web3.Web3
import org.springframework.stereotype.Service

interface LifiService {
    fun swap(
        token: Token,
        publicKey: String,
        address: String,
        amount: String
    )
}

@Service
class LifiServiceImpl(
    private val lifiClient: LifiClient,
    private val web3: Web3
) : LifiService {
    override fun swap(
        token: Token,
        publicKey: String,
        address: String,
        amount: String
    ) {
        val params = QuoteParams(
            fromChain = token.chain.key,
            toChain = token.chain.key,
            fromToken = "EURe",
            toToken = token.symbol,
            fromAmount = amount,
            fromAddress = address
        )
        val response = lifiClient.quote(params)

        web3.sendTransaction(publicKey, response.transactionRequest)
    }
}