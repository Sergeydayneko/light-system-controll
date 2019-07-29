package ru.dayneko.webservice.lightSystem.controller

import arrow.core.*
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ru.dayneko.webservice.lightSystem.exceptions.ServiceErrorException
import ru.dayneko.webservice.lightSystem.exceptions.ServiceErrorResponse
import ru.dayneko.webservice.lightSystem.model.CommonResponse
import ru.dayneko.webservice.lightSystem.model.LightResponse
import ru.dayneko.webservice.lightSystem.service.SwitchLightService

import ru.dayneko.webservice.lightSystem.model.LightSwitchHistory
import ru.dayneko.webservice.lightSystem.service.JSchService
import ru.dayneko.webservice.lightSystem.utils.lightSwitchHistoryInformation

import ru.dayneko.webservice.lightSystem.utils.reverseState
import java.util.*

/**
 * Переключение света
 *
 * @author dayneko_si
 * @since  23.01.2019
 */
@RestController
@RequestMapping(value = ["/v1/state"])
@CrossOrigin
@Api(tags = ["Переключение света"])
class SwitchStateController(
        /** Сервис тестовой информации **/
        private val switchLightService: SwitchLightService
) {
    /** Логирование **/
    private val logger: Logger = LoggerFactory.getLogger(SwitchStateController::class.java)

    /**
     * Переключение света в комнате
     *
     * @param rpId - номер ид-к ММДУИ
     * @return ResponseEntity<Any> ответ
     */
    @GetMapping(value = ["/switch"])
    @ApiOperation("Переключение света по номеру raspberry Pi")
    fun switchLight(@RequestParam rpId: Int,
                    @RequestParam state: Int): ResponseEntity<out Any> = Try {
        lightSwitchHistoryInformation.set(LightSwitchHistory(date = Date(), state = state, descriptor = "Переключение режима в комнате с портала"))
        Some(switchLightService.turnLightByRpId(rpId))
                .fold( { throw ServiceErrorException("Не удалось обновить режим освещения") },
                       { ResponseEntity.ok(LightResponse(rpId, state.reverseState(), "Режима переключен")) } )
    }.getOrElse {
        when(it) {
            is ServiceErrorException -> {
                logger.error("Ошибка записи данных. Сообщение: ${it.message}")
                ResponseEntity.status(HttpStatus.CONFLICT).body(ServiceErrorResponse("${it.message}. Ошибка записи данных"))
            }
            else -> {
                logger.error("Произошла ошибка сервиса при получения данных. Сообщение: ${it.message}")
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Exception("${it.message}. Ошибка сервиса"))
            }
        }
    }

    /**
     * Переключение света на всем этаже
     *
     * @param floor - номер этаж
     * @return ResponseEntity<Any> ответ сервиса
     */
    @PutMapping(value = ["/turn"])
    @ApiOperation("Переключение света на этаже")
    fun put(@RequestParam floor: Int, @RequestParam state: Int): ResponseEntity<out Any> =
            Try {
                lightSwitchHistoryInformation.set(LightSwitchHistory(date = Date(), state = state.reverseState(), descriptor = "Выключение света на этаже с портала"))
                val currentLightStatesCollection = switchLightService.switchLightOnFloor(floor, state)
                ResponseEntity.ok(CommonResponse("Свет на $floor этаже переключен в состояние $state", currentLightStatesCollection) )
            }.getOrElse {
                    when(it) {
                        is ServiceErrorException -> {
                            logger.error("Ошибка переключения света на этаже. Сообщение: ${it.message}")
                            ResponseEntity.status(HttpStatus.CONFLICT).body(ServiceErrorResponse("${it.message}. Ошибка записи данных"))
                        }
                        else -> {
                            logger.error("Произошла ошибка сервиса при получения данных. Сообщение: ${it.message}")
                            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Exception("${it.message}. Ошибка сервиса"))
                        }
                    }
            }
}