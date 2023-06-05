package etify.porto.hackathon.dfns.data

data class SignMessageData(
    val hash: String
)


data class SignMessageResponse(
    val id: String,
    val status: String,
    val r: String,
    val s: String,
    val recid: Int
)