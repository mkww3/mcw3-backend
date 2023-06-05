package etify.porto.hackathon.dfns

import etify.porto.hackathon.config.FeignConfiguration
import etify.porto.hackathon.dfns.data.*
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader


@FeignClient(
    value = "dfnsClient",
    url = "https://api.dfns.ninja/",
    configuration = [FeignConfiguration::class]
)
interface DfnsClient {

    @PostMapping("/assets/asset-accounts")
    fun createAssetAccount(
        @RequestHeader("Authorization") authorizationHeader: String,
        @RequestBody body: CreateAssetAccountData
    ): CreateAssetAccount

    @GetMapping("/assets/asset-accounts/{assetAccountId}")
    fun getAssetAccount(
        @RequestHeader("Authorization") authorizationHeader: String,
        @PathVariable assetAccountId: String
    ): AssetAccount

    @PostMapping("/public-keys/{publicKeyId}/signatures")
    fun signMessage(
        @RequestHeader("Authorization") authorizationHeader: String,
        @PathVariable publicKeyId: String,
        @RequestBody body: SignMessageData
    ): SignMessageResponse

    @GetMapping("/public-keys/{publicKeyId}/signatures/{signatureId}")
    fun getSignMessage(
        @RequestHeader("Authorization") authorizationHeader: String,
        @PathVariable publicKeyId: String,
        @PathVariable signatureId: String
    ): SignMessageResponse

    @PostMapping("/public-keys/{publicKeyId}/walletconnect-session")
    fun walletConnect(
        @RequestHeader("Authorization") authorizationHeader: String,
        @PathVariable publicKeyId: String,
        @RequestBody body: WalletConnectData
    )
}
