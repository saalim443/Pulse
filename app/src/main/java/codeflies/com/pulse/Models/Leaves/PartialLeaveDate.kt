package codeflies.com.pulse.Models.Leaves

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PartialLeaveDate(


    @SerializedName("leave_date")
    @Expose
    val date: String? = null,

    @SerializedName("status")
    @Expose
    val cDate: String? = null,

    @SerializedName("leave_days")
    @Expose
    val day: String? = null
)
