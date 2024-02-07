package codeflies.com.pulse.leaveDetaill

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import codeflies.com.pulse.Models.Candidates.CandidatesItem
import codeflies.com.pulse.databinding.ActivityCandidateDetailBinding


class CandidateDetailActivity : AppCompatActivity() {
    lateinit var bindind :ActivityCandidateDetailBinding


    companion object{
        lateinit var candidate: CandidatesItem
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindind=ActivityCandidateDetailBinding.inflate(layoutInflater)
        setContentView(bindind.root)

        bindind.tvName.text= candidate.name


    }
}