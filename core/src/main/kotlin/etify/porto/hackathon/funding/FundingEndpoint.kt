package etify.porto.hackathon.funding

import etify.porto.hackathon.account.Account
import etify.porto.hackathon.web3.MantleClient
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class FundingEndpoint(
    private val mantleClient: MantleClient
) {
    @PostMapping("/api/fund")
    fun fund(@RequestBody command: FundCommand) {
        val account = SecurityContextHolder.getContext().authentication.principal as? Account
            ?: throw IllegalStateException("Account doesn't exists or session token is not set.")

        mantleClient.fund(command.address, command.amount, account.publicKey, account.walletAddress)
    }
}

data class FundCommand(
    val address: String,
    val amount: String
)

