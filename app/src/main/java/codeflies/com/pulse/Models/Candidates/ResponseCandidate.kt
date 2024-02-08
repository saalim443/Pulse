package codeflies.com.pulse.Models.Candidates

import codeflies.com.pulse.Models.CandidateDetails.User
import com.google.gson.annotations.SerializedName

data class ResponseCandidate(

	@field:SerializedName("candidates")
	val candidates: List<CandidatesItem>? = null,

	@field:SerializedName("recruiters")
	val recruiters: List<Any?>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
)

data class User(

	@field:SerializedName("profile_img")
	val profileImg: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("mobile")
	val mobile: String? = null,

	@field:SerializedName("fcm_token")
	val fcmToken: Any? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("email_verified_at")
	val emailVerifiedAt: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("status")
	val status: Int? = null,

	@field:SerializedName("token")
	val token: Any? = null
)

data class CandidatesItem(

	@field:SerializedName("interview_date")
	val interviewDate: Any? = null,

	@field:SerializedName("next_round")
	val nextRound: Any? = null,

	@field:SerializedName("experience_years")
	val experienceYears: Int? = null,

	@field:SerializedName("alternate_mobile")
	val alternateMobile: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("user_created")
	val userCreated: Int? = null,

	@field:SerializedName("notice_period_left")
	val noticePeriodLeft: Int? = null,

	@field:SerializedName("interviewer_id")
	val interviewerId: Any? = null,

	@field:SerializedName("candidate_rounds")
	val candidateRounds: List<Any?>? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("added_by")
	val addedBy: AddedBy? = null,

	@field:SerializedName("experience_months")
	val experienceMonths: Int? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("resume")
	val resume: String? = null,

	@field:SerializedName("interviewer")
	val interviewer: Any? = null,

	@field:SerializedName("comments")
	val comments: List<Any?>? = null,

	@field:SerializedName("expected_salary")
	val expectedSalary: Int? = null,

	@field:SerializedName("mobile")
	val mobile: String? = null,

	@field:SerializedName("recruiter")
	val recruiter: Recruiter? = null,

	@field:SerializedName("current_salary")
	val currentSalary: Int? = null,

	@field:SerializedName("recruiter_id")
	val recruiterId: Int? = null,

	@field:SerializedName("user_id")
	val userId: Int? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("designation")
	val designation: String? = null,

	@field:SerializedName("user")
	val user: User? = null,

	@field:SerializedName("remarks")
	val remarks: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class Recruiter(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: Int? = null
)

data class AddedBy(

	@field:SerializedName("profile_img")
	val profileImg: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("mobile")
	val mobile: String? = null,

	@field:SerializedName("fcm_token")
	val fcmToken: Any? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("email_verified_at")
	val emailVerifiedAt: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("status")
	val status: Int? = null,

	@field:SerializedName("token")
	val token: Any? = null
)

data class CommentsItem(

	@field:SerializedName("candidate_id")
	val candidateId: Int? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("user_id")
	val userId: Int? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("comment")
	val comment: String? = null,

	@field:SerializedName("id")
	val id: Int? = null
)


