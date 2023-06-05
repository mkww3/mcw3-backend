package etify.porto.hackathon.dfns

import etify.porto.hackathon.dfns.data.WalletConnectData
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class WalletConnectEndpoint(
    private val dfnsService: DfnsService
) {

    @PostMapping("/api/wallet-connect")
    fun walletConnect(@RequestBody walletConnectData: WalletConnectData) {
        dfnsService.walletConnect(walletConnectData)
    }
}