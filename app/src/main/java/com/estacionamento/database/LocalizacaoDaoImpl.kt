package com.estacionamento.database

import com.estacionamento.database.model.Localizacao

class LocalizacaoDaoImpl(val database: DB): LocalizacaoDao {
    override fun getAll(): List<Localizacao> {
        TODO("Not yet implemented")
    }

    override fun getLocalizacao(key: Int): Localizacao? {
        TODO("Not yet implemented")
    }

    override fun insertAll(vararg localizacoes: Localizacao) {
        TODO("Not yet implemented")
    }

    override fun insert(localizacao: Localizacao) {
        TODO("Not yet implemented")
    }

    override fun delete(localizacao: Localizacao) {
        TODO("Not yet implemented")
    }

    override fun update(localizacao: Localizacao) {
        TODO("Not yet implemented")
    }
}