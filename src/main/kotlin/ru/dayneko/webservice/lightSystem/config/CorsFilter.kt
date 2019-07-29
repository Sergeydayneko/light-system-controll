package ru.dayneko.webservice.lightSystem.config

import org.springframework.stereotype.Component
import javax.servlet.Filter
import javax.servlet.http.HttpServletResponse
import javax.servlet.FilterChain
import javax.servlet.ServletResponse
import javax.servlet.ServletRequest

/**
 * Инициализация фильтра запросов/ответов
 * для проставление нужных заголовков.
 *
 * @author dayneko_si
 * @since 26.02.2019
 */
@Component
class CorsFilter : Filter {

    /**
     * Фильтр проставления запросов
     *
     * @param req   - объект серверного запроса
     * @param res   - объект серверного ответа
     * @param chain - объект для создания цепочки фильтров
     */
    override fun doFilter(req: ServletRequest, res: ServletResponse, chain: FilterChain) {
        val response = res as HttpServletResponse
        response.setHeader("Access-Control-Allow-Origin", "*")
        response.setHeader("Access-Control-Allow-Methods", "POST, PUT, GET, OPTIONS, DELETE")
        response.setHeader("Access-Control-Max-Age", "3600")
        response.setHeader("Access-Control-Allow-Headers", "x-requested-with")
        chain.doFilter(req, res)
    }
}