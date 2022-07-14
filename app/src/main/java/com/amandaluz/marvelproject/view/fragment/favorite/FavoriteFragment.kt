package com.amandaluz.marvelproject.view.fragment.favorite

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.amandaluz.marvelproject.R
import com.amandaluz.marvelproject.core.Status
import com.amandaluz.marvelproject.data.db.AppDatabase
import com.amandaluz.marvelproject.data.db.CharacterDAO
import com.amandaluz.marvelproject.data.db.repository.DatabaseRepository
import com.amandaluz.marvelproject.data.db.repository.DatabaseRepositoryImpl
import com.amandaluz.marvelproject.data.model.Results
import com.amandaluz.marvelproject.databinding.FragmentFavoriteBinding
import com.amandaluz.marvelproject.util.ConfirmDialog
import com.amandaluz.marvelproject.view.adapter.CharacterAdapter
import com.amandaluz.marvelproject.view.fragment.favorite.viewmodel.FavoriteViewModel
import kotlinx.coroutines.Dispatchers

class FavoriteFragment : Fragment() {
    lateinit var viewModel: FavoriteViewModel
    lateinit var repository: DatabaseRepository
    private val dao: CharacterDAO by lazy {
        AppDatabase.getDb(requireContext()).characterDao()
    }
    private lateinit var binding: FragmentFavoriteBinding
    private lateinit var characterAdapter: CharacterAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        repository = DatabaseRepositoryImpl(dao)
        viewModel = FavoriteViewModel(repository, Dispatchers.IO)

        observeVMEvents()
    }

    private fun observeVMEvents() {
        viewModel.getCharacters().observe(viewLifecycleOwner) { results ->
            when {
                results.isNotEmpty() -> {
                    setRecycler(results)
                }
                else -> {
                    setRecycler(results)
                }
            }
        }
        viewModel.delete.observe(viewLifecycleOwner) { state ->
            when (state.status) {
                Status.SUCCESS -> {
                    state.data?.let {
                        Toast.makeText(requireContext(), "Personagem deletado", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
                Status.ERROR -> {

                }
                Status.LOADING -> {

                }
            }
        }
    }

    private fun setAdapter(characterList: List<Results>) {
        characterAdapter = CharacterAdapter(characterList, ::goToDetail, ::deleteCharacters)
    }

    private fun goToDetail(result: Results) {
        findNavController().navigate(R.id.action_favoriteFragment_to_detailFragment,
            Bundle().apply {
                putSerializable("CHARACTER", result)
            })
    }

    private fun deleteCharacters(results: Results) {
        ConfirmDialog(
            getString(R.string.dialogTitleDeleteCharacter),
            getString(R.string.dialogMessageDeleteCharacter),
            getString(R.string.dialogButtonDeleteCharacter),
            getString(R.string.dialogButtonNotDeleteCharacter)
        )
            .apply {
                setListener {
                    viewModel.deleteCharacter(results)
                }
            }.show(parentFragmentManager, "Dialog")
    }

    private fun setRecycler(characterList: List<Results>) {
        setAdapter(characterList)
        binding.rvFavorite.apply {
            setHasFixedSize(true)
            adapter = characterAdapter
        }
    }

}