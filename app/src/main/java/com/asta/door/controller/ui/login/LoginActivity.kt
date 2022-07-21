package com.asta.door.controller.ui.login

import android.view.inputmethod.EditorInfo
import androidx.lifecycle.Observer
import com.asta.door.controller.AppSettings
import com.asta.door.controller.R
import com.asta.door.controller.base.BaseActivity
import com.asta.door.controller.data.EventObserver
import com.asta.door.controller.data.model.Login
import com.asta.door.controller.databinding.ActivityLoginBinding
import com.asta.door.controller.ui.home.HomeActivity
import com.asta.door.controller.ui.register.RegisterActivity
import com.asta.door.controller.utils.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : BaseActivity<ActivityLoginBinding>(
    ActivityLoginBinding::inflate
) {

    private val loginViewModel: LoginViewModel by viewModel()

    override fun initializeControls() {

    }

    override fun attachListeners() {

        binding.btnLogin.setOnClickListener { loginPressed() }
        binding.tvRegisterHere.setOnClickListener {
            startSpecificActivity(RegisterActivity::class.java)
            finish()
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

        binding.editTextPassword.apply {
            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE -> {
                        loginPressed()
                    }
                }
                false
            }
        }
    }

    override fun observeViewModel() {
        loginViewModel.dataLoading.observe(this, EventObserver {
            binding.editTextEmail.clearFocus()
            binding.editTextPassword.clearFocus()
            showGlobalProgressBar(it)
        })

        loginViewModel.errorText.observe(this, EventObserver { text ->
            showToast(text)
        })

        loginViewModel.loginFormState.observe(this@LoginActivity, Observer {
            val loginState = it ?: return@Observer
            if (loginState.emailError != null) {
                binding.editTextEmail.error = getString(loginState.emailError)
            } else {
                binding.editTextEmail.error = null
            }
            if (loginState.passwordError != null) {
                binding.editTextPassword.error = getString(loginState.passwordError)
            } else {
                binding.editTextPassword.error = null
            }

            binding.btnLogin.isEnabled = loginState.enableButton
        })

        loginViewModel.loggedInEvent.observe(this, EventObserver {
            AppSettings.userUID = it.uid
            startSpecificActivity(HomeActivity::class.java)
            finish()
        })
    }

    private fun loginPressed() {
        hideKeyboard()
        val login = Login().apply {
            email = binding.editTextEmail.text.toString().trim()
            password = binding.editTextPassword.text.toString().trim()
        }
        loginViewModel.loginPressed(login)
    }

    private fun showGlobalProgressBar(show: Boolean) {
        if (show) binding.viewProgress.show()
        else binding.viewProgress.hide()
    }

}