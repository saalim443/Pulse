package codeflies.com.pulse.Home.Fragments.Candidates

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import codeflies.com.pulse.Candidate.AddCandidate.AddCandidateActivity
import codeflies.com.pulse.Helpers.ProgressDisplay
import codeflies.com.pulse.Helpers.RetrofitClient
import codeflies.com.pulse.Helpers.SharedPreference
import codeflies.com.pulse.Models.Candidates.ResponseCandidate
import codeflies.com.pulse.databinding.FragmentCandidatesBinding
import com.codeflies.supertravel.TabsLayou.TabLayoutFragment.UpComingRides.CandidateAdapter
import codeflies.com.pulse.Helpers.Interfaces.GetData
import codeflies.com.pulse.Helpers.Interfaces.Refresh
import codeflies.com.pulse.Models.Candidates.CandidatesItem
import codeflies.com.pulse.Models.Leaves.LeavesItem
import com.codeflies.supertravel.TabsLayou.TabLayoutFragment.UpComingRides.LeaveAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Candidates : Fragment(), Refresh {

    var binding: FragmentCandidatesBinding? = null
    lateinit var sharedPreference: SharedPreference
    lateinit var progressDisplay: ProgressDisplay

    lateinit var candidateAdapter: CandidateAdapter
    var scrollStatus=0
    var page=1
    val limit=30

    companion object{
        lateinit var refresh: Refresh
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCandidatesBinding.inflate(inflater, container, false)

        refresh=this
        sharedPreference= SharedPreference(activity)
        progressDisplay= ProgressDisplay(activity)
        binding!!.addCandidate.setOnClickListener {
            val intent = Intent(requireActivity(), AddCandidateActivity::class.java)
            startActivity(intent)
        }


        if(sharedPreference.getData("role")=="admin" || sharedPreference.getData("role")=="hr_manager") {
            binding!!.addCandidate.visibility=View.VISIBLE
        }else{
            binding!!.addCandidate.visibility=View.GONE
        }



        binding?.candidate?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!recyclerView.canScrollVertically(1)) {
                    onScrolledToBottom()
                }
            }
        })

        val layoutManager = LinearLayoutManager(activity)
        binding?.candidate?.layoutManager = layoutManager

        candidateAdapter = CandidateAdapter(requireActivity(),ArrayList<CandidatesItem>())
        binding?.candidate?.adapter = candidateAdapter




        getCandidate()
        return binding!!.root
    }
    private fun onScrolledToBottom() {
        if (scrollStatus == 0) {
            scrollStatus = 1
            page += 1
            getCandidate()
        }
    }
    private fun getCandidate() {
        if(page==1) {
            progressDisplay.show()
        }else{
            binding?.progressBar?.visibility=View.VISIBLE
        }
        val getData: GetData =
            RetrofitClient.getRetrofit().create(GetData::class.java)
        val call: Call<ResponseCandidate> =
            getData.candidates("Bearer "+sharedPreference.getData("token"),page.toString(),limit.toString())
        call.enqueue(object : Callback<ResponseCandidate?> {
            override fun onResponse(call: Call<ResponseCandidate?>, response: Response<ResponseCandidate?>) {

                if(response.isSuccessful) {
                    if (response.body()?.status == true) {

                        candidateAdapter.addData(response.body()?.candidates!!);

                        if (response.body()?.candidates?.size!! < limit) {
                            scrollStatus = 1;
                        } else {
                            scrollStatus = 0;
                        }

                    } else {
                        Toast.makeText(
                            activity,
                            response.body()?.message,
                            Toast.LENGTH_SHORT
                        ).show()
                        // Toast.makeText( Dashboard.activity, "Sorry!! someone has already accepted the ride", Toast.LENGTH_SHORT ).show();
                    }

                }else{
                    Toast.makeText(
                        context,
                        "Something went wrong !",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                if(page==1) {
                    progressDisplay.dismiss()
                }else{
                    binding?.progressBar?.visibility=View.GONE
                }
            }

            override fun onFailure(call: Call<ResponseCandidate?>, t: Throwable) {
                if(page==1) {
                    progressDisplay.dismiss()
                }else{
                    binding?.progressBar?.visibility=View.GONE
                }
                Toast.makeText(
                    activity,
                    "Something went wrong !",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    override fun onRefresh() {
        page=1
        scrollStatus = 1;
        candidateAdapter.addClearData()
        getCandidate()
    }

}