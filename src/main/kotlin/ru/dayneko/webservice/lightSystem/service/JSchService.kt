package ru.dayneko.webservice.lightSystem.service

import com.jcraft.jsch.ChannelExec
import com.jcraft.jsch.Session

/**
 * Интерфейс сервис для работы с SSH
 *
 * @author dayneko_si
 * @since 14.01.2019
 */
interface JSchService {

    fun switchRaspberryPiByRpIp(rpIp: String)
}