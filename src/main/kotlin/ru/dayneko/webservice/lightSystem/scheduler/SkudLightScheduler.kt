package ru.dayneko.webservice.lightSystem.scheduler

import arrow.instances.list.foldable.forAll
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import ru.dayneko.webservice.lightSystem.repository.RoomsInfoRepository
import ru.dayneko.webservice.lightSystem.repository.SysLogRepository
import net.minidev.json.JSONObject
import ru.dayneko.webservice.lightSystem.service.LightStatesService
import ru.dayneko.webservice.lightSystem.service.SwitchLightService
import com.fasterxml.jackson.databind.ObjectMapper
import ru.dayneko.webservice.lightSystem.model.*
import ru.dayneko.webservice.lightSystem.utils.lightSwitchHistoryInformation
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*
import kotlin.NoSuchElementException

/**
 * Шедуллер для проверки наличия людей в помещении
 * @author dayneko_si
 * @since 20.02.2019
 */
@Component
class SkudLightScheduler (
        @Value("\${employee.presence.url}")
        private val employeePresenceUrl: String,
        private val roomsRepository: RoomsInfoRepository,
        private val sysLogRepository: SysLogRepository,
        private val switchLightService: SwitchLightService,
        private val lightStatesService: LightStatesService
) {
    private val logger     = LoggerFactory.getLogger(SkudLightScheduler::class.java)
    private val jsonMapper = ObjectMapper()

    private val roomNumberRegex                 = Regex("""\d{3}""")
    private val EMPLOYEES_ARE_IN_ROOM_STATE     = 1
    private val EMPLOYEES_ARE_NOT_IN_ROOM_STATE = 0
    private val NO_ROOM_NUMBER                  = "NO_ROOM_NUMBER"
    private val ROOM_DONT_HAVE_PRESENCE_SENSOR  = "[]"

    private fun initRooms(): List<RoomParamsWithRoomNumber> {
        /* Соотношение номеров комнат с их полной инфорамцией */
        fun correlateRoomNumbers(roomParameters: RoomParams): RoomParamsWithRoomNumber =
                RoomParamsWithRoomNumber(roomParameters, roomNumberRegex.find(roomParameters.title)?.value ?: NO_ROOM_NUMBER)

        return roomsRepository.findAll()
                .fold(listOf<RoomParams>()) { acc, next -> acc + next.rooms }
                .map(::correlateRoomNumbers)
        }

    /**
     * Шедуллер для проверки выключенного
     * состояния света после ухода сотрудников
     */
    @Scheduled(fixedRate = 300_000, initialDelay = 10_000)
    fun checkEmployeePresenceInWorkingTime() {
        if (isNowIsWorkTime())
            checkThenSwitchRoomState()
    }

    @Scheduled(fixedRate = 3_600_000)
    fun checkEmployeePresenceInNotWorkingTime() {
        if (!isNowIsWorkTime()) {
            checkThenSwitchRoomState()
        }
    }

    /**
     * Проверка нахождения сотрудников в помещении и
     * переключение режима освещения, если режим
     * свет включен, а сотрудники в помещении
     * отсутствуют
     */
    private fun checkThenSwitchRoomState() {
        // todo вынести в AOP
        lightSwitchHistoryInformation.set(LightSwitchHistory(date = Date(), state = EMPLOYEES_ARE_NOT_IN_ROOM_STATE, descriptor = "Переключение света при отсутствии сотрудников"))
        logger.info("Starting to check employee presence in the rooms... ")

        val lastRoomsStates = lightStatesService.getStates()

        fun isLastStatusIsEmployeesAreInRoom(roomRpId: Int): Boolean {
             val lastRoomState = lastRoomsStates.first { it.rpId == roomRpId }.state
             return isRoomHasEmployeesAreInRoomStatus(lastRoomState)
        }

        try {
            initRooms().asSequence()
                    .map(::getEmployeesPresenceInRoom)
                    .filter(::isRoomDontHavePresenceSensor)
                    .map(::getCurrentEmployeesPresenceState)
                    .filter { it.areEmployeesIsNotInRoom && isLastStatusIsEmployeesAreInRoom(it.rpId) }
                    .forEach { switchLightByRpId(it.rpId); saveViolationEventLog(it.rpId) }

            logger.info("Employee presence and corresponding room's state is checked successfully")
        } catch (ex: NoSuchElementException) {
            logger.info("All rooms are not empty")
        } catch (ex: Exception) {
            logger.error("Error has been occurred while trying to check empty rooms. Message: ${ex.message}")
        }
    }

    /**
     * Проверка на присутствие людей в комнате
     *
     * @param roomParam - JSON объект, содержащий:
     * key: идентификатор сотрудника (цифровое обозначени)
     * value: статус присутствия ("1" - присутствует, "0" - отсутствует)
     *
     * @return статус проверки пустая ли комната
     */
    private fun isRoomEmpty(roomParam: JSONObject?) = roomParam!!.values.map { it.toString() }.forAll { it == "0" }
    private fun getEmployeesPresenceInRoom(roomsInfo: RoomParamsWithRoomNumber) =
            EmployeesPresenceServerResponse(roomsInfo.roomParams.rpId, getInformationAboutEmployeePresenceFromServer(roomsInfo.roomNumber))

    private fun getInformationAboutEmployeePresenceFromServer(roomNumber: String) = RestTemplate().getForObject("""$employeePresenceUrl$roomNumber""", String::class.java)
    private fun isRoomDontHavePresenceSensor(responseFromServer: EmployeesPresenceServerResponse) = responseFromServer.serverResponse != ROOM_DONT_HAVE_PRESENCE_SENSOR
    private fun getCurrentEmployeesPresenceState(responseFromServer: EmployeesPresenceServerResponse) =
            EmployeePresenceState(responseFromServer.rpId, isRoomEmpty(jsonMapper.readValue(responseFromServer.serverResponse, JSONObject::class.java)))

    private fun switchLightByRpId(rpId: Int) = switchLightService.turnLightByRpId(rpId)
    private fun saveViolationEventLog(rpId: Int) = sysLogRepository.save(SystemLogging(null, rpId, "В помещении был оставлен свет после ухода", Date()))
    private fun isRoomHasEmployeesAreInRoomStatus(lastRoomState: Int) = lastRoomState == EMPLOYEES_ARE_IN_ROOM_STATE

    private val isNowIsWorkTime: () -> Boolean = {
        val currentDayAndTime = LocalDateTime.now(ZoneId.of("GMT+3"))
        currentDayAndTime.
                isBefore(LocalDateTime.of(currentDayAndTime.year, currentDayAndTime.month, currentDayAndTime.dayOfMonth, 20, 0))
                .and(currentDayAndTime.isAfter(LocalDateTime.of(currentDayAndTime.year, currentDayAndTime.month, currentDayAndTime.dayOfMonth, 8, 0)))
    }
}
