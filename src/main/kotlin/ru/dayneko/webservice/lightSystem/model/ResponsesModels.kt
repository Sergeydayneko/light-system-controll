package ru.dayneko.webservice.lightSystem.model

/**
 * Модель для предоставление ответа для работы с состояние света
 *
 * state = 0 свет выключен
 * state = 1 свет включен
 * state = 500 состояние ошибки переключения света
 *
 * @author dayneko_si
 * @since 23.01.2019
 */
data class LightResponse (
        /** Номер Raspberry pi**/
        val rpId: Int,

        /** Новое состояние света **/
        val state: Int,

        /** Текст сообщение об ответе **/
        val message: String
)

/**
 * Модель для предоставление ответа в JSON виде
 *
 * @author dayneko_si
 * @since 28.01.2019
 */
data class CommonResponse<T> (
        /** Текст ответа **/
        val mode: String,

        /** Данные для отправки **/
        val data: List<T>
)