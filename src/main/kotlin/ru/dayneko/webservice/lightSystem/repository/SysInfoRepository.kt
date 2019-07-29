package ru.dayneko.webservice.lightSystem.repository

import org.springframework.data.mongodb.repository.MongoRepository
import ru.dayneko.webservice.lightSystem.model.SystemConfig

/**
 * Репозиторий света
 *
 * @author dayneko_si
 * @since 16.01.2019
 */
interface SysInfoRepository : MongoRepository<SystemConfig, String>
