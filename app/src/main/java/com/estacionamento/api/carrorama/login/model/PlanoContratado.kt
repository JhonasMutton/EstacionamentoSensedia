package com.estacionamento.api.carrorama.login.model

import com.google.gson.annotations.SerializedName

data class PlanoContratado (

	@SerializedName("limiteVeiculosAtivos") val limiteVeiculosAtivos : Int,
	@SerializedName("limiteUsuariosAtivos") val limiteUsuariosAtivos : Int,
	@SerializedName("dataFimVigenciaPlano") val dataFimVigenciaPlano : String
)