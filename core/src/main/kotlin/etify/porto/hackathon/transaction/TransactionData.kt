package etify.porto.hackathon.transaction

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.OffsetDateTime
import java.util.*

data class TransactionDto(
    val id: UUID,
    val date: OffsetDateTime,
    val tokenAmount: Double,
    val tokenId: UUID,
    val accountId: UUID,
    val symbol: String,
    val logo: String,
    val projectName: String,
    val tokenName: String
)

data class CreateTransactionCommand(
    val tokenAmount: Double,
    val tokenId: UUID
)

data class TokenBalanceDto(
    val id: UUID,
    val projectId: UUID,
    val projectName: String,
    val name: String,
    val symbol: String,
    val tokenAmount: Double,
    val logo: String
)

@Entity
@Table(name = "transactions", schema = "porto")
data class Transaction(
    @Id
    @Column(name = "id")
    val id: UUID,

    @Column(name = "date")
    val date: OffsetDateTime,

    @Column(name = "token_amount")
    val tokenAmount: Double,

    @Column(name = "token_id")
    val tokenId: UUID,

    @Column(name = "account_id")
    val accountId: UUID,

    @Column(name = "symbol")
    val symbol: String,

    @Column(name = "logo")
    val logo: String,

    @Column(name = "project_name")
    val projectName: String,

    @Column(name = "token_name")
    val tokenName: String
)