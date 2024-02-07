package codeflies.com.pulse.Profiles

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import codeflies.com.pulse.Helpers.ProgressDisplay
import codeflies.com.pulse.Helpers.RetrofitClient
import codeflies.com.pulse.Helpers.SharedPreference
import codeflies.com.pulse.Home.MainActivity
import codeflies.com.pulse.Intro.SplashActivity
import codeflies.com.pulse.Models.Login.ResponseLogin
import codeflies.com.pulse.Models.UserData.ResponseProfile
import codeflies.com.pulse.R
import codeflies.com.pulse.databinding.ActivityLoginBinding
import codeflies.com.pulse.databinding.ActivityProfileBinding
import com.example.ehcf_doctor.Retrofit.GetData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Profile : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    lateinit var sharedPreference: SharedPreference
    lateinit var progressDisplay: ProgressDisplay


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)



        progressDisplay=ProgressDisplay(this);

        sharedPreference= SharedPreference(this);


        binding.back.setOnClickListener {
            finish()
        }

        binding.logout.setOnClickListener {
            exitApp()
        }

        profile()
    }


    private fun profile() {
        progressDisplay.show()
        val getData: GetData =
            RetrofitClient.getRetrofit().create(GetData::class.java)
        val call: Call<ResponseProfile> =
            getData.profile("Bearer "+sharedPreference.getData("token"),sharedPreference.getData("user_id"))
        call.enqueue(object : Callback<ResponseProfile?> {
            override fun onResponse(
                call: Call<ResponseProfile?>,
                response: Response<ResponseProfile?>
            ) {
                if (response.body()?.status == true) {

                    binding.name.text= response.body()!!.user?.name
                    binding.mobile.text= response.body()!!.user?.mobile
                    binding.email.text= response.body()!!.user?.email
//                    binding.dob.text= response.body()!!.user?.name
                    binding.job.text= response.body()!!.user?.roles?.get(0)?.name

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

            override fun onFailure(call: Call<ResponseProfile?>, t: Throwable) {
                progressDisplay.dismiss()
                Toast.makeText(
                    applicationContext,
                    "Something went wrong !",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun exitApp() {
        val dialog = Dialog(this)
        dialog?.setContentView(R.layout.logout_layout)
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog?.setCancelable(true)
        dialog?.window?.attributes?.windowAnimations = R.style.animation
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val yes = dialog?.findViewById<TextView>(R.id.yes)
        val no = dialog?.findViewById<TextView>(R.id.no)
        val title = dialog?.findViewById<TextView>(R.id.title)
        val txtDesc = dialog?.findViewById<TextView>(R.id.txtDesc)

        title!!.text = "Logout"
        txtDesc?.text = "Are you sure to want logout from the app?"


        yes?.setOnClickListener {

            sharedPreference.saveData("user_id","")
            sharedPreference.saveData("mobile","")
            sharedPreference.saveData("token","")
            startActivity(Intent(this@Profile,SplashActivity::class.java))
            finishAffinity()
            dialog.dismiss()
        }

        no?.setOnClickListener {
            dialog.dismiss()
        }

        dialog?.show()
    }
}