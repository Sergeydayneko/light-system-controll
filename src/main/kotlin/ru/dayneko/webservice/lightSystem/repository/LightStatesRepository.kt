package ru.dayneko.webservice.lightSystem.repository

import ru.dayneko.webservice.lightSystem.model.LightState

/**
 * Репозиторий состояния света
 *
 * @author dayneko_si
 * @since  22.01.2019
 */
interface LightStatesRepository {

    /**
     * Метод для нахождения всех объектов
     * в коллекции состояния света.
     *
     * @return коллекция объектлв состояния света
     */
    fun findAll(): List<LightState>

    /**
     * Метод для нахождения объекта света по rpId
     *
     * @param rpId - номер rpId для поиска
     * @return объект освещения
     */
    fun findByRpId(rpId: Int): LightState

    /**
     * Обновление состояния света по rpId
     *
     * @param rpId  - номер rpId
     * @param state - новое состояние освещения
     */
    fun updateStateByRpId(rpId: Int, state: Int)
}

