package ru.dayneko.webservice.lightSystem.model

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

/**
 * Информация о оборудовании
 *
 * @author dayneko_si
 * @since  22.01.2019
 */
@Document(collection = "sysConfig")
@ApiModel(value = "Информация о оборудовании")
data class SystemConfig(
        /** Идентификатор **/
        @Id
        @ApiModelProperty("Идентификатор")
        var id: String?,

        /** rpId комнаты **/
        @ApiModelProperty("rpId комнаты")
        var rpId: Int,

        /** Мак адресс **/
        @ApiModelProperty("Ip адресс")
        var rpIp: String?,

        /** Флаг температуры **/
        @ApiModelProperty("Флаг температуры")
        var temp: Int?,

        /** Флаг электросчетчиков **/
        @ApiModelProperty("Флаг электросчетчиков")
        var electro: Int?,

        /** Флаг протечек **/
        @ApiModelProperty("Флаг протечек")
        var leaks: Int?,

        /** РП Мак **/
        @ApiModelProperty("РП Мак ")
        var rpMac: String?,

        /** Адрес свича **/
        @ApiModelProperty("Адрес свича")
        var switch: String?,

        /** Порт **/
        @ApiModelProperty("Порт")
        var port: Int?,

        /** Статус датчика **/
        @ApiModelProperty("Статус датчика")
        var status: String?,

        /** Входящая дата **/
        @ApiModelProperty("Входящая дата")
        var dateIncome: Date?
)