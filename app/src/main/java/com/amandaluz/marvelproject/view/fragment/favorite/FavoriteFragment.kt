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
import com.amandaluz.marvelproject.data.model.Favorites
import com.amandaluz.marvelproject.data.model.Results
import com.amandaluz.marvelproject.data.model.User
import com.amandaluz.marvelproject.data.model.converterToResult
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
    private lateinit var user: User

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

        activity?.let {
            user = it.intent.getParcelableExtra<User>("USER") as User
        }

        observeVMEvents()
    }

    private fun observeVMEvents() {
        viewModel.getCharacters(user.email).observe(viewLifecycleOwner) {
            when {
                it.isNotEmpty() -> {
                    setRecycler(converterToResult(it))
                }
                else -> {
                    setRecycler(converterToResult(it))
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

    private fun goToDetail(favorite: Favorites) {
        findNavController().navigate(R.id.action_favoriteFragment_to_detailFragment,
            Bundle().apply {
                putParcelable("FAVORITE", favorite)
            })
    }

    private fun deleteCharacters(favorite: Favorites) {
        ConfirmDialog(
            getString(R.string.dialogTitleDeleteCharacter),
            getString(R.string.dialogMessageDeleteCharacter),
            getString(R.string.dialogButtonDeleteCharacter),
            getString(R.string.dialogButtonNotDeleteCharacter)
        )
            .apply {
                setListener {
                    val newfavorite = favorite.copy(email = user.email)
                    viewModel.deleteCharacter(newfavorite)
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