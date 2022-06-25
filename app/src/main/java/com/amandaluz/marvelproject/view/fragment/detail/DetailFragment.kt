package com.amandaluz.marvelproject.view.fragment.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.Fragment
import com.amandaluz.marvelproject.R
import com.amandaluz.marvelproject.core.Status
import com.amandaluz.marvelproject.data.db.AppDatabase
import com.amandaluz.marvelproject.data.db.CharacterDAO
import com.amandaluz.marvelproject.data.db.repository.DatabaseRepository
import com.amandaluz.marvelproject.data.db.repository.DatabaseRepositoryImpl
import com.amandaluz.marvelproject.data.model.Results
import com.amandaluz.marvelproject.databinding.CharacterDetailsBinding
import com.bumptech.glide.Glide
import kotlinx.coroutines.Dispatchers
import timber.log.Timber

class DetailFragment : Fragment() {
    lateinit var viewModel: DetailViewModel
    lateinit var repository: DatabaseRepository
    private val dao: CharacterDAO by lazy{
        AppDatabase.getDb(requireContext()).characterDao()
    }

    lateinit var binding: CharacterDetailsBinding
    private lateinit var character: Results

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = CharacterDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        character = arguments?.getSerializable("CHARACTER") as Results

        repository = DatabaseRepositoryImpl(dao)
        viewModel = DetailViewModel.DetailViewModelProviderFactory(repository, Dispatchers.IO)
            .create(DetailViewModel::class.java)

        binding.run {
            txtTitleDetails.text = character.name
            txtDescriptionDetails.text = character.description
            fabDetails.setOnClickListener{
                binding.fabDetails.setImageResource(R.drawable.ic_full_favorite)
            }

            setImage(imgDetail)
            setImage(imgPoster)

            fabDetails.setOnClickListener{
                viewModel.insertCharacters(character)
            }
        }
        observeVMEvents()
    }

    private fun observeVMEvents() {
        viewModel.response.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let {response ->
                        Timber.tag("Sucesso").i(response.toString())
                    }
                }
                Status.ERROR -> {
                    Timber.tag("Error").i(it.error)
                }
                Status.LOADING -> {}
            }
        }
    }

    private fun setImage(image: AppCompatImageView) {
        Glide.with(this@DetailFragment)
            .load("${character.thumbnail.path}.${character.thumbnail.extension}")
            .centerCrop()
            .into(image)
    }



}