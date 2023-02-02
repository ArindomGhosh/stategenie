package com.arindom.stategenie.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arindom.stategenie.UsersListRepository
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class UserViewModel(
    usersListRepository: UsersListRepository
) : ViewModel() {

    private val _uiState =
        MutableStateFlow<`UsersUiState$Generated`>(`UsersUiState$Generated`.LoadingState)
    val uiState: StateFlow<`UsersUiState$Generated`> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            delay(3000)
            postUiState(
                newUiState = `UsersUiState$Generated`.SuccessState(
                    data = usersListRepository.getUsersList()
                )
            )
        }
    }

    private fun postUiState(newUiState: `UsersUiState$Generated`) {
        _uiState.update {
            newUiState
        }
    }
}