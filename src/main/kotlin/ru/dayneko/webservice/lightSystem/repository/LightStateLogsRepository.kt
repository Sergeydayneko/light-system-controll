package ru.dayneko.webservice.lightSystem.repository

import org.springframework.data.mongodb.repository.MongoRepository
import ru.dayneko.webservice.lightSystem.model.LightStateLog

/**
 * Репозиторий обработки логов переключения состояния света в помещении
 *
 * @author dayneko_si
 * @since 23.01.2019
 */
interface LightStateLogsRepository: MongoRepository<LightStateLog, String>