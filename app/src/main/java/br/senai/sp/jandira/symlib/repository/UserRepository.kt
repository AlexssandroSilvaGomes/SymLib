package br.senai.sp.jandira.symlib.repository

import br.senai.sp.jandira.symlib.model.BaseResponse
import br.senai.sp.jandira.symlib.service.RetrofitFactory;
import br.senai.sp.jandira.symlib.service.UserService;
import com.google.gson.JsonObject
import retrofit2.Response

class UserRepository {
    private val apiService = RetrofitFactory.getInstance().create(UserService::class.java)

    suspend fun registerUser(email: String, senha: String, foto: String): Response<JsonObject> {
        val requestBody = JsonObject().apply {
            addProperty("login", email)
            addProperty("senha", senha)
            addProperty("imagem", foto)
        }

        return apiService.createUser(requestBody)
    }
}