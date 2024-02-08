package codeflies.com.pulse.Intro

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import codeflies.com.pulse.Helpers.Constants
import codeflies.com.pulse.Helpers.RetrofitClient
import codeflies.com.pulse.Helpers.SharedPreference
import codeflies.com.pulse.Home.MainActivity
import codeflies.com.pulse.Login.LoginPage
import codeflies.com.pulse.Models.UserData.ResponseProfile
import codeflies.com.pulse.R
import codeflies.com.pulse.databinding.ActivitySplashBinding
import com.bumptech.glide.Glide
import com.example.ehcf_doctor.Retrofit.GetData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


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
              checkUser()
            }else{
                startActivity(Intent(
                    this@SplashActivity,
                    IntroScreenActivity::class.java
                ))
                finish()
            }


        }, 2000)
    }

    private fun checkUser() {

        val getData: GetData =
            RetrofitClient.getRetrofit().create(GetData::class.java)
        val call: Call<ResponseProfile> =
            getData.profile(
                "Bearer " + sharedPreference.getData("token")
            )
        call.enqueue(object : Callback<ResponseProfile?> {
            override fun onResponse(
                call: Call<ResponseProfile?>,
                response: Response<ResponseProfile?>
            ) {
                if (response.body()?.status == true) {
                    startActivity(Intent(
                        this@SplashActivity,
                        MainActivity::class.java
                    ))
                    finish()
                } else {
                    sharedPreference.saveData("token","")
                    sharedPreference.saveData("user_id","")
                    sharedPreference.saveData("mobile","")
                    startActivity(Intent(
                        this@SplashActivity,
                        LoginPage::class.java
                    ))
                    Toast.makeText(
                        applicationContext,
                        response.body()?.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                    finish()
                    // Toast.makeText( Dashboard.activity, "Sorry!! someone has already accepted the ride", Toast.LENGTH_SHORT ).show();
                }

            }

            override fun onFailure(call: Call<ResponseProfile?>, t: Throwable) {
                sharedPreference.saveData("token","")
                sharedPreference.saveData("user_id","")
                sharedPreference.saveData("mobile","")
                startActivity(Intent(
                    this@SplashActivity,
                    LoginPage::class.java
                ))
                finish()
                Toast.makeText(
                    applicationContext,
                    "You are logged out",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

}

