package codeflies.com.pulse.Home.Fragments.Leaves

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
import codeflies.com.pulse.Models.Leaves.ResponseLeaves
import codeflies.com.pulse.databinding.FragmentHomeBinding
import com.codeflies.supertravel.TabsLayou.TabLayoutFragment.UpComingRides.LeaveAdapter
import com.example.ehcf_doctor.Retrofit.GetData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Home : Fragment() {
    var binding: FragmentHomeBinding? = null
    lateinit var sharedPreference: SharedPreference
    lateinit var progressDisplay: ProgressDisplay
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        sharedPreference= SharedPreference(activity)
        progressDisplay= ProgressDisplay(activity)


        getLeaves()
        return binding!!.root
    }


    private fun getLeaves() {
        progressDisplay.show()
        val getData: GetData =
            RetrofitClient.getRetrofit().create(GetData::class.java)
        val call: Call<ResponseLeaves> =
            getData.leaves("Bearer "+sharedPreference.getData("token"))
        call.enqueue(object : Callback<ResponseLeaves?> {
            override fun onResponse(call: Call<ResponseLeaves?>, response: Response<ResponseLeaves?>) {
                if (response.body()?.status==true) {

                    binding?.leaves?.layoutManager =LinearLayoutManager(activity)
                    binding?.leaves?.setHasFixedSize(true)
                    binding?.leaves?.adapter=LeaveAdapter(activity!!, response.body()?.leaves)
                } else {
                    Toast.makeText(
                        activity,
                        response.body()?.message ,
                        Toast.LENGTH_SHORT
                    ).show()
                    // Toast.makeText( Dashboard.activity, "Sorry!! someone has already accepted the ride", Toast.LENGTH_SHORT ).show();
                }

                progressDisplay.dismiss()
            }

            override fun onFailure(call: Call<ResponseLeaves?>, t: Throwable) {
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