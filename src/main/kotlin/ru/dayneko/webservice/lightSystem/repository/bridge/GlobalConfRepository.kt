package ru.dayneko.webservice.lightSystem.repository.bridge

import org.springframework.data.mongodb.repository.MongoRepository
import ru.dayneko.webservice.lightSystem.model.GlobalConfig

/**
 * Репозиторий для работы с данными глобальных настроек.
 * Для внедрения в impl репозиторий для выполнения CRUD операций
 *
 * @author dayneko_si
 * @since 23.01.2019
 */
interface GlobalConfRepository: MongoRepository<GlobalConfig, String>