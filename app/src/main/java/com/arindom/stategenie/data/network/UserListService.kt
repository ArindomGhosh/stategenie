package com.arindom.stategenie.data.network

import com.arindom.stategenie.data.model.User
import retrofit2.http.GET
import retrofit2.http.Query

interface UserListService {

    @GET("users")
    suspend fun getGithubUserList(
        @Query("since") since: String
    ): List<User>
}