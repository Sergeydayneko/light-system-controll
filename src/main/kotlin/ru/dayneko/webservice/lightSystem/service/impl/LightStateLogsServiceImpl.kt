package ru.dayneko.webservice.lightSystem.service.impl

import org.springframework.stereotype.Service
import ru.dayneko.webservice.lightSystem.model.LightStateLog
import ru.dayneko.webservice.lightSystem.model.LogFrontendData
import ru.dayneko.webservice.lightSystem.repository.LightStateLogsRepository
import ru.dayneko.webservice.lightSystem.service.LightModeService
import ru.dayneko.webservice.lightSystem.service.LightStateLogsService

/**
 * Сервис для обработки логов переключения состояния света
 *
 * @author dayneko_si
 * @since 23.01.2019
 */
@Service
class LightStateLogsServiceImpl(
        /**  Репозиторий логов освещения **/
        private val lightLogsRepository: LightStateLogsRepository,

        /** Сервис для получения глобального режима освещения **/
        private val modeService: LightModeService
): LightStateLogsService {

    /**
     * Метод для получения логов по обновлению состояния освещения и
     * инициализации глобального режима освещения в них
     *
     * @return коллекцию логов для отправки на фронт
     */
    override fun getLogsState(): LogFrontendData =
            initFrontData(lightLogsRepository.findAll()).also { removeLogsCollection() }

    /**
     * Метод инициализации объекта для отправки на frontend
     *
     * @param logs - файл с логами изменения состояния света
     * @return объект с логами и глобальным режимом освещения
     */
    private fun initFrontData(logs: List<LightStateLog>): LogFrontendData = LogFrontendData(logs, modeService.getCurrentLightMode())

    /**
     * Метод для удаления коллекции логов.
     * При каждой прослушке сокеты логи по изменению состояния должны быть актуальны
     */
    private fun removeLogsCollection() {
        lightLogsRepository.deleteAll()
    }
}