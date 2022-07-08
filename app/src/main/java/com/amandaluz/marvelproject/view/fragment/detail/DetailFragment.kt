package com.amandaluz.marvelproject.view.fragment.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.amandaluz.marvelproject.R
import com.amandaluz.marvelproject.core.Status
import com.amandaluz.marvelproject.data.db.AppDatabase
import com.amandaluz.marvelproject.data.db.CharacterDAO
import com.amandaluz.marvelproject.data.db.repository.DatabaseRepository
import com.amandaluz.marvelproject.data.db.repository.DatabaseRepositoryImpl
import com.amandaluz.marvelproject.data.model.Results
import com.amandaluz.marvelproject.databinding.CharacterDetailsBinding
import com.amandaluz.marvelproject.view.fragment.detail.viewmodel.DetailViewModel
import com.bumptech.glide.Glide
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

class DetailFragment : Fragment() {
    lateinit var viewModel: DetailViewModel
    lateinit var repository: DatabaseRepository
    private var checkCharacter: Boolean = false
    private val dao: CharacterDAO by lazy {
        AppDatabase.getDb(requireContext()).characterDao()
    }
    private lateinit var binding: CharacterDetailsBinding
    private lateinit var character: Results

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = CharacterDetailsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        character = arguments?.getSerializable("CHARACTER") as Results
        repository = DatabaseRepositoryImpl(dao)
        viewModel = DetailViewModel.DetailViewModelProviderFactory(repository, Dispatchers.IO)
            .create(DetailViewModel::class.java)

        viewModel.verifySavedCharacter(character.id)

        binding.run {
            setImage(imgDetail)
            setImage(imgPoster)

            txtTitleDetails.text = character.name
            txtDescriptionDetails.text = character.description
            fabDetails.setOnClickListener {
                if (checkCharacter) {
                    viewModel.deleteCharacter(character)
                    fabDetails.setImageResource(R.drawable.ic_fab_favorite)
                    checkCharacter = false
                } else {
                    viewModel.insertCharacters(character)
                    fabDetails.setImageResource(R.drawable.ic_full_favorite)
                    checkCharacter = true
                }
            }
        }
        observeVMEvents()
    }

    private fun observeVMEvents(){
        viewModel.response.observe(viewLifecycleOwner){
            when (it.status){
                Status.SUCCESS ->{
                    it.data?.let { response ->
                        Timber.tag("Success").i(response.toString())
                    }
                }
                Status.ERROR ->{
                    Timber.tag("Error").i(it.error)
                }
                Status.LOADING->{}
            }
        }
        viewModel.verifyCharacter.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let { exist ->
                        if (exist) {
                            binding.fabDetails.setImageResource(R.drawable.ic_full_favorite)
                        }
                        checkCharacter = exist
                    }
                }
                Status.ERROR -> {
                    Timber.tag("Error").i(it.error)
                }
                Status.LOADING ->{}
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