package com.amandaluz.marvelproject.view.fragment.favorite.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amandaluz.marvelproject.core.State
import com.amandaluz.marvelproject.data.db.repository.DatabaseRepository
import com.amandaluz.marvelproject.data.model.Favorites
import com.amandaluz.marvelproject.data.model.Results
import com.amandaluz.marvelproject.di.qualifier.IO
import dagger.hilt.InstallIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val repository: DatabaseRepository,
    @IO private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _delete = MutableLiveData<State<Boolean>>()
    val delete: LiveData<State<Boolean>> = _delete

    fun getCharacters(email: String) = repository.getAllCharactersByUser(email)

    fun deleteCharacter(favorite: Favorites) = viewModelScope.launch {
        try {
            _delete.value = State.loading(true)
            withContext(ioDispatcher) {
                repository.deleteCharacter(favorite)
            }
            _delete.value = State.loading(false)
            _delete.value = State.success(true)
        } catch (e: Exception) {
            _delete.value = State.loading(false)
            _delete.value = State.error(e)
        }
    }
}
