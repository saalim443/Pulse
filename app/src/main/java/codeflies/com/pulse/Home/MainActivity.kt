package codeflies.com.pulse.Home

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import codeflies.com.pulse.Helpers.Constants
import codeflies.com.pulse.Helpers.RetrofitClient
import codeflies.com.pulse.Helpers.SharedPreference
import codeflies.com.pulse.Home.Fragments.Attendence
import codeflies.com.pulse.Home.Fragments.Candidates.Candidates
import codeflies.com.pulse.Home.Fragments.Holiday.Holidays
import codeflies.com.pulse.Home.Fragments.Leaves.Home
import codeflies.com.pulse.Models.ResponseNotification
import codeflies.com.pulse.Models.UserData.ResponseProfile
import codeflies.com.pulse.Notifications.NotificationActivity
import codeflies.com.pulse.Profiles.Profile
import codeflies.com.pulse.R
import codeflies.com.pulse.databinding.ActivityMainBinding
import com.bumptech.glide.Glide
import codeflies.com.pulse.Helpers.Interfaces.GetData
import codeflies.com.pulse.Helpers.Interfaces.RefreshStatus
import com.google.firebase.FirebaseApp
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity(), RefreshStatus {

    lateinit var binding: ActivityMainBinding
    lateinit var sharedPreference: SharedPreference

    companion object{
        lateinit var activity: Activity
        lateinit var refreshStatus: RefreshStatus
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.rootLayout)

        activity=this
        refreshStatus=this
        FirebaseApp.initializeApp(this);
        sharedPreference= SharedPreference(this);
        changeMenu(1)

        binding.lyHome.setOnClickListener(
            View.OnClickListener {
                binding.title.text=getString(R.string.leaves)
                changeMenu(1)
            }
        )
        binding.lyFav.setOnClickListener(
            View.OnClickListener {
                binding.title.text=getString(R.string.candidates)
                changeMenu(2)
            }
        )
        binding.lyCart.setOnClickListener(
            View.OnClickListener {
                binding.title.text=getString(R.string.holidays)
                changeMenu(3)
            }
        )
        binding.lyMenu.setOnClickListener(
            View.OnClickListener {
                binding.title.text=getString(R.string.attendance)
                changeMenu(4)
            }
        )

         binding.profile.setOnClickListener {
             startActivity(Intent(this@MainActivity, Profile::class.java))
         }

        binding.notification.setOnClickListener {
            startActivity(Intent(this@MainActivity, NotificationActivity::class.java))
        }

        profile()

        notification()
        checkPermission()

    }

    fun changeMenu(i: Int) {

        if (i == 1) {
            changeFragment(Home())
            binding.ivHome.setColorFilter(
                ContextCompat.getColor(this, R.color.pulse_color),
                PorterDuff.Mode.SRC_ATOP
            )
            binding.vHome.visibility = View.VISIBLE

            binding.ivFav.setColorFilter(
                ContextCompat.getColor(this, R.color.white),
                PorterDuff.Mode.SRC_ATOP
            )
            binding.vFav.visibility = View.GONE

            binding.ivCart.setColorFilter(
                ContextCompat.getColor(this, R.color.white),
                PorterDuff.Mode.SRC_ATOP
            )
            binding.vCart.visibility = View.GONE

            binding.ivMenu.setColorFilter(
                ContextCompat.getColor(this, R.color.white),
                PorterDuff.Mode.SRC_ATOP
            )
            binding.vMenu.visibility = View.GONE


        } else if (i == 2) {
            changeFragment(Candidates())

            binding.ivHome.setColorFilter(
                ContextCompat.getColor(this, R.color.white),
                PorterDuff.Mode.SRC_ATOP
            )
            binding.vHome.visibility = View.GONE

            binding.ivFav.setColorFilter(
                ContextCompat.getColor(this, R.color.pulse_color),
                PorterDuff.Mode.SRC_ATOP
            )
            binding.vFav.visibility = View.VISIBLE

            binding.ivCart.setColorFilter(
                ContextCompat.getColor(this, R.color.white),
                PorterDuff.Mode.SRC_ATOP
            )
            binding.vCart.visibility = View.GONE

            binding.ivMenu.setColorFilter(
                ContextCompat.getColor(this, R.color.white),
                PorterDuff.Mode.SRC_ATOP
            )
            binding.vMenu.visibility = View.GONE
        } else if (i == 3) {
            changeFragment(Holidays())


            binding.ivHome.setColorFilter(
                ContextCompat.getColor(this, R.color.white),
                PorterDuff.Mode.SRC_ATOP
            )
            binding.vHome.visibility = View.GONE

            binding.ivFav.setColorFilter(
                ContextCompat.getColor(this, R.color.white),
                PorterDuff.Mode.SRC_ATOP
            )
            binding.vFav.visibility = View.GONE

            binding.ivCart.setColorFilter(
                ContextCompat.getColor(this, R.color.pulse_color),
                PorterDuff.Mode.SRC_ATOP
            )
            binding.vCart.visibility = View.VISIBLE

            binding.ivMenu.setColorFilter(
                ContextCompat.getColor(this, R.color.white),
                PorterDuff.Mode.SRC_ATOP
            )
            binding.vMenu.visibility = View.GONE
        } else if (i == 4) {
            changeFragment(Attendence())


            binding.ivHome.setColorFilter(
                ContextCompat.getColor(this, R.color.white),
                PorterDuff.Mode.SRC_ATOP
            )
            binding.vHome.visibility = View.GONE

            binding.ivFav.setColorFilter(
                ContextCompat.getColor(this, R.color.white),
                PorterDuff.Mode.SRC_ATOP
            )
            binding.vFav.visibility = View.GONE

            binding.ivCart.setColorFilter(
                ContextCompat.getColor(this, R.color.white),
                PorterDuff.Mode.SRC_ATOP
            )
            binding.vCart.visibility = View.GONE

            binding.ivMenu.setColorFilter(
                ContextCompat.getColor(this, R.color.pulse_color),
                PorterDuff.Mode.SRC_ATOP
            )
            binding.vMenu.visibility = View.VISIBLE
        }
    }

    fun changeFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frameLayout, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun exitApp() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.logout_layout)
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.setCancelable(true)
        dialog.window?.attributes?.windowAnimations = R.style.animation
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val yes = dialog.findViewById<TextView>(R.id.yes)
        val no = dialog.findViewById<TextView>(R.id.no)
        val title = dialog.findViewById<TextView>(R.id.title)
        val txtDesc = dialog.findViewById<TextView>(R.id.txtDesc)

        title.text = getString(R.string.exit_app)
        txtDesc.text = getString(R.string.exit_desc)


        yes.setOnClickListener {
            finishAffinity()
            dialog.dismiss()
        }

        no.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    override fun onBackPressed() {
//        super.onBackPressed()
        exitApp()
    }

    private fun notification() {
        val getData: GetData =
            RetrofitClient.getRetrofit().create(GetData::class.java)
        val call: Call<ResponseNotification> =
            getData.notification("Bearer "+sharedPreference.getData("token"))
        call.enqueue(object : Callback<ResponseNotification?> {
            override fun onResponse(call: Call<ResponseNotification?>, response: Response<ResponseNotification?>) {
               if(response.isSuccessful) {
                   if (response.body()?.status == true) {
                       var i = 0
                       var count = 0;
                       while (i < response!!.body()?.notifications?.size!!) {
                           if (response!!.body()?.notifications?.get(i)?.readAt == null) {
                               count++
                           }
                           i++
                       }

                       binding.count.text = count.toString()
                   } else {
                       binding.count.text = "0"
                   }
               }else{
                   binding.count.text = "0"
               }

            }

            override fun onFailure(call: Call<ResponseNotification?>, t: Throwable) {
                binding.count.text="0"
            }
        })
    }

    override fun onResume() {
        super.onResume()
        notification()
        profile()
    }


    private fun profile() {
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
                if(response.isSuccessful) {
                    if (response.body()?.status == true) {
                        sharedPreference.saveData("role", response.body()?.user?.primaryRole)
                        Glide.with(applicationContext)
                            .load(Constants.IMG_URL + response.body()?.user?.profileImg)
                            .placeholder(R.drawable.person).into(binding.profile)

                    } else {
                        Toast.makeText(
                            applicationContext,
                            response.body()?.message.toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                        // Toast.makeText( Dashboard.activity, "Sorry!! someone has already accepted the ride", Toast.LENGTH_SHORT ).show();
                    }
                }else{
                    Toast.makeText(
                        this@MainActivity,
                        "Something went wrong !",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }

            override fun onFailure(call: Call<ResponseProfile?>, t: Throwable) {

                Toast.makeText(
                    applicationContext,
                    "Something went wrong !",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
    fun checkPermission() {
        val permissionsToRequest = mutableListOf<String>()

            if (checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(Manifest.permission.POST_NOTIFICATIONS)
                requestPermissions(
                    permissionsToRequest.toTypedArray(),
                    109
                )
            }
    }

    override fun onRefresh(status: String, name: String) {
        notification()
    }
}