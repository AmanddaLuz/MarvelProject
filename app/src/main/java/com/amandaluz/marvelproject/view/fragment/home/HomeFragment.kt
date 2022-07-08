package com.amandaluz.marvelproject.view.fragment.home

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.amandaluz.marvelproject.R
import com.amandaluz.marvelproject.core.BaseFragment
import com.amandaluz.marvelproject.core.Status
import com.amandaluz.marvelproject.core.hasInternet
import com.amandaluz.marvelproject.data.model.Results
import com.amandaluz.marvelproject.data.network.ApiService
import com.amandaluz.marvelproject.data.repository.CharacterRepository
import com.amandaluz.marvelproject.data.repository.CharactersRepositoryImpl
import com.amandaluz.marvelproject.databinding.FragmentHomeBinding
import com.amandaluz.marvelproject.util.ConfirmDialog
import com.amandaluz.marvelproject.util.apikey
import com.amandaluz.marvelproject.util.hash
import com.amandaluz.marvelproject.util.ts
import com.amandaluz.marvelproject.view.adapter.CharacterAdapter
import com.amandaluz.marvelproject.view.fragment.home.viewmodel.HomeViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import timber.log.Timber

class HomeFragment : BaseFragment() {
    lateinit var viewModel: HomeViewModel
    lateinit var repository: CharacterRepository

    lateinit var binding: FragmentHomeBinding
    private lateinit var characterAdapter: CharacterAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        setupToolbar()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.tag("CONNECTION").i(hasInternet(context).toString())

        repository = CharactersRepositoryImpl(ApiService.service)
        viewModel = HomeViewModel.HomeViewModelProviderFactory(repository, Dispatchers.IO)
            .create(HomeViewModel::class.java)

        binding.includeToolbar.findCharacter.addTextChangedListener { inputText ->
            inputText?.let {
                searchCharacter(it.toString())
            }
        }
        checkConnection()
        observeVMEvents()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.search -> {
                binding.includeToolbar.findCharacter.visibility = View.VISIBLE
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupToolbar(){
        setHasOptionsMenu(true)
        (activity as AppCompatActivity).setSupportActionBar(binding.includeToolbar.toolbarLayout)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(false)
    }


    override fun checkConnection() {
        if (hasInternet(context)) {
            getCharacters()
        } else {
            ConfirmDialog("Erro de conexão!", "Verifique sua internet e tente novamente!")
                .show(parentFragmentManager, "Connection")
        }
    }

    private fun getCharacters() {
        val ts = ts()
        viewModel.getCharacters(apikey(), hash(ts), ts.toLong())
    }

    private fun searchCharacter(nameStart: String) {
        val ts = ts()
        viewModel.searchCharacter(nameStart, apikey(), hash(ts), ts.toLong())
    }

    private fun observeVMEvents() {
        viewModel.response.observe(viewLifecycleOwner) {
            if (viewLifecycleOwner.lifecycle.currentState != Lifecycle.State.RESUMED) return@observe
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let { response ->
                        Timber.tag("Sucesso").i(response.toString())
                        setRecyclerView(response.data.results)
                    }
                }
                Status.ERROR -> {
                    val snack =
                        Snackbar.make(binding.container, "Not found", Snackbar.LENGTH_INDEFINITE)
                    snack.setAction("Confirmar") {}
                    snack.show()
                    Timber.tag("Error").i(it.error)
                }
                Status.LOADING -> {
                    Timber.tag("Loading").i(it.loading.toString())
                }
            }
        }

        viewModel.search.observe(viewLifecycleOwner) {
            if (viewLifecycleOwner.lifecycle.currentState != Lifecycle.State.RESUMED) return@observe
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let { searchResponse ->
                        Timber.tag("Sucesso").i(searchResponse.toString())
                        setRecyclerView(searchResponse.data.results)
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

    private fun setAdapter(characterList: List<Results>) {
        characterAdapter = CharacterAdapter(characterList, { character ->
            Timber.tag("Click").i(character.name)
            findNavController().navigate(R.id.action_homeFragment_to_detailFragment,
                Bundle().apply {
                    putSerializable("CHARACTER", character)
                })
        })
    }

    private fun setRecyclerView(characterList: List<Results>) {
        setAdapter(characterList = characterList)
        binding.rvHomeFragment.apply {
            setHasFixedSize(true)
            adapter = characterAdapter
        }
    }
}
