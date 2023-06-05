package etify.porto.hackathon.lifi

import etify.porto.hackathon.config.FeignConfiguration
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.cloud.openfeign.SpringQueryMap
import org.springframework.web.bind.annotation.GetMapping

@FeignClient(
    value = "lifiClient",
    url = "https://li.quest/v1",
    configuration = [FeignConfiguration::class]
)
interface LifiClient {
    @GetMapping("/quote")
    fun quote(
        @SpringQueryMap params: QuoteParams
    ): QuoteResponse
}

data class QuoteResponse(
    val id: String,
    val transactionRequest: TransactionRequest
)

data class QuoteParams(
    val fromChain: String,
    val toChain: String,
    val fromToken: String,
    val toToken: String,
    val fromAddress: String,
    val fromAmount: String
)

data class TransactionRequest(
    val data: String,
    val to: String,
    val value: String,
    val from: String,
    val chainId: Int,
    val gasPrice: String,
    val gasLimit: String
)