package codeflies.com.pulse.Intro

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import codeflies.com.pulse.Helpers.SharedPreference
import codeflies.com.pulse.Home.MainActivity
import codeflies.com.pulse.databinding.ActivitySplashBinding


class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    lateinit var sharedPreference: SharedPreference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreference=SharedPreference(this)


        Handler().postDelayed({
            if(sharedPreference.getData("token")!=null&&sharedPreference.getData("token")!=""){
                startActivity(Intent(
                    this@SplashActivity,
                    MainActivity::class.java
                ))
            }else{
                startActivity(Intent(
                    this@SplashActivity,
                    IntroScreenActivity::class.java
                ))
            }

            finish()
        }, 3000)
    }
}

