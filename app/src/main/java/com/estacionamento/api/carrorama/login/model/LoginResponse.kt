package com.estacionamento.api.carrorama.login.model

import com.google.gson.annotations.SerializedName

data class LoginResponse (
	@SerializedName("success") val success : Boolean,
	@SerializedName("messages") val messages : List<String>,
	@SerializedName("object") val objeto : Objeto
)
