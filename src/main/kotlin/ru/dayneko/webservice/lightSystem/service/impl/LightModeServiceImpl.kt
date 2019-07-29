package ru.dayneko.webservice.lightSystem.service.impl

import arrow.core.Try
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import ru.dayneko.webservice.lightSystem.model.LightState
import ru.dayneko.webservice.lightSystem.model.LightSwitchHistory
import ru.dayneko.webservice.lightSystem.model.RoomParams
import ru.dayneko.webservice.lightSystem.repository.GlobalConfRepositoryExtended
import ru.dayneko.webservice.lightSystem.service.LightModeService
import ru.dayneko.webservice.lightSystem.service.LightStatesService
import ru.dayneko.webservice.lightSystem.service.RoomsService
import ru.dayneko.webservice.lightSystem.service.SwitchLightService
import ru.dayneko.webservice.lightSystem.utils.crossBy
import ru.dayneko.webservice.lightSystem.utils.lightSwitchHistoryInformation
import java.lang.Exception
import java.util.*

/**
 * Прокси сервис для работы с переключением режима
 *
 * @author dayneko_si
 * @since 10.01.2019
 */
@Service
class LightModeServiceImpl (
        private val switchLightService: SwitchLightService,
        private val globalConfigRepository: GlobalConfRepositoryExtended,
        private val roomsService: RoomsService,
        private val lightStatesService: LightStatesService
): LightModeService {
    /** Логгер **/
    private val logger = LoggerFactory.getLogger(LightModeServiceImpl::class.java)
    /** Режим энергосбережения **/
    private val SAVING_MODE = "saving"

    /** Рабочий режим **/
    private val WORKING_MODE = "working"

    /** Регулярное выражение для нечетных коридоров **/
    private val ODD_CORRIDOR_REGEX = """Коридор \d*[13579]$""".toRegex()

    /** Регулярное выражение для четных коридоров **/
    private val EVEN_CORRIDOR_REGEX = """Коридор \d*[02468]$""".toRegex()

    /**
     * Ообновление режима света
     *
     * @param mode - режим света
     * @return статус состояния обновления режима
     */
    override fun updateMode(mode: String): List<LightState> = Try {
        when(mode) {
            SAVING_MODE  -> setSavingMode()
            WORKING_MODE -> setWorkingMode()
            else         -> throw Exception("Неверный режим освещения")
        }}.fold({ throw Exception("Произошла ошибка при переключении режима освещения ") }, {
        /* Этот костыль необходимый для получения актуальных данных после отправвки сигнала на raspberry */
        Thread.sleep(2000)
        lightStatesService.getStates()
    })

    /**
     * Получение текущего режима света
     *
     * @return строчное представление режима освещения
     */
    override fun getCurrentLightMode(): String = globalConfigRepository.findAll().first().globalLightMode

    /**
     * Обработка запроса на переключение режима
     * сохранения энергии
     */
    private fun setSavingMode() {
        lightSwitchHistoryInformation.set(LightSwitchHistory(date = Date(), state = 0, descriptor = "Переключение режима освещения на \"Энергосберегающий\""))
        roomsWithLightState().filterNot { ODD_CORRIDOR_REGEX.matches(it.title) }
                .filter(lightStatesService::isLightOn)
                .forEach(::handleSwitch)
                .also { globalConfigRepository.updateGlobalLightMode(SAVING_MODE) }
    }

    /**
     * Обработка запроса на переключение
     * рабочего режима
     */
    private fun setWorkingMode() {
        lightSwitchHistoryInformation.set(LightSwitchHistory(date = Date(), state = 1, descriptor = "Переключение режима освещения на \"Рабочий режим\""))
        roomsWithLightState().filter {EVEN_CORRIDOR_REGEX.matches(it.title)}
                .filterNot(lightStatesService::isLightOn)
                .forEach(::handleSwitch)
                .also {globalConfigRepository.updateGlobalLightMode(WORKING_MODE)}
    }

    /**
     * Нахождение всех комнат, которые содержар состояние света
     *
     * @return функция определения
     */
    private val roomsWithLightState: () -> List<RoomParams> = {
        roomsService.getAllRooms().crossBy(lightStatesService.lightRpIds()) {
            room, states -> states.contains(room.rpId)
        }
    }

    /**
     * Переключение режима в сервисе обработки света
     *
     * @param room - обрабатываемый объект света
     */
    private fun handleSwitch(room: RoomParams) {
        logger.info("Starting to turn the light for room = ${room.title}")
        switchLightService.turnLightByRpId(room.rpId)
    }
}