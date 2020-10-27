package com.estacionamento.database

import androidx.room.*

@Dao
interface VeiculoDao {

    @Query("SELECT * FROM veiculos")
    fun getAllVeiculos(): List<Veiculo>
    @Query("SELECT * from veiculos WHERE id_veiculo = :key")
    fun getVeiculo(key: Int): Veiculo
    @Query("SELECT id_veiculo FROM veiculos WHERE chapa = :paramchapa limit 1")
    fun getVeiculo(paramchapa: String): Int

    @Insert
    fun insertAllVeiculos(vararg veiculos: Veiculo)
    @Insert
    fun insert(veiculo: Veiculo)
    @Insert
    fun insertVeiculo(veiculo: Veiculo)

    @Delete
    fun delete(veiculo: Veiculo)

    @Update
    fun update(veiculo: Veiculo)
}