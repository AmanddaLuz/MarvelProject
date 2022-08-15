package com.amandaluz.marvelproject.view.login.register.fragment.photofragment

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import com.amandaluz.marvelproject.R
import com.amandaluz.marvelproject.core.Status
import com.amandaluz.marvelproject.data.db.AppDatabase
import com.amandaluz.marvelproject.data.db.CharacterDAO
import com.amandaluz.marvelproject.data.model.User
import com.amandaluz.marvelproject.databinding.FragmentPhotoBinding
import com.amandaluz.marvelproject.util.toast
import com.amandaluz.marvelproject.view.login.register.fragment.photofragment.viewmodel.PhotoViewModel
import com.amandaluz.marvelproject.view.login.repository.RegisterRepository
import com.amandaluz.marvelproject.view.login.repository.RegisterRepositoryImpl
import kotlinx.coroutines.Dispatchers

class PhotoFragment : Fragment() {
    private lateinit var binding: FragmentPhotoBinding
    private lateinit var viewModel: PhotoViewModel
    private lateinit var repository: RegisterRepository
    private lateinit var user: User
    private var uriImage: Uri? = null

    private val dao: CharacterDAO by lazy {
        AppDatabase.getDb(requireContext()).characterDao()
    }

    private var getContent = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            setImageFromGallery(it)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPhotoBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        user = arguments?.getParcelable<User>("REGISTER_USER") as User

        repository = RegisterRepositoryImpl(dao)
        viewModel = PhotoViewModel.PhotoViewModelProvider(repository, Dispatchers.IO)
            .create(PhotoViewModel::class.java)

        clickToChoosePhoto()
        insertUserOnDatabase(user)
        createUserWithoutPhoto()
        observeVMEvents()
    }

    private fun createUserWithoutPhoto() {
        binding.registerChooseLater.setOnClickListener {
            val userWithoutPhoto = User(
                email = user.email,
                name = user.name,
                password = user.password,
                photo = uriImage
            )
            viewModel.insertNewUserOnDatabase(userWithoutPhoto)
        }
    }

    private fun clickToChoosePhoto() {
        binding.registerCardview.setOnClickListener {
            gallery()
        }
    }

    private fun insertUserOnDatabase(user: User) {
        binding.btnConfirm.setOnClickListener {
            if (uriImage != null){
                makeUserWithPhotoOrNot(user)
            } else {
                uriImage = Uri.parse("")
                makeUserWithPhotoOrNot(user)
            }
        }
    }

    private fun makeUserWithPhotoOrNot(user: User){
        val finalUser = user.copy(photo = uriImage)
        viewModel.insertNewUserOnDatabase(finalUser)
    }

    private fun gallery() =
        getContent.launch("image/*")

    private fun setImageFromGallery(uri: Uri) {
        binding.registerImage.setImageURI(uri)
        uriImage = uri
    }

    private fun observeVMEvents() {
        viewModel.user.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let {
                        goToNextStepWithValidUser()
                    }
                }
                Status.LOADING -> {}
                Status.ERROR -> {
                    toast("Erro ao cadastrar usuário")
                }
            }
        }
    }

    private fun goToNextStepWithValidUser() {
        findNavController().navigate(R.id.action_photoFragment_to_welcomeFragment,
            Bundle().apply {
                putParcelable("REGISTER_USER", user)
            })
    }
}