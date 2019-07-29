package ru.dayneko.webservice.lightSystem.model

/**
 * Объект для загрузки изнчального состояния системы освещения
 *
 * @author dayneko_si
 * @since 23.01.2019
 */
data class LightModeInit(
        /** Глобальный режим освещения **/
        val lightMode: String,

        /** Информация по комнатам **/
        val roomsData: List<LightState>
)