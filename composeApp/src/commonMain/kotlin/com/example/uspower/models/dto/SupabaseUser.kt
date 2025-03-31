package com.example.uspower.models.dto


import com.example.uspower.models.User
import kotlinx.datetime.LocalDate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class SupabaseUser(
    @SerialName("uuid")
    val uuid: String,
    @SerialName("firstname")
    val firstName: String,
    @SerialName("last_name")
    val lastName: String,
    @SerialName("position")
    val position: String?,
    @SerialName("start_date")
    val startDate: LocalDate?,
    @SerialName("approved")
    val approved: Boolean,
    @SerialName("admin")
    val admin: Boolean,
    @SerialName("email")
    val email: String,
    @SerialName("phone")
    val phone: String?,
    @SerialName("photo_url")
    val photoUrl: String?,
    @SerialName("password")
    val password: String

)


fun SupabaseUser.toFirebaseUser(): User {
//    scription: String = "",
//    val phone: String = "",
//    val approved: Boolean = false,
//    val rememberMe: Boolean = false,
//    val admin: Boolean = false,
//    val startDate: String = "",
//    var fcmToken: String = ""
    return User(
        uuid = this.uuid,
        email = this.email,
        firstName = this.firstName,
        lastName = this.lastName,
        photoUrl = this.photoUrl ?: "",
        role = this.position ?: "",
        /*
            TODO ADD to SUPABASE description
         */
        description = "",
        phone = this.phone ?: "",
        approved = this.approved,
        /*
            TODO ADD TO SUPABASE remember me
         */
        rememberMe = false,

        admin = this.admin,
        startDate = this.startDate?.toString() ?: "",
        /*
            TODO ADD TO SUPABASE
         */
        fcmToken = "",
        password = this.password



    )
}