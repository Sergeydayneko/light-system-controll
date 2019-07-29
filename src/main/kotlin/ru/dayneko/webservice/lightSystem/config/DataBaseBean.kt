package ru.dayneko.webservice.lightSystem.config

import com.mongodb.MongoClient
import com.mongodb.client.MongoDatabase
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties("spring.data.mongodb")
class DataBaseBean(
        /** MongoClient **/
        @Autowired
        private val client: MongoClient
) {
    /** Имя БД **/
    private lateinit var database: String

    /**
     * Получить имя БД
     *
     * @return имя БД
     */
    fun getDatabase(): String = database

    /**
     * Сеттер имени БД.
     *
     * @param database - имя бд
     */
    fun setDatabase(database: String) { if (!this::database.isInitialized) this.database = database }

    /**
     * Получить бин коннекта к БД.
     *
     * @return бин коннекта к БД.
     */
    @Bean
    fun getMongoDB(): MongoDatabase = client.getDatabase(database)

}