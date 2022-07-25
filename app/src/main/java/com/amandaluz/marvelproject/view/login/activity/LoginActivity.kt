package com.amandaluz.marvelproject.view.login.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.amandaluz.marvelproject.R
import com.amandaluz.marvelproject.databinding.ActivityLoginBinding
import com.amandaluz.marvelproject.util.Watcher
import com.amandaluz.marvelproject.util.setError
import com.amandaluz.marvelproject.view.login.viewmodel.LoginViewModel

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityLoginBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        viewModel = LoginViewModel()

        observeVMEvents()

        binding.run {
            btnLogin.setOnClickListener {
                val email = binding.loginUsernameEdit.text.toString()
                val password = binding.loginPasswordEdit.text.toString()

                viewModel.login(email, password)
            }
            loginUsernameEdit.addTextChangedListener(watcher)
            loginPasswordEdit.addTextChangedListener(watcher)
        }
    }


    private val watcher = Watcher {
        binding.btnLogin.isEnabled = binding.loginUsernameEdit.text.toString().isNotEmpty() &&
                binding.loginPasswordEdit.text.toString().isNotEmpty()
    }

    private fun observeVMEvents() {
        viewModel.loginFieldErrorResId.observe(this) {
            binding.loginUsernameLayout.setError(this@LoginActivity, it)
        }
        viewModel.passwordFieldErrorResId.observe(this) {
            binding.loginPasswordLayout.setError(this@LoginActivity, it)
        }
        viewModel.loading.observe(this) {
            binding.btnLogin.progress(it)
        }
    }
}
