package com.estacionamento.api.carrorama.login.model

import com.google.gson.annotations.SerializedName

data class Parametros (
	@SerializedName("checklistInicioViagemPool") val checklistInicioViagemPool : Boolean,
	@SerializedName("minutosAntecedenciaRetiradaViagemPool") val minutosAntecedenciaRetiradaViagemPool : Int,
	@SerializedName("apontamentoNivelCombustivel") val apontamentoNivelCombustivel : Int,
	@SerializedName("viagemSemReservaApp") val viagemSemReservaApp : Boolean
)