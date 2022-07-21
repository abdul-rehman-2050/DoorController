package com.asta.door.controller.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.asta.door.controller.data.Event
import com.asta.door.controller.data.Result

abstract class BaseViewModel : ViewModel() {
    private val mErrorText = MutableLiveData<Event<String>>()
    val errorText: LiveData<Event<String>> = mErrorText

    private val mDataLoading = MutableLiveData<Event<Boolean>>()
    val dataLoading: LiveData<Event<Boolean>> = mDataLoading

    protected fun <T> onResult(mutableLiveData: MutableLiveData<T>? = null, result: Result<T>) {
        when (result) {
            is Result.Loading -> mDataLoading.value = Event(true)

            is Result.Error -> {
                mDataLoading.value = Event(false)
                result.msg?.let { mErrorText.value = Event(it) }
            }

            is Result.Success -> {
                mDataLoading.value = Event(false)
                result.data?.let { mutableLiveData?.value = it }
            }
        }
    }
}