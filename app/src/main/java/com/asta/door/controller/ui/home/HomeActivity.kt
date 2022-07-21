package com.asta.door.controller.ui.home

import android.app.ActivityManager
import android.content.Intent
import com.asta.door.controller.AppSettings
import com.asta.door.controller.base.BaseActivity
import com.asta.door.controller.data.EventObserver
import com.asta.door.controller.databinding.ActivityHomeBinding
import com.asta.door.controller.dialog.LocalHostDialog
import com.asta.door.controller.ui.browse.BrowseActivity
import com.asta.door.controller.ui.login.LoginActivity
import com.asta.door.controller.ui.service.NotificationService
import com.asta.door.controller.utils.showToast
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class HomeActivity : BaseActivity<ActivityHomeBinding>(
    ActivityHomeBinding::inflate
) {

    private val homeViewModel: HomeViewModel by viewModel { parametersOf(AppSettings.userUID) }

    override fun initializeControls() {
//        AppSettings.localHostURL = "https://www.google.com/"
        if (!isServiceEnabled()) {
            startService(Intent(this, NotificationService::class.java))
        }
    }

    override fun attachListeners() {
        binding.btnLockOpen.setOnClickListener { openLock() }
        binding.btnLockClose.setOnClickListener { closeLock() }
        binding.btnBuzzerOn.setOnClickListener { openBuzzer() }
        binding.btnBuzzerOff.setOnClickListener { closeBuzzer() }
        binding.ivSettings.setOnClickListener { showLocalHostDialog() }
        binding.btnViewCamera.setOnClickListener { showBrowserActivity() }
        binding.btnLogout.setOnClickListener { homeViewModel.logoutPressed() }
    }

    override fun observeViewModel() {
        homeViewModel.logoutEvent.observe(this, EventObserver {
            logoutUser()
        })
        homeViewModel.userInfo.observe(this) {
            binding.tvWelcomeText.text = it.displayName
        }
    }

    private fun openLock() {
        homeViewModel.updateOpenLock()
    }

    private fun closeLock() {
        homeViewModel.updateCloseLock()
    }

    private fun openBuzzer() {
        homeViewModel.updateOpenBuzzer()
    }

    private fun closeBuzzer() {
        homeViewModel.updateCloseBuzzer()
    }

    private fun showLocalHostDialog() {
        val dialog = LocalHostDialog()
        dialog.localHostURL = AppSettings.localHostURL
        dialog.onSaveLocalHost = { AppSettings.localHostURL = it }
        dialog.onClearLocalHost = { AppSettings.localHostURL = "" }
        dialog.show(supportFragmentManager, "show_local_host_dialog")
    }

    private fun showBrowserActivity() {
        val url = AppSettings.localHostURL
        if (url.isNotBlank()) {
            startSpecificActivity(BrowseActivity::class.java)
        } else {
            showToast("Please enter local host url first.")
        }
    }

    private fun logoutUser() {
        AppSettings.userUID = ""
        startSpecificActivity(LoginActivity::class.java)
        finish()
    }

    private fun isServiceEnabled(): Boolean {
        val activityManager = getSystemService(ACTIVITY_SERVICE) as ActivityManager
        for (serviceInfo in activityManager.getRunningServices(Int.MAX_VALUE)) {
            if (NotificationService::class.java.name == serviceInfo.service.className) {
                return true
            }
        }
        return false
    }
}