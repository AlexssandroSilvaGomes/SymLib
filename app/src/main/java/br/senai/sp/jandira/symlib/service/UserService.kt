package br.senai.sp.jandira.symlib.service

import br.senai.sp.jandira.symlib.model.BaseResponse
import com.google.gson.JsonObject
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface UserService {

    @Headers("Content-Type: application/json")
    @POST("/usuario/cadastrarUsuario")
    suspend fun createUser(@Body body: JsonObject): Response<JsonObject>
}