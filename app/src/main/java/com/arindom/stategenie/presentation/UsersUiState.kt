package com.arindom.stategenie.presentation

import com.arindom.stategenie.annotations.StateGenie
import com.arindom.stategenie.data.model.User

@StateGenie(
    rootName = "UsersUiState"
)
interface UsersUiState: BaseUIiState<List<User>>