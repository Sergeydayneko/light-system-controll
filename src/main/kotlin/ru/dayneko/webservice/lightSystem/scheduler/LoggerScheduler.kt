package ru.dayneko.webservice.lightSystem.scheduler

import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import ru.dayneko.webservice.lightSystem.config.WebsocketHandler
import ru.dayneko.webservice.lightSystem.service.LightStateLogsService
import java.time.LocalDateTime
import java.time.ZoneId

/**
 * Шедуллер для проверки изменения логов включения света
 *
 * @author dayneko_si
 * @since 23.01.2019
 */
@Component
class LoggerScheduler(
        /** Сервис для работы с логами по освещению **/
        private val lightStateLogService: LightStateLogsService,

        /** Хэндлер для работы с вебсокетами **/
        private val socketHandler: WebsocketHandler
) {
    /** Метод обработки данных **/
    @Scheduled(fixedRate = 5000, initialDelay = 1000)
    fun handleLightLogsInWorkTime() {
        socketHandler.broadcast(lightStateLogService.getLogsState())
    }
}