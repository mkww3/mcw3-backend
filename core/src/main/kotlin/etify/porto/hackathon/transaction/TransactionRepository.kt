package etify.porto.hackathon.transaction

import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface TransactionRepository : JpaRepository<Transaction, UUID> {

}