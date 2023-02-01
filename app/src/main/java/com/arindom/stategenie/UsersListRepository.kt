package com.arindom.stategenie

import android.app.Application
import android.content.Context
import android.util.Log
import com.arindom.stategenie.presentation.User
import com.arindom.stategenie.presentation.UserList
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.context.GlobalContext.get

class UsersListRepository(
    private val context: Context
) {
    private var jsonString = ""
    private var users = UserList()
    fun getUsersList(): List<User> {
        try {
            jsonString =
                context.assets.open("users.json").bufferedReader().use { it.readText() }
            users = Gson().fromJson<UserList>(jsonString, UserList::class.java)
        } catch (e: Exception) {
            Log.d("UsersListRepository", "getUsersList: ${e.message}")
        }
        return users.users
    }
}