package ru.dayneko.webservice.lightSystem.service.impl

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import ru.dayneko.webservice.lightSystem.model.LightState
import ru.dayneko.webservice.lightSystem.model.SystemConfig
import ru.dayneko.webservice.lightSystem.repository.SysInfoRepository
import ru.dayneko.webservice.lightSystem.service.JSchService
import ru.dayneko.webservice.lightSystem.service.LightStatesService
import ru.dayneko.webservice.lightSystem.service.RoomsService
import ru.dayneko.webservice.lightSystem.service.SwitchLightService

import ru.dayneko.webservice.lightSystem.utils.zipByPredicate

/**
 * Переключение света
 *
 * @author dayneko_si
 * @since  23.01.2019
 */
@Service
class SwitchLightServiceImpl(
        private val repositorySysInfo: SysInfoRepository,
        private val lightStatesService: LightStatesService,
        private val roomsService: RoomsService,
        private val jschService: JSchService
): SwitchLightService {
    /** Логирование **/
    private val logger = LoggerFactory.getLogger(SwitchLightServiceImpl::class.java)

    /**
     * Вкл/выкл света.
     * Данная функция, помимо выключения по ssh, так же
     * обновляет состояния света в базе данных.
     *
     * @param rpId - к комнаты
     * @return значение света (1 - вкл, 0 - выкл)
     */
    override fun turnLightByRpId(rpId: Int) {
        return try {

            repositorySysInfo.findAll()
                    .find { sysConfPredicate(it, rpId) }!!.rpIp
                    ?.let(jschService::switchRaspberryPiByRpIp)
                    ?: throw Exception("Empty config for rpId = $rpId")
        } catch (e: Exception) {
            logger.error("Can`t turn light for this rpId: $rpId. Message: ${e.message}")
        }
    }

    /**
     * Переключение света на всем этаже
     *
     * @param floor - Int этаж
     * @return String ответ сервиса
     */
    override fun switchLightOnFloor(floor: Int, state: Int): List<LightState> {
       try {
           val lightStates  = lightStatesService.getStates()
           val roomsByFloor =  roomsService.getRoomsByFloor(floor)

           roomsByFloor.zipByPredicate(lightStates
                   , {room, roomState -> room.rpId == roomState.rpId && roomState.state != state}
                   , {room, _ -> room.rpId}
                   ).forEach(this::turnLightByRpId)
        } catch (e: Exception) {
            val err = "Ошибка при переключении света на этаже $floor"
            logger.error(err, e)
        } finally {
           Thread.sleep(2000)
           return lightStatesService.getStates()
        }
    }

    /**
     * Предикат для нахождение значения в коллекции
     *
     * @param systemConf - объект системного конфига
     * @param rpId    - необходимый id распбери
     */
    private fun sysConfPredicate(systemConf: SystemConfig, rpId: Int): Boolean =
            systemConf.rpId == rpId &&
               systemConf.port != 0 &&
               systemConf.switch != null

}