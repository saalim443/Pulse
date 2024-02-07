package codeflies.com.pulse.Models

import com.google.gson.annotations.SerializedName

data class ResponseNotification(

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("notifications")
	val notifications: List<NotificationsItem>? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
)

data class Data(

	@field:SerializedName("icon")
	val icon: String? = null,

	@field:SerializedName("action")
	val action: String? = null,

	@field:SerializedName("text")
	val text: String? = null,

	@field:SerializedName("title")
	val title: String? = null
)

data class NotificationsItem(

	@field:SerializedName("fcm_count")
	val fcmCount: Int? = null,

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("read_at")
	val readAt: Any? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("notifiable_id")
	val notifiableId: Int? = null,

	@field:SerializedName("type")
	val type: String? = null,

	@field:SerializedName("notifiable_type")
	val notifiableType: String? = null
)
