package com.amandaluz.marvelproject.view.login.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RestrictTo
import com.amandaluz.marvelproject.R
import com.amandaluz.marvelproject.core.Status
import com.amandaluz.marvelproject.data.db.AppDatabase
import com.amandaluz.marvelproject.data.db.CharacterDAO
import com.amandaluz.marvelproject.data.model.User
import com.amandaluz.marvelproject.data.repository.loginrepository.LoginRepository
import com.amandaluz.marvelproject.data.repository.loginrepository.LoginRepositoryImpl
import com.amandaluz.marvelproject.data.repository.loginrepository.LoginRepositoryMock
import com.amandaluz.marvelproject.databinding.ActivityLoginBinding
import com.amandaluz.marvelproject.util.Watcher
import com.amandaluz.marvelproject.util.setError
import com.amandaluz.marvelproject.util.toast
import com.amandaluz.marvelproject.view.home.activity.HomeActivity
import com.amandaluz.marvelproject.view.login.viewmodel.LoginViewModel
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import timber.log.Timber

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel
    private lateinit var repository: LoginRepository
    private val dao: CharacterDAO by lazy { AppDatabase.getDb(applicationContext).characterDao() }

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityLoginBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        repository = LoginRepositoryImpl(dao)
        viewModel = LoginViewModel.LoginViewModelProviderFactory(repository)
            .create(LoginViewModel::class.java)

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


