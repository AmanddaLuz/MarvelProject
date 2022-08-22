package com.amandaluz.marvelproject.view.register.fragment.passwordfragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.amandaluz.marvelproject.R
import com.amandaluz.marvelproject.data.model.User
import com.amandaluz.marvelproject.databinding.FragmentPasswordBinding
import com.amandaluz.marvelproject.util.setError
import com.amandaluz.marvelproject.view.register.fragment.passwordfragment.viewmodel.PasswordViewModel

class PasswordFragment : Fragment() {

    private lateinit var binding: FragmentPasswordBinding
    private lateinit var viewModel: PasswordViewModel
    private lateinit var user: User

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPasswordBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = PasswordViewModel.PasswordViewModelProvideFactory()
            .create(PasswordViewModel::class.java)
        user = arguments?.getParcelable<User>("REGISTER_USER") as User

        observeVMEvents()
        goToPhotoStep()
        goBackButtom()

    }

    private fun goToPhotoStep() {
        binding.materialButton.setOnClickListener {
            val password = binding.loginPasswordEdit.text.toString()
            val passwordConfirmation = binding.loginConfirmPasswordEdit.text.toString()

            if (viewModel.checkIfPasswordAreValid(password, passwordConfirmation)) {
                navigateToNextStepWithValidPassword(password)
            }
        }
    }

    private fun navigateToNextStepWithValidPassword(password: String) {
        val userWithNewValues = User(user.email, user.name, password, null)

        findNavController().navigate(R.id.action_passwordFragment_to_photoFragment,
            Bundle().apply {
                putParcelable("REGISTER_USER", userWithNewValues)
            })
    }

    private fun observeVMEvents() {
        viewModel.passwordFieldErrorResId.observe(viewLifecycleOwner) {
            binding.loginPasswordLayout.setError(requireContext(), it)
        }
        viewModel.passwordIsDifferentFieldErrorResId.observe(viewLifecycleOwner) {
            binding.loginConfirmPasswordLayout.setError(requireContext(), it)
        }
    }

    private fun goBackButtom(){
        binding.btnBackPassword.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}