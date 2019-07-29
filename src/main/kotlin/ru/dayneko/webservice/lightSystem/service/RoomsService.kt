package ru.dayneko.webservice.lightSystem.service

import ru.dayneko.webservice.lightSystem.model.RoomParams

/**
 * Сервис для работы с комнатами
 *
 * @author dayneko_si
 * @since 16.04.2019
 */
interface RoomsService {
    fun getRoomsByFloor(floor: Int): List<RoomParams>
    fun getAllRooms(): List<RoomParams>
}