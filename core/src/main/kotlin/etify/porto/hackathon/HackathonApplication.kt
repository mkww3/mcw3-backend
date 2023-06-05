package etify.porto.hackathon

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients

@SpringBootApplication
@EnableFeignClients
class HackathonApplication

fun main(args: Array<String>) {
    runApplication<HackathonApplication>(*args)
}
