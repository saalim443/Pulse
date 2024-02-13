package codeflies.com.pulse.Profiles

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import codeflies.com.pulse.Helpers.Constants
import codeflies.com.pulse.Helpers.FunctionClass
import codeflies.com.pulse.Helpers.ProgressDisplay
import codeflies.com.pulse.Helpers.RetrofitClient
import codeflies.com.pulse.Helpers.SharedPreference
import codeflies.com.pulse.Intro.SplashActivity
import codeflies.com.pulse.Models.ResponseNormal
import codeflies.com.pulse.Models.UserData.ResponseProfile
import codeflies.com.pulse.R
import codeflies.com.pulse.Settings.WebviewActivity
import codeflies.com.pulse.databinding.ActivityProfileBinding
import com.bumptech.glide.Glide
import com.codeflies.supertravel.TabsLayou.TabLayoutFragment.UpComingRides.ManagersListAdapter
import codeflies.com.pulse.Helpers.Interfaces.GetData
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

        progressDisplay = ProgressDisplay(this);

        sharedPreference = SharedPreference(this);


        binding.back.setOnClickListener {
            finish()
        }

        binding.help.setOnClickListener {
            WebviewActivity.pageTitle="Leave Management"
            startActivity(Intent(this@Profile,WebviewActivity::class.java))
        }

        binding.logout.setOnClickListener {
            exitApp()
        }

        if(sharedPreference.getData("role")=="employee"){
            binding.lyReporting.visibility= View.VISIBLE
            binding.lyDob.visibility= View.VISIBLE
            binding.vDob.visibility= View.VISIBLE
        }else{
            binding.lyReporting.visibility= View.GONE
            binding.lyDob.visibility= View.GONE
            binding.vDob.visibility= View.GONE
        }

        if(sharedPreference.getData("role")=="admin"){
            binding.lySalary.visibility= View.GONE
            binding.vSalary.visibility= View.GONE
        }else{
            binding.lySalary.visibility= View.VISIBLE
            binding.vSalary.visibility= View.VISIBLE
        }
        profile()
    }


    private fun profile() {
        progressDisplay.show()
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

                    binding.name.text = response.body()!!.user?.name
                    binding.mobile.text = response.body()!!.user?.mobile
                    binding.email.text = response.body()!!.user?.email
                    binding.job.text = FunctionClass.getRole(response.body()!!.user?.primaryRole)
                    if(sharedPreference.getData("role")!="admin") {
                        binding.salary.text = response.body()!!.user?.currentSalary?.amount
                    }
                    if(sharedPreference.getData("role")=="employee"){
                        binding.dob.text= FunctionClass.changeDate(response.body()!!.user?.employee?.dateOfBirth)
                        binding.managerList.layoutManager=LinearLayoutManager(this@Profile,LinearLayoutManager.HORIZONTAL,false)
                        binding.managerList.setHasFixedSize(true)
                        binding.managerList.adapter=ManagersListAdapter(applicationContext,response!!.body()?.user?.employee?.managers)
                    }else{
                        binding.managerList.visibility=View.GONE
                    }

                    Glide.with(applicationContext).load(Constants.IMG_URL+response.body()?.user?.profileImg).placeholder(R.drawable.person).into(binding.image)

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

        title?.text = getString(R.string.logout)
        txtDesc?.text = getString(R.string.logout_desc)


        yes?.setOnClickListener {
            logout()
            dialog.dismiss()
        }

        no?.setOnClickListener {
            dialog.dismiss()
        }

        dialog?.show()
    }

    private fun logout() {
        progressDisplay.show()
        val getData: GetData =
            RetrofitClient.getRetrofit().create(GetData::class.java)
        val call: Call<ResponseNormal> =
            getData.logout(
                "Bearer " + sharedPreference.getData("token")
            )
        call.enqueue(object : Callback<ResponseNormal?> {
            override fun onResponse(
                call: Call<ResponseNormal?>,
                response: Response<ResponseNormal?>
            ) {
                if (response.body()?.status == true) {

                    sharedPreference.saveData("user_id", "")
                    sharedPreference.saveData("mobile", "")
                    sharedPreference.saveData("token", "")
                    startActivity(Intent(this@Profile, SplashActivity::class.java))
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

            override fun onFailure(call: Call<ResponseNormal?>, t: Throwable) {
                progressDisplay.dismiss()
                Toast.makeText(
                    applicationContext,
                    "Something went wrong !",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}