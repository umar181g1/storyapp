package com.umar.storyapp.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.umar.storyapp.MainActivity
import com.umar.storyapp.R
import com.umar.storyapp.databinding.ActivityLoginBinding
import com.umar.storyapp.factoryviewmodel.UserFactoryVM
import com.umar.storyapp.model.ResponseLogin
import com.umar.storyapp.model.Result
import com.umar.storyapp.register.RegisterActivity


class LoginActivity : AppCompatActivity() {

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var login: ResponseLogin
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupViewModel()
        setupAction()
        playAnimation()

    }


    private fun setupView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupViewModel() {
        val factory: UserFactoryVM = UserFactoryVM.getInstance(this)
        loginViewModel = ViewModelProvider(this, factory)[LoginViewModel::class.java]

        loginViewModel.getToken().observe(this) { token ->
            startActivity(Intent(this, MainActivity::class.java))
            finish()

        }

    }


    private fun setupAction() {
        binding.loginButton.setOnClickListener {
            val email = binding.edtEmail.text.toString()
            val password = binding.edtPass.text.toString()
            when {
                email.isEmpty() -> {
                    binding.edtEmail.error = resources.getString(R.string.msq)
                }

                password.isEmpty() -> {
                    binding.edtPass.error = resources.getString(R.string.msa)
                }

                else -> {
                    loginViewModel.login(email, password).observe(this) { result ->
                        if (result != null) {
                            when (result) {
                                is Result.Loading -> {
                                    showLoading(true)
                                }
                                is Result.Success -> {
                                    showLoading(false)
                                    val data = result.data
                                    if (data.error == true) {
                                        Toast.makeText(
                                            this@LoginActivity,
                                            data.message,
                                            Toast.LENGTH_LONG
                                        ).show()
                                    } else {
                                        val token = data.loginResult?.token ?: ""
                                        loginViewModel.setToken(token, true)
                                    }
                                }
                                is Result.Error -> {
                                    showLoading(false)
                                    Toast.makeText(
                                        this,
                                        resources.getString(R.string.loginer),
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }
                        }
                    }
                }
            }
        }
        binding.register.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 7000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val emailTextView =
            ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(500)
        val emailEditTextLayout =
            ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(500)
        val passwordTextView =
            ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(500)
        val passwordEditTextLayout =
            ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(500)
        val login = ObjectAnimator.ofFloat(binding.loginButton, View.ALPHA, 1f).setDuration(500)
        val tvReg = ObjectAnimator.ofFloat(binding.noAcunt, View.ALPHA, 1f).setDuration(500)
        val tvRegLog = ObjectAnimator.ofFloat(binding.register, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(
                emailTextView,
                emailEditTextLayout,
                passwordTextView,
                passwordEditTextLayout,
                login,
                tvReg,
                tvRegLog
            )
            startDelay = 500
        }.start()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.apply {
            edtEmail.isEnabled = !isLoading
            edtPass.isEnabled = !isLoading
            loginButton.isEnabled = !isLoading

            if (isLoading) {
                progressBar.visibility = View.VISIBLE
            } else {
                progressBar.visibility = View.GONE
            }
        }
    }
}