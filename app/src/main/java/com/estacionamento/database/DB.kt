package com.estacionamento.database

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception
import java.sql.Connection
import java.sql.DriverManager
import kotlin.concurrent.thread

class DB {
    private var connection: Connection? = null
    private val host: String = ""
    private val db: String = ""
    private val port: Int = 0
    private val user: String = ""
    private val pass: String = ""
    private val url: String = ""

    suspend fun connect(){
        Class.forName("com.mysql.jdbc.Driver")
        return withContext(Dispatchers.IO){
            connection = DriverManager.getConnection(url, user, pass)
        }
    }

    fun disconnect(){
        connection?.close()
        connection = null
    }

}