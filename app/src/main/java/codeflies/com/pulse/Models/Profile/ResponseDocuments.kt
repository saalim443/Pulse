package codeflies.com.pulse.Models.Profile

import com.google.gson.annotations.SerializedName

data class ResponseDocuments(

	@field:SerializedName("docTypes")
	val docTypes: List<DocTypesItem?>? = null,

	@field:SerializedName("documents")
	val documents: List<DocumentsItem>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
)

data class DocumentsItem(

	@field:SerializedName("file_path")
	val filePath: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("user_id")
	val userId: Int? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("doc_type")
	val docType: String? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("remarks")
	val remarks: String? = null
)

data class DocTypesItem(

	@field:SerializedName("for_user")
	val forUser: Boolean? = null,

	@field:SerializedName("title")
	val title: String? = null
)
