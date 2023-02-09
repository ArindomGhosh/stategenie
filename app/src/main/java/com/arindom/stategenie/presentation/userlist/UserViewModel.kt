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

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arindom.stategenie.repository.UserListRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class UserViewModel(
  private val userListRepository: UserListRepository
) : ViewModel() {
  private val _isRefreshing = MutableStateFlow(false)

  val isRefreshing: StateFlow<Boolean>
    get() = _isRefreshing.asStateFlow()

  private val _uiState =
    MutableStateFlow<`UsersUiState$Generated`>(`UsersUiState$Generated`.LoadingState)
  val uiState: StateFlow<`UsersUiState$Generated`> = _uiState.asStateFlow()

  init {
    getUserList()
  }

  fun refresh() {
    viewModelScope.launch {
      _isRefreshing.emit(true)
    }
    getUserList()
  }

  private fun getUserList() {
    postUiState(newUiState = `UsersUiState$Generated`.LoadingState)
    viewModelScope.launch {
      userListRepository.getGithubUserList()
        .catch {
          postUiState(
            newUiState = `UsersUiState$Generated`.ErrorState(
              error = Throwable(
                message = it.localizedMessage
              )
            )
          )
        }
        .onEach {
          postUiState(
            newUiState = `UsersUiState$Generated`.SuccessState(
              data = it
            )
          )
        }
        .onCompletion {
          _isRefreshing.emit(false)
        }
        .collect()

    }
  }

  private fun postUiState(newUiState: `UsersUiState$Generated`) {
    _uiState.update {
      newUiState
    }
  }
}
