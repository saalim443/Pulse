package codeflies.com.pulse.Candidate.cadidatedetail

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import codeflies.com.pulse.Helpers.Constants
import codeflies.com.pulse.Helpers.FunctionClass
import codeflies.com.pulse.Helpers.RetrofitClient
import codeflies.com.pulse.Helpers.SharedPreference
import codeflies.com.pulse.Models.Candidates.CandidatesItem
import codeflies.com.pulse.R
import codeflies.com.pulse.databinding.ActivityCandidateDetailBinding
import com.codeflies.supertravel.TabsLayou.TabLayoutFragment.UpComingRides.CommentAdapter
import codeflies.com.pulse.Helpers.Interfaces.GetData
import codeflies.com.pulse.Helpers.ProgressDisplay
import codeflies.com.pulse.Helpers.SnackBarUtils
import codeflies.com.pulse.Helpers.getRealPathFromUri
import codeflies.com.pulse.Models.CandidateDetails.CandidateDetails
import codeflies.com.pulse.Models.CandidateDetails.CandidateStatus
import codeflies.com.pulse.Models.CandidateDetails.Interviewers
import codeflies.com.pulse.Models.ResponseNormal
import com.codeflies.supertravel.TabsLayou.TabLayoutFragment.UpComingRides.InterviewAdapter
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import com.google.gson.JsonObject
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class CandidateDetailActivity : AppCompatActivity() {
    lateinit var binding: ActivityCandidateDetailBinding
    var context: Context = this@CandidateDetailActivity
    val REQUEST_CODE = 200
    private val REQUEST_CODE_PERMISSION = 123

    lateinit var sharedPreference: SharedPreference
    lateinit var progressDisplay: ProgressDisplay

    companion object {
        lateinit var candidate: CandidatesItem
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCandidateDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreference = SharedPreference(this@CandidateDetailActivity)
        progressDisplay= ProgressDisplay(this)

        binding.tvName.text = FunctionClass.makeColoredText(
            candidate?.designation,
            candidate.name + " (" + candidate?.designation + ")",
            context.getColor(R.color.pulse_color)
        )
        binding.tvRemark.text = candidate.remarks
        binding.tvEmail.text = candidate.email
        binding.tvPhoneOne.text = candidate.mobile
        binding.tvPhoneTwo.text = ", " + candidate.alternateMobile
        binding.tvapproval.text = candidate.status
        binding.tvRecuriter.text = "By: " + candidate.recruiter?.name

        if (candidate.status == "in_progress") {
            binding.tvapproval.setTextColor(context.getColor(R.color.orange))
            binding.tvapproval.text = "In Progress"
        } else if (candidate.status == "selected") {
            binding.tvapproval.setTextColor(context.getColor(R.color.pulse_color))
            binding.tvapproval.text = "Selected"
        } else if (candidate.status == "rejected") {
            binding.tvapproval.setTextColor(context.getColor(R.color.red))
            binding.tvapproval.text = "Rejected"
        } else {
            binding.tvapproval.setTextColor(context.getColor(R.color.orange))
            binding.tvapproval.text = "Not Interested"
        }

        binding.tvCurrentPakage.text = "\u20b9" + candidate.currentSalary.toString() + "/-"
        binding.tvExpirience.text =
            candidate.experienceYears.toString() + "y" + " " + candidate.experienceMonths.toString() + "m"

        binding.tvDateCreated.text = "Date: " + FunctionClass.changeDate(candidate.createdAt)

        binding.tvPhoneOne.setOnClickListener {
            val phoneNumber = candidate.mobile
            val intent = Intent(Intent.ACTION_DIAL).apply {
                data = Uri.parse("tel:$phoneNumber")
            }
            startActivity(intent)
        }

        binding.tvPhoneTwo.setOnClickListener {
            val phoneNumber = candidate.alternateMobile
            val intent = Intent(Intent.ACTION_DIAL).apply {
                data = Uri.parse("tel:$phoneNumber")
            }
            startActivity(intent)
        }

        binding.btnDownloadResume.setOnClickListener {
            FunctionClass.downloadFile(
                applicationContext,"Resume",
                Constants.IMG_URL + candidate.resume.toString()
            ) // Change the URL to your actual download link
        }

        binding.back.setOnClickListener {
            finish()
        }

        binding.statusChange.setOnClickListener {
            showCustomMenu(binding.tvapproval)
        }

        binding.addInterview.setOnClickListener {
            addInterviewDialog()
        }

        binding.addComment.setOnClickListener {
            addCommentDialog()
        }

        getCandidate()

    }


    private fun showCustomMenu(view: View) {
        val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val customView = inflater.inflate(R.layout.status_menu_item, null)

        val popupWindow = PopupWindow(
            customView,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        )

        // Set click listeners for menu items
        val pending = customView.findViewById<TextView>(R.id.pending)
        val approve = customView.findViewById<TextView>(R.id.approve)
        val reject = customView.findViewById<TextView>(R.id.reject)

        pending.setOnClickListener {
            // Handle option 1 click
            showToast("Option 1 Clicked")
            popupWindow.dismiss()
        }

        approve.setOnClickListener {
            // Handle option 2 click
            showToast("Option 2 Clicked")
            popupWindow.dismiss()
        }
        reject.setOnClickListener {
            // Handle option 2 click
            showToast("Option 2 Clicked")
            popupWindow.dismiss()
        }
        popupWindow.showAsDropDown(view)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    fun gotoBack(view: View) {
        finish()
    }

    private fun getCandidate() {
        progressDisplay.show()
        val getData: GetData =
            RetrofitClient.getRetrofit().create(GetData::class.java)
        val call: Call<CandidateDetails> =
            getData.candidatesDetails(
                "Bearer " + sharedPreference.getData("token"),
                candidate.id.toString()
            )
        call.enqueue(object : Callback<CandidateDetails?> {
            override fun onResponse(
                call: Call<CandidateDetails?>,
                response: Response<CandidateDetails?>
            ) {
                if (response.body()?.status == true) {

                    binding.comments.layoutManager = LinearLayoutManager(applicationContext)
                    binding.comments.setHasFixedSize(true)
                    binding.comments.adapter =
                        CommentAdapter(applicationContext!!, response.body()?.comments)


                    binding.interviews.layoutManager = LinearLayoutManager(applicationContext)
                    binding.interviews.setHasFixedSize(true)
                    binding.interviews.adapter =
                        InterviewAdapter(applicationContext!!, response.body()?.candidateRounds)

                } else {
                    Toast.makeText(
                        applicationContext,
                        response.body()?.message,
                        Toast.LENGTH_SHORT
                    ).show()
                    // Toast.makeText( Dashboard.activity, "Sorry!! someone has already accepted the ride", Toast.LENGTH_SHORT ).show();
                }
                progressDisplay.dismiss()

            }

            override fun onFailure(call: Call<CandidateDetails?>, t: Throwable) {
                progressDisplay.dismiss()
                Toast.makeText(
                    applicationContext,
                    "Something went wrong !",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }


    fun addInterviewDialog() {
        val dialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.add_interview_dialog, null)
        val submit = view.findViewById<Button>(R.id.submit)


        val statusSpinner = view.findViewById<Spinner>(R.id.status)
        val date = view.findViewById<TextView>(R.id.date)
        val round = view.findViewById<Spinner>(R.id.round)
        val interviewer = view.findViewById<Spinner>(R.id.interviewer)
        val progressBar = view.findViewById<ProgressBar>(R.id.progressBar)


        date.setOnClickListener {
            openDatePickerDialog(date);
        }
        getStatus(progressBar,statusSpinner, round, interviewer)
        submit.setOnClickListener {

            if (interviewersTxt != "") {
                if (date.text.toString() != "") {
                    if (roundsTxt != "") {
                        if (status != "") {
                            addInterview(progressBar,dialog, date.text.toString())
                        } else {
                            Toast.makeText(
                                applicationContext,
                                "Please select status",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        Toast.makeText(
                            applicationContext,
                            "Please select round",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(applicationContext, "Please select date", Toast.LENGTH_SHORT)
                        .show()
                }
            } else {
                Toast.makeText(applicationContext, "Please select interviewer", Toast.LENGTH_SHORT)
                    .show()
            }

        }

        dialog.setCancelable(true)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));

        dialog.setContentView(view)

        dialog.show()
    }

    lateinit var status: String
    lateinit var interviewersTxt: String
    lateinit var roundsTxt: String
    fun getStatus(progressBar: ProgressBar,view: Spinner, rounds: Spinner, interviewers: Spinner) {
        progressBar.visibility=View.VISIBLE
        Log.e("token", "Bearer " + sharedPreference.getData("token"))
        val getData: GetData =
            RetrofitClient.getRetrofit().create(GetData::class.java)
        val call: Call<Interviewers> =
            getData.getInterviewers("Bearer " + sharedPreference.getData("token"))
        call.enqueue(object : Callback<Interviewers?> {
            override fun onResponse(
                call: Call<Interviewers?>,
                response: Response<Interviewers?>
            ) {
                if (response.body()?.status == true) {

                    /*  Status  */
                    var statusList = Array(response.body()!!.roundStatus!!.size + 1) { "" }
                    statusList[0] = "-- Select --"
                    var i = 0
                    for (data in response.body()!!.roundStatus!!) {
                        statusList[i + 1] = data?.name.toString()
                        i++
                    }


                    val adapter = ArrayAdapter<String>(
                        this@CandidateDetailActivity,
                        android.R.layout.simple_spinner_item,
                        statusList
                    )

                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    view.adapter = adapter

                    view.onItemSelectedListener =
                        object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(
                                parent: AdapterView<*>?,
                                view: View?,
                                position: Int,
                                id: Long
                            ) {
                                if (position != 0) {
                                    status =
                                        response.body()!!.roundStatus?.get(position - 1)?.slug.toString()
                                } else {
                                    status = ""
                                }
                            }

                            override fun onNothingSelected(parent: AdapterView<*>?) {
                                // Do nothing
                            }
                        }


                    /*  Status  */


                    /*  Rounds  */
                    var roundlist = Array(response.body()!!.roundTypes!!.size + 1) { "" }
                    roundlist[0] = "-- Select --"
                    var j = 0
                    for (data in response.body()!!.roundTypes!!) {
                        roundlist[j + 1] = data?.name.toString()
                        j++
                    }


                    val adapter1 = ArrayAdapter<String>(
                        this@CandidateDetailActivity,
                        android.R.layout.simple_spinner_item,
                        roundlist
                    )

                    adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    rounds.adapter = adapter1

                    rounds.onItemSelectedListener =
                        object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(
                                parent: AdapterView<*>?,
                                view: View?,
                                position: Int,
                                id: Long
                            ) {
                                if (position != 0) {
                                    roundsTxt =
                                        parent?.getItemAtPosition(position).toString()
                                } else {
                                    roundsTxt = ""
                                }
                            }

                            override fun onNothingSelected(parent: AdapterView<*>?) {
                                // Do nothing
                            }
                        }


                    /*  Status  */


                    /*  Status  */
                    var interviewList = Array(response.body()!!.interviewers!!.size + 1) { "" }
                    interviewList[0] = "-- Select --"
                    var k = 0
                    for (data in response.body()!!.interviewers!!) {
                        interviewList[k + 1] = data?.name.toString()
                        k++
                    }


                    val adapter2 = ArrayAdapter<String>(
                        this@CandidateDetailActivity,
                        android.R.layout.simple_spinner_item,
                        interviewList
                    )

                    adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    interviewers.adapter = adapter2

                    interviewers.onItemSelectedListener =
                        object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(
                                parent: AdapterView<*>?,
                                view: View?,
                                position: Int,
                                id: Long
                            ) {
                                if (position != 0) {
                                    interviewersTxt =
                                        response.body()!!.interviewers?.get(position - 1)?.id.toString()
                                } else {
                                    interviewersTxt = ""
                                }
                            }

                            override fun onNothingSelected(parent: AdapterView<*>?) {
                                // Do nothing
                            }
                        }


                    /*  Status  */
                    progressBar.visibility=View.GONE

                } else {
                    progressBar.visibility=View.GONE
                    Toast.makeText(
                        this@CandidateDetailActivity,
                        response.body()?.message,
                        Toast.LENGTH_SHORT
                    ).show()
                    // Toast.makeText( Dashboard.activity, "Sorry!! someone has already accepted the ride", Toast.LENGTH_SHORT ).show();
                }

            }

            override fun onFailure(call: Call<Interviewers?>, t: Throwable) {

                Toast.makeText(
                    this@CandidateDetailActivity,
                    "Something went wrong !",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

    }


    private fun openDatePickerDialog(date: TextView) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { view: DatePicker, selectedYear, selectedMonth, selectedDay ->
                // Set the selected date to the TextView
                val selectedDate = Calendar.getInstance()
                selectedDate.set(selectedYear, selectedMonth, selectedDay)

                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val formattedDate = dateFormat.format(selectedDate.time)

                date.text = formattedDate
            },
            year,
            month,
            dayOfMonth
        )

        // Show the DatePickerDialog
        datePickerDialog.show()
    }


    private fun addInterview(progressBar: ProgressBar,dialog: Dialog, date: String) {
        progressBar.visibility=View.VISIBLE
        val getData: GetData =
            RetrofitClient.getRetrofit().create(GetData::class.java)
        val call: Call<ResponseNormal> =
            getData.addInterviewRound(
                "Bearer " + sharedPreference.getData("token"),
                interviewersTxt,
                candidate.id.toString(),
                date,
                roundsTxt,
                status
            )
        call.enqueue(object : Callback<ResponseNormal?> {
            override fun onResponse(
                call: Call<ResponseNormal?>,
                response: Response<ResponseNormal?>
            ) {
                if (response.body()?.status == true) {
                    SnackBarUtils.showTopSnackbar(
                        this@CandidateDetailActivity,
                        response.body()?.message ?: "",
                        getColor(R.color.green)
                    )

                    getCandidate()
                    dialog.dismiss()
                } else {
                    SnackBarUtils.showTopSnackbar(
                        this@CandidateDetailActivity,
                        response.body()?.message ?: "",
                        getColor(R.color.red)
                    )
                }
                progressBar.visibility=View.GONE
            }

            override fun onFailure(call: Call<ResponseNormal?>, t: Throwable) {
                progressBar.visibility=View.GONE
                Toast.makeText(
                    applicationContext,
                    "Something went wrong !",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

    }

    fun addCommentDialog() {
        val dialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.add_comment_dialog, null)
        val submit = view.findViewById<Button>(R.id.submit)


        val comment = view.findViewById<EditText>(R.id.comment)
        fileName = view.findViewById<TextView>(R.id.fileName)
        val chooseFiles = view.findViewById<LinearLayout>(R.id.chooseFiles)
        val progressBar = view.findViewById<ProgressBar>(R.id.progressBar)


        chooseFiles.setOnClickListener {
            checkPermission()
        }
        submit.setOnClickListener {

            if (comment.text.toString() != "") {
                addComment(progressBar,dialog, comment.text.toString())

            } else {
                Toast.makeText(applicationContext, "Please write comment", Toast.LENGTH_SHORT).show()
            }


        }

        dialog.setCancelable(true)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));

        dialog.setContentView(view)

        dialog.show()
    }

    lateinit var  fileName: TextView
    private fun addComment(progressBar:ProgressBar,dialog:Dialog, comments: String) {
        progressBar.visibility=View.VISIBLE
        val token = sharedPreference.getData("token").toString()

        val comment =
            comments.toRequestBody("text/plain".toMediaTypeOrNull())


        val candidate_id =
            candidate.id.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        var  imagePart: MultipartBody.Part? =null
        if(selectedImageUri!=null) {
            val fileNam =
                FunctionClass.getFileNameFromUriWithoutPath(this@CandidateDetailActivity, selectedImageUri!!)
            val file = File(getRealPathFromUri(this@CandidateDetailActivity, selectedImageUri!!))
            val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
             imagePart =
                MultipartBody.Part.createFormData("attachments", fileNam ?: "", requestFile)
        }
        val getData: GetData = RetrofitClient.getRetrofit().create(GetData::class.java)
        val call: Call<ResponseNormal> = getData.addComment(
            "Bearer $token",
            comment,
            candidate_id,
            imagePart
        )

        call.enqueue(object : Callback<ResponseNormal> {
            override fun onResponse(
                call: Call<ResponseNormal>,
                response: Response<ResponseNormal>
            ) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    if (apiResponse?.status == true) {
                        SnackBarUtils.showTopSnackbar(
                            this@CandidateDetailActivity,
                            apiResponse?.message ?: "",
                            getColor(R.color.green)
                        )

                        selectedImageUri = null
                        fileName.text = ""
                        getCandidate()
                        dialog.dismiss()
                    } else {
                        SnackBarUtils.showTopSnackbar(
                            this@CandidateDetailActivity,
                            apiResponse?.message ?: "",
                            Color.RED
                        )
                    }
                } else {
                    val errorResponse = response.errorBody()?.string()
                    val gson = Gson()
                    try {
                        val errorJson = gson.fromJson(errorResponse, JsonObject::class.java)
                        val errorMessage = errorJson.get("message").asString
                        SnackBarUtils.showTopSnackbar(
                            this@CandidateDetailActivity,
                            errorMessage,
                            Color.RED
                        )
                    } catch (e: Exception) {
                        SnackBarUtils.showTopSnackbar(
                            this@CandidateDetailActivity,
                            e.message ?: "",
                            Color.RED
                        )
                    }
                }
                progressBar.visibility=View.GONE
            }

            override fun onFailure(call: Call<ResponseNormal>, t: Throwable) {
                progressBar.visibility=View.GONE
                SnackBarUtils.showTopSnackbar(this@CandidateDetailActivity, t.message ?: "", Color.RED)
            }
        })
    }


    fun checkPermission() {
        val permissionsToRequest = mutableListOf<String>()

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(Manifest.permission.READ_EXTERNAL_STORAGE)
                requestPermissions(
                    permissionsToRequest.toTypedArray(),
                    REQUEST_CODE_PERMISSION
                )
            } else {
                openGalleryForImages()
            }
        } else {
            if (checkSelfPermission(Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(Manifest.permission.READ_MEDIA_IMAGES)
                requestPermissions(
                    permissionsToRequest.toTypedArray(),
                    REQUEST_CODE_PERMISSION
                )
            } else {
                openGalleryForImages()
            }
        }
    }

    private fun openGalleryForImages() {
        val validImageTypes = arrayOf("image/jpeg", "image/jpg", "image/png")

        if (Build.VERSION.SDK_INT < 19) {
            // For earlier versions
            val intent = Intent()
            intent.type = "image/*"
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false)
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Choose Pictures"), REQUEST_CODE)
        } else {
            // For API LEVEL 19 and above
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false)
            intent.addCategory(Intent.CATEGORY_OPENABLE)

            // Set the MIME type filter
            intent.type = "image/*"
            intent.putExtra(Intent.EXTRA_MIME_TYPES, validImageTypes)

            startActivityForResult(intent, REQUEST_CODE)
        }

    }

    var selectedImageUri: Uri? = null
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE) {

            selectedImageUri = data!!.data!!

                fileName.text = FunctionClass.getFileNameFromUri(selectedImageUri!!,applicationContext)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSION && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Permission granted, proceed with accessing images
            openGalleryForImages()
        } else {
            // Permission denied, handle accordingly (e.g., show a message to the user)
        }
    }


}