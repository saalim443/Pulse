package codeflies.com.pulse.Models.Holidays

import com.google.gson.annotations.SerializedName

data class ResponseHoliday(

	@field:SerializedName("holidays")
	val holidays: List<HolidaysItem>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
)

data class HolidaysItem(

	@field:SerializedName("day_type")
	val dayType: String? = null,

	@field:SerializedName("end_at")
	val endAt: String? = null,

	@field:SerializedName("is_holiday")
	val isHoliday: Int? = null,

	@field:SerializedName("event_type")
	val eventType: Any? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("duration_days")
	val durationDays: Int? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("start_at")
	val startAt: String? = null,

	@field:SerializedName("remarks")
	val remarks: Any? = null
)
