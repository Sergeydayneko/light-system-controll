package ru.dayneko.webservice.lightSystem.repository

import ru.dayneko.webservice.lightSystem.model.GlobalConfig

/**
 * Интерфейс расширенного репозитория для получения настроек
 * глобального конфига
 *
 * @author dayneko_si
 * @since 25.01.2019
 */
interface GlobalConfRepositoryExtended {
    /**
     * Получение глобального конфига
     *
     * @return коллекцию массивов, но по факту возвращается один элемент
     */
    fun findAll(): List<GlobalConfig>

    /**
     * Обновление режима освещения
     * глобального конфмга
     *
     * @param mode - новый режим освещения
     */
    fun updateGlobalLightMode(mode: String)
}