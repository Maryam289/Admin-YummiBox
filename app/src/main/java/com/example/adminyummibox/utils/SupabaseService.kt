package com.example.adminyummibox.utils

import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path


interface SupabaseService {
    @Multipart
    @POST("storage/v1/object/{bucket}/{fileName}")
    fun uploadImage(
        @Header("apikey") apiKey: String,
        @Header("Authorization") authorization: String,
        @Part file: MultipartBody.Part,
        @Path("bucket") bucket: String,
        @Path("fileName") fileName: String
    ): Call<ResponseBody>
}
