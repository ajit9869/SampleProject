package com.example.sampleproject.api

import com.example.sampleproject.model.MainModel
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {


    @GET(NetworkConstants.GET_USERS)
    suspend fun getUsers(@Query(NetworkConstants.PAGE_NO) key: Int) : Response<MainModel>

    companion object {

        var BASE_URL = "https://reqres.in/"

        fun create() : ApiInterface {

            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
            return retrofit.create(ApiInterface::class.java)

        }
    }
}