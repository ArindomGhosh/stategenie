/*
 * Copyright 2023 Arindom Ghosh
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.arindom.stategenie.presentation.userlist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.TabRowDefaults.Divider
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun UserListWidget(
  viewModel: UserViewModel = get()
) {
  val content by viewModel.uiState.collectAsState()
  val refreshing by viewModel.isRefreshing.collectAsState()
  val pullRefreshState = rememberPullRefreshState(refreshing, { viewModel.refresh() })
  Box(Modifier.pullRefresh(pullRefreshState)){
    when (content) {
      is `UsersUiState$Generated`.SuccessState -> {
        val users = (content as `UsersUiState$Generated`.SuccessState).data
        LazyColumn {
          items(users) {
            UserInfo(user = it)
          }
        }
      }
      is `UsersUiState$Generated`.LoadingState -> {
        ProgressWidget()
      }
      is `UsersUiState$Generated`.ErrorState -> {
        val error = (content as `UsersUiState$Generated`.ErrorState).error
        ErrorWidget(
          modifier = Modifier,
          message = error.message ?: "Something went wrong, please try again!"
        )
      }
    }
    PullRefreshIndicator(refreshing, pullRefreshState, Modifier.align(Alignment.TopCenter))
  }
}