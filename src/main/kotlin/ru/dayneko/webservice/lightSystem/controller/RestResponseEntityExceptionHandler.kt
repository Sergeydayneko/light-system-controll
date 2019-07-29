package ru.dayneko.webservice.lightSystem.controller

import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.web.context.request.WebRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

/**
 * Объект controller advice для обработки исключений на контроллерах
 *
 * @author dayneko_si
 * @since 14.03.2019
 */
@ControllerAdvice
class RestResponseEntityExceptionHandler : ResponseEntityExceptionHandler() {

    /**
     * Стандартный обработчик всех исключений
     *
     * @return объект Response со стандартным текстом ошибки
     */
    @ExceptionHandler(Exception::class)
    protected fun handleConflict( ex: RuntimeException, request: WebRequest): ResponseEntity<Any> {
        return handleExceptionInternal(ex, Error("Internal server error"),
                HttpHeaders(), HttpStatus.BAD_REQUEST, request)
    }
}