package codeflies.com.pulse.Models.Leaves

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PartialLeaveRequest(

    @SerializedName("id")
    @Expose
    var id: String? = null,

    @SerializedName("status")
    @Expose
    var status: String? = null,

    @SerializedName("status_reason")
    @Expose
    var remark: String? = null,

    @SerializedName("partialData")
    @Expose
    var partialData: List<PartialLeaveDate>? = null
)
