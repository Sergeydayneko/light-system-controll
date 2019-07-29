package ru.dayneko.webservice.lightSystem.controller

import arrow.core.Try
import arrow.core.getOrElse
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.dayneko.webservice.lightSystem.exceptions.ServiceErrorException
import ru.dayneko.webservice.lightSystem.exceptions.ServiceErrorResponse
import ru.dayneko.webservice.lightSystem.repository.RoomsInfoRepository

/**
 * @author dayneko_si
 * @since  24.01.2019
 */
@RestController
@RequestMapping(value = ["/v1/roomsInfo"])
@CrossOrigin
@Api(tags = ["Контроллер информации о комнатах"])
class RoomsInfoController(
        /** Репозиторий комнат **/
        @Autowired
        private  val roomsInfoRepository: RoomsInfoRepository
) {
    /** Логирование **/
    private val logger = LoggerFactory.getLogger(RoomsInfoController::class.java)

    /**
    * Контроллер получения информация по комнатам
    *
    * @return ResponseEntity с информацией по комнатам
    */
    @GetMapping
    @ApiOperation("Получение информации о комнатах")
    fun get(): ResponseEntity<out Any> =
        Try { ResponseEntity.ok(roomsInfoRepository.findAll()) }.getOrElse {
            when(it) {
                is ServiceErrorException -> {
                    logger.error("Произошла ошибка при получения данных. Сообщение: ${it.message}")
                    ResponseEntity.status(HttpStatus.CONFLICT).body(ServiceErrorResponse("${it.message}. Ошибка получения данных"))
                }
                else -> {
                    logger.error("Произошла ошибка при получения данных. Сообщение: ${it.message}")
                    ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Exception("${it.message}. Ошибка сервиса"))
                }
            }
        }
}