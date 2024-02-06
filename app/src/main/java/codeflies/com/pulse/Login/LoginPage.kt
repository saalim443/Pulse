package codeflies.com.pulse.Login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import codeflies.com.pulse.MainActivity
import codeflies.com.pulse.databinding.ActivityLoginBinding

class LoginPage : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.back.setOnClickListener {
            finish()
        }
        binding.login.setOnClickListener {
            startActivity(Intent(this,MainActivity::class.java))
        }
    }

}