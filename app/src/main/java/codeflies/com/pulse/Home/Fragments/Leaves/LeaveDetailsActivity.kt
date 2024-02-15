package codeflies.com.pulse.Home.Fragments.Leaves

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import codeflies.com.pulse.Helpers.FunctionClass
import codeflies.com.pulse.Helpers.ProgressDisplay
import codeflies.com.pulse.Helpers.RetrofitClient
import codeflies.com.pulse.Helpers.SharedPreference
import codeflies.com.pulse.Models.Leaves.LeaveStatusDetails
import codeflies.com.pulse.Models.Leaves.LeavesDetails
import codeflies.com.pulse.Models.Leaves.LeavesItem
import codeflies.com.pulse.Models.Leaves.PartialLeaveDate
import codeflies.com.pulse.Models.Leaves.PartialLeaveRequest
import codeflies.com.pulse.Models.Leaves.attachmentsItem
import codeflies.com.pulse.Models.ResponseNormal
import codeflies.com.pulse.R
import codeflies.com.pulse.databinding.ActivityLeaveDetailsBinding
import com.codeflies.supertravel.TabsLayou.TabLayoutFragment.UpComingRides.PartialDateAdapter
import codeflies.com.pulse.Helpers.Interfaces.GetData
import com.google.android.material.bottomsheet.BottomSheetDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Date

class LeaveDetailsActivity : AppCompatActivity() {

    lateinit var binding: ActivityLeaveDetailsBinding
    var context: Context = this@LeaveDetailsActivity
    var selectedStatus = ""

    var partialListDate: ArrayList<PartialLeaveDate> = ArrayList<PartialLeaveDate>()


    companion object {
        lateinit var leave: LeavesItem
        var finalList: ArrayList<PartialLeaveDate> = ArrayList<PartialLeaveDate>()


        fun saveValue(date: String,status: String, position: Int){
            val startDate: Date =  SimpleDateFormat("EEE, dd-MMM-yy").parse(date)
            var dates=SimpleDateFormat("yyyy-MM-dd").format(startDate)
            finalList.set(position, PartialLeaveDate(dates,status,"1"))
        }
    }

    lateinit var sharedPreference: SharedPreference
    lateinit var progressDisplay: ProgressDisplay
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLeaveDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreference = SharedPreference(context)
        progressDisplay = ProgressDisplay(context)


        binding.back.setOnClickListener {
            finish()
        }



        if(sharedPreference.getData("role")=="admin" || sharedPreference.getData("role")=="hr_manager") {
            binding.statusChange.visibility=View.VISIBLE
        }else{
            binding.statusChange.visibility=View.GONE
        }

        binding.statusChange.setOnClickListener {

            showDialog()
        }
        getLeavesDetails()
        getPartialDates()
    }


    fun getPartialDates(){
        var dates=FunctionClass.getDatesBetween(leave.leaveFrom, leave.leaveEnd)
        for (date in dates) {
            partialListDate.add(PartialLeaveDate(date, FunctionClass.changeDate(leave.createdAt)))
        }
    }



    private fun getLeavesDetails() {
        progressDisplay.show()
        Log.e("token", "Bearer " + sharedPreference.getData("token"))
        val getData: GetData =
            RetrofitClient.getRetrofit().create(GetData::class.java)
        val call: Call<LeavesDetails> =
            getData.getLeaveData("Bearer " + sharedPreference.getData("token"), leave.id.toString())
        call.enqueue(object : Callback<LeavesDetails?> {
            override fun onResponse(
                call: Call<LeavesDetails?>,
                response: Response<LeavesDetails?>
            ) {
                if (response.body()?.status == true) {
                    binding.dayBefore.text = response.body()?.leave?.dateAndTimeHuman.toString()
                    binding.dayCreate.text =
                        FunctionClass.changeDate(response.body()?.leave?.createdAt)
                    binding.ShowTitle.text = response.body()?.leave?.title.toString()
                    binding.nameLeave.text = response.body()?.leave?.employeeName.toString()
                    binding.emailLeave.text =
                        "| " + response.body()?.leave?.employeeEmail.toString()
                    binding.codeLeave.text = response.body()?.leave?.employeeCode.toString()
                    binding.notifyToLeaveDate.text =
                        response.body()?.leave?.notifierDetail.toString()
                    binding.contentLeave.text = response.body()?.leave?.content.toString()

                    if (response.body()?.leave?.status == "approved") {
                        binding.statusLeave.setTextColor(context.getColor(R.color.pulse_color))
                        binding.statusLeave.text = "Approved"
                        binding.statusChangeLeave.text = "Approved"
                    } else if (response.body()?.leave?.status == "rejected") {
                        binding.statusLeave.setTextColor(context.getColor(R.color.red))
                        binding.statusLeave.text = "Rejected"
                        binding.statusChangeLeave.text = "Rejected"
                    } else if (response.body()?.leave?.status == "partial") {
                        binding.statusLeave.setTextColor(context.getColor(R.color.red))
                        binding.statusLeave.text = "Partial"
                        binding.statusChangeLeave.text = "Partial"
                    } else {
                        binding.statusLeave.setTextColor(context.getColor(R.color.orange))
                        binding.statusLeave.text = "Pending"
                        binding.statusChangeLeave.text = "Pending"
                    }


                    binding.dateLeave.text =
                        FunctionClass.changeDate(response.body()?.leave?.leaveFrom) + " to " +
                                FunctionClass.changeDate(response.body()?.leave?.leaveEnd) + " | " + response.body()?.leave?.leaveDays + " days" +
                                " | " + response.body()?.leave?.leaveTypes

                    binding.recyclerView.layoutManager = GridLayoutManager(this@LeaveDetailsActivity, 2)
                    val attachmentsList: List<attachmentsItem> =
                        response.body()?.leave?.attachments!!
                    val adapter = AttachmentsAdapter(this@LeaveDetailsActivity,attachmentsList)
                    binding.recyclerView.adapter = adapter
                } else {
                    Toast.makeText(
                        context,
                        response.body()?.message,
                        Toast.LENGTH_SHORT
                    ).show()
                    // Toast.makeText( Dashboard.activity, "Sorry!! someone has already accepted the ride", Toast.LENGTH_SHORT ).show();
                }

                progressDisplay.dismiss()
            }

            override fun onFailure(call: Call<LeavesDetails?>, t: Throwable) {
                progressDisplay.dismiss()
                Toast.makeText(
                    context,
                    "Something went wrong !",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }


    fun changeLeavestatus( dialog: BottomSheetDialog, remark: String){


        var partialLeaveRequest=PartialLeaveRequest()
        partialLeaveRequest.id=leave.id.toString()
        partialLeaveRequest.status=selectedStatus
        partialLeaveRequest.remark=remark
        partialLeaveRequest.partialData= finalList

        progressDisplay.show()
        Log.e("token", "Bearer " + sharedPreference.getData("token"))
        val updateStatus: GetData =
            RetrofitClient.getRetrofit().create(GetData::class.java)
        val callStatus: Call<ResponseNormal> =
            updateStatus.statusUpdate("Bearer " + sharedPreference.getData("token"),partialLeaveRequest)
        callStatus.enqueue(object : Callback<ResponseNormal?> {
            override fun onResponse(call: Call<ResponseNormal?>, response: Response<ResponseNormal?>) {
                if (response.body()?.status == true) {
                    dialog.dismiss()
                    finish()
                } else {
                    Toast.makeText(
                        this@LeaveDetailsActivity,
                        response.body()?.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }

                progressDisplay.dismiss()
            }

            override fun onFailure(call: Call<ResponseNormal?>, t: Throwable) {
                progressDisplay.dismiss()
                Toast.makeText(
                    this@LeaveDetailsActivity,
                    "Something went wrong !",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

    }

    fun getStatus(view: View,txtRemark: TextView,remark: EditText, listPartial: RecyclerView){
        progressDisplay.show()
        Log.e("token", "Bearer " + sharedPreference.getData("token"))
        val getData: GetData =
            RetrofitClient.getRetrofit().create(GetData::class.java)
        val call: Call<LeaveStatusDetails> =
            getData.getStatus("Bearer " + sharedPreference.getData("token"))
        call.enqueue(object : Callback<LeaveStatusDetails?> {
            override fun onResponse(call: Call<LeaveStatusDetails?>, response: Response<LeaveStatusDetails?>) {
                if (response.body()?.status == true) {
                    val notifyToList: List<String>? =
                        response.body()?.leaveStatus?.mapNotNull { it?.name }

                    if (!notifyToList.isNullOrEmpty()) {
                        // Assuming you have a spinner named 'notifyToSpinner' in your layout XML
                        val adapter = ArrayAdapter<String>(
                            this@LeaveDetailsActivity,
                            android.R.layout.simple_spinner_item,
                            notifyToList
                        )
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        val spinner =  view.findViewById<Spinner>(R.id.statusToSpinner)
                        spinner.adapter = adapter

                        spinner.onItemSelectedListener =
                            object : AdapterView.OnItemSelectedListener {
                                override fun onItemSelected(
                                    parent: AdapterView<*>?,
                                    view: View?,
                                    position: Int,
                                    id: Long
                                ) {
                                    selectedStatus = response.body()?.leaveStatus?.get(position)?.slug.toString()
                                   if(selectedStatus=="pending"){
                                       txtRemark.visibility=View.INVISIBLE
                                       remark.visibility=View.INVISIBLE
                                       listPartial.visibility=View.GONE
                                   }else if(selectedStatus=="approved"){
                                       txtRemark.visibility=View.VISIBLE
                                       remark.visibility=View.VISIBLE
                                       listPartial.visibility=View.GONE
                                   }else if(selectedStatus=="rejected"){
                                       txtRemark.visibility=View.VISIBLE
                                       remark.visibility=View.VISIBLE
                                       listPartial.visibility=View.GONE
                                   }else if(selectedStatus=="partial"){
                                       txtRemark.visibility=View.GONE
                                       remark.visibility=View.GONE
                                       listPartial.visibility=View.VISIBLE
                                   }else{
                                       txtRemark.visibility=View.VISIBLE
                                       remark.visibility=View.VISIBLE
                                       listPartial.visibility=View.GONE
                                   }
                                }

                                override fun onNothingSelected(parent: AdapterView<*>?) {
                                    // Do nothing
                                }
                            }
                    }

                } else {
                    Toast.makeText(
                        this@LeaveDetailsActivity,
                        response.body()?.message,
                        Toast.LENGTH_SHORT
                    ).show()
                    // Toast.makeText( Dashboard.activity, "Sorry!! someone has already accepted the ride", Toast.LENGTH_SHORT ).show();
                }

                progressDisplay.dismiss()
            }

            override fun onFailure(call: Call<LeaveStatusDetails?>, t: Throwable) {
                progressDisplay.dismiss()
                Toast.makeText(
                    this@LeaveDetailsActivity,
                    "Something went wrong !",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

    }


    fun showDialog(){
        val dialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.bottomsheet_deatils, null)
        val  submit = view.findViewById<Button>(R.id.submit)
        val  remark = view.findViewById<EditText>(R.id.remarkStatus)
        val  txtRemark = view.findViewById<TextView>(R.id.txtRemark)
        val  partialDates = view.findViewById<RecyclerView>(R.id.partialDates)


        partialDates.layoutManager=LinearLayoutManager(context)
        partialDates.setHasFixedSize(true)
        partialDates.adapter=PartialDateAdapter(context,partialListDate)


        getStatus(view,txtRemark,remark,partialDates)


        submit.setOnClickListener {

            changeLeavestatus(dialog,remark.text.toString())
        }

        dialog.setCancelable(true)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));

        dialog.setContentView(view)

        dialog.show()
    }

}