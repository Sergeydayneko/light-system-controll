package ru.dayneko.webservice.lightSystem.service.impl

import org.springframework.stereotype.Service
import ru.dayneko.webservice.lightSystem.model.LightState
import ru.dayneko.webservice.lightSystem.model.RoomParams
import ru.dayneko.webservice.lightSystem.repository.LightStatesRepository
import ru.dayneko.webservice.lightSystem.service.LightStatesService

/**
 * Имплементация сервиса для работы с состояниями освещениям
 *
 * @author dayneko_si
 * @since 23.01.2019S
 */
@Service
class LightStatesServiceImpl(
        private val lightStatesRepository: LightStatesRepository
): LightStatesService {
    override fun getStates(): List<LightState> = lightStatesRepository.findAll()
    override fun lightRpIds(): Set<Int> = lightStatesRepository.findAll().map { it.rpId }.toSet()
    override fun isLightOn(room: RoomParams): Boolean = getStates().find { it.rpId == room.rpId }.let { it!!.state == 1 }
}