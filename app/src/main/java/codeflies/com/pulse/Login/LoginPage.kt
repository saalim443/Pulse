package codeflies.com.pulse.Login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import codeflies.com.pulse.Helpers.ProgressDisplay
import codeflies.com.pulse.Helpers.RetrofitClient
import codeflies.com.pulse.Helpers.SharedPreference
import codeflies.com.pulse.Home.MainActivity
import codeflies.com.pulse.Models.Login.ResponseLogin
import codeflies.com.pulse.databinding.ActivityLoginBinding
import com.example.ehcf_doctor.Retrofit.GetData
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginPage : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    lateinit var sharedPreference: SharedPreference
    lateinit var progressDisplay: ProgressDisplay

    lateinit var token: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        FirebaseApp.initializeApp(this);
        progressDisplay=ProgressDisplay(this);

        sharedPreference= SharedPreference(this);
        binding.back.setOnClickListener {
            finish()
        }
        binding.login.setOnClickListener {

            if(binding.email.text.length>0){
                if(binding.email.text.contains("@")){
                    if(binding.password.text.length>0){
                        login(binding.email.text.toString(),binding.password.text.toString())
                    }else{
                        Toast.makeText(applicationContext,"Please Enter Password",Toast.LENGTH_SHORT).show()
                    }
                }else{
                    Toast.makeText(applicationContext,"Please Enter valid Email ID",Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(applicationContext,"Please Enter Email ID",Toast.LENGTH_SHORT).show()
            }
        }

        fcmToken()
    }


    private fun login(email: String, password: String) {
        progressDisplay.show()
        val getData: GetData =
            RetrofitClient.getRetrofit().create(GetData::class.java)
        val call: Call<ResponseLogin> =
            getData.login(email, password, token)
        call.enqueue(object : Callback<ResponseLogin?> {
            override fun onResponse(
                call: Call<ResponseLogin?>,
                response: Response<ResponseLogin?>
            ) {
                if (response.body()?.status == true) {

                    Toast.makeText(
                        applicationContext,
                        "Welcome back, ${response.body()?.user?.name}",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.d("fcm_token", response.body()?.user?.token.toString() )
                    Log.d("token", response.body()?.user?.fcmToken.toString() )
                    sharedPreference.saveData("token", response.body()?.user?.token  )
                    sharedPreference.saveData("mobile", response.body()?.user?.mobile )
                    sharedPreference.saveData("user_id", response.body()?.user?.id.toString() )
                    sharedPreference.saveData("role", response.body()?.user?.primaryRole )
                    startActivity(Intent(this@LoginPage, MainActivity::class.java))
                    finishAffinity()
                } else {
                    Toast.makeText(
                        applicationContext,
                        response.body()?.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                    // Toast.makeText( Dashboard.activity, "Sorry!! someone has already accepted the ride", Toast.LENGTH_SHORT ).show();
                }
                progressDisplay.dismiss()
            }

            override fun onFailure(call: Call<ResponseLogin?>, t: Throwable) {
                progressDisplay.dismiss()
                Toast.makeText(
                    applicationContext,
                    "Something went wrong !",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun fcmToken() {
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task: Task<String?> ->
                if (task.isSuccessful) {
                    token = task.result.toString()
                    Log.d("FCM_TOKEN",token)
                } else {
                    Log.d("FCM_TOKEN_ERROR",task.exception?.message.toString())
                }
            }
    }
}