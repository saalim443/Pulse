package codeflies.com.pulse.leaveDetaill

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import codeflies.com.pulse.Helpers.Constants
import codeflies.com.pulse.Helpers.FunctionClass
import codeflies.com.pulse.Models.Candidates.CandidatesItem
import codeflies.com.pulse.R
import codeflies.com.pulse.databinding.ActivityCandidateDetailBinding


class CandidateDetailActivity : AppCompatActivity() {
    lateinit var bindind :ActivityCandidateDetailBinding
    var context : Context= this@CandidateDetailActivity


    companion object{
        lateinit var candidate: CandidatesItem
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindind=ActivityCandidateDetailBinding.inflate(layoutInflater)
        setContentView(bindind.root)

        bindind.tvName.text= candidate.name
        bindind.tvRemark.text= candidate.remarks
        bindind.tvEmail.text= candidate.email
        bindind.tvPhoneOne.text= candidate.mobile
        bindind.tvPhoneTwo.text= candidate.alternateMobile
        bindind.tvapproval.text= candidate.status
        bindind.tvRecuriter.text= "By: "+candidate.recruiter?.name
        if (candidate.status == "in_progress") {
            bindind.tvapproval.setTextColor(ContextCompat.getColor(context, R.color.green))
        }

        bindind.tvCurrentPakage.text= candidate.currentSalary.toString()+" /"
        bindind.tvExpirience.text= candidate.experienceYears.toString()+"y"+" "+candidate.experienceMonths.toString()+"m"


        bindind.tvDateCreated.text="Date: "+FunctionClass.changeDate(candidate.createdAt)


        bindind.tvPhoneOne.setOnClickListener {
            val phoneNumber =  candidate.mobile
            val intent = Intent(Intent.ACTION_DIAL).apply {
                data = Uri.parse("tel:$phoneNumber")
            }
            startActivity(intent)
        }

        bindind.tvPhoneTwo.setOnClickListener {
            val phoneNumber =  candidate.alternateMobile
            val intent = Intent(Intent.ACTION_DIAL).apply {
                data = Uri.parse("tel:$phoneNumber")
            }
            startActivity(intent)
        }

        // Download resume when clicked
        bindind.btnDownloadResume.setOnClickListener {
            downloadFile( Constants.IMG_URL+candidate.resume.toString()) // Change the URL to your actual download link
        }

    }


    private fun downloadFile(url: String) {
        val request = DownloadManager.Request(Uri.parse(url))
            .setTitle("Resume")
            .setDescription("Downloading resume...")
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            //.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "resume.pdf") // Change the file name and extension as needed

        val downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        downloadManager.enqueue(request)

        Toast.makeText(context, "Downloading resume...", Toast.LENGTH_SHORT).show()
    }

    fun gotoBack(view: View) {

        onBackPressed()
    }
}