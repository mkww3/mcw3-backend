package etify.porto.hackathon.dfns

import etify.porto.hackathon.account.Account
import etify.porto.hackathon.dfns.data.*
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.web3j.crypto.Sign
import org.web3j.utils.Numeric
import java.util.HexFormat

interface DfnsService {
    fun createAssetAccount(): AssetAccount
    fun getAssetAccount(id: String): AssetAccount
    fun sign(publicKeyId: String, messageBytes: ByteArray): ByteArray
    fun walletConnect(walletConnect: WalletConnectData)
}

@Service
class DfnsServiceImpl(
    @Value("\${dfns.authorization}") private val authorization: String,
    private val client: DfnsClient
) : DfnsService {
    companion object {
        private const val SYMBOL = "EURE.ETH"
        private const val HEX_PREFIX = "0x"
    }

    override fun createAssetAccount(): AssetAccount {
        val header = buildAuthorizationHeader()
        val createMessage = client.createAssetAccount(
            authorizationHeader = header,
            body = CreateAssetAccountData(SYMBOL)
        )

        while (getAssetAccount(createMessage.id).status != "Enabled") {
            Thread.sleep(2500)
        }
        return getAssetAccount(createMessage.id)
    }

    override fun getAssetAccount(id: String): AssetAccount {
        val header = buildAuthorizationHeader()
        return client.getAssetAccount(authorizationHeader = header, assetAccountId = id)
    }


    override fun sign(publicKeyId: String, messageBytes: ByteArray): ByteArray {
        val header = buildAuthorizationHeader()
        val message = SignMessageData(Numeric.toHexString(Sign.getEthereumMessageHash(messageBytes)))
        val response = client.signMessage(
            authorizationHeader = header,
            publicKeyId = publicKeyId,
            body = message
        )

        while (client.getSignMessage(header, publicKeyId, response.id).status != "Executed") {
            Thread.sleep(2500)
        }

        return client.getSignMessage(header, publicKeyId, response.id).toByteArray()
    }

    override fun walletConnect(walletConnect: WalletConnectData) {
        val account = SecurityContextHolder.getContext().authentication.principal as? Account
            ?: throw IllegalStateException("Account doesn't exists or session token is not set.")
        val header = buildAuthorizationHeader()

        client.walletConnect(
            header,
            account.publicKey,
            walletConnect
        )
    }

    private fun SignMessageResponse.toByteArray(): ByteArray {
        val r = HexFormat.of().parseHex(r.removePrefix(HEX_PREFIX))
        val s = HexFormat.of().parseHex(s.removePrefix(HEX_PREFIX))
        val v = 0.toByte()

        val signedBytes = ByteArray(65)

        System.arraycopy(r, 0, signedBytes, 0, 32)
        System.arraycopy(s, 0, signedBytes, 32, 32)
        signedBytes[64] = v

        return signedBytes
    }

    private fun buildAuthorizationHeader() = "Bearer $authorization"
}

