package ru.dayneko.webservice.lightSystem.repository.Impl

import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.data.mongodb.core.updateFirst
import org.springframework.stereotype.Repository
import ru.dayneko.webservice.lightSystem.model.LightState
import ru.dayneko.webservice.lightSystem.repository.LightStatesRepository
import ru.dayneko.webservice.lightSystem.repository.bridge.LightStatesCrudRepository

/**
 * Имплеменатция репозитория данных по состояниям света и комнатам
 *
 * @author dayneko_si
 * @since 28.01.2019
 */
@Repository
class LightStatesRepositoryImpl(
        /** Репозиторий для CRUD операция **/
        private val lightStateRepository: LightStatesCrudRepository,

        /** Темплейт монго **/
        private val mongo: MongoTemplate
): LightStatesRepository {

    /**
     * Обновление состояния света по rpId
     *
     * @param rpId - номер rpId
     */
    override fun updateStateByRpId(rpId: Int, state: Int) {
        mongo.updateFirst(Query()
                .addCriteria(Criteria
                        .where("rpId")
                        .`is`(rpId)), Update().set("state", state), LightState::class)
    }

    /**
     * Метод для нахождения всех объектов
     * в коллекции состояния света.
     */
    override fun findAll(): List<LightState> = lightStateRepository.findAll()

    /**
     * Метод для нахождения объекта света по rpId
     *
     * @param rpId - номер rpId для поиска
     * @return объект освещения
     */
    override fun findByRpId(rpId: Int): LightState = lightStateRepository.findByRpId(rpId)
}