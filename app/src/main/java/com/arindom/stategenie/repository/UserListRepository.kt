package com.arindom.stategenie.repository

import com.arindom.stategenie.data.network.UserListService
import com.arindom.stategenie.data.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class UserListRepository(
    private val service: UserListService
) {
    suspend fun getGithubUserList(): Flow<List<User>> {
      return flow {
            emit(service.getGithubUserList(since = "2021"))
        }
    }
}