package com.example.car2go.presentation.ui.fragment

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.car2go.R
import com.example.car2go.common.cryptographic.IEncryption
import com.example.car2go.common.cryptographic.builder.EncryptionBuilder
import com.example.car2go.common.cryptographic.core.enums.Alias
import com.example.car2go.common.cryptographic.core.enums.CipherAlgorithm
import com.example.car2go.common.cryptographic.core.enums.EncryptionMode
import com.example.car2go.common.cryptographic.core.enums.EncryptionPadding
import com.example.car2go.common.ui.replaceMobilePhonePattern
import com.example.car2go.databinding.FragmentSummaryBinding
import com.example.car2go.domain.model.RegistrationDataClass
import com.example.car2go.presentation.ui.activity.SummaryActivity
import com.example.car2go.presentation.ui.fragment.IdentityVerificationFragment.Companion.REGISTRATION_ARG
import com.example.car2go.presentation.viewmodel.RegistrationViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SummaryFragment : Fragment() {

    companion object {
        const val TAG = "SummaryFragment"

        fun newInstance(registration: RegistrationDataClass?): SummaryFragment {
            val fragment = SummaryFragment()
            fragment.arguments = Bundle().apply {
                putParcelable(REGISTRATION_ARG, registration)
            }
            return fragment
        }
    }

    private lateinit var binding: FragmentSummaryBinding
    private val viewModel: RegistrationViewModel by viewModels()
    private var receiverData: RegistrationDataClass? = null
    private lateinit var preferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            receiverData = it.getParcelable(REGISTRATION_ARG)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSummaryBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        preferences =
            activity?.getSharedPreferences("androidEncryption", AppCompatActivity.MODE_PRIVATE)!!
        viewModel.setPreference(preferences)
        setUpObserve()
        setUpView()
    }

    private fun setUpObserve() {
        viewModel.registrationLiveData.observe(viewLifecycleOwner) { registration ->
            binding.apply {
                registration.profileImage?.let { image ->
                    val stringToBitmap = viewModel.convertStringToBitmap(image)
                    profileRegisterImageView.setImageBitmap(stringToBitmap)
                }

                registration.profileCarImage?.let { image ->
                    val stringToBitmap = viewModel.convertStringToBitmap(image)
                    profileCarRegisterImageView.setImageBitmap(stringToBitmap)
                }

                profileLastnameTHTextView.text = registration.firstNameTH
                profileLastnameENTextView.text = registration.lastNameTH
                profileDateOfBirthTextView.text = registration.dateOfBirth
                profileCitizenIdentityNumberTextView.text = registration.citizenId
                profilePhoneTextView.text = registration.phone?.replaceMobilePhonePattern()
                profileEmailTextView.text = registration.email
            }
        }

        viewModel.regisSuccess.observe(viewLifecycleOwner) {
            Toast.makeText(
                context,
                context?.resources?.getString(R.string.regis_success),
                Toast.LENGTH_SHORT
            ).show()
            startSummaryActivity()
        }
    }

    private fun setUpView() {
        binding.apply {
            titleRegisterTextView.text =
                context?.resources?.getString(R.string.regis_title).plus(" 3/3")
            viewModel.setRegistrationData(receiverData)
            confirmRegistrationButton.setOnClickListener {
                viewModel.submitRegister()
            }
        }
    }

    private fun startSummaryActivity() {
        activity?.let {
            val intent = Intent(it, SummaryActivity::class.java)
            it.startActivity(intent)
            it.finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        receiverData = null
    }
}