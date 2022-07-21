package com.asta.door.controller.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.asta.door.controller.base.BaseViewModel
import com.asta.door.controller.data.Event
import com.asta.door.controller.data.Result
import com.asta.door.controller.data.model.UserInfo
import com.asta.door.controller.data.remote.FirebaseReferenceValueObserver
import com.asta.door.controller.data.repository.AuthRepository
import com.asta.door.controller.data.repository.DatabaseRepository

class HomeViewModel(
    private val authRepository: AuthRepository,
    private val databaseRepository: DatabaseRepository,
    private val myUserID: String
) : BaseViewModel() {

    private val _userInfo: MutableLiveData<UserInfo> = MutableLiveData()
    private val _logoutEvent = MutableLiveData<Event<Unit>>()

    val userInfo: LiveData<UserInfo> = _userInfo
    val logoutEvent: LiveData<Event<Unit>> = _logoutEvent

    private val firebaseReferenceObserver = FirebaseReferenceValueObserver()

    init {
        loadAndObserveUserInfo()
    }

    override fun onCleared() {
        super.onCleared()
        firebaseReferenceObserver.clear()
    }

    private fun loadAndObserveUserInfo() {
        databaseRepository.loadAndObserveUserInfo(myUserID, firebaseReferenceObserver)
        { result: Result<UserInfo> -> onResult(_userInfo, result) }
    }

    fun updateOpenLock() {
        databaseRepository.updateOpenLock()
    }

    fun updateCloseLock() {
        databaseRepository.updateCloseLock()
    }

    fun updateOpenBuzzer() {
        databaseRepository.updateOpenBuzzer()
    }

    fun updateCloseBuzzer() {
        databaseRepository.updateCloseBuzzer()
    }

    fun logoutPressed() {
        authRepository.logoutUser()
        _logoutEvent.value = Event(Unit)
    }

}