package com.arindom.stategenie.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arindom.stategenie.repository.UserListRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class UserViewModel(
    userListRepository: UserListRepository
) : ViewModel() {

    private val _uiState =
        MutableStateFlow<`UsersUiState$Generated`>(`UsersUiState$Generated`.LoadingState)
    val uiState: StateFlow<`UsersUiState$Generated`> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            postUiState(newUiState = `UsersUiState$Generated`.LoadingState)
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
                .collect{
                    postUiState(
                        newUiState = `UsersUiState$Generated`.SuccessState(
                            data = it
                        )
                    )
                }
        }
    }

    private fun postUiState(newUiState: `UsersUiState$Generated`) {
        _uiState.update {
            newUiState
        }
    }
}