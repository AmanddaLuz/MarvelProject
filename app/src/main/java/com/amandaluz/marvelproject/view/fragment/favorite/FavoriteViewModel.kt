package com.amandaluz.marvelproject.view.fragment.favorite

import androidx.lifecycle.ViewModel
import com.amandaluz.marvelproject.data.db.repository.DatabaseRepository
import kotlinx.coroutines.CoroutineDispatcher

class FavoriteViewModel(
    private val repository: DatabaseRepository,
    private val ioDispatcher: CoroutineDispatcher
): ViewModel() {

    fun getCharacters() = repository.getAllCharacters()

}