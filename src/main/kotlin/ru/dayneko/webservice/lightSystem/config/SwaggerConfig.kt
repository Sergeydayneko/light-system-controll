package ru.dayneko.webservice.lightSystem.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.service.ApiInfo
import springfox.documentation.service.Contact
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2

@Configuration
@EnableSwagger2
class SwaggerConfig {

    /**
     * Настройка сваггера
     */
    @Bean
    fun api(): Docket =
            Docket(DocumentationType.SWAGGER_2)
                    .select()
                    .apis(RequestHandlerSelectors.basePackage("ru.dayneko.webservice.lightSystem.controller"))
                    .paths(PathSelectors.any())
                    .build()
                    .apiInfo(getApiInfo())
                    .useDefaultResponseMessages(false)

    /**
     * Описание сервиса
     */
    private fun getApiInfo(): ApiInfo =
            ApiInfo(
                    "LIGHT SYSTEM API",
                    "Light service module",
                    "2.1.0",
                    "http://www.dayneko.ru/",
                    Contact("sdayneko", "http://localhost/", "sergeydayneko@mail.ru"),
                    "sdayneko",
                    "http://localhost/", ArrayList())
}