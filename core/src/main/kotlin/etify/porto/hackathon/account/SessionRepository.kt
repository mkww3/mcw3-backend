package etify.porto.hackathon.account

import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface SessionRepository: JpaRepository<Session, UUID> {
    fun findByToken(token: UUID): Session?
}