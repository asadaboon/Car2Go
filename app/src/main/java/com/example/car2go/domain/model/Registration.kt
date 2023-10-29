package com.example.car2go.domain.model

import io.realm.RealmModel
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass

@RealmClass
open class Registration : RealmModel {
    @PrimaryKey
    var id: String? = ""
    var citizenId: String? = ""
    var firstNameTH: String? = ""
    var lastNameTH: String? = ""
    var firstNameEN: String? = ""
    var lastNameEN: String? = ""
    var dateOfBirth: String? = ""
    var email: String? = ""
    var phone: String? = ""
    var profileImage:  String? = ""
    var profileCarImage:  String? = ""
}
