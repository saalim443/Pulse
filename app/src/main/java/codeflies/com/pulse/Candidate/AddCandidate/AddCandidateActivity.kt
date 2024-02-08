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
import android.view.View
import android.widget.AdapterView
import androidx.documentfile.provider.DocumentFile
import codeflies.com.pulse.Helpers.ProgressDisplay
import codeflies.com.pulse.Helpers.RetrofitClient
import codeflies.com.pulse.Helpers.SharedPreference
import codeflies.com.pulse.Helpers.SnackBarUtils
import codeflies.com.pulse.Helpers.getRealPathFromUri
import codeflies.com.pulse.Models.ResponseNormal

import codeflies.com.pulse.databinding.ActivityAddCandidateBinding
import com.example.ehcf_doctor.Retrofit.GetData
import com.google.gson.Gson
import com.google.gson.JsonObject
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class AddCandidateActivity : AppCompatActivity() {
    var context : Context =this@AddCandidateActivity
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
    private var imageUriList = mutableListOf<Uri>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityAddCandidateBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPreference = SharedPreference(this)
        progressDisplay = ProgressDisplay(this)




        binding.spnDesignation.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedItemDesignation = parent?.getItemAtPosition(position).toString()
                // Do something with the selected item
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle case when nothing is selected
            }
        }

        binding.spnYear.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedItemyyyy = parent?.getItemAtPosition(position).toString()
                // Do something with the selected item
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle case when nothing is selected
            }
        }

        binding.spnMonth.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedItemmm  = parent?.getItemAtPosition(position).toString()
                // Do something with the selected item
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle case when nothing is selected
            }
        }

        binding.spnStatus.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedItemstatus = parent?.getItemAtPosition(position).toString()
                // Do something with the selected item
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle case when nothing is selected
            }
        }

        binding.spnRecruiter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedItemrecruiter = parent?.getItemAtPosition(position).toString()
                // Do something with the selected item
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle case when nothing is selected
            }
        }
//
//        binding.chooseFiles.setOnClickListener {
//            if (checkSelfPermission(Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED) {
//                // Permission already granted, proceed with accessing images
//                openGalleryForImages()
//            } else {
//                // Request permission
//                requestPermissions(
//                    arrayOf(Manifest.permission.READ_MEDIA_IMAGES),
//                    REQUEST_CODE_PERMISSION
//                )
//            }
//        }

        binding.chooseFiles.setOnClickListener {
            val permissionsToRequest = mutableListOf<String>()

            // Check if permission to read media images is granted
            if (checkSelfPermission(Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(Manifest.permission.READ_MEDIA_IMAGES)
            }

            // Check if permission to read external storage is granted
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(Manifest.permission.READ_EXTERNAL_STORAGE)
            }

            // Request permissions if any are missing
            if (permissionsToRequest.isNotEmpty()) {
                requestPermissions(
                    permissionsToRequest.toTypedArray(),
                    REQUEST_CODE_PERMISSION
                )
            } else {
                // Both permissions are granted, proceed with accessing files
                openGalleryForImages()
            }
        }

        binding.login.setOnClickListener {

            uploadLeave()
        }

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE) {

            // if multiple images are selected
            if (data?.clipData != null) {
                val count = data.clipData!!.itemCount

                for (i in 0 until count) {
                    val imageUri: Uri = data.clipData!!.getItemAt(i).uri
                    //     val filePath = getPathFromUri(imageUri)
                    // Log.e("filePath","filePath = "+filePath)
                    // Now 'filePath' contains the file path corresponding to the Uri
                    imageUriList.add(imageUri)

                    // iv_image.setImageURI(imageUri) Here you can assign your Image URI to the ImageViews
                }

                binding.filesCount.text = imageUriList.size.toString() + " Files"

            } else if (data?.data != null) {
                // if a single image is selected

                val imageUri: Uri = data.data!!
                // val filePath = getPathFromUri(imageUri)
                // Now 'filePath' contains the file path corresponding to the Uri
                imageUriList.add(imageUri)

                binding.filesCount.text = "1 File"
                // iv_image.setImageURI(imageUri) Here you can assign the picked image uri to your imageview
            }
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

    fun gotoBack(view: View) {
        onBackPressed()

    }


    private fun uploadLeave() {
        progressDisplay.show()
        val token = sharedPreference.getData("token").toString()
        val name = RequestBody.create("text/plain".toMediaTypeOrNull(), binding.edtName.text.toString())
        val email = RequestBody.create("text/plain".toMediaTypeOrNull(), binding.edtEmail.text.toString())
        val mobile = RequestBody.create("text/plain".toMediaTypeOrNull(), binding.edtMobile.text.toString())
        val alternateMobile = RequestBody.create("text/plain".toMediaTypeOrNull(), binding.edtAlternateMobile.text.toString())
        val currentsalary = RequestBody.create("text/plain".toMediaTypeOrNull(), binding.edtCurrentSalary.text.toString())
        val expectedSalary = RequestBody.create("text/plain".toMediaTypeOrNull(), binding.edtExpectedSalary.text.toString())
        val noticePeriod = RequestBody.create("text/plain".toMediaTypeOrNull(), binding.edtNoticePeriod.text.toString())
        val year = RequestBody.create("text/plain".toMediaTypeOrNull(),selectedItemyyyy)
        val month = RequestBody.create("text/plain".toMediaTypeOrNull(),selectedItemmm)
        val status = RequestBody.create("text/plain".toMediaTypeOrNull(),selectedItemstatus)
        val designation = RequestBody.create("text/plain".toMediaTypeOrNull(), selectedItemDesignation)
        val recruiter = RequestBody.create("text/plain".toMediaTypeOrNull(), selectedItemrecruiter)

        // Create a list of MultipartBody.Part for images
        val imageParts = mutableListOf<MultipartBody.Part>()
        for (uri in imageUriList) {
            val fileName = getFileNameFromUriWithoutPath(uri)
            val file = File(getRealPathFromUri(this@AddCandidateActivity, uri))
            val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), file)
            val imagePart = MultipartBody.Part.createFormData("attachments[]", fileName ?: "", requestFile)
            imageParts.add(imagePart)
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
            recruiter,
            imageParts,

        )
        call.enqueue(object : Callback<ResponseNormal> {
            override fun onResponse(call: Call<ResponseNormal>, response: Response<ResponseNormal>) {
                progressDisplay.dismiss()
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    if (apiResponse?.status == true) {
                        finish()
                    } else {
                        SnackBarUtils.showTopSnackbar(
                            this@AddCandidateActivity,
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
                            this@AddCandidateActivity,
                            errorMessage,
                            Color.RED
                        )
                    } catch (e: Exception) {
                        SnackBarUtils.showTopSnackbar(
                            this@AddCandidateActivity,
                            e.message ?: "",
                            Color.RED
                        )
                    }
                }
            }

            override fun onFailure(call: Call<ResponseNormal>, t: Throwable) {
                progressDisplay.dismiss()
                SnackBarUtils.showTopSnackbar(this@AddCandidateActivity, t.message ?: "", Color.RED)
            }
        })
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
            val cursor = this@AddCandidateActivity.contentResolver.query(uri, null, null, null, null)

            cursor?.use {
                if (it.moveToFirst()) {
                    val displayName = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    fileName = it.getString(displayName)
                }
            }
        }

        return fileName
    }
}