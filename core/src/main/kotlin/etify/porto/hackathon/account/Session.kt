package etify.porto.hackathon.account

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.OffsetDateTime
import java.util.UUID

@Entity
@Table(name = "sessions", schema = "porto")
data class Session(
    @Id
    @Column(name = "id")
    val id: UUID,

    @Column(name = "account_id")
    val account: UUID,

    @Column(name = "token")
    val token: UUID,

    @Column(name = "expiry")
    val expiry: OffsetDateTime
)