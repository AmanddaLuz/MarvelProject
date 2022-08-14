package com.amandaluz.marvelproject.view.fragment.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.SnapHelper
import com.amandaluz.marvelproject.R
import com.amandaluz.marvelproject.core.Status
import com.amandaluz.marvelproject.data.db.AppDatabase
import com.amandaluz.marvelproject.data.db.CharacterDAO
import com.amandaluz.marvelproject.data.db.repository.DatabaseRepository
import com.amandaluz.marvelproject.data.db.repository.DatabaseRepositoryImpl
import com.amandaluz.marvelproject.data.model.Favorites
import com.amandaluz.marvelproject.data.model.Results
import com.amandaluz.marvelproject.data.model.User
import com.amandaluz.marvelproject.databinding.CharacterDetailsBinding
import com.amandaluz.marvelproject.view.fragment.detail.adapter.CarouselAdapter
import com.amandaluz.marvelproject.view.fragment.detail.decoration.BoundsOffsetDecoration
import com.amandaluz.marvelproject.view.fragment.detail.decoration.LinearHorizontalSpacingDecoration
import com.amandaluz.marvelproject.view.fragment.detail.decoration.ProminentLayoutManager
import com.amandaluz.marvelproject.view.fragment.detail.viewmodel.DetailViewModel
import com.bumptech.glide.Glide
import kotlinx.coroutines.Dispatchers
import timber.log.Timber
import com.amandaluz.marvelproject.data.model.modelcomics.Result
import com.amandaluz.marvelproject.data.network.ApiService
import com.amandaluz.marvelproject.data.repository.categoryrepository.CategoryRepository
import com.amandaluz.marvelproject.data.repository.categoryrepository.CategoryRepositoryImpl
import com.amandaluz.marvelproject.databinding.CharacterCategoryDetailBinding
import com.amandaluz.marvelproject.databinding.ComicDetailBinding
import com.amandaluz.marvelproject.util.*

class DetailFragment : Fragment() {
    lateinit var viewModel: DetailViewModel
    lateinit var repository: DatabaseRepository
    lateinit var repositoryCategory: CategoryRepository
    private var checkCharacter: Boolean = false
    private val dao: CharacterDAO by lazy {
        AppDatabase.getDb(requireContext()).characterDao()
    }
    private lateinit var snapHelper: SnapHelper
    private lateinit var carouselAdapter: CarouselAdapter
    private lateinit var binding: CharacterCategoryDetailBinding
    private lateinit var favorite: Favorites
    private lateinit var user: User


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = CharacterCategoryDetailBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        favorite = arguments?.getParcelable<Favorites>("FAVORITE") as Favorites
        repository = DatabaseRepositoryImpl(dao)
        repositoryCategory = CategoryRepositoryImpl(ApiService.service)
        viewModel = DetailViewModel.DetailViewModelProviderFactory(
            repository, repositoryCategory, Dispatchers.IO)
            .create(DetailViewModel::class.java)

        getUserByIntent()

        viewModel.verifySavedCharacter(favorite.id, user.email)
        getComics(id = favorite.id)
        getSeries(id = favorite.id)

        binding.run {
            setImage(imgDetail)

            detailsTitle.text = favorite.name
            detailsDescription.text = favorite.description
            fabDetails.setOnClickListener {


                setFavoriteCharacter()
            }
        }
        observeVMEvents()
    }

    private fun CharacterCategoryDetailBinding.setFavoriteCharacter() {
        checkCharacter = if (checkCharacter) {
            val newfavorite = favorite.copy(email = user.email)
            viewModel.deleteCharacter(newfavorite)
            fabDetails.setImageResource(R.drawable.ic_fab_favorite)
            false
        } else {
            val copyFavorite = favorite.copy(email = user.email)
            viewModel.insertFavorite(copyFavorite)
            fabDetails.setImageResource(R.drawable.ic_full_favorite)
            true
        }
    }

    private fun getUserByIntent() {
        activity?.let {
            user = it.intent.getParcelableExtra<User>("USER") as User
        }
    }

    private fun getComics(id: Long){
        val ts = ts()
        viewModel.getComics(apikey(), hash(ts), ts.toLong(), id)
    }

    private fun getSeries(id: Long){
        val ts = ts()
        viewModel.getSeries(apikey(), hash(ts), ts.toLong(), id)
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
        viewModel.comicsResponse.observe(viewLifecycleOwner){
            when(it.status){
                Status.SUCCESS ->{
                    it.data?.let { category ->
                        setRecycler(category.data.results)
                    }
                }
                Status.ERROR -> {
                    Timber.tag("Error").i(it.error)
                }
                Status.LOADING ->{}
            }
        }
        viewModel.seriesResponse.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let { category ->
                        setRecyclerSeries(category.data.results)
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
            .load("${favorite.thumbnail.path}.${favorite.thumbnail.extension}")
            .centerCrop()
            .into(image)
    }

    private fun setAdapter(list: List<Result>) {
        carouselAdapter = CarouselAdapter(list)
    }

    private fun setRecycler(list: List<Result>) {
        setAdapter(list)
        snapHelper = PagerSnapHelper()

        binding.recyclerCategory.apply {
            adapter = carouselAdapter
            layoutManager = ProminentLayoutManager(context)
            setItemViewCacheSize(4)
            val spacing = resources.getDimensionPixelSize(R.dimen.carousel_spacing)
            addItemDecoration(LinearHorizontalSpacingDecoration(spacing))
            addItemDecoration(BoundsOffsetDecoration())
        }
        snapHelper.attachToRecyclerView(binding.recyclerCategory)
    }

    private fun setRecyclerSeries(list: List<Result>){
        setAdapter(list)
        snapHelper = PagerSnapHelper()

        binding.run {
            recyclerCategorySeries.apply {
                adapter = carouselAdapter
                layoutManager = ProminentLayoutManager(context)
                setItemViewCacheSize(4)
                val spacing = resources.getDimensionPixelSize(R.dimen.carousel_spacing)
                addItemDecoration(LinearHorizontalSpacingDecoration(spacing))
                addItemDecoration(BoundsOffsetDecoration())
            }
            snapHelper.attachToRecyclerView(binding.recyclerCategorySeries)

        }
    }
}