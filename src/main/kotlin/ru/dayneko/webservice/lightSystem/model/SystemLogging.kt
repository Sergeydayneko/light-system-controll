package ru.dayneko.webservice.lightSystem.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

/**
 * Модель данных описывающая разные системные логи.
 * В данном модуле необходима для отправки логов,
 * когда был оставлен свет, а все сотрудники ушли
 *
 * @author dayneko_si
 * @since 20.02.2019
 */
@Document(value = "sysLogs")
data class SystemLogging (
        /** ID Монго **/
        @Id
        val id: String?,

        /** rpId оборудования **/
        val rpId: Int,

        /** Сообщение лога **/
        val message: String,

        /** Дата лога **/
        val date: Date
)