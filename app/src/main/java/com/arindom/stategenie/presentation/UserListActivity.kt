/*
 * Copyright 2023 Arindom Ghosh
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.arindom.stategenie.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.TabRowDefaults.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.arindom.stategenie.presentation.widgets.ErrorWidget
import com.arindom.stategenie.presentation.widgets.ProgressWidget
import com.arindom.stategenie.presentation.widgets.UserInfo
import com.arindom.stategenie.ui.theme.StateGenieTheme
import org.koin.androidx.compose.get

class UserListActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StateGenieTheme {
                // A surface container using the 'background' color from the theme
                UserListWidget()
            }
        }
    }
}

@Composable
fun UserListWidget(
    viewModel: UserViewModel = get()
) {
    val content by viewModel.uiState.collectAsState()
    when (content) {
        is `UsersUiState$Generated`.SuccessState -> {
            val users = (content as `UsersUiState$Generated`.SuccessState).data
            LazyColumn {
                items(users) {
                    UserInfo(modifier = Modifier, user = it)
                    Divider(color = Color.LightGray)
                }
            }
        }
        is `UsersUiState$Generated`.LoadingState -> {
            ProgressWidget()
        }
        is `UsersUiState$Generated`.ErrorState -> {
            val error = (content as `UsersUiState$Generated`.ErrorState).error
            ErrorWidget(modifier = Modifier, message = error.message ?: "Something went wrong, please try again!")
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    StateGenieTheme {
        Greeting("Android")
    }
}