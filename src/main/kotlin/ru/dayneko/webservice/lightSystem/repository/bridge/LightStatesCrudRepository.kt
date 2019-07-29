package ru.dayneko.webservice.lightSystem.repository.bridge

import org.springframework.data.mongodb.repository.MongoRepository
import ru.dayneko.webservice.lightSystem.model.LightState

/**
 * Репозиторий для работы с данными состояний комнат.
 * Для внедрения в impl репозиторий для выполнения CRUD операций.
 *
 * @author dayneko_si
 * @sincen 28.01.2019
 */
interface LightStatesCrudRepository: MongoRepository<LightState, String> {

    /**
     * Метод для нахождения по rpId(метод spring data)
     *
     * @param rpId - номер rpId
     * @return объект состояния света
     */
    fun findByRpId(rpId: Int): LightState
}