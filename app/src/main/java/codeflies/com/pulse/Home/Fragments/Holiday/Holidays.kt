package codeflies.com.pulse.Home.Fragments.Holiday

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import codeflies.com.pulse.Helpers.ProgressDisplay
import codeflies.com.pulse.Helpers.RetrofitClient
import codeflies.com.pulse.Helpers.SharedPreference
import codeflies.com.pulse.Models.Holidays.ResponseHoliday
import codeflies.com.pulse.databinding.FragmentHolidaysBinding
import com.codeflies.supertravel.TabsLayou.TabLayoutFragment.UpComingRides.HolidayAdapter
import codeflies.com.pulse.Helpers.Interfaces.GetData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Holidays : Fragment() {

    var binding: FragmentHolidaysBinding? = null
    lateinit var sharedPreference: SharedPreference
    lateinit var progressDisplay: ProgressDisplay
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHolidaysBinding.inflate(inflater, container, false)

        sharedPreference = SharedPreference(activity)
        progressDisplay = ProgressDisplay(activity)


        getHolidays()
        return binding!!.root
    }

    private fun getHolidays() {
        progressDisplay.show()
        val getData: GetData =
            RetrofitClient.getRetrofit().create(GetData::class.java)
        val call: Call<ResponseHoliday> =
            getData.holiday("Bearer " + sharedPreference.getData("token"))
        call.enqueue(object : Callback<ResponseHoliday?> {
            override fun onResponse(
                call: Call<ResponseHoliday?>,
                response: Response<ResponseHoliday?>
            ) {
                if (response.isSuccessful) {
                    if (response.body()?.status == true) {

                        binding?.holiday?.layoutManager = LinearLayoutManager(activity)
                        binding?.holiday?.setHasFixedSize(true)
                        binding?.holiday?.adapter =
                            HolidayAdapter(activity!!, response.body()?.holidays)
                    } else {
                        Toast.makeText(
                            activity,
                            response.body()?.message,
                            Toast.LENGTH_SHORT
                        ).show()
                        // Toast.makeText( Dashboard.activity, "Sorry!! someone has already accepted the ride", Toast.LENGTH_SHORT ).show();
                    }
                } else {
                    Toast.makeText(
                        context,
                        "Something went wrong !",
                        Toast.LENGTH_SHORT
                    ).show()

                }

                progressDisplay.dismiss()
            }

            override fun onFailure(call: Call<ResponseHoliday?>, t: Throwable) {
                progressDisplay.dismiss()
                Toast.makeText(
                    activity,
                    "Something went wrong !",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}