package com.example.apptesttmbd.data.api

import com.example.apptesttmbd.data.model.Content
import com.example.apptesttmbd.data.model.ResponseGenres
import com.example.apptesttmbd.data.model.ResponseMovieDetail
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("/3/movie/now_playing?")
    fun getNowPlayin(@Header("Authorization")  auth: String,
                     @Header("accept" ) type: String,
                     @Query("page" ) numpage: Int
    ): Call<Content>

    @GET("/3/genre/movie/list")
    fun getGenre(@Header("Authorization")  auth: String, @Header("accept" ) type: String): Call<ResponseGenres>

    @GET("/3/movie/{idmovie}")
    fun getMovieDetail(@Header("Authorization")  auth: String,
                       @Header("accept" ) type: String,
                       @Path("idmovie") idmovie: Int): Call<ResponseMovieDetail>
}