package ru.dayneko.webservice.lightSystem.config

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import ru.dayneko.webservice.lightSystem.config.interceptors.AuthCheckHandler

/**
 * Класс настройки веб-компонентов приложения.
 * В данном случае использую для регистрации
 * перехватчиков запроса для проверки токена
 * авторизации.
 *
 * @author dayneko_si
 * @since 25.02.2019
 */
@Configuration
class WebConfig (
        /** Компонент интерсептора авторизации **/
        private val authInterceptor: AuthCheckHandler
) : WebMvcConfigurer {

    /**
     * Метод для добавления кастомных перехватчиков запроса
     *
     * @param registry - объект для регистрации перехватчиков запроса
     */
    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(authInterceptor)
    }
}