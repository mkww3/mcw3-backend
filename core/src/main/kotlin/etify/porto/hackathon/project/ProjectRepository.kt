package etify.porto.hackathon.project

import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface ProjectRepository: JpaRepository<Project, UUID> {
}