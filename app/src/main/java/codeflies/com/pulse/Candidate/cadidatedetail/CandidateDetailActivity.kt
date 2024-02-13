package codeflies.com.pulse.Candidate.cadidatedetail

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import codeflies.com.pulse.Helpers.Constants
import codeflies.com.pulse.Helpers.FunctionClass
import codeflies.com.pulse.Helpers.RetrofitClient
import codeflies.com.pulse.Helpers.SharedPreference
import codeflies.com.pulse.Models.CandidateDetails.ResponseDetails
import codeflies.com.pulse.Models.Candidates.CandidatesItem
import codeflies.com.pulse.R
import codeflies.com.pulse.databinding.ActivityCandidateDetailBinding
import com.codeflies.supertravel.TabsLayou.TabLayoutFragment.UpComingRides.CommentAdapter
import codeflies.com.pulse.Helpers.Interfaces.GetData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CandidateDetailActivity : AppCompatActivity() {
    lateinit var binding :ActivityCandidateDetailBinding
    var context : Context= this@CandidateDetailActivity

    lateinit var sharedPreference: SharedPreference

    companion object{
        lateinit var candidate: CandidatesItem
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityCandidateDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreference=SharedPreference(this@CandidateDetailActivity)

        binding.tvName.text = FunctionClass.makeColoredText(candidate?.designation,
            candidate?.name+" ("+ candidate?.designation+")",context.getColor(R.color.pulse_color))
        binding.tvRemark.text= candidate.remarks
        binding.tvEmail.text= candidate.email
        binding.tvPhoneOne.text= candidate.mobile
        binding.tvPhoneTwo.text= ", "+ candidate.alternateMobile
        binding.tvapproval.text= candidate.status
        binding.tvRecuriter.text= "By: "+ candidate.recruiter?.name

        if(candidate.status=="in_progress"){
            binding.tvapproval.setTextColor(context.getColor(R.color.orange))
            binding.tvapproval.text ="In Progress"
        }else if(candidate.status=="selected"){
            binding.tvapproval.setTextColor(context.getColor(R.color.pulse_color))
            binding.tvapproval.text ="Selected"
        }else if(candidate.status=="rejected"){
            binding.tvapproval.setTextColor(context.getColor(R.color.red))
            binding.tvapproval.text ="Rejected"
        }else{
            binding.tvapproval.setTextColor(context.getColor(R.color.orange))
            binding.tvapproval.text ="Not Interested"
        }

        binding.tvCurrentPakage.text= "\u20b9"+candidate.currentSalary.toString()+"/-"
        binding.tvExpirience.text= candidate.experienceYears.toString()+"y"+" "+ candidate.experienceMonths.toString()+"m"


        binding.tvDateCreated.text="Date: "+FunctionClass.changeDate(candidate.createdAt)


        binding.tvPhoneOne.setOnClickListener {
            val phoneNumber =  candidate.mobile
            val intent = Intent(Intent.ACTION_DIAL).apply {
                data = Uri.parse("tel:$phoneNumber")
            }
            startActivity(intent)
        }

        binding.tvPhoneTwo.setOnClickListener {
            val phoneNumber =  candidate.alternateMobile
            val intent = Intent(Intent.ACTION_DIAL).apply {
                data = Uri.parse("tel:$phoneNumber")
            }
            startActivity(intent)
        }

        // Download resume when clicked
        binding.btnDownloadResume.setOnClickListener {
            FunctionClass.downloadFile(applicationContext, Constants.IMG_URL+ candidate.resume.toString()) // Change the URL to your actual download link
        }


        binding.back.setOnClickListener {
            finish()
        }

        getCandidate()

    }




    fun gotoBack(view: View) {

        onBackPressed()
    }

    private fun getCandidate() {

        val getData: GetData =
            RetrofitClient.getRetrofit().create(GetData::class.java)
        val call: Call<ResponseDetails> =
            getData.candidatesDetails("Bearer "+sharedPreference.getData("token"), candidate.id.toString())
        call.enqueue(object : Callback<ResponseDetails?> {
            override fun onResponse(call: Call<ResponseDetails?>, response: Response<ResponseDetails?>) {
                if (response.body()?.status==true) {

                    binding?.comments?.layoutManager = LinearLayoutManager(applicationContext)
                    binding?.comments?.setHasFixedSize(true)
                    binding?.comments?.adapter= CommentAdapter(applicationContext!!, response.body()?.comments)
                } else {
                    Toast.makeText(
                        applicationContext,
                        response.body()?.message ,
                        Toast.LENGTH_SHORT
                    ).show()
                    // Toast.makeText( Dashboard.activity, "Sorry!! someone has already accepted the ride", Toast.LENGTH_SHORT ).show();
                }

            }

            override fun onFailure(call: Call<ResponseDetails?>, t: Throwable) {

                Toast.makeText(
                    applicationContext,
                    "Something went wrong !",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}