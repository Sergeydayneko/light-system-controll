package ru.dayneko.webservice.lightSystem.model

import io.swagger.annotations.ApiModel
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

/**
 * Entity хранения истории переключения света
 *
 * @author dayneko_si
 * @since 16.04.2019
 */
@ApiModel("Объект хранения информации по переключению света")
@Document("lightHistory")
data class LightSwitchHistory(
        @Id
        val id: String? = null,

        /** Номер raspberry **/
        val rpId: Int? = null,

        /** Дата записи **/
        val date: Date,

        /** Событие (1 - включили | 0 - выключили) **/
        val state: Int,

        /** Описание события **/
        val descriptor: String? = null
)