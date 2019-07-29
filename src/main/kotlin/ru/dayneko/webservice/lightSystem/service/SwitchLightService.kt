package ru.dayneko.webservice.lightSystem.service

import ru.dayneko.webservice.lightSystem.model.LightState

/**
 * Интерфейс сервиса для работы со светом
 *
 * @author dayneko_si
 * @since  23.01.2019
 */
interface SwitchLightService {
    /**
     * Метод для выключения света по Ssh
     *
     * @param rpId - номер id распбери
     */
    fun turnLightByRpId(rpId: Int)

    /**
     * Вкл/выкл свет на всем этаже
     *
     * @param floor - номер этажа
     * @return String ответ сервиса
     */
    fun switchLightOnFloor(floor: Int, state: Int): List<LightState>
}