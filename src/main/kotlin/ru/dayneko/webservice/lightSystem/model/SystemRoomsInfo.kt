package ru.dayneko.webservice.lightSystem.model

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

/**
 * Модель данных комнаты
 *
 * @author dayneko_si
 * @since  19.01.2019
 */
@Document(collection = "sysRoomsInfo")
@ApiModel(value = "Информация по комнатам")
data class SystemRoomsInfo(

        /** ID монго **/
        @Id
        @ApiModelProperty("Идентификатор")
        var id: String?,

        /** Этаж **/
        var floor: Int,

        /** Список комнат **/
        var rooms: List<RoomParams>,

        /** Параметры карты **/
        val map: VisualParams? = null
)