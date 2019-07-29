package ru.dayneko.webservice.lightSystem.service

import ru.dayneko.webservice.lightSystem.model.LightState

/**
 * Интерфейс сервиса для работы с режимами
 *
 * @author dayneko_si
 * @since 10.01.2019
 */
interface LightModeService {
    /**
     * Ообновление режима света
     *
     * @param mode - режим света
     * @return статус состояния обновления режима
     */
    fun updateMode(mode: String): List<LightState>

    /**
     * Получение текущего режима света
     *
     * @return строчное представление режима освещения
     */
    fun getCurrentLightMode(): String
}