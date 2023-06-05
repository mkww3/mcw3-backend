package etify.porto.hackathon.dfns.data

data class CreateAssetAccount(
    val id: String,
    val status: String
)

data class AssetAccount(
    val id: String,
    val status: String,
    val address: String?,
    val publicKey: String?
)