package ru.dayneko.webservice.lightSystem

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication(scanBasePackages = ["ru.dayneko.webservice.lightSystem"])
@EnableScheduling
class LightSystemApplication

/**
 * Стартовый метод приложения
 *
 * @param args аргументы
 */
fun main(args: Array<String>) {
    runApplication<LightSystemApplication>(*args)
}
