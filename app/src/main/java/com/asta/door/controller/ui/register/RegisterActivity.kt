package com.asta.door.controller.ui.register

import android.view.inputmethod.EditorInfo
import androidx.lifecycle.Observer
import com.asta.door.controller.AppSettings
import com.asta.door.controller.R
import com.asta.door.controller.base.BaseActivity
import com.asta.door.controller.data.EventObserver
import com.asta.door.controller.data.model.CreateUser
import com.asta.door.controller.databinding.ActivityRegisterBinding
import com.asta.door.controller.ui.home.HomeActivity
import com.asta.door.controller.ui.login.LoginActivity
import com.asta.door.controller.utils.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegisterActivity : BaseActivity<ActivityRegisterBinding>(
    ActivityRegisterBinding::inflate
) {

    private val registerViewModel: RegisterViewModel by viewModel()

    override fun initializeControls() {

    }

    override fun attachListeners() {
        binding.btnCreate.setOnClickListener { registerPressed() }
        binding.tvLoginHere.setOnClickListener {
            startSpecificActivity(LoginActivity::class.java)
            finish()
        }
        binding.editTextDisplayName.addFocusChangeListener {
            if (!it.isDisplayNameValid()) {
                binding.editTextDisplayName.error = getString(R.string.invalid_name)
            } else {
                binding.editTextDisplayName.error = null
            }
        }
        binding.editTextEmail.addFocusChangeListener {
            if (!it.isEmailValid()) {
                binding.editTextEmail.error = getString(R.string.invalid_email)
            } else {
                binding.editTextEmail.error = null
            }
        }
        binding.editTextPassword.addFocusChangeListener {
            if (!it.isPasswordValid()) {
                binding.editTextPassword.error = getString(R.string.invalid_password)
            } else {
                binding.editTextPassword.error = null
            }
        }
        binding.editTextPhone.addFocusChangeListener {
            if (it.isBlank()) {
                binding.editTextPhone.error = getString(R.string.invalid_phone)
            } else {
                binding.editTextPhone.error = null
            }
        }

        binding.editTextPhone.apply {
            binding.editTextPassword.apply {
                setOnEditorActionListener { _, actionId, _ ->
                    when (actionId) {
                        EditorInfo.IME_ACTION_DONE -> {
                            registerPressed()
                        }
                    }
                    false
                }
            }
        }
    }

    override fun observeViewModel() {
        registerViewModel.dataLoading.observe(this, EventObserver {
            hideKeyboard()
            showGlobalProgressBar(it)
        })

        registerViewModel.errorText.observe(this, EventObserver { text ->
            showToast(text)
        })

        registerViewModel.registerFormState.observe(this, Observer {
            val registerStates = it ?: return@Observer

            if (registerStates.nameError != null) {
                binding.editTextDisplayName.error = getString(registerStates.nameError)
            } else {
                binding.editTextDisplayName.error = null
            }
            if (registerStates.emailError != null) {
                binding.editTextEmail.error = getString(registerStates.emailError)
            } else {
                binding.editTextEmail.error = null
            }
            if (registerStates.passwordError != null) {
                binding.editTextPassword.error = getString(registerStates.passwordError)
            } else {
                binding.editTextPassword.error = null
            }
            if (registerStates.phoneError != null) {
                binding.editTextPhone.error = getString(registerStates.phoneError)
            } else {
                binding.editTextPhone.error = null
            }
        })

        registerViewModel.accountCreatedEvent.observe(this, EventObserver {
            AppSettings.userUID = it.uid
            startSpecificActivity(HomeActivity::class.java)
            finish()
        })
    }

    private fun showGlobalProgressBar(show: Boolean) {
        if (show) binding.viewProgress.show()
        else binding.viewProgress.hide()
    }

    private fun registerPressed() {
        hideKeyboard()
        val user = CreateUser().apply {
            displayName = binding.editTextDisplayName.text.toString().trim()
            email = binding.editTextEmail.text.toString().trim()
            password = binding.editTextPassword.text.toString().trim()
            phone = binding.editTextPhone.text.toString().trim()
        }
        registerViewModel.registerPressed(user)
    }
}