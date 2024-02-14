package codeflies.com.pulse.Profiles.EditProfile

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.PorterDuff
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.DocumentsContract
import android.provider.OpenableColumns
import android.text.InputType
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.documentfile.provider.DocumentFile
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import codeflies.com.pulse.Helpers.Constants
import codeflies.com.pulse.Helpers.FunctionClass
import codeflies.com.pulse.Helpers.Interfaces.GetData
import codeflies.com.pulse.Helpers.ProgressDisplay
import codeflies.com.pulse.Helpers.RetrofitClient
import codeflies.com.pulse.Helpers.SharedPreference
import codeflies.com.pulse.Helpers.SnackBarUtils
import codeflies.com.pulse.Helpers.getRealPathFromUri
import codeflies.com.pulse.Home.Fragments.Attendence
import codeflies.com.pulse.Home.Fragments.Candidates.Candidates
import codeflies.com.pulse.Home.Fragments.Holiday.Holidays
import codeflies.com.pulse.Home.Fragments.Leaves.Home
import codeflies.com.pulse.Models.Profile.ResponseDocuments
import codeflies.com.pulse.Models.ResponseNormal
import codeflies.com.pulse.Models.UserData.ResponseProfile
import codeflies.com.pulse.R
import codeflies.com.pulse.Settings.WebviewActivity
import codeflies.com.pulse.databinding.ActivityEditProfileBinding
import com.bumptech.glide.Glide
import com.codeflies.supertravel.TabsLayou.TabLayoutFragment.UpComingRides.DocumentListAdapter
import com.codeflies.supertravel.TabsLayou.TabLayoutFragment.UpComingRides.ManagersListAdapter
import com.google.gson.Gson
import com.google.gson.JsonObject
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class EditProfile : AppCompatActivity() {

    lateinit var binding: ActivityEditProfileBinding
    val REQUEST_CODE = 200
    private val REQUEST_CODE_PERMISSION = 123

    lateinit var progressDisplay: ProgressDisplay
    lateinit var sharedPreference: SharedPreference


    var imageSelection = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)


        progressDisplay = ProgressDisplay(this)
        sharedPreference = SharedPreference(this)


        binding.details.setOnClickListener {
            changePage(1)
        }

        binding.document.setOnClickListener {
            changePage(2)
        }

        binding.password.setOnClickListener {
            changePage(3)
        }

        binding.back.setOnClickListener {
            finish()
        }
        binding.help.setOnClickListener {
            WebviewActivity.pageTitle = "Leave Management"
            startActivity(Intent(this@EditProfile, WebviewActivity::class.java))
        }


        binding.image.setOnClickListener {
            imageSelection = 1
            checkPermission()
        }

        binding.uploadFile.setOnClickListener {
            imageSelection = 2
            checkPermission()
        }


        binding.detailSubmit.setOnClickListener {
            uploadDetails()
        }

        binding.documentSubmit.setOnClickListener {
            if (doctype != "") {
                if (binding.dTitle.text.toString() != "") {
                    if (selectedImageUri != null) {
                        uploadDocuments()
                    } else {
                        Toast.makeText(
                            applicationContext,
                            "Please select document",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        applicationContext,
                        "Please select document title",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Toast.makeText(
                    applicationContext,
                    "Please select document type",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }


        binding.passwordSubmit.setOnClickListener {
            if (binding.oldPass.text.toString() != "") {
                if (binding.newPass.text.toString() != "") {
                    if (binding.newPass.text.toString().length>5) {
                        if (binding.newPass.text.toString() == binding.confirmPass.text.toString()) {
                            changePassword()
                        } else {
                            Toast.makeText(
                                applicationContext,
                                "Password did not match",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        Toast.makeText(
                            applicationContext,
                            "Password should be minimum 6 digit long",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        applicationContext,
                        "Please create new password",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Toast.makeText(
                    applicationContext,
                    "Please enter your old password",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }



        profile()

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

    fun changePage(i: Int) {

        if (i == 1) {

            binding.details.backgroundTintList = getColorStateList(R.color.pulse_color)
            binding.document.backgroundTintList = getColorStateList(R.color.black)
            binding.password.backgroundTintList = getColorStateList(R.color.black)

            binding.lyDetails.visibility = View.VISIBLE
            binding.lyDocument.visibility = View.GONE
            binding.lyPassword.visibility = View.GONE


            profile()
        } else if (i == 2) {
            binding.details.backgroundTintList = getColorStateList(R.color.black)
            binding.document.backgroundTintList = getColorStateList(R.color.pulse_color)
            binding.password.backgroundTintList = getColorStateList(R.color.black)
            binding.lyDetails.visibility = View.GONE
            binding.lyDocument.visibility = View.VISIBLE
            binding.lyPassword.visibility = View.GONE
            getDocuments()
        } else if (i == 3) {
            binding.details.backgroundTintList = getColorStateList(R.color.black)
            binding.document.backgroundTintList = getColorStateList(R.color.black)
            binding.password.backgroundTintList = getColorStateList(R.color.pulse_color)
            binding.lyDetails.visibility = View.GONE
            binding.lyDocument.visibility = View.GONE
            binding.lyPassword.visibility = View.VISIBLE

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
            if (imageSelection == 1) {
                binding.image.setImageURI(selectedImageUri)
            }

            if (imageSelection == 2) {
                binding.txtFileName.text = getFileNameFromUri(selectedImageUri!!)
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

    private fun profile() {
        progressDisplay.show()
        val getData: GetData =
            RetrofitClient.getRetrofit().create(GetData::class.java)
        val call: Call<ResponseProfile> =
            getData.profile(
                "Bearer " + sharedPreference.getData("token")
            )
        call.enqueue(object : Callback<ResponseProfile?> {
            override fun onResponse(
                call: Call<ResponseProfile?>,
                response: Response<ResponseProfile?>
            ) {
                if (response.body()?.status == true) {

                    binding.eCode.text = response.body()!!.user?.employee?.employeeCode
                    binding.name.setText(response.body()!!.user?.name)
                    if(response.body()!!.user?.name!="") {
                        binding.name.isEnabled = false
                    }

                    binding.mobile.setText(response.body()!!.user?.mobile)
                    if(response.body()!!.user?.mobile!="") {
                        binding.mobile.isEnabled = false
                    }

                    binding.aMobile.setText(response.body()!!.user?.employee?.alternateMobile)
                    if(response.body()!!.user?.employee?.alternateMobile!="") {
                        binding.aMobile.isEnabled = false
                    }

                    binding.email.setText( response.body()!!.user?.email)
                    if(response.body()!!.user?.email!="") {
                        binding.email.isEnabled = false
                    }

                    binding.jDate.setText(response.body()!!.user?.employee?.joiningDate)
                    if(response.body()!!.user?.employee?.joiningDate!="") {
                        binding.jDate.isEnabled = false
                    }

                    binding.pAddress.setText(response.body()!!.user?.employee?.permanentAddr)
                    if(response.body()!!.user?.employee?.permanentAddr!="") {
                        binding.pAddress.isEnabled = false
                    }

                    binding.tAddress.setText(response.body()!!.user?.employee?.temporaryAddr.toString())
                    if(response.body()!!.user?.employee?.temporaryAddr!="") {
                        binding.tAddress.isEnabled = false
                    }

                    binding.fName.setText(response.body()!!.user?.employee?.fatherName)
                    if(response.body()!!.user?.employee?.fatherName!="") {
                        binding.fName.isEnabled = false
                    }

                    binding.fOcupation.setText(response.body()!!.user?.employee?.fatherProfession.toString())
                    if(response.body()!!.user?.employee?.fatherProfession!="") {
                        binding.fOcupation.isEnabled = false
                    }


                    binding.mName.setText(response.body()!!.user?.employee?.motherName)
                    if(response.body()!!.user?.employee?.motherName!="") {
                        binding.mName.isEnabled = false
                    }


                    binding.mOcupation.setText(response.body()!!.user?.employee?.motherProfession.toString())
                    if(response.body()!!.user?.employee?.motherProfession!="") {
                        binding.mOcupation.isEnabled = false
                    }

                    binding.designation.setText(FunctionClass.getRole(response.body()!!.user?.primaryRole))
                    if(response.body()!!.user?.primaryRole!="") {
                        binding.designation.isEnabled = false
                    }

                    binding.bDate.setText(FunctionClass.changeDate(response.body()!!.user?.employee?.dateOfBirth))
                    if(response.body()!!.user?.employee?.dateOfBirth!="") {
                        binding.bDate.isEnabled = false
                    }
//
                    Glide.with(applicationContext)
                        .load(Constants.IMG_URL + response.body()?.user?.profileImg)
                        .placeholder(R.drawable.person).into(binding.image)

                } else {
                    Toast.makeText(
                        applicationContext,
                        response.body()?.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                    // Toast.makeText( Dashboard.activity, "Sorry!! someone has already accepted the ride", Toast.LENGTH_SHORT ).show();
                }
                progressDisplay.dismiss()
            }

            override fun onFailure(call: Call<ResponseProfile?>, t: Throwable) {
                progressDisplay.dismiss()
                Toast.makeText(
                    applicationContext,
                    "Something went wrong !",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

    }


    private fun uploadDetails() {
        progressDisplay.show()
        val token = sharedPreference.getData("token").toString()

        val amobile =
            binding.aMobile.text.toString().toRequestBody("text/plain".toMediaTypeOrNull())


        val fprofession =
            binding.fOcupation.text.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val dob =
            binding.bDate.text.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val mname =
            binding.mName.text.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val mprofession = binding.mOcupation.text.toString()
            .toRequestBody("text/plain".toMediaTypeOrNull())

        val taddress = binding.tAddress.text.toString()
            .toRequestBody("text/plain".toMediaTypeOrNull())

        val fileName = getFileNameFromUriWithoutPath(selectedImageUri!!)
        val file = File(getRealPathFromUri(this@EditProfile, selectedImageUri!!))
        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
        val imagePart =
            MultipartBody.Part.createFormData("profile_img", fileName ?: "", requestFile)


        val getData: GetData = RetrofitClient.getRetrofit().create(GetData::class.java)
        val call: Call<ResponseNormal> = getData.uploadDetails(
            "Bearer $token",
            amobile,
            fprofession,
            dob,
            mname,
            mprofession,
            taddress,
            imagePart
        )

        call.enqueue(object : Callback<ResponseNormal> {
            override fun onResponse(
                call: Call<ResponseNormal>,
                response: Response<ResponseNormal>
            ) {
                progressDisplay.dismiss()
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    if (apiResponse?.status == true) {
                        SnackBarUtils.showTopSnackbar(
                            this@EditProfile,
                            apiResponse?.message ?: "",
                            getColor(R.color.green)
                        )

                        selectedImageUri = null
                    } else {
                        SnackBarUtils.showTopSnackbar(
                            this@EditProfile,
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
                            this@EditProfile,
                            errorMessage,
                            Color.RED
                        )
                    } catch (e: Exception) {
                        SnackBarUtils.showTopSnackbar(
                            this@EditProfile,
                            e.message ?: "",
                            Color.RED
                        )
                    }
                }
            }

            override fun onFailure(call: Call<ResponseNormal>, t: Throwable) {
                progressDisplay.dismiss()
                SnackBarUtils.showTopSnackbar(this@EditProfile, t.message ?: "", Color.RED)
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

        if (DocumentsContract.isDocumentUri(this@EditProfile, uri)) {
            // Handle document URI
            val document = DocumentFile.fromSingleUri(this@EditProfile, uri)
            fileName = document?.name
        } else {
            // Handle other URI types
            val cursor = this@EditProfile.contentResolver.query(uri, null, null, null, null)

            cursor?.use {
                if (it.moveToFirst()) {
                    val displayName = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    fileName = it.getString(displayName)
                }
            }
        }

        return fileName
    }

    lateinit var doctype: String
    private fun getDocuments() {
        progressDisplay.show()
        val getData: GetData =
            RetrofitClient.getRetrofit().create(GetData::class.java)
        val call: Call<ResponseDocuments> =
            getData.documents(
                "Bearer " + sharedPreference.getData("token")
            )
        call.enqueue(object : Callback<ResponseDocuments?> {
            override fun onResponse(
                call: Call<ResponseDocuments?>,
                response: Response<ResponseDocuments?>
            ) {
                if (response.body()?.status == true) {
                    var typeList = Array(response.body()!!.docTypes!!.size + 1) { "" }
                    typeList[0] = "-- Select --"
                    var i = 0
                    for (data in response.body()!!.docTypes!!) {
                        typeList[i + 1] = data?.title.toString()
                        i++
                    }


                    val adapter = ArrayAdapter<String>(
                        this@EditProfile,
                        android.R.layout.simple_spinner_item,
                        typeList
                    )

                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    binding.docType.adapter = adapter

                    binding.docType.onItemSelectedListener =
                        object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(
                                parent: AdapterView<*>?,
                                view: View?,
                                position: Int,
                                id: Long
                            ) {
                                if (position != 0) {
                                    doctype =
                                        parent?.getItemAtPosition(position).toString()
                                } else {
                                    doctype = ""
                                }
                            }

                            override fun onNothingSelected(parent: AdapterView<*>?) {
                                // Do nothing
                            }
                        }



                    binding.documents.layoutManager = LinearLayoutManager(applicationContext)
                    binding.documents.setHasFixedSize(true)
                    binding.documents.adapter =
                        DocumentListAdapter(applicationContext, response.body()!!.documents)


                } else {
                    Toast.makeText(
                        applicationContext,
                        response.body()?.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                progressDisplay.dismiss()
            }

            override fun onFailure(call: Call<ResponseDocuments?>, t: Throwable) {
                progressDisplay.dismiss()
                Toast.makeText(
                    applicationContext,
                    "Something went wrong !",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

    }


    private fun uploadDocuments() {
        progressDisplay.show()
        val token = sharedPreference.getData("token").toString()

        val dTitle =
            binding.dTitle.text.toString().toRequestBody("text/plain".toMediaTypeOrNull())


        val remark =
            binding.remark.text.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val type =
            doctype.toRequestBody("text/plain".toMediaTypeOrNull())


        val fileName = getFileNameFromUriWithoutPath(selectedImageUri!!)
        val file = File(getRealPathFromUri(this@EditProfile, selectedImageUri!!))
        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
        val imagePart =
            MultipartBody.Part.createFormData("upload_file", fileName ?: "", requestFile)


        val getData: GetData = RetrofitClient.getRetrofit().create(GetData::class.java)
        val call: Call<ResponseNormal> = getData.uploadDocuments(
            "Bearer $token",
            type,
            dTitle,
            remark,
            imagePart
        )

        call.enqueue(object : Callback<ResponseNormal> {
            override fun onResponse(
                call: Call<ResponseNormal>,
                response: Response<ResponseNormal>
            ) {
                progressDisplay.dismiss()
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    if (apiResponse?.status == true) {
                        SnackBarUtils.showTopSnackbar(
                            this@EditProfile,
                            apiResponse?.message ?: "",
                            getColor(R.color.green)
                        )

                        selectedImageUri = null
                        binding.txtFileName.text = ""
                        getDocuments()
                    } else {
                        SnackBarUtils.showTopSnackbar(
                            this@EditProfile,
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
                            this@EditProfile,
                            errorMessage,
                            Color.RED
                        )
                    } catch (e: Exception) {
                        SnackBarUtils.showTopSnackbar(
                            this@EditProfile,
                            e.message ?: "",
                            Color.RED
                        )
                    }
                }
            }

            override fun onFailure(call: Call<ResponseNormal>, t: Throwable) {
                progressDisplay.dismiss()
                SnackBarUtils.showTopSnackbar(this@EditProfile, t.message ?: "", Color.RED)
            }
        })
    }


    private fun changePassword() {
        progressDisplay.show()
        val getData: GetData =
            RetrofitClient.getRetrofit().create(GetData::class.java)
        val call: Call<ResponseNormal> =
            getData.resetPassword(
                "Bearer " + sharedPreference.getData("token"),
                binding.oldPass.text.toString(),
                binding.newPass.text.toString(),
                binding.confirmPass.text.toString()
            )
        call.enqueue(object : Callback<ResponseNormal?> {
            override fun onResponse(
                call: Call<ResponseNormal?>,
                response: Response<ResponseNormal?>
            ) {
                if (response.body()?.status == true) {
                    SnackBarUtils.showTopSnackbar(
                        this@EditProfile,
                        response.body()?.message ?: "",
                        getColor(R.color.green)
                    )

                    binding.newPass.setText("")
                    binding.oldPass.setText("")
                    binding.confirmPass.setText("")
                } else {
                    SnackBarUtils.showTopSnackbar(
                        this@EditProfile,
                        response.body()?.message ?: "",
                        getColor(R.color.red)
                    )
                }
                progressDisplay.dismiss()
            }

            override fun onFailure(call: Call<ResponseNormal?>, t: Throwable) {
                progressDisplay.dismiss()
                Toast.makeText(
                    applicationContext,
                    "Something went wrong !",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

    }
}