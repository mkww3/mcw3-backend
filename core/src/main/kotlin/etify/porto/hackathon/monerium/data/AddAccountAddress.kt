package etify.porto.hackathon.monerium.data

data class AddAccountAddressData(
    val address: String,
    val signature: String,
    val message: String = "I hereby declare that I am the address owner.",
    val accounts: List<AccountData> = listOf(AccountData())
)

data class AccountData(
    val currency: String = "eur",
    val chain: String = "ethereum",
    val network: String = "goerli",
    val isVisible: Boolean = true
)

data class AddAccountAddressResponse(
    val id: String
)