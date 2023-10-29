package com.example.car2go.data.dao

import com.example.car2go.domain.model.Registration
import javax.inject.Inject

class RegistrationDao @Inject constructor(
) : BaseDao<Registration>() {

    fun insertOrUpdate(registration: Registration) {
        save(registration)
    }
}