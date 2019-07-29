package ru.dayneko.webservice.lightSystem.service.impl

import com.jcraft.jsch.ChannelExec
import com.jcraft.jsch.JSch
import com.jcraft.jsch.Session
import lombok.Data
import lombok.Setter
import org.slf4j.LoggerFactory
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Service
import ru.dayneko.webservice.lightSystem.service.JSchService
import java.io.ByteArrayOutputStream

/**
 * Сервис класс для инициализации соединения по SSH
 *
 * @author dayneko_si
 * @since 14.01.2019
 */
@Service
@ConfigurationProperties("light.ssh.settings")
@Data
class JSchServiceImpl: JSchService {
    private val logger = LoggerFactory.getLogger(JSchServiceImpl::class.java)

    lateinit var username: String
    lateinit var password: String
    lateinit var command: String
    lateinit var port: String
    lateinit var confOptionOne: String
    lateinit var confOptionTwo: String
    lateinit var channelName: String

    override fun switchRaspberryPiByRpIp(rpIp: String) {
        try {
            val session = initSession(rpIp).also { it.connect() }
            val channel = initChannel(session)

            ByteArrayOutputStream().use {
                channel.setCommand(command)
                channel.setErrStream(it)
                channel.connect()

                Thread.sleep(20)
            }

            session.disconnect()
            channel.disconnect()
            logger.info("Mode switched by command to rpIp = $rpIp ")
        } catch (e: Exception) {
            logger.error("Error has been occurred when trying to connect to Raspberry by SSH for rpId = $rpIp with message: ${e.message}")
        }
    }

    /**
     * Инициализация сессия передачи данных по SSH
     *
     * @return настроенный объект SSH
     */
    private fun initSession(rpIp: String): Session =
            JSch().getSession(username, rpIp, port.toInt()).apply {
                setPassword(password)
                setConfig(confOptionOne, confOptionTwo)
        }

    /**
     * Инициализация канала передачи данных по SSH
     *
     * @param session - объект сессии взаимодействия
     * @return объект канала
     */
    private fun initChannel(session: Session): ChannelExec = session.openChannel(channelName) as ChannelExec
}