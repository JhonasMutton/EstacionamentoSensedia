package com.estacionamento.database

import androidx.room.*
import com.estacionamento.database.model.VeiculoLocalizacao

@Dao
interface VeiculoLocalizacaoDao {

    @Query("SELECT * FROM veiculolocalizacao")
    fun getAll(): List<VeiculoLocalizacao>
    @Query("SELECT * from veiculolocalizacao WHERE id_veiculo_localizacao = :key")
    fun getVeiculoLocalizacao(key: Int): VeiculoLocalizacao
    @Query("SELECT id_localizacao from veiculolocalizacao WHERE id_veiculo = :key AND disponibilidade = 1")
    fun veiculoDisponivel(key: Int): Int
    @Query("SELECT id_veiculo_localizacao from veiculolocalizacao WHERE id_veiculo = :idVeiculo AND id_localizacao = :idLocalizacao AND disponibilidade = 1")
    fun getVeiculoLocalizacao(idVeiculo: Int, idLocalizacao: Int): Int
    @Query("UPDATE veiculolocalizacao SET disponibilidade = 0 WHERE id_veiculo_localizacao = :id")
    fun updateDisponibilidade(id: Int)
    @Insert
    fun insert(veiculolocalizacao: VeiculoLocalizacao)

    @Delete
    fun delete(veiculolocalizacao: VeiculoLocalizacao)

    @Update
    fun update(veiculolocalizacao: VeiculoLocalizacao)
}