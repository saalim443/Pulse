package codeflies.com.pulse.Notifications

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import codeflies.com.pulse.Helpers.ProgressDisplay
import codeflies.com.pulse.Helpers.RetrofitClient
import codeflies.com.pulse.Helpers.SharedPreference
import codeflies.com.pulse.Models.Holidays.ResponseHoliday
import codeflies.com.pulse.Models.ResponseNormal
import codeflies.com.pulse.Models.ResponseNotification
import codeflies.com.pulse.databinding.ActivityNotificationBinding
import codeflies.com.pulse.Notifications.Adapters.NotificationAdapter
import com.codeflies.supertravel.TabsLayou.TabLayoutFragment.UpComingRides.HolidayAdapter
import com.example.ehcf_doctor.Retrofit.GetData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NotificationActivity : AppCompatActivity() {
    lateinit var binding: ActivityNotificationBinding
    lateinit var progressDisplay: ProgressDisplay
    lateinit var sharedPreference: SharedPreference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityNotificationBinding.inflate(layoutInflater)
        setContentView(binding.root)


        progressDisplay= ProgressDisplay(this@NotificationActivity);
        sharedPreference= SharedPreference(this@NotificationActivity);
        binding.back.setOnClickListener {
            finish()
        }



        notification()
    }


    private fun notification() {
        progressDisplay.show()
        val getData: GetData =
            RetrofitClient.getRetrofit().create(GetData::class.java)
        val call: Call<ResponseNotification> =
            getData.notification("Bearer "+sharedPreference.getData("token"))
        call.enqueue(object : Callback<ResponseNotification?> {
            override fun onResponse(call: Call<ResponseNotification?>, response: Response<ResponseNotification?>) {
                if (response.body()?.status==true) {


                    binding.notifications.layoutManager = LinearLayoutManager(this@NotificationActivity)
                    binding.notifications.setHasFixedSize(true)
                    binding.notifications.adapter = NotificationAdapter(response.body()!!.notifications, this@NotificationActivity)
                    if(response.body()?.notifications?.size==0) {
                        binding.noData.visibility= View.VISIBLE
                        binding.notifications.visibility= View.GONE
                    }else{
                        binding.noData.visibility= View.GONE
                        binding.notifications.visibility= View.VISIBLE
                    }
                } else {
                    binding.noData.visibility= View.VISIBLE
                    binding.notifications.visibility= View.GONE
                    Toast.makeText(
                        applicationContext,
                        response.body()?.message ,
                        Toast.LENGTH_SHORT
                    ).show()
                    // Toast.makeText( Dashboard.activity, "Sorry!! someone has already accepted the ride", Toast.LENGTH_SHORT ).show();
                }

                readNotification()
                progressDisplay.dismiss()
            }

            override fun onFailure(call: Call<ResponseNotification?>, t: Throwable) {
                progressDisplay.dismiss()
                binding.noData.visibility= View.VISIBLE
                binding.notifications.visibility= View.GONE
                Toast.makeText(
                    applicationContext,
                    "Something went wrong !",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun readNotification() {

        val getData: GetData =
            RetrofitClient.getRetrofit().create(GetData::class.java)
        val call: Call<ResponseNormal> =
            getData.readNotification("Bearer "+sharedPreference.getData("token"))
        call.enqueue(object : Callback<ResponseNormal?> {
            override fun onResponse(call: Call<ResponseNormal?>, response: Response<ResponseNormal?>) {
                if (response.body()?.status==true) {


                } else {

                }
            }

            override fun onFailure(call: Call<ResponseNormal?>, t: Throwable) {

            }
        })
    }
}