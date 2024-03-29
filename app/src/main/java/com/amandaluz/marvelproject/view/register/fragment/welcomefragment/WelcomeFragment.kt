package com.amandaluz.marvelproject.view.register.fragment.welcomefragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.amandaluz.marvelproject.data.model.User
import com.amandaluz.marvelproject.databinding.FragmentWelcomeBinding
import com.amandaluz.marvelproject.view.register.RegisterActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WelcomeFragment : Fragment() {
    private lateinit var binding: FragmentWelcomeBinding
    private lateinit var user: User

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWelcomeBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        user = arguments?.getParcelable<User>("REGISTER_USER") as User
        welcomeWith(user.name)
        goToHomePage()
    }

    private fun welcomeWith(username: String){
        val welcomeName = "SEJA BEM VINDO(A), $username!"
        binding.welcome.text = welcomeName
    }

    private fun goToHomePage() {
        binding.btnGoToLogin.setOnClickListener {
            (activity as RegisterActivity).goTo()
        }
    }

}