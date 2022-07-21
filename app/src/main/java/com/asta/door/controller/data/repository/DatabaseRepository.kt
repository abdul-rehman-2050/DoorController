package com.asta.door.controller.data.repository

import com.asta.door.controller.data.model.User
import com.asta.door.controller.data.model.UserInfo
import com.asta.door.controller.data.Result
import com.asta.door.controller.data.remote.FirebaseDataSource
import com.asta.door.controller.data.remote.FirebaseReferenceValueObserver

class DatabaseRepository {
    private val firebaseDatabaseService = FirebaseDataSource()

    fun updateNewUser(user: User) {
        firebaseDatabaseService.updateNewUser(user)
    }

    fun updateOpenLock() {
        firebaseDatabaseService.updateLockValue("1")
    }

    fun updateCloseLock() {
        firebaseDatabaseService.updateLockValue("0")
    }

    fun updateOpenBuzzer() {
        firebaseDatabaseService.updateBuzzerValue("1")
    }

    fun updateCloseBuzzer() {
        firebaseDatabaseService.updateBuzzerValue("0")
    }

    fun loadAndObserveUserInfo(
        userID: String,
        observer: FirebaseReferenceValueObserver,
        b: ((Result<UserInfo>) -> Unit)
    ) {
        firebaseDatabaseService.attachUserInfoObserver(UserInfo::class.java, userID, observer, b)
    }
}