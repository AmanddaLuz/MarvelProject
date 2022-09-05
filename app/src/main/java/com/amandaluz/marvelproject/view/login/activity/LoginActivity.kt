package com.amandaluz.marvelproject.view.login.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.amandaluz.marvelproject.core.Status
import com.amandaluz.marvelproject.data.db.AppDatabase
import com.amandaluz.marvelproject.data.db.CharacterDAO
import com.amandaluz.marvelproject.data.model.User
import com.amandaluz.marvelproject.data.repository.loginrepository.LoginRepository
import com.amandaluz.marvelproject.data.repository.loginrepository.LoginRepositoryImpl
import com.amandaluz.marvelproject.databinding.ActivityLoginBinding
import com.amandaluz.marvelproject.util.Watcher
import com.amandaluz.marvelproject.util.setError
import com.amandaluz.marvelproject.util.toast
import com.amandaluz.marvelproject.view.home.activity.HomeActivity
import com.amandaluz.marvelproject.view.register.RegisterActivity
import com.amandaluz.marvelproject.view.login.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val viewModel by viewModels<LoginViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityLoginBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        observeVMEvents()

        binding.run {
            onClickLoginButton()
            goToRegister()
            loginUsernameEdit.addTextChangedListener(watcher)
            loginPasswordEdit.addTextChangedListener(watcher)
        }
        binding.run {
            loginUsernameEdit.setText("a@gmail.com")
            loginPasswordEdit.setText("11111111")
        }

    }

    private fun ActivityLoginBinding.goToRegister() {
        loginRegisterButton.setOnClickListener {
            goTo(null, RegisterActivity::class.java)
        }
    }

    private fun ActivityLoginBinding.onClickLoginButton() {
        btnLogin.setOnClickListener {
            val email = binding.loginUsernameEdit.text.toString()
            val password = binding.loginPasswordEdit.text.toString()

            viewModel.login(email, password)
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
        viewModel.user.observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let { user ->
                        goTo(user, HomeActivity::class.java)
                    }
                }
                Status.ERROR -> {
                    toast("Erro ao logar!")
                    Timber.tag("Login").i(it.error)
                }
                Status.LOADING -> {}
            }
        }
    }

    private fun <T> goTo(user: User?, clazz: Class<T>) {
        val intent = Intent(this@LoginActivity, clazz)
        user?.let {
            intent.putExtra("USER", user)
        }
        startActivity(intent)
        finish()
    }
}


