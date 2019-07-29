package ru.dayneko.webservice.lightSystem.repository

import org.springframework.data.mongodb.repository.MongoRepository
import ru.dayneko.webservice.lightSystem.model.LightSwitchHistory

/**
 * Репозиторий хранения информации по переключению света
 *
 * @author dayneko_si
 * @since 17.04.2019
 */
interface LightSwitchRepository : MongoRepository<LightSwitchHistory, String>