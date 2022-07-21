package com.asta.door.controller.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.asta.door.controller.R
import com.asta.door.controller.base.BaseViewModel
import com.asta.door.controller.data.Event
import com.asta.door.controller.data.Result
import com.asta.door.controller.data.model.Login
import com.asta.door.controller.data.model.LoginFormState
import com.asta.door.controller.data.repository.AuthRepository
import com.asta.door.controller.utils.isEmailValid
import com.asta.door.controller.utils.isPasswordValid
import com.google.firebase.auth.FirebaseUser

class LoginViewModel(
    private val authRepository: AuthRepository
) : BaseViewModel() {
    private val mLoggedInEvent = MutableLiveData<Event<FirebaseUser>>()
    private val mLoginFormState = MutableLiveData<LoginFormState>()

    val loggedInEvent: LiveData<Event<FirebaseUser>> = mLoggedInEvent
    val loginFormState: LiveData<LoginFormState> = mLoginFormState

    fun loginPressed(login: Login) {
        val email = login.email
        val password = login.password

        mLoginFormState.value = LoginFormState(enableButton = false)

        if (!email.isEmailValid()) {
            mLoginFormState.value =
                LoginFormState(emailError = R.string.invalid_email, enableButton = true)
            return
        }
        if (!password.isPasswordValid()) {
            mLoginFormState.value =
                LoginFormState(passwordError = R.string.invalid_password, enableButton = true)
            return
        }
        authRepository.loginUser(login) { result: Result<FirebaseUser> ->
            onResult(null, result)
            if (result is Result.Success) mLoggedInEvent.value = Event(result.data!!)
            if (result is Result.Success || result is Result.Error) {
                mLoginFormState.value = LoginFormState(enableButton = true)
            }
        }
    }
}