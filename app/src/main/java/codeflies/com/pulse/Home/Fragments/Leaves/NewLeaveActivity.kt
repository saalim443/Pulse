package codeflies.com.pulse.Home.Fragments.Leaves

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import codeflies.com.pulse.Helpers.ProgressDisplay
import codeflies.com.pulse.Helpers.SharedPreference
import codeflies.com.pulse.R
import codeflies.com.pulse.databinding.ActivityMainBinding
import codeflies.com.pulse.databinding.ActivityNewLeaveBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.concurrent.TimeUnit

class NewLeaveActivity : AppCompatActivity() {

    lateinit var binding: ActivityNewLeaveBinding
    lateinit var sharedPreference: SharedPreference
    lateinit var progressDisplay: ProgressDisplay
    private var dateValue: Int = 0
    private val REQUEST_CODE_PERMISSION = 123
    val REQUEST_CODE = 200
    private var imageUriList = mutableListOf<Uri>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewLeaveBinding.inflate(layoutInflater)
        setContentView(binding.root)


        sharedPreference = SharedPreference(this)
        progressDisplay = ProgressDisplay(this)


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
                val selectedItemValue = parent.getItemAtPosition(position).toString()
                // Show a toast message with the selected item position

                if (selectedItemValue.equals("First Half Leave")) {
                    binding.fromTime.visibility = View.VISIBLE
                    binding.toTime.visibility = View.VISIBLE
                    binding.fromTime.text = "09:30"
                    binding.toTime.text = "02:10"
                    binding.LeaveDay.text = "0.5"

                } else if (selectedItemValue.equals("Second Half Leave")) {
                    binding.fromTime.visibility = View.VISIBLE
                    binding.toTime.visibility = View.VISIBLE
                    binding.fromTime.text = "02:10"
                    binding.toTime.text = "06:30"
                    binding.LeaveDay.text = "0.5"

                } else if (selectedItemValue.equals("Full Day Leave")) {
                    binding.fromTime.visibility = View.GONE
                    binding.toTime.visibility = View.GONE
                    binding.LeaveDay.text = "1"
                }
                //Toast.makeText(applicationContext, "Selected position: $selectedItemValue", Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing
            }
        }


        binding.chooseFiles.setOnClickListener {
            if (this.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                // Permission already granted, proceed with accessing images
                openGalleryForImages()
            } else {
                // Request permission
                requestPermissions(
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    REQUEST_CODE_PERMISSION
                )
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
                    binding.LeaveFrom.setText(formattedDate)
                } else if (dateValue == 2) {
                    binding.LeaveTo.setText(formattedDate)
                    differentDay()

                }

            },
            year,
            month,
            day
        )
        //  datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000

        datePickerDialog.show()
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

            // Output the difference in days
            binding.LeaveDay.text = differenceInDays.toString()
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

    private fun formatDate(year: Int, month: Int, day: Int): String {
        // Create a Calendar instance and set it to the selected date
        val calendar = Calendar.getInstance()
        calendar.set(year, month, day)

        // Create a SimpleDateFormat instance with the desired date format
        val dateFormat = SimpleDateFormat("MM/dd/yyyy")

        // Format the selected date and return the formatted string
        return dateFormat.format(calendar.time)
    }
}
