package ru.dayneko.webservice.lightSystem.service

import org.bson.Document

/**
 * Сервис по работе с сотрудниками
 *
 * @author dayneko_si
 * @since  29.03.2019
 */
interface DBService {

    /**
     * Получить мета-информацию о контроллерах.
     *
     * @return мета-информацию о контроллерах
     */
    fun getMetaInfo(): List<Document>
}