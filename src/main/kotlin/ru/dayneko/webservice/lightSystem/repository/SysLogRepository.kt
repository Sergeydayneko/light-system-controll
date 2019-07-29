package ru.dayneko.webservice.lightSystem.repository

import org.springframework.data.mongodb.repository.MongoRepository
import ru.dayneko.webservice.lightSystem.model.SystemLogging

/**
 * Репозиторий данных связанных
 * с обработкой системных логов
 *
 * @author dayneko_si
 * @since 20.02.2019
 */
interface SysLogRepository : MongoRepository<SystemLogging, String>