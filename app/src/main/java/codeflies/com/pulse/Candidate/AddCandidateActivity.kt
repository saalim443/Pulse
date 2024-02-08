package codeflies.com.pulse.Candidate

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import codeflies.com.pulse.Helpers.ProgressDisplay
import codeflies.com.pulse.Helpers.SharedPreference

import codeflies.com.pulse.R
import codeflies.com.pulse.databinding.ActivityAddCandidateBinding
import codeflies.com.pulse.databinding.ActivityCandidateDetailBinding

class AddCandidateActivity : AppCompatActivity() {
    var context : Context =this@AddCandidateActivity
    lateinit var binding: ActivityAddCandidateBinding
    lateinit var sharedPreference: SharedPreference
    lateinit var progressDisplay: ProgressDisplay

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityAddCandidateBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPreference = SharedPreference(this)
        progressDisplay = ProgressDisplay(this)


    }
}