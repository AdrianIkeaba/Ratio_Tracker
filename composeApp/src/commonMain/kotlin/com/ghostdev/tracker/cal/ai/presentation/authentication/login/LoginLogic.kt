package com.ghostdev.tracker.cal.ai.presentation.authentication.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ghostdev.tracker.cal.ai.data.repo.user.UserRepo
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.transform
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


class LoginLogic : ViewModel(), KoinComponent {
    private val userRepo: UserRepo by inject()

    val loginState: Flow<LoginState> = userRepo.isUserExist()
        .transform { exists ->
            emit(LoginState.Loading)
            delay(500)
            emit(LoginState.Authenticated(exists))
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = LoginState.Loading
        )
}

sealed class LoginState {
    data object Loading : LoginState()
    data class Authenticated(val exists: Boolean) : LoginState()
}