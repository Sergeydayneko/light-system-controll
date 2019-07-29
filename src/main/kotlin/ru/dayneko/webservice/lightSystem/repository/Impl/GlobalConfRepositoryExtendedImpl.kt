package ru.dayneko.webservice.lightSystem.repository.Impl

import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.data.mongodb.core.updateFirst
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import ru.dayneko.webservice.lightSystem.model.GlobalConfig
import ru.dayneko.webservice.lightSystem.repository.GlobalConfRepositoryExtended

/**
 * Интерфейс расширенного репозитория для получения настроек
 * глобального конфига
 *
 * @author dayneko_si
 * @since 25.01.2019
 */
@Repository
class GlobalConfRepositoryExtendedImpl(
        /** Обычный монго репозиторий глобального конфига **/
        private val globalConfRepository: MongoRepository<GlobalConfig, String>,

        /** Шоблон монго **/
        private val mongo: MongoTemplate
): GlobalConfRepositoryExtended {

    /**
     * Получение глобального конфига
     *
     * @return коллекцию массивов, но по факту возвращается один элемент
     */
    override fun findAll(): List<GlobalConfig> = globalConfRepository.findAll()

    /**
     * Обновление режима освещения
     * глобального конфмга
     *
     * @param mode - новый режим освещения
     */
    override fun updateGlobalLightMode(mode: String) {
        mongo.updateFirst(Query()
                .addCriteria(Criteria
                        .where("globalLightMode")
                        .exists(true)), Update().set("globalLightMode", mode), GlobalConfig::class)
    }
}