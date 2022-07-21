package com.asta.door.controller.data.repository

import com.asta.door.controller.data.model.CreateUser
import com.asta.door.controller.data.model.Login
import com.asta.door.controller.data.remote.FirebaseAuthSource
import com.google.firebase.auth.FirebaseUser
import com.asta.door.controller.data.Result

class AuthRepository {

    private val firebaseAuthService = FirebaseAuthSource()

    fun loginUser(login: Login, b: ((Result<FirebaseUser>) -> Unit)) {
        b.invoke(Result.Loading)
        firebaseAuthService.loginWithEmailAndPassword(login).addOnSuccessListener {
            b.invoke(Result.Success(it.user))
        }.addOnFailureListener {
            b.invoke(Result.Error(msg = it.message))
        }
    }

    fun createUser(createUser: CreateUser, b: ((Result<FirebaseUser>) -> Unit)) {
        b.invoke(Result.Loading)
        firebaseAuthService.createUser(createUser).addOnSuccessListener {
            b.invoke(Result.Success(it.user))
        }.addOnFailureListener {
            b.invoke(Result.Error(msg = it.message))
        }
    }

    fun logoutUser() {
        firebaseAuthService.logout()
    }
}