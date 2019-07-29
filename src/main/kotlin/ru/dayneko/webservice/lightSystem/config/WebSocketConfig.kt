package ru.dayneko.webservice.lightSystem.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Component
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.config.annotation.EnableWebSocket
import org.springframework.web.socket.config.annotation.WebSocketConfigurer
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry
import org.springframework.web.socket.handler.TextWebSocketHandler
import ru.dayneko.webservice.lightSystem.model.LogFrontendData
import java.util.concurrent.atomic.AtomicLong

/**
 * Компонент хэндлер для реализации работы с вебсокетом
 *
 * @author dayneko_si
 * @since 23.01.2019
 */
@Component
class WebsocketHandler : TextWebSocketHandler() {
    /**
     * Класс для регистрации пользователя при создании сессии
     *
     * @author dayneko_si
     * @since 23.01.2019
     */
    data class User(val id: Long, val name: String)

    /**
     * Метод для уничтожении сессии при дисконекте
     *
     * @param session отключающаяся сессия
     * @param status статус закрытия
     * @throws Exception
     */
    @Throws(Exception::class)
    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        sessionList -= session
    }

    /**
     * Метод для поддержки hand shake'a
     *
     * @param session подключаемая сессия
     * @param message сообщение при подключении
     */
    public override fun handleTextMessage(session: WebSocketSession?, message: TextMessage?) {
        try {
            ObjectMapper().readTree(message?.payload).apply {
                when (this.get("type").asText()) {
                    "join" -> sessionList[session!!] = User(uids.getAndIncrement(), this.get("data").asText())
                    else -> throw Exception("Ошибка при инициализации сессии веб сокета")
                }
            }
        } catch (e: Exception) {
            throw Exception("Произошла ошибка при инициализации сессии с сообшением: ${e.message}")
        }
    }

    /**
     * Метод для реализации отправки по номеру сессии на сторону клиента
     *
     * @param session сессия для отправки
     * @param data данные для отправки
     */
    fun emit(session: WebSocketSession, data: LogFrontendData) = session.sendMessage(TextMessage(jacksonObjectMapper().writeValueAsString(data)))

    /**
     * Изначальный метод для отправки данных на фронт
     *
     * @param data данные для отправки на фронт
     */
    fun broadcast(data: LogFrontendData) = sessionList.forEach { emit(it.key, data) }

    /**
     * Компаньон ообъект для хранение разделямой сессии
     *
     * @author dayneko_si
     * @since 23.01.2019
     */
    companion object {
        /** Создание уникального UID для идентификатора сессии **/
        var uids        = AtomicLong(0)

        /** Хранилище сессий откртых в сокете **/
        val sessionList = HashMap<WebSocketSession, User>()
    }
}

/**
 * Настройки вебсокетов
 *
 * @author dayneko_si
 * @since 23.01.2019
 */
@Configuration
@EnableWebSocket
class WebSocketConfig: WebSocketConfigurer {
    /**
     * Создание эндпоинта для работы с вебсокетом
     *
     * @param registry обработтчик событий вебсокета
     */
    override fun registerWebSocketHandlers(registry: WebSocketHandlerRegistry) {
        registry.addHandler(WebsocketHandler(), "/v1/light/stream").setAllowedOrigins("*").withSockJS()
    }
}