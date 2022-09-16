package com.umar.storyapp.register

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.umar.storyapp.R
import com.umar.storyapp.databinding.ActivityRegisterBinding
import com.umar.storyapp.factoryviewmodel.UserFactoryVM
import com.umar.storyapp.model.Result

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var regisViewModel: RegisViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupViewModel()
        setupAction()
        playAnimation()
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
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
        regisViewModel = ViewModelProvider(this, factory)[RegisViewModel::class.java]
    }

    private fun setupAction() {
        binding.signupButton.setOnClickListener {
            val name = binding.nameEditText.text.toString()
            val email = binding.emailEditText.text.toString()
            val pass = binding.passwordEditText.text.toString()
            when {
                name.isEmpty() -> {
                    binding.nameEditText.error = resources.getString(R.string.name)
                }
                email.isEmpty() -> {
                    binding.emailEditText.error = resources.getString(R.string.email)
                }
                pass.isEmpty() -> {
                    binding.passwordEditText.error = resources.getString(R.string.password)
                }
                else -> {
                    regisViewModel.register(name, email, pass).observe(this) { result ->
                        if (result != null) {
                            when (result) {
                                is Result.Loading -> {
                                    showLoad(true)
                                }
                                is Result.Success -> {
                                    showLoad(false)
                                    val data = result.data
                                    if (data.error == true) {
                                        Toast.makeText(
                                            this@RegisterActivity,
                                            data.message,
                                            Toast.LENGTH_LONG
                                        ).show()
                                    } else {
                                        AlertDialog.Builder(this).apply {
                                            setTitle("yeahhh")
                                            setMessage("Akun Anda BerHasil di buat")
                                            setPositiveButton("lanjut") { _, _ ->
                                                finish()
                                            }
                                            create()
                                            show()
                                        }
                                    }
                                }
                                else -> {}
                            }
                        }
                    }
                }
            }
        }
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(binding.titleName, View.ALPHA, 1f).setDuration(500)
        val nameTextView =
            ObjectAnimator.ofFloat(binding.nameTextView, View.ALPHA, 1f).setDuration(500)
        val nameEditTextLayout =
            ObjectAnimator.ofFloat(binding.nameEditTextLayout, View.ALPHA, 1f).setDuration(500)
        val emailTextView =
            ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(500)
        val emailEditTextLayout =
            ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(500)
        val passwordTextView =
            ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(500)
        val passwordEditTextLayout =
            ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(500)
        val signup = ObjectAnimator.ofFloat(binding.signupButton, View.ALPHA, 1f).setDuration(500)


        AnimatorSet().apply {
            playSequentially(
                title,
                nameTextView,
                nameEditTextLayout,
                emailTextView,
                emailEditTextLayout,
                passwordTextView,
                passwordEditTextLayout,
                signup
            )
            startDelay = 500
        }.start()
    }

    private fun showLoad(isLoading: Boolean) {
        binding.apply {
            emailEditText.isEnabled = !isLoading
            passwordEditText.isEnabled = !isLoading
            signupButton.isEnabled = !isLoading

            if (isLoading) {
                progressBar.visibility = View.VISIBLE
            } else {
                progressBar.visibility = View.GONE
            }
        }

    }
}