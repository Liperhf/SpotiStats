package com.example.spotistats.data.api

import com.example.spotistats.data.dto.AuthorizationDto
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface SpotifyAuthApi {
    @FormUrlEncoded
    @POST("api/token")
    suspend fun exchangeCodeForToken(
        @Field("grant_type") grantType:String = "authorization_code",
        @Field("code") code:String,
        @Field("redirect_uri") redirectUri:String,
        @Field("client_id") clientId:String,
        @Field("client_secret") clientSecret:String
    ): AuthorizationDto
}