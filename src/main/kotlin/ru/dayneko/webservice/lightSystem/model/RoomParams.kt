package ru.dayneko.webservice.lightSystem.model

/**
 * Параметры комнаты
 *
 * @author dayneko_si
 * @since  19.01.2019
 */
data class RoomParams (
        /** Комната **/
        val title: String,

        /** Ид-к комнаты **/
        val rpId: Int,

        /** Координаты на фронте **/
        val svgPath: String? = null
)