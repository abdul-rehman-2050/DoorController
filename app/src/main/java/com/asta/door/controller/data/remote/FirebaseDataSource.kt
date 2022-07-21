package com.asta.door.controller.data.remote

import com.asta.door.controller.data.model.User
import com.asta.door.controller.utils.wrapSnapshotToClass
import com.asta.door.controller.data.Result
import com.google.firebase.database.*

class FirebaseReferenceValueObserver {
    private var valueEventListener: ValueEventListener? = null
    private var dbRef: DatabaseReference? = null

    fun start(valueEventListener: ValueEventListener, reference: DatabaseReference) {
        reference.addValueEventListener(valueEventListener)
        this.valueEventListener = valueEventListener
        this.dbRef = reference
    }

    fun clear() {
        valueEventListener?.let { dbRef?.removeEventListener(it) }
        valueEventListener = null
        dbRef = null
    }
}

class FirebaseDataSource {
    companion object {
        val dbInstance = FirebaseDatabase.getInstance()
    }

    //region Private

    private fun refToPath(path: String): DatabaseReference {
        return dbInstance.reference.child(path)
    }

    private fun <T> attachValueListenerToBlock(
        resultClassName: Class<T>,
        b: ((Result<T>) -> Unit)
    ): ValueEventListener {
        return (object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                b.invoke(Result.Error(error.message))
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if (wrapSnapshotToClass(resultClassName, snapshot) == null) {
                    b.invoke(Result.Error(msg = snapshot.key))
                } else {
                    b.invoke(Result.Success(wrapSnapshotToClass(resultClassName, snapshot)))
                }
            }
        })
    }

    fun updateNewUser(user: User) {
        refToPath("users/${user.info.id}").setValue(user)
    }

    fun updateLockValue(str: String) {
        refToPath("lock").setValue(str)
    }

    fun updateBuzzerValue(str: String) {
        refToPath("buzzer").setValue(str)
    }

    fun <T> attachUserInfoObserver(
        resultClassName: Class<T>,
        userID: String,
        refObs: FirebaseReferenceValueObserver,
        b: ((Result<T>) -> Unit)
    ) {
        val listener = attachValueListenerToBlock(resultClassName, b)
        refObs.start(listener, refToPath("users/$userID/info"))
    }
}