package com.asta.door.controller.data.remote

import com.asta.door.controller.data.model.CreateUser
import com.asta.door.controller.data.model.Login
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth

class FirebaseAuthSource {

    companion object {
        val authInstance = FirebaseAuth.getInstance()
    }

    fun loginWithEmailAndPassword(login: Login): Task<AuthResult> {
        return authInstance.signInWithEmailAndPassword(login.email, login.password)
    }

    fun createUser(createUser: CreateUser): Task<AuthResult> {
        return authInstance.createUserWithEmailAndPassword(createUser.email, createUser.password)
    }

    fun logout() {
        authInstance.signOut()
    }
}