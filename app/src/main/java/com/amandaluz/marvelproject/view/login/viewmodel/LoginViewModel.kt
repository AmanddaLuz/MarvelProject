package com.amandaluz.marvelproject.view.login.viewmodel

import android.util.Patterns
import androidx.lifecycle.*
import com.amandaluz.marvelproject.R
import com.amandaluz.marvelproject.core.State
import com.amandaluz.marvelproject.data.model.User
import com.amandaluz.marvelproject.data.repository.loginrepository.LoginRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException
import kotlin.jvm.Throws

class LoginViewModel(
    private val repository: LoginRepository
) : ViewModel() {

    private val _loginFieldErrorResId = MutableLiveData<Int?>()
    val loginFieldErrorResId: LiveData<Int?> = _loginFieldErrorResId

    private val _passwordFieldErrorResId = MutableLiveData<Int?>()
    val passwordFieldErrorResId: LiveData<Int?> = _passwordFieldErrorResId

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _user = MutableLiveData<State<User>>()
    val user: LiveData<State<User>> = _user

    private var isValid = false

    fun login(email: String, password: String) =
        viewModelScope.launch {

            isValid = true

            _loginFieldErrorResId.value = getErrorStringResIdEmptyEmail(email)
            _passwordFieldErrorResId.value = getErrorStringResIdEmptyPassword(password)

            if (isValid) {
                _loading.value = true
                try {
                    delay(3000)

                    val response = repository.login(email, password)
                    _user.value = State.success(response)
                    _loading.value = false
                } catch (e: Exception) {
                    _loading.value = false
                    _user.value = State.error(e)
                }
            }
        }

    private fun getErrorStringResIdEmptyEmail(value: String): Int? =
        if (!Patterns.EMAIL_ADDRESS.matcher(value).matches()) {
            isValid = false
            R.string.invalid_email
        } else null

    private fun getErrorStringResIdEmptyPassword(value: String): Int? =
        when {
            value.isEmpty() -> {
                isValid = false
                R.string.required_field
            }
            value.length < 8 -> {
                isValid = false
                R.string.small_password
            }
            else -> null
        }

    class LoginViewModelProviderFactory(
        private val repository: LoginRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
                return LoginViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown viewModel Class")
        }
    }
}