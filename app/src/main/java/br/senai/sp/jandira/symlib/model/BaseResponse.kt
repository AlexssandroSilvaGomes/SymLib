package br.senai.sp.jandira.symlib.model

import com.google.gson.annotations.SerializedName

class BaseResponse (
    @SerializedName("login") var login: String = "",
    @SerializedName("senha") var senha: String = "",
    @SerializedName("imagem") var imagem: String = ""
)