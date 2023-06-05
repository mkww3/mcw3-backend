package etify.porto.hackathon.config

import feign.Feign
import feign.Logger
import feign.slf4j.Slf4jLogger
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class FeignConfiguration {
    @Bean
    fun client(): Feign.Builder = Feign.builder()
        .client(feign.okhttp.OkHttpClient())
        .logger(Slf4jLogger())
        .logLevel(Logger.Level.FULL)
}