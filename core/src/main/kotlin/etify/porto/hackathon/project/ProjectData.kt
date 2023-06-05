package etify.porto.hackathon.project

import jakarta.persistence.*
import java.util.*

data class ProjectDto(
    val id: UUID,
    val name: String,
    val logo: String,
    val description: String,
    val status: ProjectStatus,
    val websiteURL: String,
    val twitterURL: String,
    val telegramURL: String,
    val mediumURL: String,
    val tokenContractAddress: String,
    val tokenList: List<TokenDto>
)

data class CreateProjectCommand(
    val name: String,
    val status: ProjectStatus,
    val logo: String,
    val description: String,
    val websiteURL: String,
    val twitterURL: String,
    val telegramURL: String,
    val mediumURL: String,
    val tokenContractAddress: String,
    val tokenList: List<CreateTokenCommand>
)

data class TokenDto(
    val id: UUID,
    val name: String,
    val tokenAddress: String,
    val symbol: String,
    val chain: Chain,
    val logo: String
) {
    constructor(name: String, tokenAddress: String, symbol: String, chain: Chain, logo: String) :
            this(UUID.randomUUID(), name, tokenAddress, symbol, chain, logo)
}

data class CreateTokenCommand(
    val name: String,
    val tokenAddress: String,
    val symbol: String,
    val chain: Chain,
    val logo: String
)

@Entity
@Table(name = "projects", schema = "porto")
data class Project(
    @Id
    @Column(name = "id")
    val id: UUID,

    @Column(name = "name")
    val name: String,

    @Column(name = "logo")
    val logo: String,

    @Column(name = "description")
    val description: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    val status: ProjectStatus,

    @Column(name = "website_url")
    val websiteURL: String,

    @Column(name = "twitter_url")
    val twitterURL: String,

    @Column(name = "telegram_url")
    val telegramURL: String,

    @Column(name = "medium_url")
    val mediumURL: String,

    @Column(name = "token_contract_address")
    val tokenContractAddress: String,

    @OneToMany(mappedBy = "project", fetch = FetchType.EAGER, cascade = [CascadeType.ALL])
    var tokens: MutableList<Token> = mutableListOf()
)

@Entity
@Table(name = "tokens", schema = "porto")
data class Token(
    @Id
    @Column(name = "id")
    val id: UUID,

    @Column(name = "name")
    val name: String,

    @Column(name = "token_address")
    val tokenAddress: String,

    @Column(name = "symbol")
    val symbol: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "chain")
    val chain: Chain,

    @ManyToOne
    @JoinColumn(name = "project_id")
    val project: Project,

    @Column(name = "logo")
    val logo: String,
)

enum class ProjectStatus {
    VERIFIED,
    NOT_VERIFIED,
    IN_PROGRESS
}

enum class Chain(val key: String) {
    BSC("bsc"),
    ETH("eth"),
    MATIC("pol"),
    AVAX("ava"),
    GNOSIS("dai")
}