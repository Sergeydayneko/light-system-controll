package ru.dayneko.webservice.lightSystem.service

import ru.dayneko.webservice.lightSystem.model.LightModeInit
import ru.dayneko.webservice.lightSystem.model.LightState
import ru.dayneko.webservice.lightSystem.model.RoomParams

/**
 * Сервис для работы с освещением
 *
 * @author dayneko_si
 * @since 23.01.2019
 */
interface LightStatesService {
    /**
     * Метод получение настроек состояния комнат по освещению
     *
     * @return коллекцию данных по комнатам
     */
    fun getStates(): List<LightState>

    /**
     * Множество rpId, который находится в коллекции состояния комнат
     *
     * @return множеств rpId
     */
    fun lightRpIds(): Set<Int>

    /**
     * Метод определение включенного
     * света в комнате
     *
     * @param room - проверяемая комната
     * @return true - свет включен, false - свет выключен
     */
    fun isLightOn(room: RoomParams): Boolean
}