package com.amandaluz.marvelproject.view.home.fragment.home

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.amandaluz.marvelproject.R
import com.amandaluz.marvelproject.core.BaseFragment
import com.amandaluz.marvelproject.core.ModuleHawk
import com.amandaluz.marvelproject.core.Status
import com.amandaluz.marvelproject.core.hasInternet
import com.amandaluz.marvelproject.data.model.CharacterResponse
import com.amandaluz.marvelproject.data.model.Results
import com.amandaluz.marvelproject.data.repository.CharacterRepository
import com.amandaluz.marvelproject.data.repository.loginrepository.hawk.CharacterCacheRepositoryImpl
import com.amandaluz.marvelproject.data.repository.loginrepository.hawk.CharacterKeys
import com.amandaluz.marvelproject.databinding.FragmentHomeBinding
import com.amandaluz.marvelproject.util.ConfirmDialog
import com.amandaluz.marvelproject.util.apikey
import com.amandaluz.marvelproject.util.hash
import com.amandaluz.marvelproject.util.ts
import com.amandaluz.marvelproject.view.adapter.CharacterAdapter
import com.amandaluz.marvelproject.view.home.fragment.home.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class HomeFragment : BaseFragment() {
    private val viewModel by viewModels<HomeViewModel>()
    lateinit var repository: CharacterRepository
    lateinit var binding: FragmentHomeBinding
    private lateinit var response: CharacterResponse
    private lateinit var characterAdapter: CharacterAdapter
    private var offsetCharacters: Int = 0

    private val cacheHawk by lazy { CharacterCacheRepositoryImpl(ModuleHawk) }

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

        paginationSetup()
        checkConnection()
        observeVMEvents()
    }

    private fun search(menu: Menu) {
        val search = menu.findItem(R.id.search)
        val searchView = search.actionView as SearchView
        searchView.queryHint = "Pesquisar"

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false
            override fun onQueryTextChange(newText: String?): Boolean {
                when (newText) {
                    "" -> {
                        getCharacters(offset = offsetCharacters)
                    }
                    else -> {
                        searchCharacter(newText.toString())
                    }
                }
                return false
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
        search(menu)
    }

    private fun paginationSetup() {
        binding.fabItemNext.setOnClickListener {
            if (offsetCharacters >= 0) {
                offsetCharacters += 50
                getCharacters(offset = offsetCharacters)
            }
        }
        binding.fabItemPrevious.setOnClickListener {
            if (offsetCharacters >= 50) {
                offsetCharacters -= 50
                getCharacters(offset = offsetCharacters)
            }
        }
    }

    private fun setupToolbar() {
        setHasOptionsMenu(true)
        (activity as AppCompatActivity).setSupportActionBar(binding.includeToolbar.toolbarLayout)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    override fun checkConnection() {
        if (hasInternet(context)) {
            verifyResponse()
            getCharacters()
        } else {
            ConfirmDialog(
                getString(R.string.dialogTitleConnection),
                getString(R.string.dialogMessageConnection),
                getString(R.string.dialogButtonConnection),
                getString(R.string.dialogButtonNotConnection)
            ).apply {
                setListener {
                    checkConnection()
                }
            }.show(parentFragmentManager, "Connection")
        }
    }

    private fun verifyResponse() {
        if (ModuleHawk.contains(CharacterKeys.CHARACTERS)) {
            getHawk()
        } else
            getCharacters()
    }

    private fun getCharacters(limit: Int = 50, offset: Int = 0) {
        val ts = ts()
        viewModel.getCharacters(apikey(), hash(ts), ts.toLong(), limit, offset)
    }

    private fun getHawk() {
        response = cacheHawk.get(CharacterKeys.CHARACTERS)
        setRecyclerView(response.data.results)
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
        characterAdapter = CharacterAdapter(characterList, {
            Timber.tag("Click").i(it.name)
            findNavController().navigate(R.id.action_homeFragment_to_detailFragment,
                Bundle().apply {
                    putParcelable("FAVORITE", it)
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