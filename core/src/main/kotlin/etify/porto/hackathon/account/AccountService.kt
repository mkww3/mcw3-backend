package etify.porto.hackathon.account

import etify.porto.hackathon.dfns.DfnsService
import etify.porto.hackathon.dfns.data.AssetAccount
import etify.porto.hackathon.monerium.MoneriumService
import etify.porto.hackathon.monerium.data.MoneriumAccount
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.time.OffsetDateTime
import java.util.*

interface AccountService {
    fun login(command: LoginCommand): SessionDto
}

@Service
class AccountServiceImpl(
    private val accountRepository: AccountRepository,
    private val dfnsService: DfnsService,
    private val moneriumService: MoneriumService,
    private val passwordEncoder: PasswordEncoder,
    private val sessionRepository: SessionRepository
) : AccountService {
    companion object {
        private val MESSAGE = "I hereby declare that I am the address owner.".encodeToByteArray()
    }

    private fun createAccount(vaultId: String): Account {
        val assetAccount = dfnsService.createAssetAccount()

        val signedMessage = dfnsService.sign(
            publicKeyId = assetAccount.publicKey ?: throw IllegalStateException("Public key has not been created."),
            messageBytes = MESSAGE
        )

        val moneriumAccount = moneriumService.createAccount(
            accountAddress = assetAccount.address ?: throw IllegalStateException("Address has not been created."),
            signedBytes = signedMessage
        )


        val account = Account(
            id = UUID.randomUUID(),
            vaultId = vaultId,
            iban = moneriumAccount.iban,
            publicKey = assetAccount.publicKey ?: throw IllegalStateException("Public key has not been created."),
            walletAddress = assetAccount.address ?: throw IllegalStateException("Address has not been created.")
        )

        return accountRepository.save(account)
    }

    override fun login(command: LoginCommand): SessionDto {
        val account = accountRepository.findByVaultId(command.vaultId).orElseGet { createAccount(command.vaultId) }

        val session = Session(
            id = UUID.randomUUID(),
            account = account.id,
            token = UUID.randomUUID(),
            expiry = OffsetDateTime.now().plusDays(1)
        )
        sessionRepository.save(session)

        return SessionDto(
            id = session.id,
            account = account.id,
            token = session.token,
            expiry = session.expiry,
            iban = account.iban
        )
    }
}

data class SessionDto(
    val id: UUID,
    val account: UUID,
    val iban: String,
    val token: UUID,
    val expiry: OffsetDateTime
)