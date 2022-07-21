package com.asta.door.controller.ui.splash

import android.animation.Animator
import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import android.view.animation.OvershootInterpolator
import com.asta.door.controller.AppSettings
import com.asta.door.controller.base.BaseActivity
import com.asta.door.controller.databinding.ActivitySplashBinding
import com.asta.door.controller.ui.home.HomeActivity
import com.asta.door.controller.ui.login.LoginActivity

@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity<ActivitySplashBinding>(
    ActivitySplashBinding::inflate
) {

    override fun initializeControls() {
        binding.ivSplashImage.scaleX = 0F
        binding.ivSplashImage.scaleY = 0F

        binding.ivSplashImage
            .animate()
            .scaleX(1F)
            .scaleY(1F)
            .setDuration(1000)
            .setInterpolator(OvershootInterpolator())
            .setStartDelay(200)
            .setListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animator: Animator) {}
                override fun onAnimationEnd(animator: Animator) {
                    callNextActivity()
                }

                override fun onAnimationCancel(animator: Animator) {}
                override fun onAnimationRepeat(animator: Animator) {}
            }).start()
    }

    override fun attachListeners() {

    }

    private fun callNextActivity() {
        Handler(Looper.getMainLooper()).postDelayed({
            if (AppSettings.userUID.isEmpty()) {
                startSpecificActivity(LoginActivity::class.java)
            } else {
                startSpecificActivity(HomeActivity::class.java)
            }
            finish()
        }, 1000)
    }
}