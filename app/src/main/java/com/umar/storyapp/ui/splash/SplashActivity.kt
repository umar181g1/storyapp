package com.umar.storyapp.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.umar.storyapp.R
import com.umar.storyapp.ui.login.LoginActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val splashTime: Long = 3000 // lama splashscreen berjalan

        Handler().postDelayed({
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent) // Pindah ke Home Activity setelah 3 detik
            finish()
        }, splashTime)
    }
}