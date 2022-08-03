package com.amandaluz.marvelproject.view.login.register.fragment.welcomefragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.amandaluz.marvelproject.R
import com.amandaluz.marvelproject.data.model.User
import com.amandaluz.marvelproject.databinding.FragmentWelcomeBinding
import com.amandaluz.marvelproject.view.home.activity.HomeActivity
import com.amandaluz.marvelproject.view.login.activity.LoginActivity

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
        binding.materialButton3.setOnClickListener {
            val intent = Intent(context, LoginActivity::class.java)
            startActivity(intent)
        }
    }

}