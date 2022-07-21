package com.asta.door.controller.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.asta.door.controller.R
import com.asta.door.controller.base.BaseViewModel
import com.asta.door.controller.data.Event
import com.asta.door.controller.data.Result
import com.asta.door.controller.data.model.CreateUser
import com.asta.door.controller.data.model.RegisterFormState
import com.asta.door.controller.data.model.User
import com.asta.door.controller.data.repository.AuthRepository
import com.asta.door.controller.data.repository.DatabaseRepository
import com.asta.door.controller.utils.isDisplayNameValid
import com.asta.door.controller.utils.isEmailValid
import com.asta.door.controller.utils.isPasswordValid
import com.google.firebase.auth.FirebaseUser

class RegisterViewModel(
    private val authRepository: AuthRepository,
    private val databaseRepository: DatabaseRepository
) : BaseViewModel() {

    private val mRegisterFormState = MutableLiveData<RegisterFormState>()
    private val mAccountCreatedEvent = MutableLiveData<Event<FirebaseUser>>()

    val registerFormState: LiveData<RegisterFormState> = mRegisterFormState
    val accountCreatedEvent: LiveData<Event<FirebaseUser>> = mAccountCreatedEvent

    fun registerPressed(createUser: CreateUser) {

        val name = createUser.displayName
        val email = createUser.email
        val password = createUser.password
        val phone = createUser.phone

        mRegisterFormState.value = RegisterFormState(enableButton = false)

        if (!name.isDisplayNameValid()) {
            mRegisterFormState.value =
                RegisterFormState(nameError = R.string.invalid_name, enableButton = true)
            return
        }

        if (!email.isEmailValid()) {
            mRegisterFormState.value =
                RegisterFormState(emailError = R.string.invalid_email, enableButton = true)
            return
        }
        if (!password.isPasswordValid()) {
            mRegisterFormState.value =
                RegisterFormState(passwordError = R.string.invalid_password, enableButton = true)
            return
        }
        if (phone.isEmpty()) {
            mRegisterFormState.value =
                RegisterFormState(phoneError = R.string.invalid_phone, enableButton = true)
            return
        }

        authRepository.createUser(createUser) { result: Result<FirebaseUser> ->
            onResult(null, result)
            if (result is Result.Success) {
                mAccountCreatedEvent.value = Event(result.data!!)
                databaseRepository.updateNewUser(User().apply {
                    info.id = result.data.uid
                    info.displayName = createUser.displayName
                    info.email = createUser.email
                    info.phone = createUser.phone
                })
            }
            if (result is Result.Success || result is Result.Error) {
                mRegisterFormState.value = RegisterFormState(enableButton = true)
            }
        }
    }

}