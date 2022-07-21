package com.asta.door.controller.data.model

import com.google.firebase.database.PropertyName

data class LoginFormState(
    val emailError: Int? = null,
    val passwordError: Int? = null,
    val enableButton: Boolean = false
)

data class RegisterFormState(
    val emailError: Int? = null,
    val passwordError: Int? = null,
    val nameError: Int? = null,
    val phoneError: Int? = null,
    val enableButton: Boolean = false
)

data class Login(
    var email: String = "",
    var password: String = ""
)

data class CreateUser(
    var displayName: String = "",
    var email: String = "",
    var password: String = "",
    var phone: String = ""
)

data class User(
    @get:PropertyName("info") @set:PropertyName("info") var info: UserInfo = UserInfo()
)

data class UserInfo(
    @get:PropertyName("id") @set:PropertyName("id") var id: String = "",
    @get:PropertyName("displayName") @set:PropertyName("displayName") var displayName: String = "",
    @get:PropertyName("email") @set:PropertyName("email") var email: String = "",
    @get:PropertyName("phone") @set:PropertyName("phone") var phone: String = ""
)