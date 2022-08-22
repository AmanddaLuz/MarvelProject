package com.amandaluz.marvelproject.view.register.fragment.accountfragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.amandaluz.marvelproject.R
import com.amandaluz.marvelproject.data.model.User
import com.amandaluz.marvelproject.databinding.FragmentAccountBinding
import com.amandaluz.marvelproject.util.setError
import com.amandaluz.marvelproject.view.register.fragment.accountfragment.viewmodel.AccountViewModel

class AccountFragment : Fragment() {
    private lateinit var binding: FragmentAccountBinding
    private lateinit var viewModel: AccountViewModel
    private lateinit var user: User

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAccountBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = AccountViewModel.AccountViewModelProvideFactory()
            .create(AccountViewModel::class.java)

        observeVMEvents()
        goToPasswordStep()
    }

    private fun goToPasswordStep() {
        binding.materialButton2.setOnClickListener {
            val name = binding.fullNameEdit.text.toString()
            val email = binding.emailEdit.text.toString()
            val emailConfirmation = binding.emailConfirmationEdit.text.toString()

            if (viewModel.checkIfAllFieldAreValid(email, emailConfirmation, name)) {
                navigateToNextStepWithFieldsValid(email, name)
            }
        }
    }

    private fun navigateToNextStepWithFieldsValid(email: String, name: String) {
        user = User(email, name, "", null)

        findNavController().navigate(
            R.id.action_accountFragment_to_passwordFragment,
            Bundle().apply {
                putParcelable("REGISTER_USER", user)
            })
    }

    private fun observeVMEvents() {
        viewModel.emailFieldErrorResId.observe(viewLifecycleOwner) {
            binding.emailLayout.setError(requireContext(), it)
        }
        viewModel.nameFieldErrorResId.observe(viewLifecycleOwner) {
            binding.fullNameLayout.setError(requireContext(), it)
        }
        viewModel.sameEmailsFieldErrorResId.observe(viewLifecycleOwner) {
            binding.emailConfirmationLayout.setError(requireContext(), it)
        }
    }
}