package ru.dayneko.webservice.lightSystem.repository

import org.springframework.data.mongodb.repository.MongoRepository
import ru.dayneko.webservice.lightSystem.model.SystemRoomsInfo

/**
 * Репозиторий хранения
 * информации по комнатам в здании
 *
 * @author dayneko_si
 * @since 16.01.2019
 */
interface RoomsInfoRepository : MongoRepository<SystemRoomsInfo, String> {

    /**
     * Получение данных по этажу
     * @param floor Int этаж
     * @return MutableList<SystemRoomsInfo>
     */
    fun findByFloor(floor: Int): MutableList<SystemRoomsInfo>
}