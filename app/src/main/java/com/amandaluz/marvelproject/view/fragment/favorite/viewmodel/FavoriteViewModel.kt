package com.amandaluz.marvelproject.view.fragment.favorite.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amandaluz.marvelproject.core.State
import com.amandaluz.marvelproject.data.db.repository.DatabaseRepository
import com.amandaluz.marvelproject.data.model.Results
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class FavoriteViewModel(
    private val repository: DatabaseRepository,
    private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    fun getCharacters() = repository.getAllCharacters()

    private val _delete = MutableLiveData<State<Boolean>>()
    val delete: LiveData<State<Boolean>> = _delete

    fun deleteCharacter(results: Results) = viewModelScope.launch {
        try {
            _delete.value = State.loading(true)
            withContext(ioDispatcher) {
                repository.deleteCharacter(results)
            }
            _delete.value = State.loading(false)
            _delete.value = State.success(true)
        } catch (e: Exception) {
            _delete.value = State.loading(false)
            _delete.value = State.error(e)
        }
    }
}
