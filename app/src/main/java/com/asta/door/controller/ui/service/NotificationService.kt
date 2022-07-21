package com.asta.door.controller.ui.service

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.asta.door.controller.R
import com.asta.door.controller.ui.home.HomeActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import io.karn.notify.Notify

class NotificationService : Service() {
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        FirebaseDatabase.getInstance().getReference("notify")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val value = snapshot.getValue(String()::class.java)
                    if (value != null) {
                        showNotification(value)
                    }
                }

                override fun onCancelled(error: DatabaseError) {}

            })
        return START_STICKY
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun showNotification(str: String) {
        Notify.with(this@NotificationService)
            .meta {
                clickIntent = PendingIntent.getActivity(
                    this@NotificationService,
                    0,
                    Intent(
                        this@NotificationService,
                        HomeActivity::class.java
                    ).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        .setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP),
                    FLAG_UPDATE_CURRENT
                )
            }
            .content { // this: Payload.Content.Default
                title = getString(R.string.app_name)
                text = str
            }
            .show()
    }
}