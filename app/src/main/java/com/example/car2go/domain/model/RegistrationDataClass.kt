package com.example.car2go.domain.model

import android.os.Parcelable
import io.realm.annotations.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
data class RegistrationDataClass(
    @PrimaryKey
    var id: String? = "",
    var citizenId: String? = "",
    var firstNameTH: String? = "",
    var lastNameTH: String? = "",
    var firstNameEN: String? = "",
    var lastNameEN: String? = "",
    var dateOfBirth: String? = "",
    var email: String? = "",
    var phone: String? = "",
    var profileImage: String? = null,
    var profileCarImage: String? = null,
) : Parcelable