package com.estacionamento.database

import androidx.room.*
import com.estacionamento.database.model.Localizacao

@Dao
interface LocalizacaoDao {

    @Query("SELECT * FROM localizacoes")
    fun getAll(): List<Localizacao>
    @Query("SELECT * from localizacoes WHERE id_localizacao = :key")
    fun getLocalizacao(key: Int): Localizacao?
    @Insert
    fun insertAll(vararg localizacoes: Localizacao)
    @Insert
    fun insert(localizacao: Localizacao)
    @Delete
    fun delete(localizacao: Localizacao)
    @Update
    fun update(localizacao: Localizacao)


}