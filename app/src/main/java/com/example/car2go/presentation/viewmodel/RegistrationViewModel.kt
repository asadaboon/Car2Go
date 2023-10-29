package com.example.car2go.presentation.viewmodel

import android.Manifest
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Environment
import android.util.Base64
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.car2go.common.Constants.FIELD_ID
import com.example.car2go.common.cryptographic.IEncryption
import com.example.car2go.common.cryptographic.builder.EncryptionBuilder
import com.example.car2go.common.cryptographic.core.enums.Alias
import com.example.car2go.common.cryptographic.core.enums.CipherAlgorithm
import com.example.car2go.common.cryptographic.core.enums.EncryptionMode
import com.example.car2go.common.cryptographic.core.enums.EncryptionPadding
import com.example.car2go.domain.model.Registration
import com.example.car2go.domain.model.RegistrationDataClass
import dagger.hilt.android.lifecycle.HiltViewModel
import io.realm.Realm
import java.io.ByteArrayOutputStream
import java.util.*
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(

) :
    ViewModel() {

    private var realm: Realm = Realm.getDefaultInstance()
    val isRequestPermission = MutableLiveData<Boolean>()
    var registrationLiveData = MutableLiveData<RegistrationDataClass>()
    private var registration = RegistrationDataClass()
    var regisSuccess = MutableLiveData<Registration>()
    var alreadyRegisLiveData = MutableLiveData<Registration?>()
    private lateinit var preferences: SharedPreferences

    private val iEncryptionAES: IEncryption by lazy {
        val builder = EncryptionBuilder(alias = Alias.AES.value, type = CipherAlgorithm.AES)
        builder.config.blockMode = EncryptionMode.CBC
        builder.config.encryptionPadding = EncryptionPadding.PKCS7
        builder.build()
    }

    private var cipherIV: ByteArray?
        get() {
            preferences.getString("cipher_iv", null)?.let {
                return Base64.decode(it, Base64.DEFAULT)
            }
            return null
        }
        set(value) {
            val editor = preferences.edit()
            editor.putString("cipher_iv", Base64.encodeToString(value, Base64.DEFAULT))
            editor.apply()
        }

    fun prepareRegistrationData(
        citizenId: String = "",
        firstNameTH: String = "",
        lastNameTH: String = "",
        firstNameEN: String = "",
        lastNameEN: String = "",
        dateOfBirth: String = "",
        email: String = "",
        phone: String = "",
        profileImage: String? = "",
        profileCarImage: String? = "",
    ) {
        registrationLiveData.value = RegistrationDataClass(
            id = UUID.randomUUID().toString(),
            citizenId = citizenId,
            firstNameTH = firstNameTH,
            lastNameTH = lastNameTH,
            firstNameEN = firstNameEN,
            lastNameEN = lastNameEN,
            dateOfBirth = dateOfBirth,
            email = email,
            phone = phone,
            profileImage = profileImage,
            profileCarImage = profileCarImage
        )
    }

    fun updateImage(
        profileImage: Bitmap? = null,
        profileCarImage: Bitmap? = null
    ) {
        registrationLiveData.value?.apply {
            profileImage?.let {
                val bitmapToString = convertBitmapToString(it)
                this.profileImage = bitmapToString
            }
            profileCarImage?.let {
                val bitmapToString = convertBitmapToString(it)
                this.profileCarImage = bitmapToString
            }
        }
        registrationLiveData.value?.let {
            registration = it
        }
        setRegistrationData(registration)
    }

    fun setRegistrationData(receiverData: RegistrationDataClass?) {
        receiverData.let {
            registrationLiveData.value = it
        }
    }

    fun getRequestPermission(requestPermission: Boolean) {
        isRequestPermission.value = requestPermission
    }

    fun submitRegister() {
        val registrationData = registrationLiveData.value
        realm.executeTransaction { realm: Realm ->
            val registration =
                realm.createObject(Registration::class.java, UUID.randomUUID().toString())
            registration.apply {
                citizenId = registrationData?.citizenId
                firstNameTH = registrationData?.firstNameTH
                lastNameTH = registrationData?.lastNameTH
                firstNameEN = registrationData?.firstNameEN
                lastNameEN = registrationData?.lastNameEN
                dateOfBirth = registrationData?.dateOfBirth
                email = registrationData?.email
                phone = registrationData?.phone
                profileImage = registrationData?.profileImage?.let {
                    encryptAES(it)
                }
                profileCarImage = registrationData?.profileCarImage?.let {
                    encryptAES(it)
                }
            }
            realm.insertOrUpdate(registration)
            val result = realm.copyToRealmOrUpdate(registration)
            if (result != null) {
                regisSuccess.value = result
            }
        }
    }

    fun getUserInfoById(id: String) {
        var result: Registration? = null

        Realm.getDefaultInstance().use { realm ->
            realm.executeTransaction {
                val realmResult = it.where(Registration::class.java)
                    .equalTo(FIELD_ID, id)
                    .findFirst()

                if (realmResult != null) {
                    result = realm.copyFromRealm(realmResult)
                }
            }
        }
        alreadyRegisLiveData.value = result
    }

    fun permissionIsGranted(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Environment.isExternalStorageManager()
        } else {
            val writePermissionIsGranted = ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED

            val readPermissionIsGranted = ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED

            val cameraPermissionIsGranted = ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA,
            ) == PackageManager.PERMISSION_GRANTED
            return writePermissionIsGranted && readPermissionIsGranted && cameraPermissionIsGranted
        }
    }

    private fun encryptAES(plainStr: String): String {
        try {
            val plainByte = plainStr.toByteArray()
            val result = iEncryptionAES.doEncrypt(plainByte = plainByte)
            cipherIV = result.cipherIV
            return Base64.encodeToString(result.bytes, Base64.DEFAULT)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }

    fun decryptAES(encryptedStr: String): String {
        try {
            val encryptedByte = Base64.decode(encryptedStr, Base64.DEFAULT)
            val result =
                iEncryptionAES.doDecrypt(encryptedByte = encryptedByte, cipherIV = cipherIV)
            return String(result.bytes)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return "dataHasDecrypted"
    }

    private fun convertBitmapToString(bitmap: Bitmap): String {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.WEBP, 50, stream)
        val byteArray = stream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    fun convertStringToBitmap(string: String?): Bitmap? {
        val byteArray1: ByteArray = Base64.decode(string, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(
            byteArray1, 0,
            byteArray1.size
        )
    }

    fun setPreference(sharedPreferences: SharedPreferences) {
        preferences = sharedPreferences
    }
}