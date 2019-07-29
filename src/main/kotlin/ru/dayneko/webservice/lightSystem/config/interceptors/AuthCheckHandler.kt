package ru.dayneko.webservice.lightSystem.config.interceptors

import arrow.core.Try
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Интерсептор проверки
 * авторизированных пользователей
 *
 * @author dayneko_si
 * @since 25.02.2019
 */
@Component
class AuthCheckHandler (
        /** URL для проверка валидности токена **/
        @Value("\${auth.token-handler}")
        private val tokenCheckServerUrl: String
) : HandlerInterceptorAdapter() {
    /** Объект логгера **/
    private val logger = LoggerFactory.getLogger(AuthCheckHandler::class.java)

    /**
     * Инициализация перехватчика запроса для
     * проверки токена авторизации.
     *
     * @param request  - объект серверного запроса со стороны клиента
     * @param response - объекта серверного ответа на сторону клиента
     * @param handler  - объект обработчика
     */
    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean =
        when(tokenValidationCheck(request)) {
             true  -> true
             false -> { response.status = 401; false }
        }

    /**
     * Метод проверки валидности передаваемого токена
     *
     * @param request - объект серверного запроса со стороны клиента
     * @return состояние проверки токена
     */
    private fun tokenValidationCheck(request: HttpServletRequest): Boolean =
             Try { RestTemplate().exchange(tokenCheckServerUrl
                    , HttpMethod.GET
                    , HttpEntity<Any>(HttpHeaders().apply { set("x-authorization", request.getHeader("x-authorization")) } )
                    , Boolean::class.java)
             }.fold (
                        { logger.error("An error has been occurred with message: ${it.message}"); false },
                        { true}
                    )
}