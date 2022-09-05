package com.amandaluz.marvelproject.view.register.fragment.passwordfragment.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.amandaluz.marvelproject.R
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PasswordViewModel @Inject constructor () : ViewModel() {

    private val _passwordFieldErrorResId = MutableLiveData<Int?>()
    val passwordFieldErrorResId: LiveData<Int?> = _passwordFieldErrorResId

    private val _passwordIsDifferentFieldErrorResId = MutableLiveData<Int?>()
    val passwordIsDifferentFieldErrorResId: LiveData<Int?> = _passwordIsDifferentFieldErrorResId

    fun checkIfPasswordAreValid(password: String, passwordConfirmation: String): Boolean {
        _passwordFieldErrorResId.value = getErrorStringResIdInvalidPassword(password)

        return if (passwordConfirmation == password) {
            true
        } else {
            _passwordIsDifferentFieldErrorResId.value =
                getErrorStringResIdIfPasswordAreDifferent(password, passwordConfirmation)
            false
        }
    }

    private fun getErrorStringResIdInvalidPassword(value: String): Int? =
        when {
            value.isEmpty() -> R.string.required_field
            value.length < 8 -> R.string.invalid_password
            else -> null
        }

    private fun getErrorStringResIdIfPasswordAreDifferent(
        password: String,
        passwordConfirmation: String
    ): Int? =
        if (passwordConfirmation != password) {
            R.string.different_password
        } else null
}