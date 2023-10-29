package com.example.car2go.presentation.ui.activity

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.car2go.R
import com.example.car2go.common.ui.byteArrayToBitmap
import com.example.car2go.common.ui.replaceMobilePhonePattern
import com.example.car2go.databinding.ActivitySummaryBinding
import com.example.car2go.presentation.viewmodel.RegistrationViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SummaryActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySummaryBinding
    private val viewModel: RegistrationViewModel by viewModels()
    private lateinit var preferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySummaryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preferences =
            getSharedPreferences("androidEncryption", MODE_PRIVATE)!!
        viewModel.setPreference(preferences)
        viewModel.getUserInfoById("")
        setUpObserve()
        Log.e("ASADA", "SummaryActivity onCreate: ")
    }

    override fun onStart() {
        super.onStart()
        Log.e("ASADA", "SummaryActivity onStart: ")
    }

    override fun onResume() {
        super.onResume()
        Log.e("ASADA", "SummaryActivity  onResume: ")
    }

    override fun onPause() {
        super.onPause()
        Log.e("ASADA", "SummaryActivity  onPause: ")
    }

    override fun onStop() {
        super.onStop()
        Log.e("ASADA", "SummaryActivity  onStop: ")
    }

    override fun onRestart() {
        super.onRestart()
        Log.e("ASADA", "SummaryActivity  onRestart: ")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e("ASADA", "SummaryActivity  onDestroy: ")
    }

    private fun setUpObserve() {
        viewModel.alreadyRegisLiveData.observe(this) { result ->
            binding.apply {
                profileLastnameTHTextView.text = result?.firstNameTH
                profileLastnameENTextView.text = result?.lastNameTH
                profileDateOfBirthTextView.text = result?.dateOfBirth
                profileCitizenIdentityNumberTextView.text = result?.citizenId
                profilePhoneTextView.text = result?.phone?.replaceMobilePhonePattern()
                profileEmailTextView.text = result?.email

                result?.profileImage?.let { image ->
                    val deCryptResult = viewModel.decryptAES(image)
                    val stringToBitmap = viewModel.convertStringToBitmap(deCryptResult)
                    Glide.with(this@SummaryActivity)
                        .load(stringToBitmap)
                        .placeholder(R.mipmap.ic_icon_profile)
                        .error(R.mipmap.ic_icon_profile)
                        .into(profileRegisterImageView)
                }

                result?.profileCarImage?.let { image ->
                    val deCryptResult = viewModel.decryptAES(image)
                    val stringToBitmap = viewModel.convertStringToBitmap(deCryptResult)
                    Glide.with(this@SummaryActivity)
                        .load(stringToBitmap)
                        .placeholder(R.mipmap.ic_icon_place_holder)
                        .error(R.mipmap.ic_icon_place_holder)
                        .into(profileCarRegisterImageView)
                }
            }
        }
    }
}