package etify.porto.hackathon.account

import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface AccountRepository : JpaRepository<Account, UUID> {
    fun findByVaultId(vaultId: String): Optional<Account>
}
