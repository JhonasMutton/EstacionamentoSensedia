package com.estacionamento.api.carrorama.login.model

import com.google.gson.annotations.SerializedName

data class Tutoriais (

	@SerializedName("moduloID") val moduloID : Int,
	@SerializedName("moduloNome") val moduloNome : String,
	@SerializedName("tutoriais") val tutoriais : List<Tutoriais>
)