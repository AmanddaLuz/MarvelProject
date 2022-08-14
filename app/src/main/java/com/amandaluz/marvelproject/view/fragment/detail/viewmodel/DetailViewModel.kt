package com.amandaluz.marvelproject.view.fragment.detail.viewmodel

import androidx.lifecycle.*
import com.amandaluz.marvelproject.core.State
import com.amandaluz.marvelproject.data.db.repository.DatabaseRepository
import com.amandaluz.marvelproject.data.model.Favorites
import com.amandaluz.marvelproject.data.model.Results
import com.amandaluz.marvelproject.data.model.modelcomics.ComicsResponse
import com.amandaluz.marvelproject.data.repository.categoryrepository.CategoryRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.lang.IllegalArgumentException
import kotlin.Exception

class DetailViewModel(
    private val databaseRepository: DatabaseRepository,
    private val categoryRepository: CategoryRepository,
    private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _response = MutableLiveData<State<Boolean>>()
    val response: LiveData<State<Boolean>> = _response
    private val _verifyCharacter = MutableLiveData<State<Boolean>>()
    val verifyCharacter: LiveData<State<Boolean>> = _verifyCharacter
    private val _delete = MutableLiveData<State<Boolean>>()
    val delete : LiveData<State<Boolean>>
        get() = _delete

    private val _comicsResponse = MutableLiveData<State<ComicsResponse>>()
    val comicsResponse: LiveData<State<ComicsResponse>> = _comicsResponse

    private val _seriesResponse = MutableLiveData<State<ComicsResponse>>()
    val seriesResponse: LiveData<State<ComicsResponse>> = _seriesResponse


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

    fun getComics(apikey: String, hash: String, ts: Long, id: Long){
        viewModelScope.launch {
            try {
                _comicsResponse.value = State.loading(true)
                withContext(ioDispatcher){
                    categoryRepository.getComics(apikey, hash, ts, id)
                }.let {
                    _comicsResponse.value = State.loading(false)
                    _comicsResponse.value = State.success(it)
                }
            } catch (e: Exception) {
                Timber.tag("ERROR").i("EXCEPTION")
                _comicsResponse.value = State.error(e)
            }
        }
    }

    fun getSeries(apikey: String, hash: String, ts: Long, id: Long){
        viewModelScope.launch {
            try {
                _seriesResponse.value = State.loading(true)
                withContext(ioDispatcher){
                    categoryRepository.getSeries(apikey, hash, ts, id)
                }.let {
                    _seriesResponse.value = State.loading(false)
                    _seriesResponse.value = State.success(it)
                }
            } catch (e: Exception) {
                Timber.tag("ERROR").i("EXCEPTION")
                _seriesResponse.value = State.error(e)
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
        private val categoryRepository: CategoryRepository,
        private val ioDispatcher: CoroutineDispatcher
    ): ViewModelProvider.Factory{
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if(modelClass.isAssignableFrom(DetailViewModel::class.java)){
                return DetailViewModel(repository,categoryRepository, ioDispatcher) as T
            }
            throw IllegalArgumentException("Unknown viewModel Class")
        }
    }
}