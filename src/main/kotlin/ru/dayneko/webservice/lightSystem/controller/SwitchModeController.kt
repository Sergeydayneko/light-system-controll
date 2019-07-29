package ru.dayneko.webservice.lightSystem.controller

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ru.dayneko.webservice.lightSystem.model.CommonResponse
import ru.dayneko.webservice.lightSystem.model.LightState
import ru.dayneko.webservice.lightSystem.service.LightModeService

/**
 * Контроллер для переключения режима света
 *
 * @author dayneko_si
 * @since 10.01.2019
 */
@CrossOrigin
@RestController
@RequestMapping("/v1/mode")
@Api(tags = ["Контроллер для переключения режимов освещения"])
class SwitchModeController(
    /** Сервис для работы с режимами **/
    val lightModeService: LightModeService
) {
    /**
     * Обновление режима света
     *
     * @param newLightMode - режим света
     * @return ResponseEntity со статусом обновления режима
     */
    @ApiOperation("Обновление режима освещения")
    @PutMapping("/switch")
    fun switchMode(@RequestBody newLightMode: String): ResponseEntity<out Any> {
        val switchData = lightModeService.updateMode(newLightMode)
        return when(switchData) {
            is Throwable -> ResponseEntity(CommonResponse<LightState>("Произошла ошибка при переключении режима освещения", emptyList()), HttpStatus.INTERNAL_SERVER_ERROR)
            else         -> ResponseEntity(CommonResponse(newLightMode, switchData), HttpStatus.OK)
        }
    }

}