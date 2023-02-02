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

package com.arindom.stategenie

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arindom.stategenie.presentation.UserViewModel
import com.arindom.stategenie.presentation.`UsersUiState$Generated`
import com.arindom.stategenie.ui.theme.StateGenieTheme
import org.koin.android.ext.android.inject
import org.koin.androidx.compose.get
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StateGenieTheme {
                // A surface container using the 'background' color from the theme
                UserListWidget()
                /* Surface(
                     modifier = Modifier.fillMaxSize(),
                     color = MaterialTheme.colors.background
                 ) {
                     Greeting("Android")
                 }*/
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
            LazyColumn(modifier = Modifier.padding(10.dp)) {
                items(users) {
                    Greeting(name = it.name)
                }
            }
        }
          is `UsersUiState$Generated`.LoadingState -> {
              Column(
                  modifier = Modifier,
                  verticalArrangement = Arrangement.Center,
                  horizontalAlignment = Alignment.CenterHorizontally
              ) {
                  CircularProgressIndicator()
              }
          }
        is `UsersUiState$Generated`.ErrorState -> {}
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