package ru.dayneko.webservice.lightSystem.model

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

/**
 * Модель информации по свету
 *
 * @author dayneko_si
 * @since 19.01.2019
 */
@Document(collection = "lightStates")
@ApiModel(value = "Состояние света в комнатах")
data class LightState (
        /** Идентификатор **/
        @Id
        @ApiModelProperty("Идентификатор")
        val id: String?,

        /** rpId комнаты **/
        @ApiModelProperty("rpId комнаты")
        val rpId: Int,

        /** Состояние света (0/1 выкл/вкл) **/
        @ApiModelProperty("Состояние света")
        var state: Int,

        /** Дата записи **/
        @ApiModelProperty("Дата записи")
        val dateIncome: Date?
)