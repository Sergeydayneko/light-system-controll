package ru.dayneko.webservice.lightSystem.service.impl

import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.Projections
import org.bson.Document
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import ru.dayneko.webservice.lightSystem.service.DBService
import java.util.*

/**
 * Сервис по работе с сотрудниками
 *
 * @author dayneko_si
 * @since  29.03.2019
 */
@Component
class DBServiceImpl(
        /** База данных монго **/
        @Autowired
        private val mongoDB : MongoDatabase
): DBService {

    /**
     * Получить мета-информацию о контроллерах.
     *
     * @return мета-информацию о контроллерах
     */
    override fun getMetaInfo() : List<Document> =
            mongoDB.getCollection("metaInfoLightSystem")
                    .find()
                    .projection(Projections
                            .fields(Projections.excludeId()))
                            .into(ArrayList())
}