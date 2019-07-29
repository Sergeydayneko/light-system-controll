package ru.dayneko.webservice.lightSystem.model

import io.swagger.annotations.ApiModelProperty
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

/**
 * Модель логгирование состояния света
 *
 * @author dayneko_si
 * @since 23.01.2019
 */
@Document(collection = "lightStateLogs")
data class LightStateLog (
        /** Идентификатор **/
        @Id
        @ApiModelProperty("Идентификатор")
        val id: String?,

        /** rpId комнаты **/
        @ApiModelProperty("rpId комнаты")
        val rpId: Int,

        /** Состояние света (0/1 вкл/выкл) **/
        @ApiModelProperty("Состояние света")
        var state: Int
)

/**
 * Модель для отправки данных по логам на фронт
 *
 * @author dayneko_si
 * @since 23.01.2019
 */
data class LogFrontendData (
        /** Коллекция измененных состояний **/
        val roomsData: List<LightStateLog>,

        /** Глобальный режим освещения **/
        val lightMode: String
)