package codeflies.com.pulse.Home.Fragments.Leaves

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import codeflies.com.pulse.Helpers.FunctionClass
import codeflies.com.pulse.Helpers.ProgressDisplay
import codeflies.com.pulse.Helpers.RetrofitClient
import codeflies.com.pulse.Helpers.SharedPreference
import codeflies.com.pulse.Models.Candidates.CandidatesItem
import codeflies.com.pulse.Models.Leaves.LeavesDetails
import codeflies.com.pulse.Models.Leaves.LeavesItem
import codeflies.com.pulse.Models.Leaves.ResponseLeaves
import codeflies.com.pulse.Models.Leaves.attachmentsItem
import codeflies.com.pulse.R
import codeflies.com.pulse.databinding.ActivityLeaveDetailsBinding
import codeflies.com.pulse.databinding.ActivityNewLeaveBinding
import com.codeflies.supertravel.TabsLayou.TabLayoutFragment.UpComingRides.LeaveAdapter
import com.example.ehcf_doctor.Retrofit.GetData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LeaveDetailsActivity : AppCompatActivity() {

    lateinit var binding: ActivityLeaveDetailsBinding
    var context: Context = this@LeaveDetailsActivity


    companion object {
        lateinit var leave: LeavesItem
    }

    lateinit var sharedPreference: SharedPreference
    lateinit var progressDisplay: ProgressDisplay
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLeaveDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreference = SharedPreference(context)
        progressDisplay = ProgressDisplay(context)


        binding.back.setOnClickListener {
            finish()
        }

        getLeavesDetails()
    }


    private fun getLeavesDetails() {
        progressDisplay.show()
        Log.e("token", "Bearer " + sharedPreference.getData("token"))
        val getData: GetData =
            RetrofitClient.getRetrofit().create(GetData::class.java)
        val call: Call<LeavesDetails> =
            getData.getLeaveData("Bearer " + sharedPreference.getData("token"), leave.id.toString())
        call.enqueue(object : Callback<LeavesDetails?> {
            override fun onResponse(
                call: Call<LeavesDetails?>,
                response: Response<LeavesDetails?>
            ) {
                if (response.body()?.status == true) {
                    binding.dayBefore.text = response.body()?.leave?.dateAndTimeHuman.toString()
                    binding.dayCreate.text =
                        FunctionClass.changeDate(response.body()?.leave?.createdAt)
                    binding.ShowTitle.text = response.body()?.leave?.title.toString()
                    binding.nameLeave.text = response.body()?.leave?.employeeName.toString()
                    binding.emailLeave.text =
                        "| " + response.body()?.leave?.employeeEmail.toString()
                    binding.codeLeave.text = response.body()?.leave?.employeeCode.toString()
                    binding.notifyToLeaveDate.text =
                        response.body()?.leave?.notifierDetail.toString()
                    binding.contentLeave.text = response.body()?.leave?.content.toString()

                    if (response.body()?.leave?.status == "approved") {
                        binding.statusLeave.setTextColor(context.getColor(R.color.pulse_color))
                        binding.statusLeave.text = "Approved"
                    } else if (response.body()?.leave?.status == "rejected") {
                        binding.statusLeave.setTextColor(context.getColor(R.color.red))
                        binding.statusLeave.text = "Rejected"
                    } else {
                        binding.statusLeave.setTextColor(context.getColor(R.color.orange))
                        binding.statusLeave.text = "Pending"
                    }


                    binding.dateLeave.text =
                        FunctionClass.changeDate(response.body()?.leave?.leaveFrom) + " to " +
                                FunctionClass.changeDate(response.body()?.leave?.leaveEnd) + " | " + response.body()?.leave?.leaveDays + " days" +
                                " | " + response.body()?.leave?.leaveTypes

                    val attachmentsList: List<attachmentsItem> =
                        response.body()?.leave?.attachments!!
                    val adapter = AttachmentsAdapter(attachmentsList)
                    binding.recyclerView.adapter = adapter
                } else {
                    Toast.makeText(
                        context,
                        response.body()?.message,
                        Toast.LENGTH_SHORT
                    ).show()
                    // Toast.makeText( Dashboard.activity, "Sorry!! someone has already accepted the ride", Toast.LENGTH_SHORT ).show();
                }

                progressDisplay.dismiss()
            }

            override fun onFailure(call: Call<LeavesDetails?>, t: Throwable) {
                progressDisplay.dismiss()
                Toast.makeText(
                    context,
                    "Something went wrong !",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

}