package com.amandaluz.marvelproject.view.login.register.fragment.accountfragment.viewmodel

import android.util.Patterns
import androidx.lifecycle.*
import com.amandaluz.marvelproject.R
import com.amandaluz.marvelproject.core.State
import com.amandaluz.marvelproject.data.model.User
import com.amandaluz.marvelproject.view.login.repository.RegisterRepository
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class AccountViewModel : ViewModel() {

    private val _emailFieldErrorResId = MutableLiveData<Int?>()
    val emailFieldErrorResId: LiveData<Int?> = _emailFieldErrorResId

    private val _nameFieldErrorResId = MutableLiveData<Int?>()
    val nameFieldErrorResId: LiveData<Int?> = _nameFieldErrorResId

    private val _sameEmailsFieldErrorResId = MutableLiveData<Int?>()
    val sameEmailsFieldErrorResId: LiveData<Int?> = _sameEmailsFieldErrorResId

    fun checkIfAllFieldAreValid(email: String, emailConfirmation: String, name: String): Boolean {

        _nameFieldErrorResId.value = getErrorStringResIdInvalidName(name)
        _emailFieldErrorResId.value = getErrorStringResIdEmptyEmail(email)

        return if (emailConfirmation == email && name.isNotEmpty()) {
            true
        } else {
            _sameEmailsFieldErrorResId.value =
                getErrorStringResIdEmailIsNotTheSame(email, emailConfirmation)
            false
        }
    }

    private fun getErrorStringResIdEmptyEmail(value: String): Int? =
        if (!Patterns.EMAIL_ADDRESS.matcher(value).matches()) {
            R.string.invalid_email
        } else null

    private fun getErrorStringResIdEmailIsNotTheSame(
        email: String,
        emailConfirmation: String
    ): Int? =
        if (emailConfirmation != email) {
            R.string.email_confirmation
        } else null

    private fun getErrorStringResIdInvalidName(
        name: String
    ): Int? =
        if (name.isEmpty()) {
            R.string.invalid_name
        } else null

    class AccountViewModelProvideFactory(
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(AccountViewModel::class.java)) {
                return AccountViewModel() as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }

    }
}