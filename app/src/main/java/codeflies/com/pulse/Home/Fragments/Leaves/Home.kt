package codeflies.com.pulse.Home.Fragments.Leaves

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import codeflies.com.pulse.Helpers.Interfaces.GetData
import codeflies.com.pulse.Helpers.Interfaces.Refresh
import codeflies.com.pulse.Helpers.Interfaces.RefreshStatus
import codeflies.com.pulse.Helpers.ProgressDisplay
import codeflies.com.pulse.Helpers.RetrofitClient
import codeflies.com.pulse.Helpers.SharedPreference
import codeflies.com.pulse.Models.Leaves.LeavesItem
import codeflies.com.pulse.Models.Leaves.ResponseLeaves
import codeflies.com.pulse.databinding.FragmentHomeBinding
import com.codeflies.supertravel.TabsLayou.TabLayoutFragment.UpComingRides.LeaveAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Home : Fragment(), Refresh {
    var binding: FragmentHomeBinding? = null
    lateinit var sharedPreference: SharedPreference
    lateinit var progressDisplay: ProgressDisplay

    lateinit var leaveAdapter: LeaveAdapter
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
        binding = FragmentHomeBinding.inflate(inflater, container, false)


        refresh=this
        sharedPreference = SharedPreference(activity)
        progressDisplay = ProgressDisplay(activity)



        if(sharedPreference.getData("role")=="admin") {
            binding!!.addLeave.visibility=View.GONE
        }else{
            binding!!.addLeave.visibility=View.VISIBLE
        }


        binding!!.addLeave.setOnClickListener {
            val intent = Intent(requireActivity(), NewLeaveActivity::class.java)
            startActivity(intent)
        }


        binding?.leaves?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!recyclerView.canScrollVertically(1)) {
                    onScrolledToBottom()
                }
            }
        })

        val layoutManager = LinearLayoutManager(activity)
        binding?.leaves?.layoutManager = layoutManager

        leaveAdapter = LeaveAdapter(requireActivity(),ArrayList<LeavesItem>())
        binding?.leaves?.adapter = leaveAdapter


        getLeaves()

        return binding!!.root
    }
    private fun onScrolledToBottom() {
        if (scrollStatus == 0) {
            scrollStatus = 1
            page += 1
            getLeaves()
        }
    }

    private fun getLeaves() {
        if(page==0) {
            progressDisplay.show()
        }else{
            binding?.progressBar?.visibility=View.VISIBLE
        }
        Log.e("token", "Bearer " + sharedPreference.getData("token"))
        val getData: GetData =
            RetrofitClient.getRetrofit().create(GetData::class.java)
        val call: Call<ResponseLeaves> =
            getData.leaves("Bearer " + sharedPreference.getData("token"),page.toString(),limit.toString())
        call.enqueue(object : Callback<ResponseLeaves?> {
            override fun onResponse(
                call: Call<ResponseLeaves?>,
                response: Response<ResponseLeaves?>
            ) {
                if(response.isSuccessful) {
                    if (response.body()?.status == true) {


                        leaveAdapter.addData(response.body()?.leaves!!);

                        if (response.body()?.leaves?.size!! < limit) {
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
                if(page==0) {
                    progressDisplay.dismiss()
                }else{
                    binding?.progressBar?.visibility=View.GONE
                }
            }

            override fun onFailure(call: Call<ResponseLeaves?>, t: Throwable) {
                if(page==0) {
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
        page=0
        scrollStatus = 1;
        leaveAdapter.addClearData()
       getLeaves()
    }

}