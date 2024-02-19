package codeflies.com.pulse.Candidate.AddCandidate

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.DocumentsContract
import android.provider.OpenableColumns
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.documentfile.provider.DocumentFile
import androidx.recyclerview.widget.RecyclerView
import codeflies.com.pulse.Helpers.Constants
import codeflies.com.pulse.Helpers.FunctionClass
import codeflies.com.pulse.Helpers.ProgressDisplay
import codeflies.com.pulse.Helpers.RetrofitClient
import codeflies.com.pulse.Helpers.SharedPreference
import codeflies.com.pulse.Helpers.SnackBarUtils
import codeflies.com.pulse.Helpers.getRealPathFromUri

import codeflies.com.pulse.Models.ResponseNormal
import codeflies.com.pulse.R

import codeflies.com.pulse.databinding.ActivityAddCandidateBinding
import codeflies.com.pulse.Helpers.Interfaces.GetData
import codeflies.com.pulse.Home.Fragments.Candidates.Candidates
import codeflies.com.pulse.Models.Candidates.ResponseRecruiters
import codeflies.com.pulse.Models.Leaves.LeaveStatusDetails
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class AddCandidateActivity : AppCompatActivity() {
    var context: Context = this@AddCandidateActivity
    lateinit var binding: ActivityAddCandidateBinding
    lateinit var sharedPreference: SharedPreference
    lateinit var progressDisplay: ProgressDisplay
    lateinit var selectedItemDesignation: String
    lateinit var selectedItemyyyy: String
    lateinit var selectedItemmm: String
    lateinit var selectedItemrecruiter: String
    lateinit var selectedItemstatus: String

    private val REQUEST_CODE_PERMISSION = 123
    val REQUEST_CODE = 200


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddCandidateBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPreference = SharedPreference(this)
        progressDisplay = ProgressDisplay(this)


        setSpinner()






        binding.chooseFiles.setOnClickListener {
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

        binding.login.setOnClickListener {


            if (isValid()) {
                uploadLeave()
            }


        }

    }

    private fun getFileNameFromUriWithoutPath(uri: Uri): String? {
        val fullFileName = getFileNameFromUri(uri)
        // Use substringAfterLast to get the file name without the path
        return fullFileName?.substringAfterLast('/')
    }

    private fun getFileNameFromUri(uri: Uri): String? {
        var fileName: String? = null

        if (DocumentsContract.isDocumentUri(this@AddCandidateActivity, uri)) {
            // Handle document URI
            val document = DocumentFile.fromSingleUri(this@AddCandidateActivity, uri)
            fileName = document?.name
        } else {
            // Handle other URI types
            val cursor =
                this@AddCandidateActivity.contentResolver.query(uri, null, null, null, null)

            cursor?.use {
                if (it.moveToFirst()) {
                    val displayName = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    fileName = it.getString(displayName)
                }
            }
        }

        return fileName
    }

    private fun openGalleryForImages() {
        val validImageTypes = arrayOf("image/jpeg", "image/jpg", "image/png")


        if (Build.VERSION.SDK_INT < 19) {
            // For earlier versions
            val intent = Intent()
            intent.type = "image/*"
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Choose Pictures"), REQUEST_CODE)
        } else {
            // For API LEVEL 19 and above
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
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

            binding.filesCount.text =
                FunctionClass.getFileNameFromUri(selectedImageUri!!, this@AddCandidateActivity)

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


    private fun isValid(): Boolean {


        if (binding.edtName.text.isEmpty()) {
            SnackBarUtils.showTopSnackbar(
                this@AddCandidateActivity,
                "Name is required!",
                getColor(R.color.red)
            )
            binding.edtName.requestFocus()
            return false
        } else if (binding.edtEmail.text.isEmpty()) {
            SnackBarUtils.showTopSnackbar(
                this@AddCandidateActivity,
                "Email is required!",
                getColor(R.color.red)
            )
            binding.edtEmail.requestFocus()
            return false
        } else if (binding.edtMobile.text.isEmpty()) {
            SnackBarUtils.showTopSnackbar(
                this@AddCandidateActivity,
                "Mobile number is required!",
                getColor(R.color.red)
            )
            binding.edtMobile.requestFocus()
            return false
        } else if (binding.edtAlternateMobile.text.isEmpty()) {
            SnackBarUtils.showTopSnackbar(
                this@AddCandidateActivity,
                "Alternate mobile number is required!",
                getColor(R.color.red)
            )
            binding.edtAlternateMobile.requestFocus()
            return false
        }else if (selectedItemDesignation.equals("-- Select --")) {
            SnackBarUtils.showTopSnackbar(
                this@AddCandidateActivity,
                "Designation is required!",
                getColor(R.color.red)
            )
            binding.edtNoticePeriod.requestFocus()
            return false
        }else if (selectedItemyyyy.equals("YYYY")) {
            SnackBarUtils.showTopSnackbar(
                this@AddCandidateActivity,
                "Experience Year is required!",
                getColor(R.color.red)
            )
            binding.edtNoticePeriod.requestFocus()
            return false
        }else if (selectedItemmm.equals("MM")) {
            SnackBarUtils.showTopSnackbar(
                this@AddCandidateActivity,
                "Experience Month is required!",
                getColor(R.color.red)
            )
            binding.edtNoticePeriod.requestFocus()
            return false
        } else if (binding.edtNoticePeriod.text.isEmpty()) {
            SnackBarUtils.showTopSnackbar(
                this@AddCandidateActivity,
                "Notice period is required!",
                getColor(R.color.red)
            )
            binding.edtNoticePeriod.requestFocus()
            return false
        } else if (binding.edtCurrentSalary.text.isEmpty()) {
            SnackBarUtils.showTopSnackbar(
                this@AddCandidateActivity,
                "Current salary is required!",
                getColor(R.color.red)
            )
            binding.edtCurrentSalary.requestFocus()
            return false
        } else if (binding.edtExpectedSalary.text.isEmpty()) {
            SnackBarUtils.showTopSnackbar(
                this@AddCandidateActivity,
                "Expected salary is required!",
                getColor(R.color.red)
            )
            binding.edtExpectedSalary.requestFocus()
            return false
        }else if ("selectedFileUri" == null) {
            SnackBarUtils.showTopSnackbar(
                this@AddCandidateActivity,
                "Please choose Attachedment !",
                getColor(R.color.red)
            )
            return false
        } else if (selectedItemstatus.equals("-- Select --")) {
            SnackBarUtils.showTopSnackbar(
                this@AddCandidateActivity,
                "Status is required!",
                getColor(R.color.red)
            )
            binding.edtNoticePeriod.requestFocus()
            return false
        }else if (binding.remarkWriteYourContent.text.isEmpty()) {
            SnackBarUtils.showTopSnackbar(
                this@AddCandidateActivity,
                "Content is required!",
                getColor(R.color.red)
            )
            return false
        }

        return true
    }


    fun gotoBack(view: View) {
        onBackPressed()

    }


    private fun uploadLeave() {
        progressDisplay.show()
        val token = sharedPreference.getData("token").toString()
        val name =
            RequestBody.create("text/plain".toMediaTypeOrNull(), binding.edtName.text.toString())
        val email =
            RequestBody.create("text/plain".toMediaTypeOrNull(), binding.edtEmail.text.toString())
        val mobile =
            RequestBody.create("text/plain".toMediaTypeOrNull(), binding.edtMobile.text.toString())
        val alternateMobile = RequestBody.create(
            "text/plain".toMediaTypeOrNull(),
            binding.edtAlternateMobile.text.toString()
        )
        val currentsalary = RequestBody.create(
            "text/plain".toMediaTypeOrNull(),
            binding.edtCurrentSalary.text.toString()
        )
        val expectedSalary = RequestBody.create(
            "text/plain".toMediaTypeOrNull(),
            binding.edtExpectedSalary.text.toString()
        )
        val remarks = RequestBody.create(
            "text/plain".toMediaTypeOrNull(),
            binding.remarkWriteYourContent.text.toString()
        )
        val noticePeriod = RequestBody.create(
            "text/plain".toMediaTypeOrNull(),
            binding.edtNoticePeriod.text.toString()
        )
        val year = RequestBody.create("text/plain".toMediaTypeOrNull(), selectedItemyyyy)
        val month = RequestBody.create("text/plain".toMediaTypeOrNull(), selectedItemmm)
        val status = RequestBody.create("text/plain".toMediaTypeOrNull(), selectedItemstatus)
        val designation =
            RequestBody.create("text/plain".toMediaTypeOrNull(), selectedItemDesignation)
        val recruiter = RequestBody.create("text/plain".toMediaTypeOrNull(), "13")



        var imagePart: MultipartBody.Part? = null
        if (selectedImageUri != null) {
            val fileName =
                FunctionClass.getFileNameFromUriWithoutPath(this@AddCandidateActivity, selectedImageUri!!)
            val file = File(getRealPathFromUri(this@AddCandidateActivity, selectedImageUri!!))
            val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
            imagePart =
                MultipartBody.Part.createFormData("resume", fileName ?: "", requestFile)
        }


        // Make the API call
        val getData: GetData = RetrofitClient.getRetrofit().create(GetData::class.java)
        val call: Call<ResponseNormal> = getData.uploadCandidate(
            "Bearer $token",
            name,
            email,
            mobile,
            alternateMobile,
            designation,
            year,
            month,
            noticePeriod,
            expectedSalary,
            currentsalary,
            status,
            remarks,
            recruiter,
            imagePart

        )
        call.enqueue(object : Callback<ResponseNormal> {
            override fun onResponse(
                call: Call<ResponseNormal>,
                response: Response<ResponseNormal>
            ) {
                progressDisplay.dismiss()
                try {
                    if (response.isSuccessful) {
                        if (response.body()!!.status == true) {

                            Log.i(
                                "errorImageResumeOnfailiour",
                                response.body()!!.message.toString()
                            )
                            SnackBarUtils.showTopSnackbar(
                                this@AddCandidateActivity,
                                response.body()!!.message ?: "",
                                getColor(R.color.green)
                            )
                            Candidates.refresh.onRefresh()
                            finish()
                        } else {
                            SnackBarUtils.showTopSnackbar(
                                this@AddCandidateActivity,
                                response.body()!!.message ?: "",
                                Color.RED
                            )

                        }
                    } else {
                        Toast.makeText(
                            this@AddCandidateActivity,
                            "Something went wrong !",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(
                        this@AddCandidateActivity,
                        "Something went wrong !",
                        Toast.LENGTH_SHORT
                    ).show()
                }


            }

            override fun onFailure(call: Call<ResponseNormal>, t: Throwable) {
                progressDisplay.dismiss()
                SnackBarUtils.showTopSnackbar(this@AddCandidateActivity, t.message ?: "", Color.RED)
                Log.i("errorImageResumeOnfailiour", t.message.toString())

            }
        })
    }


    fun setSpinner() {
        val adDesignation = ArrayAdapter<String>(
            this@AddCandidateActivity,
            android.R.layout.simple_spinner_item,
            Constants.designation
        )
        adDesignation.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spnDesignation.adapter = adDesignation

        binding.spnDesignation.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {

                    selectedItemDesignation = parent?.getItemAtPosition(position).toString()

                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // Do nothing
                }
            }


        val adYear = ArrayAdapter<String>(
            this@AddCandidateActivity,
            android.R.layout.simple_spinner_item,
            Constants.year
        )
        adYear.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spnYear.adapter = adYear

        binding.spnYear.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {

                    selectedItemyyyy = parent?.getItemAtPosition(position).toString()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // Do nothing
                }
            }

        val adMonth = ArrayAdapter<String>(
            this@AddCandidateActivity,
            android.R.layout.simple_spinner_item,
            Constants.month
        )
        adMonth.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spnMonth.adapter = adMonth

        binding.spnMonth.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {

                    selectedItemmm = parent?.getItemAtPosition(position).toString()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // Do nothing
                }
            }

        val adStatus = ArrayAdapter<String>(
            this@AddCandidateActivity,
            android.R.layout.simple_spinner_item,
            Constants.status
        )
        adStatus.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spnStatus.adapter = adStatus

        binding.spnStatus.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {

                    selectedItemstatus = parent?.getItemAtPosition(position).toString()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // Do nothing
                }
            }

        getRecruiters()
    }


    fun getRecruiters() {

        Log.e("token", "Bearer " + sharedPreference.getData("token"))
        val getData: GetData =
            RetrofitClient.getRetrofit().create(GetData::class.java)
        val call: Call<ResponseRecruiters> =
            getData.getRecruiters("Bearer " + sharedPreference.getData("token"))
        call.enqueue(object : Callback<ResponseRecruiters?> {
            override fun onResponse(
                call: Call<ResponseRecruiters?>,
                response: Response<ResponseRecruiters?>
            ) {
                if (response.isSuccessful) {
                    if (response.body()?.status == true) {
                        val notifyToList: List<String>? =
                            response.body()?.recruiters?.mapNotNull { it?.name }

                        if (!notifyToList.isNullOrEmpty()) {
                            val adStatus = ArrayAdapter<String>(
                                this@AddCandidateActivity,
                                android.R.layout.simple_spinner_item,
                                notifyToList
                            )
                            adStatus.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                            binding.spnRecruiter.adapter = adStatus

                            binding.spnRecruiter.onItemSelectedListener =
                                object : AdapterView.OnItemSelectedListener {
                                    override fun onItemSelected(
                                        parent: AdapterView<*>?,
                                        view: View?,
                                        position: Int,
                                        id: Long
                                    ) {

                                        selectedItemrecruiter =
                                            parent?.getItemAtPosition(position).toString()
                                    }

                                    override fun onNothingSelected(parent: AdapterView<*>?) {
                                        // Do nothing
                                    }
                                }
                        }

                    } else {
                        Toast.makeText(
                            this@AddCandidateActivity,
                            response.body()?.message,
                            Toast.LENGTH_SHORT
                        ).show()
                        // Toast.makeText( Dashboard.activity, "Sorry!! someone has already accepted the ride", Toast.LENGTH_SHORT ).show();
                    }
                } else {
                    Toast.makeText(
                        this@AddCandidateActivity,
                        "Something went wrong !",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                progressDisplay.dismiss()
            }

            override fun onFailure(call: Call<ResponseRecruiters?>, t: Throwable) {
                progressDisplay.dismiss()
                Toast.makeText(
                    this@AddCandidateActivity,
                    "Something went wrong !",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

    }

}