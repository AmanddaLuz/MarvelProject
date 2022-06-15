package com.amandaluz.marvelproject.view.fragment.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import com.amandaluz.marvelproject.core.Status
import com.amandaluz.marvelproject.data.model.Results
import com.amandaluz.marvelproject.data.network.ApiService
import com.amandaluz.marvelproject.data.repository.CharacterRepository
import com.amandaluz.marvelproject.data.repository.CharactersRepositoryImpl
import com.amandaluz.marvelproject.databinding.FragmentHomeBinding
import com.amandaluz.marvelproject.util.apikey
import com.amandaluz.marvelproject.util.hash
import com.amandaluz.marvelproject.util.ts
import com.amandaluz.marvelproject.view.adapter.CharacterAdapter
import com.amandaluz.marvelproject.view.fragment.home.viewmodel.HomeViewModel
import kotlinx.coroutines.Dispatchers
import timber.log.Timber

class HomeFragment : Fragment() {
    lateinit var viewModel: HomeViewModel
    lateinit var repository: CharacterRepository

    lateinit var binding: FragmentHomeBinding
    private lateinit var characterAdapter: CharacterAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        repository = CharactersRepositoryImpl(ApiService.service)
        viewModel = HomeViewModel.HomeViewModelProviderFactory(repository, Dispatchers.IO)
            .create(HomeViewModel::class.java)

        getCharacters()
        observeVMEvents()
    }

    private fun setAdapter(characterList: MutableList<Results>){
        characterAdapter = CharacterAdapter(characterList)
    }

    private fun setRecyclerView(characterList: MutableList<Results>) {
        setAdapter(characterList = characterList)
        binding.rvHomeFragment.apply {
            setHasFixedSize(true)
            adapter = characterAdapter
        }
    }

    private fun getCharacters() {
        val ts = ts()
        viewModel.getCharacters(apikey(), hash(ts), ts.toLong())
    }

    private fun observeVMEvents() {
        viewModel.response.observe(viewLifecycleOwner) {
            if (viewLifecycleOwner.lifecycle.currentState != Lifecycle.State.RESUMED) return@observe
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let { response ->
                        Timber.tag("Sucesso").i(response.toString())
                        setRecyclerView(response.data.results as MutableList<Results>)
                    }
                }
                Status.ERROR -> {
                    Timber.tag("Error").i(it.error)
                }
                Status.LOADING -> {
                    Timber.tag("Loading").i(it.loading.toString())
                }
            }
        }
    }
}