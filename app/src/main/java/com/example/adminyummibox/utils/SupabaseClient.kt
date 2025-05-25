package com.example.adminyummibox.utils

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.logging.HttpLoggingInterceptor


object SupabaseClient {
    private const val BASE_URL = "https://wjejxjzodprauxdjtiys.supabase.co" // من Supabase Dashboard
    private const val API_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6IndqZWp4anpvZHByYXV4ZGp0aXlzIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NDYzNzYxNDMsImV4cCI6MjA2MTk1MjE0M30.5LsQa51okC7owyYLPTTB03XFw0srhjwgAks260ERmIo"

    val service: SupabaseService by lazy {
        val logger = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
        val client = OkHttpClient.Builder().addInterceptor(logger).build()

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SupabaseService::class.java)
    }

    fun getApiKey() = API_KEY
    fun getAuthorization() = "Bearer $API_KEY"
}
