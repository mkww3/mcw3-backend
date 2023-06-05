package etify.porto.hackathon.account

data class LoginCommand(
    val vaultId: String,
    val secret: String?
)