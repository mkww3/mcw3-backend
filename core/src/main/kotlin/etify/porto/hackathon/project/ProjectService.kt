package etify.porto.hackathon.project

import etify.porto.hackathon.web3.MantleClient
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.util.*

interface ProjectService {
    fun getProjects(): List<ProjectDto>
    fun getProject(projectId: UUID): ProjectDto
    fun postProjects(command: CreateProjectCommand): ProjectDto
    fun putProjects(projectId: UUID, command: CreateProjectCommand): ProjectDto
}

@Service
class ProjectServiceImpl(
    private val projectRepository: ProjectRepository,
    private val mantleClient: MantleClient
) : ProjectService {

    override fun getProjects(): List<ProjectDto> {
        return projectRepository.findAll().map { it.toDto() }
    }

    override fun getProject(projectId: UUID): ProjectDto {
        val response = projectRepository.findByIdOrNull(projectId) ?: throw NoSuchElementException()
        return response.toDto()
    }

    override fun postProjects(command: CreateProjectCommand): ProjectDto {
        val newProject = Project(
            id = UUID.randomUUID(),
            name = command.name,
            logo = command.logo,
            description = command.description,
            status = command.status,
            websiteURL = command.websiteURL,
            twitterURL = command.twitterURL,
            telegramURL = command.telegramURL,
            mediumURL = command.mediumURL,
            tokenContractAddress = command.tokenContractAddress,
        )
        newProject.tokens = command.tokenList.map { it.toDomain(newProject) }.toMutableList()
        mantleClient.createProject(newProject.id.toString())
        projectRepository.save(newProject)
        return newProject.toDto()
    }

    private fun CreateTokenCommand.toDomain(project: Project): Token {
        return Token(UUID.randomUUID(), name, tokenAddress, symbol, chain, project, logo)
    }

    override fun putProjects(projectId: UUID, command: CreateProjectCommand): ProjectDto {
        val newProject = Project(
            id = projectId,
            name = command.name,
            logo = command.logo,
            description = command.description,
            status = command.status,
            websiteURL = command.websiteURL,
            twitterURL = command.twitterURL,
            telegramURL = command.telegramURL,
            mediumURL = command.mediumURL,
            tokenContractAddress = command.tokenContractAddress
        )
        newProject.tokens = command.tokenList.map { it.toDomain(newProject) }.toMutableList()
        projectRepository.save(newProject)
        return newProject.toDto()

    }

    private fun Project.toDto(): ProjectDto {
        return ProjectDto(
            id = id,
            name = name,
            logo = logo,
            description = description,
            status = status,
            websiteURL = websiteURL,
            twitterURL = twitterURL,
            telegramURL = telegramURL,
            mediumURL = mediumURL,
            tokenContractAddress = tokenContractAddress,
            tokenList = tokens.map { it.toDto() }
        )
    }

    private fun ProjectDto.toDomain(): Project {
        val project = Project(
            id = id,
            name = name,
            logo = logo,
            description = description,
            status = status,
            websiteURL = websiteURL,
            twitterURL = twitterURL,
            telegramURL = telegramURL,
            mediumURL = mediumURL,
            tokenContractAddress = tokenContractAddress
        )
        project.tokens = tokenList.map { it.toDomain(project) }.toMutableList()
        return project
    }

    private fun Token.toDto(): TokenDto {
        return TokenDto(
            id = id,
            name = name,
            tokenAddress = tokenAddress,
            symbol = symbol,
            chain = chain,
            logo = logo
        )
    }

    private fun TokenDto.toDomain(project: Project): Token {
        return Token(
            id = UUID.randomUUID(),
            name = name,
            tokenAddress = tokenAddress,
            symbol = symbol,
            chain = chain,
            project = project,
            logo = logo
        )
    }
}

