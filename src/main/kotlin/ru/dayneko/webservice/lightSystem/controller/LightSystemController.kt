package ru.dayneko.webservice.lightSystem.controller

import arrow.core.Some
import arrow.core.Try
import arrow.core.getOrElse
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ru.dayneko.webservice.lightSystem.exceptions.ServiceErrorException
import ru.dayneko.webservice.lightSystem.exceptions.ServiceErrorResponse
import ru.dayneko.webservice.lightSystem.model.LightModeInit
import ru.dayneko.webservice.lightSystem.model.LightState
import ru.dayneko.webservice.lightSystem.service.LightModeService
import ru.dayneko.webservice.lightSystem.service.LightStatesService

/**
 * Контроллер данных по свету
 *
 * @author dayneko_si
 * @since  24.01.2019
 */
@RestController
@RequestMapping(value = ["/v1/light"])
@CrossOrigin
@Api(tags = ["Контроллер данных по свету"])
class LightSystemController(
        private val lightStatesService: LightStatesService,
        private val lightModeService: LightModeService
) {
    /** Логирование **/
    private val logger = LoggerFactory.getLogger(LightSystemController::class.java)

    /**
     * Получение состояния света в комнатах
     *
     * @return ResponseEntity с данными по состоянию света
     */
    @GetMapping("/system")
    @ApiOperation("Получение состояния света в комнатах")
    fun get(): ResponseEntity<out Any> = Try {
        Some(LightModeInit(lightModeService.getCurrentLightMode(), lightStatesService.getStates()))
                .fold({ ResponseEntity.ok(emptyList<LightState>()) }, {ResponseEntity.ok(it) })
    }.getOrElse {
        when(it) {
            is ServiceErrorException -> {
                logger.error("Произошла ошибка при получения данных по свету. Сообщение: ${it.message}")
                ResponseEntity.status(HttpStatus.CONFLICT).body(ServiceErrorResponse("${it.message}. Ошибка получения данных"))
            }
            else -> {
                logger.error("Произошла ошибка при получения данных по свету. Сообщение: ${it.message}")
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Exception("${it.message}. Ошибка сервиса"))
            }
        }
    }
}