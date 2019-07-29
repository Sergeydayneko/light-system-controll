package ru.dayneko.webservice.lightSystem.exceptions

/**
 * @author dayneko_si
 * @since  19.01.2019
 */
interface ServiceResponse {
    val message: String
}

data class ServiceErrorResponse(
        override val message: String
): ServiceResponse
