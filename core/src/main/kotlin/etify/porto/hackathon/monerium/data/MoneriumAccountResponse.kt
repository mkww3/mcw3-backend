package etify.porto.hackathon.monerium.data

data class MoneriumAccountResponse(
    val id: String,
    val address: String,
    val currency: String,
    val chain: String,
    val network: String
)