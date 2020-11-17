package com.estacionamento.database

import com.estacionamento.database.model.VeiculoLocalizacao

class VeiculoLocalizacaoDaoImpl(val database: DB):VeiculoLocalizacaoDao {
    override fun getAll(): List<VeiculoLocalizacao> {
        TODO("Not yet implemented")
    }

    override fun getVeiculoLocalizacao(key: Int): VeiculoLocalizacao {
        TODO("Not yet implemented")
    }

    override fun getVeiculoLocalizacao(idVeiculo: Int, idLocalizacao: Int): Int {
        TODO("Not yet implemented")
    }

    override fun veiculoDisponivel(key: Int): Int {
        TODO("Not yet implemented")
    }

    override fun updateDisponibilidade(id: Int) {
        TODO("Not yet implemented")
    }

    override fun insert(veiculolocalizacao: VeiculoLocalizacao) {
        TODO("Not yet implemented")
    }

    override fun delete(veiculolocalizacao: VeiculoLocalizacao) {
        TODO("Not yet implemented")
    }

    override fun update(veiculolocalizacao: VeiculoLocalizacao) {
        TODO("Not yet implemented")
    }
}