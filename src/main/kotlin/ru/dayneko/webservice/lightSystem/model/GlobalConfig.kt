package ru.dayneko.webservice.lightSystem.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

/**
 * Модель глобальных настроек системы
 *
 * @author dayneko_si
 * @since 23.01.2019
 */
@Document(collection = "globalConfig")
data class GlobalConfig(
        /** Mongo ID **/
        @Id
        val id: String,

        /** Общий режим освещения **/
        val globalLightMode: String
)
