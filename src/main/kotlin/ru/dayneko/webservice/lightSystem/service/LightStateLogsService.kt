package ru.dayneko.webservice.lightSystem.service

import ru.dayneko.webservice.lightSystem.model.LogFrontendData

/**
 * Сервис для обработки логов переключения состояния света
 *
 * @author dayneko_si
 * @since 23.01.2019
 */
interface LightStateLogsService {

    /**
     * Метод для получения логов по обновлению состояния освещения и
     * инициализации глобального режима освещения в них
     *
     * @return коллекцию логов для отправки на фронт
     */
    fun getLogsState(): LogFrontendData
}