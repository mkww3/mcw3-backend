package etify.porto.hackathon.account

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(name = "accounts", schema = "porto")
class Account(
    @Id
    @Column(name = "id")
    val id: UUID,

    @Column(name = "vault_id")
    val vaultId: String,

    @Column(name = "iban")
    val iban: String,

    @Column(name = "public_key")
    val publicKey: String,

    @Column(name = "wallet_address")
    val walletAddress: String
)
