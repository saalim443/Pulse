package codeflies.com.pulse.Home.Fragments.Candidates

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import codeflies.com.pulse.Candidate.AddCandidate.AddCandidateActivity
import codeflies.com.pulse.Helpers.ProgressDisplay
import codeflies.com.pulse.Helpers.RetrofitClient
import codeflies.com.pulse.Helpers.SharedPreference
import codeflies.com.pulse.Models.Candidates.ResponseCandidate
import codeflies.com.pulse.databinding.FragmentCandidatesBinding
import com.codeflies.supertravel.TabsLayou.TabLayoutFragment.UpComingRides.CandidateAdapter
import com.example.ehcf_doctor.Retrofit.GetData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Candidates : Fragment() {

    var binding: FragmentCandidatesBinding? = null
    lateinit var sharedPreference: SharedPreference
    lateinit var progressDisplay: ProgressDisplay
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCandidatesBinding.inflate(inflater, container, false)

        sharedPreference= SharedPreference(activity)
        progressDisplay= ProgressDisplay(activity)
        binding!!.addCandidate.setOnClickListener {
            val intent = Intent(requireActivity(), AddCandidateActivity::class.java)
            startActivity(intent)
        }


        if(sharedPreference.getData("role")=="admin"){
            binding!!.addCandidate.visibility=View.VISIBLE
        }else if(sharedPreference.getData("role")=="hr_manager"){
            binding!!.addCandidate.visibility=View.VISIBLE
        }else{
            binding!!.addCandidate.visibility=View.GONE
        }

        getCandidate()
        return binding!!.root
    }

    private fun getCandidate() {
        progressDisplay.show()
        val getData: GetData =
            RetrofitClient.getRetrofit().create(GetData::class.java)
        val call: Call<ResponseCandidate> =
            getData.candidates("Bearer "+sharedPreference.getData("token"))
        call.enqueue(object : Callback<ResponseCandidate?> {
            override fun onResponse(call: Call<ResponseCandidate?>, response: Response<ResponseCandidate?>) {
                if (response.body()?.status==true) {

                    binding?.candidate?.layoutManager = LinearLayoutManager(activity)
                    binding?.candidate?.setHasFixedSize(true)
                    binding?.candidate?.adapter= CandidateAdapter(activity!!, response.body()?.candidates)
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

            override fun onFailure(call: Call<ResponseCandidate?>, t: Throwable) {
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