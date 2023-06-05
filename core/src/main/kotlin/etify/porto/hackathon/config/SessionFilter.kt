package etify.porto.hackathon.config

import etify.porto.hackathon.account.AccountRepository
import etify.porto.hackathon.account.SessionRepository
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.GenericFilterBean
import java.util.UUID

@Component
class SessionFilter(
    private val sessionRepository: SessionRepository,
    private val accountRepository: AccountRepository
) : GenericFilterBean() {
    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        try {
            if (request is HttpServletRequest) {
                val token = request.getHeader("Authorization") ?: return
                val session = sessionRepository.findByToken(UUID.fromString(token)) ?: return
                val account = accountRepository.findById(session.account).orElse(null)
                SecurityContextHolder.getContext().authentication =
                    UsernamePasswordAuthenticationToken(account, null, listOf())
            }
        } catch (ex: Exception) {
            println("Exception during session filtering.")
        } finally {
            chain.doFilter(request, response)
        }
    }
}