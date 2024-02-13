package codeflies.com.pulse.Home.Fragments.Leaves

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
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
import android.widget.Toast
import androidx.documentfile.provider.DocumentFile
import androidx.recyclerview.widget.LinearLayoutManager
import codeflies.com.pulse.Helpers.ProgressDisplay
import codeflies.com.pulse.Helpers.RetrofitClient
import codeflies.com.pulse.Helpers.SharedPreference
import codeflies.com.pulse.Helpers.SnackBarUtils
import codeflies.com.pulse.Helpers.getRealPathFromUri
import codeflies.com.pulse.Models.Leaves.NotifyTo
import codeflies.com.pulse.Models.ResponseNormal
import codeflies.com.pulse.R
import codeflies.com.pulse.databinding.ActivityNewLeaveBinding
import codeflies.com.pulse.Helpers.Interfaces.GetData
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
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.concurrent.TimeUnit

class NewLeaveActivity : AppCompatActivity() {

    lateinit var binding: ActivityNewLeaveBinding
    lateinit var sharedPreference: SharedPreference
    lateinit var progressDisplay: ProgressDisplay
    private lateinit var adapter: SelectedEmailsAdapter
    private var dateValue: Int = 0
    private lateinit var selectedEmails: MutableList<String>
    private val REQUEST_CODE_PERMISSION = 123
    val REQUEST_CODE = 200
    private var imageUriList = mutableListOf<Uri>()
    private var emailStringList = mutableListOf<String>()
    private var selectedItemValue = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewLeaveBinding.inflate(layoutInflater)
        setContentView(binding.root)


        sharedPreference = SharedPreference(this)
        progressDisplay = ProgressDisplay(this)
        selectedEmails = mutableListOf() // Initialize selectedEmails here


        binding.back.setOnClickListener {
            finish()
        }

        ArrayAdapter.createFromResource(
            this,
            R.array.spinner_options,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            binding.leaveType.adapter = adapter
        }

        binding.leaveType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                // Get the selected item position
                val selectedItemPosition = parent.getItemAtPosition(position)
                // Get the selected item value from the adapter using the position
                selectedItemValue = parent.getItemAtPosition(position).toString()
                // Show a toast message with the selected item position

                if (selectedItemValue.equals("First Half Leave")) {
                    binding.fromTime.visibility = View.VISIBLE
                    binding.toTime.visibility = View.VISIBLE
                    binding.LeaveFrom.text = SimpleDateFormat("MM/dd/yyyy").format(Date())
                    binding.LeaveTo.text = SimpleDateFormat("MM/dd/yyyy").format(Date())
                    binding.fromTime.text = "09:30"
                    binding.toTime.text = "02:10"
                    binding.LeaveDay.text = "0.5"

                } else if (selectedItemValue.equals("Second Half Leave")) {
                    binding.fromTime.visibility = View.VISIBLE
                    binding.toTime.visibility = View.VISIBLE
                    binding.LeaveFrom.text = SimpleDateFormat("MM/dd/yyyy").format(Date())
                    binding.LeaveTo.text = SimpleDateFormat("MM/dd/yyyy").format(Date())
                    binding.fromTime.text = "02:10"
                    binding.toTime.text = "06:30"
                    binding.LeaveDay.text = "0.5"

                } else if (selectedItemValue.equals("Full Day Leave")) {
                    binding.fromTime.visibility = View.GONE
                    binding.toTime.visibility = View.GONE
                    binding.LeaveFrom.text = SimpleDateFormat("MM/dd/yyyy").format(Date())
                    binding.LeaveTo.text = SimpleDateFormat("MM/dd/yyyy").format(Date())
                    binding.LeaveDay.text = "1"
                }
                //Toast.makeText(applicationContext, "Selected position: $selectedItemValue", Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing
            }
        }


//        binding.chooseFiles.setOnClickListener {
//            if (this.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
//                // Permission already granted, proceed with accessing images
//                openGalleryForImages()
//            } else {
//                // Request permission
//                requestPermissions(
//                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
//                    REQUEST_CODE_PERMISSION
//                )
//            }
//        }

        binding.chooseFiles.setOnClickListener {
            val permissionsToRequest = mutableListOf<String>()

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    permissionsToRequest.add(Manifest.permission.READ_EXTERNAL_STORAGE)
                    requestPermissions(
                        permissionsToRequest.toTypedArray(),
                        REQUEST_CODE_PERMISSION
                    )
                }else{
                    openGalleryForImages()
                }
            } else {
                if (checkSelfPermission(Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                    permissionsToRequest.add(Manifest.permission.READ_MEDIA_IMAGES)
                    requestPermissions(
                        permissionsToRequest.toTypedArray(),
                        REQUEST_CODE_PERMISSION
                    )
                }else{
                    openGalleryForImages()
                }
            }
        }

        binding.LeaveFrom.setOnClickListener {

            dateValue = 1
            showDatePicker()
        }
        binding.LeaveTo.setOnClickListener {
            dateValue = 2
            showDatePicker()
        }

        getNotifyTo()
        binding.submitLeave.setOnClickListener {
            if (validation()) {
                uploadLeave()
            }
        }
    }


    private fun uploadLeave() {
        progressDisplay.show()
        val token = sharedPreference.getData("token").toString()

        // Create a RequestBody for form_step and id
        val formStepRequestBody = "3".toRequestBody("text/plain".toMediaTypeOrNull())
        val title =
            binding.leaveTitle.text.toString().toRequestBody("text/plain".toMediaTypeOrNull())

        // Create a RequestBody for leave_type based on selectedItemValue
        val leaveType: RequestBody = when (selectedItemValue) {
            "First Half Leave" -> "first_half".toRequestBody("text/plain".toMediaTypeOrNull())
            "Second Half Leave" -> "second_half"
                .toRequestBody("text/plain".toMediaTypeOrNull())

            "Full Day Leave" -> "full_days".toRequestBody("text/plain".toMediaTypeOrNull())
            else -> "".toRequestBody("text/plain".toMediaTypeOrNull()) // Handle default case
        }

        val leaveFromDate =
            binding.LeaveFrom.text.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val leaveToDate =
            binding.LeaveTo.text.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val leaveDays =
            binding.LeaveDay.text.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val content = binding.WriteYourContent.text.toString()
            .toRequestBody("text/plain".toMediaTypeOrNull())

        // Create a list of MultipartBody.Part for images
        val imageParts = mutableListOf<MultipartBody.Part>()
        for (uri in imageUriList) {
            val fileName = getFileNameFromUriWithoutPath(uri)
            val file = File(getRealPathFromUri(this@NewLeaveActivity, uri))
            val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
            val imagePart =
                MultipartBody.Part.createFormData("attachments[]", fileName ?: "", requestFile)
            imageParts.add(imagePart)
        }

        // Create a list of RequestBody for email addresses
        val emailParts = mutableListOf<RequestBody>()
        emailStringList.forEach { emailString ->
            val emailPart = emailString.toRequestBody("text/plain".toMediaTypeOrNull())
            emailParts.add(emailPart)
        }

        // Make the API call
        val getData: GetData = RetrofitClient.getRetrofit().create(GetData::class.java)
        val call: Call<ResponseNormal> = getData.uploadLeave(
            "Bearer $token",
            title,
            leaveType,
            leaveFromDate,
            leaveToDate,
            leaveDays,
            content,
            imageParts,
            emailParts
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
                        finish()
                    } else {
                        SnackBarUtils.showTopSnackbar(
                            this@NewLeaveActivity,
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
                            this@NewLeaveActivity,
                            errorMessage,
                            Color.RED
                        )
                    } catch (e: Exception) {
                        SnackBarUtils.showTopSnackbar(
                            this@NewLeaveActivity,
                            e.message ?: "",
                            Color.RED
                        )
                    }
                }
            }

            override fun onFailure(call: Call<ResponseNormal>, t: Throwable) {
                progressDisplay.dismiss()
                SnackBarUtils.showTopSnackbar(this@NewLeaveActivity, t.message ?: "", Color.RED)
            }
        })
    }


    private fun validation(): Boolean {
        if (binding.leaveTitle.text.length == 0) {
            SnackBarUtils.showTopSnackbar(this@NewLeaveActivity, "Please Fill Title!", R.color.red)
            binding.leaveTitle.requestFocus()
            return false
        } else if (selectedItemValue.equals("--Select--")) {
            SnackBarUtils.showTopSnackbar(
                this@NewLeaveActivity,
                "Please Select Leave Type!",
                R.color.red
            )
            return false
        } else if (binding.LeaveFrom.text.length == 0) {
            SnackBarUtils.showTopSnackbar(
                this@NewLeaveActivity,
                "Please Select From Date!",
                R.color.red
            )
            return false
        } else if (binding.LeaveTo.text.length == 0) {
            SnackBarUtils.showTopSnackbar(
                this@NewLeaveActivity,
                "Please Select To Date!",
                R.color.red
            )
            return false
        }else if (binding.LeaveDay.text.length == 0) {
            SnackBarUtils.showTopSnackbar(
                this@NewLeaveActivity,
                "Please Select To Valid Date!",
                R.color.red
            )
            return false
        } else if (binding.WriteYourContent.text.length == 0) {
            SnackBarUtils.showTopSnackbar(
                this@NewLeaveActivity,
                "Please Write Your Content!",
                R.color.red
            )
            binding.WriteYourContent.requestFocus()
            return false
        }

        return true
    }

    private fun getNotifyTo() {
        progressDisplay.show()
        Log.e("token", "Bearer " + sharedPreference.getData("token"))
        val getData: GetData =
            RetrofitClient.getRetrofit().create(GetData::class.java)
        val call: Call<NotifyTo> =
            getData.notify_to("Bearer " + sharedPreference.getData("token"))
        call.enqueue(object : Callback<NotifyTo?> {
            override fun onResponse(call: Call<NotifyTo?>, response: Response<NotifyTo?>) {
                if (response.body()?.status == true) {
                    val notifyToList: List<String>? =
                        response.body()?.users?.mapNotNull { it?.email }

                    if (!notifyToList.isNullOrEmpty()) {
                        // Assuming you have a spinner named 'notifyToSpinner' in your layout XML
                        val adapter = ArrayAdapter<String>(
                            this@NewLeaveActivity,
                            android.R.layout.simple_spinner_item,
                            notifyToList
                        )
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        binding.notifyToSpinner.adapter = adapter

                        binding.notifyToSpinner.onItemSelectedListener =
                            object : AdapterView.OnItemSelectedListener {
                                override fun onItemSelected(
                                    parent: AdapterView<*>?,
                                    view: View?,
                                    position: Int,
                                    id: Long
                                ) {
                                    // Get selected email
                                    val selectedEmail =
                                        parent?.getItemAtPosition(position).toString()
                                    // Call function to fetch RecyclerView data based on selected email

                                    emailStringList.add(response.body()?.users?.get(position)?.id.toString())
                                    fetchRecyclerViewData(selectedEmail)
                                }

                                override fun onNothingSelected(parent: AdapterView<*>?) {
                                    // Do nothing
                                }
                            }
                    }

                } else {
                    Toast.makeText(
                        this@NewLeaveActivity,
                        response.body()?.message,
                        Toast.LENGTH_SHORT
                    ).show()
                    // Toast.makeText( Dashboard.activity, "Sorry!! someone has already accepted the ride", Toast.LENGTH_SHORT ).show();
                }

                progressDisplay.dismiss()
            }

            override fun onFailure(call: Call<NotifyTo?>, t: Throwable) {
                progressDisplay.dismiss()
                Toast.makeText(
                    this@NewLeaveActivity,
                    "Something went wrong !",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

    }




    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { _, selectedYear, selectedMonth, selectedDay ->
                // Handle the selected date
                val formattedDate = formatDate(selectedYear, selectedMonth, selectedDay)
                // Use the formatted date as needed

                if (dateValue == 1) {
                    if (!selectedItemValue.equals("Full Day Leave")) {
                        binding.LeaveFrom.text = formattedDate
                        binding.LeaveTo.text = formattedDate
                    } else {
                        binding.LeaveFrom.text = formattedDate
                        differentDay()
                    }
                } else if (dateValue == 2) {
                    if (!selectedItemValue.equals("Full Day Leave")) {
                        binding.LeaveFrom.text = formattedDate
                        binding.LeaveTo.text = formattedDate
                    } else {
                        binding.LeaveTo.text = formattedDate
                        differentDay()
                    }

                }

            },
            year,
            month,
            day
        )
        datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000

        datePickerDialog.show()
    }

    private fun getFileNameFromUriWithoutPath(uri: Uri): String? {
        val fullFileName = getFileNameFromUri(uri)
        // Use substringAfterLast to get the file name without the path
        return fullFileName?.substringAfterLast('/')
    }

    private fun getFileNameFromUri(uri: Uri): String? {
        var fileName: String? = null

        if (DocumentsContract.isDocumentUri(this@NewLeaveActivity, uri)) {
            // Handle document URI
            val document = DocumentFile.fromSingleUri(this@NewLeaveActivity, uri)
            fileName = document?.name
        } else {
            // Handle other URI types
            val cursor = this@NewLeaveActivity.contentResolver.query(uri, null, null, null, null)

            cursor?.use {
                if (it.moveToFirst()) {
                    val displayName = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    fileName = it.getString(displayName)
                }
            }
        }

        return fileName
    }

    private fun differentDay() {
        // Example date strings
        val dateString1 = binding.LeaveFrom.text.toString() // First date
        val dateString2 = binding.LeaveTo.text.toString() // Second date

        // Create a SimpleDateFormat instance with the desired date format
        val simpleDateFormat = SimpleDateFormat("MM/dd/yyyy")

        try {
            // Parse the date strings into Date objects
            val date1: Date = simpleDateFormat.parse(dateString1) ?: Date()
            val date2: Date = simpleDateFormat.parse(dateString2) ?: Date()

            // Calculate the difference in milliseconds between the two dates
            val differenceInMillis: Long = date2.time - date1.time

            // Convert milliseconds to days
            val differenceInDays: Long = TimeUnit.MILLISECONDS.toDays(differenceInMillis)

            if (differenceInDays < 0) {
                // Handle the case where date2 is before date1
                SnackBarUtils.showTopSnackbar(
                    this@NewLeaveActivity,
                    "Error: End date is before start date",
                    R.color.red
                )
                binding.LeaveDay.text = ""

            } else {
                // Output the difference in days
                binding.LeaveDay.text = differenceInDays.toString()
            }
            println("Difference in days: $differenceInDays")
        } catch (e: Exception) {
            e.printStackTrace()
            // Handle parsing exceptions
        }
    }

    private fun onDateSet(selectedYear: Int, selectedMonth: Int, selectedDay: Int) {
        val formattedMonth = selectedMonth + 1
        val formattedDate = "$formattedMonth-$selectedDay-$selectedYear"

        //else if (dateValue == 3) {
//            binding.memberExpireTxt.setText(formattedDate)
//        } else if (dateValue == 4) {
//            binding.policyExpireTxt.setText(formattedDate)
//        }
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

    private fun formatDate(year: Int, month: Int, day: Int): String {
        // Create a Calendar instance and set it to the selected date
        val calendar = Calendar.getInstance()
        calendar.set(year, month, day)

        // Create a SimpleDateFormat instance with the desired date format
        val dateFormat = SimpleDateFormat("MM/dd/yyyy")

        // Format the selected date and return the formatted string
        return dateFormat.format(calendar.time)
    }

    // Define the function outside of fetchRecyclerViewData
    private fun fetchRecyclerViewData(selectedEmail: String) {
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.allEmails.layoutManager = layoutManager

        // Add the selected email to the list
        selectedEmails.add(selectedEmail)


        // Initialize the adapter
        adapter = SelectedEmailsAdapter(selectedEmails) { clickedPosition ->
            // Handle the delete button click event (remove the image at the clicked position)
            adapter.removeImage(clickedPosition)
            adapter.notifyDataSetChanged()
        }
        binding.allEmails.adapter = adapter
    }
}
