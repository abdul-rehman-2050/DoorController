package com.asta.door.controller.dialog

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import com.asta.door.controller.R
import com.asta.door.controller.utils.showToast
import com.asta.door.controller.utils.toEditable

class LocalHostDialog : DialogFragment() {

    var onSaveLocalHost: (String) -> Unit = {}
    var onClearLocalHost: () -> Unit = {}

    private lateinit var mActivity: FragmentActivity
    lateinit var localHostURL: String

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is Activity) {
            mActivity = context as FragmentActivity
        }
    }

    @SuppressLint("UseGetLayoutInflater")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialogBuilder = AlertDialog.Builder(mActivity)
        val view = LayoutInflater.from(mActivity).inflate(R.layout.dialog_local_host, null)
        val etLocalHost = view.findViewById<EditText>(R.id.etLocalHost)
        val btnSave = view.findViewById<Button>(R.id.btnSave)
        val btnClear = view.findViewById<Button>(R.id.btnClear)
        val btnCancel = view.findViewById<Button>(R.id.btnCancel)
        dialogBuilder.setView(view)

        etLocalHost.text = localHostURL.toEditable()

        btnSave.setOnClickListener {
            val url = etLocalHost.text.toString().trim()
            if (url.isBlank()){
                showToast("Please enter url")
                return@setOnClickListener
            }
            dialog!!.dismiss()
            onSaveLocalHost.invoke(url)
        }
        btnClear.setOnClickListener {
            dialog!!.dismiss()
            onClearLocalHost.invoke()
        }
        btnCancel.setOnClickListener {
            dialog!!.dismiss()
        }
        return dialogBuilder.create()
    }
}