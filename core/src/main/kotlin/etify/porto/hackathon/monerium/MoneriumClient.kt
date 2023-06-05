package etify.porto.hackathon.monerium

import etify.porto.hackathon.config.FeignConfiguration
import etify.porto.hackathon.monerium.data.*
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.*

@FeignClient(
    value = "moneriumClient",
    url = "https://sandbox.monerium.dev/api",
    configuration = [FeignConfiguration::class]
)
interface MoneriumClient {
    @PostMapping("emoney/profiles/{profileId}/addresses")
    fun addAccountAddress(
        @RequestHeader("Authorization") authorizationHeader: String,
        @PathVariable profileId: String,
        @RequestBody body: AddAccountAddressData
    ): AddAccountAddressResponse

    @GetMapping("emoney/profiles/{profileId}/accounts")
    fun getAccounts(
        @RequestHeader("Authorization") authorizationHeader: String,
        @PathVariable profileId: String,
    ): List<MoneriumAccountResponse>

    @PatchMapping("emoney/accounts/{accountId}")
    fun patchAccountData(
        @RequestHeader("Authorization") authorizationHeader: String,
        @PathVariable accountId: String,
        @RequestBody body: PatchAccountData
    ): PatchAccountResponse

    @PatchMapping("treasury/accounts/{treasureId}")
    fun patchTreasuryData(
        @RequestHeader("Authorization") authorizationHeader: String,
        @PathVariable treasureId: String,
        @RequestBody body: PatchTreasuryData
    )
}



