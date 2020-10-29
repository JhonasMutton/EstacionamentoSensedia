package com.estacionamento.api.carrorama.login.model

import com.google.gson.annotations.SerializedName
data class Objeto (
	@SerializedName("id") val id : Int,
	@SerializedName("empresaID") val empresaID : Int,
	@SerializedName("empresa") val empresa : String,
	@SerializedName("condutorID") val condutorID : Int,
	@SerializedName("hashAcesso") val hashAcesso : String,
	@SerializedName("parametros") val parametros : Parametros,
	@SerializedName("versaoNome") val versaoNome : String,
	@SerializedName("versaoCodigo") val versaoCodigo : String,
	@SerializedName("ambiente") val ambiente : String,
	@SerializedName("fcmToken") val fcmToken : String,
	@SerializedName("deviceIdentifier") val deviceIdentifier : String,
	@SerializedName("nome") val nome : String,
	@SerializedName("cnpj") val cnpj : Int,
	@SerializedName("grupoPermissao") val grupoPermissao : String,
	@SerializedName("modulos") val modulos : List<String>,
	@SerializedName("tutoriais") val tutoriais : List<Tutoriais>,
	@SerializedName("planoContratado") val planoContratado : PlanoContratado,
	@SerializedName("servicosAtivos") val servicosAtivos : List<String>
)