package com.amandaluz.marvelproject.view.login.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.amandaluz.marvelproject.R
import com.amandaluz.marvelproject.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener{
            binding.btnLogin.progress(true)
        }
    }
}