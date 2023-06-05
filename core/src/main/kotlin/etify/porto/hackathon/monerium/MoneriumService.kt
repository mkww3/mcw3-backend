package etify.porto.hackathon.monerium

import etify.porto.hackathon.monerium.data.AddAccountAddressData
import etify.porto.hackathon.monerium.data.MoneriumAccount
import etify.porto.hackathon.monerium.data.PatchAccountData
import etify.porto.hackathon.monerium.data.PatchTreasuryData
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.web3j.utils.Numeric
import java.util.*

interface MoneriumService {
    fun createAccount(
        accountAddress: String,
        signedBytes: ByteArray
    ): MoneriumAccount
}

@Service
class MoneriumServiceImpl(
    @Value("\${monerium.authorization}") private val authorization: String,
    @Value("\${monerium.profile-id}") private val profileId: String,
    private val client: MoneriumClient
) : MoneriumService {
    override fun createAccount(
        accountAddress: String,
        signedBytes: ByteArray
    ): MoneriumAccount {
        val header = buildAuthorizationHeader()

        val body = AddAccountAddressData(
            address = accountAddress,
            signature = Numeric.toHexString(signedBytes)
        )

        client.addAccountAddress(header, profileId, body)
        val account = client.getAccounts(header, profileId)
            .filter { it.address == accountAddress }
            .filter { it.currency == "eur" }
            .filter { it.chain == "ethereum" }
            .filter { it.network == "goerli" }
            .firstOrNull() ?: throw IllegalStateException("No account found.")

        val accountData = client.patchAccountData(
            authorizationHeader = header,
            accountId = account.id,
            body = PatchAccountData("3cd75adc-c1dd-11ed-a042-2a5f7fc2c676")
        )

        client.patchTreasuryData(
            authorizationHeader = header,
            treasureId = "3cd75adc-c1dd-11ed-a042-2a5f7fc2c676",
            body = PatchTreasuryData(accountData.id)
        )

        return MoneriumAccount(
            account.id,
            account.address,
            // Monerium API doesn't allow for multiple IBAN allows for only two, have to ask for this functionality
            "IS13 2635 6907 1360 2643 7306 84"
        )
    }

    private fun buildAuthorizationHeader() = "Basic $authorization"
}

