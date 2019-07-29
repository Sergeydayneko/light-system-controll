package ru.dayneko.webservice.lightSystem.service.impl

import arrow.instances.list.foldable.foldLeft
import org.springframework.stereotype.Service
import ru.dayneko.webservice.lightSystem.model.RoomParams
import ru.dayneko.webservice.lightSystem.model.SystemRoomsInfo
import ru.dayneko.webservice.lightSystem.repository.RoomsInfoRepository
import ru.dayneko.webservice.lightSystem.service.RoomsService
import ru.dayneko.webservice.lightSystem.utils.andThen

@Service
class RoomsServiceImpl(
        /** Репозиторий данных по комнатам **/
        private val roomsInfoRepository: RoomsInfoRepository
) : RoomsService {
    /** Open **/
    override fun getAllRooms(): List<RoomParams> = findAllSystemRooms() andThen concatRoomsFromSysRoomsInfo
    override fun getRoomsByFloor(floor: Int): List<RoomParams> = findSystemRoomsByFloor(floor) andThen concatRoomsFromSysRoomsInfo

    /** Private **/
    private val concatRoomsFromSysRoomsInfo: (List<SystemRoomsInfo>) -> List<RoomParams> = {
        it.foldLeft(emptyList()) { acc, current -> acc + current.rooms }
    }
    private val findSystemRoomsByFloor: (Int) -> List<SystemRoomsInfo> = { roomsInfoRepository.findByFloor(it) }
    private val findAllSystemRooms: () -> List<SystemRoomsInfo> = { roomsInfoRepository.findAll() }
}