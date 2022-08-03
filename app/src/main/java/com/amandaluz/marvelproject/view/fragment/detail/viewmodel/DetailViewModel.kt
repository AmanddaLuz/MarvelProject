package com.amandaluz.marvelproject.view.fragment.detail.viewmodel

import androidx.lifecycle.*
import com.amandaluz.marvelproject.core.State
import com.amandaluz.marvelproject.data.db.repository.DatabaseRepository
import com.amandaluz.marvelproject.data.model.Favorites
import com.amandaluz.marvelproject.data.model.Results
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.IllegalArgumentException
import kotlin.Exception

class DetailViewModel(
    private val databaseRepository: DatabaseRepository,
    private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _response = MutableLiveData<State<Boolean>>()
    val response: LiveData<State<Boolean>> = _response
    private val _verifyCharacter = MutableLiveData<State<Boolean>>()
    val verifyCharacter: LiveData<State<Boolean>> = _verifyCharacter
    private val _delete = MutableLiveData<State<Boolean>>()
    val delete : LiveData<State<Boolean>>
        get() = _delete

    fun insertCharacters(result: Favorites) {
        viewModelScope.launch {
            try {
                withContext(ioDispatcher) {
                    databaseRepository.insertCharacter(result)
                }
                _response.value = State.success(true)
            } catch (throwable: Throwable) {
                _response.value = State.error(throwable)
            }
        }
    }

    fun insertFavorite(favorite: Favorites){
        viewModelScope.launch {
            try {
                databaseRepository.insertFavorite(favorite)
            } catch (e: Exception){
                //Timber
            }
        }
    }

    fun verifySavedCharacter(characterId: Long, email: String){
        viewModelScope.launch {
            try {
                val result = withContext(ioDispatcher){
                    databaseRepository.getFavouriteCharacterByUser(characterId, email)
                }
                _verifyCharacter.value = State.success(result != null)

            } catch(throwable: Throwable){
                _verifyCharacter.value = State.error(throwable)
            }
        }
    }

    fun deleteCharacter(favorite: Favorites) = viewModelScope.launch {
        try {
            _delete.value = State.loading(true)
            withContext(ioDispatcher) {
                databaseRepository.deleteCharacter(favorite)
            }
            _delete.value = State.loading(false)
            _delete.value = State.success(true)
        } catch (e: Exception){
            _delete.value = State.loading(false)
            _delete.value = State.error(e)
        }
    }
    class DetailViewModelProviderFactory(
        private val repository: DatabaseRepository,
        private val ioDispatcher: CoroutineDispatcher
    ): ViewModelProvider.Factory{
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if(modelClass.isAssignableFrom(DetailViewModel::class.java)){
                return DetailViewModel(repository,ioDispatcher) as T
            }
            throw IllegalArgumentException("Unknown viewModel Class")
        }
    }
}