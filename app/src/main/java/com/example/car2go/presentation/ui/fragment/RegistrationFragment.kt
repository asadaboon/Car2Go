package com.example.car2go.presentation.ui.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.car2go.R
import com.example.car2go.common.ui.DatePickerUtils
import com.example.car2go.common.validate.isEmailValid
import com.example.car2go.common.validate.isPhoneValid
import com.example.car2go.databinding.FragmentRegistrationBinding
import com.example.car2go.domain.model.RegistrationDataClass
import com.example.car2go.presentation.ui.activity.MainActivity
import com.example.car2go.presentation.viewmodel.RegistrationViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class RegistrationFragment : Fragment(), IdentityVerificationListener {

    companion object {
        const val TAG = "RegistrationFragment"
        fun newInstance() = RegistrationFragment().apply {
            arguments = Bundle().apply {}
        }
    }

    private lateinit var binding: FragmentRegistrationBinding
    private val viewModel: RegistrationViewModel by viewModels()
    private lateinit var datePicker: DatePickerUtils
    private var data: RegistrationDataClass? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.e("ASADA", "RegistrationFragment onAttach: ")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {}
        Log.e("ASADA", "RegistrationFragment onCreate: ")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        Log.e("ASADA", "RegistrationFragment onCreateView: ")
        binding = FragmentRegistrationBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpView()
        setUpObserve()
        Log.e("ASADA", "RegistrationFragment onViewCreated: ")
        context?.let {
            datePicker = DatePickerUtils(context = it, isSpinnerType = true)
        }
    }

    override fun onStart() {
        super.onStart()
        Log.e("ASADA", "RegistrationFragment onStart: ")
    }

    override fun onResume() {
        super.onResume()
        Log.e("ASADA", "RegistrationFragment onResume: ")
    }

    override fun onPause() {
        super.onPause()
        Log.e("ASADA", "RegistrationFragment onPause: ")
    }

    override fun onStop() {
        super.onStop()
        Log.e("ASADA", "RegistrationFragment onStop: ")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.e("ASADA", "RegistrationFragment onDestroyView: ")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e("ASADA", "RegistrationFragment onDestroy: ")
    }

    override fun onDetach() {
        super.onDetach()
        Log.e("ASADA", "RegistrationFragment onDetach: ")
    }

    private fun setUpObserve() {
        viewModel.registrationLiveData.observe(viewLifecycleOwner) { registration ->
            data = registration
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setUpView() {
        binding.apply {
            titleRegisterTextView.text =
                context?.resources?.getString(R.string.regis_title).plus(" 1/1")
            dateOfBirthTextInputEditText.setOnTouchListener { _, event ->
                context?.hideKeyboard(dateOfBirthTextInputEditText)
                if (MotionEvent.ACTION_UP == event.action) showDatePicker()
                false
            }

            registrationButton.setOnClickListener {
                /*        if (validateForm()) {
                            viewModel.prepareRegistrationData(
                                citizenId = citizenIDTextField.editText?.text.toString(),
                                firstNameTH = firstNameTHTextField.editText?.text.toString(),
                                lastNameTH = lastNameTHTextField.editText?.text.toString(),
                                firstNameEN = firstNameENTextField.editText?.text.toString(),
                                lastNameEN = lastNameEBTextField.editText?.text.toString(),
                                dateOfBirth = dateOfBirthTextField.editText?.text.toString(),
                                email = eMailTextField.editText?.text.toString(),
                                phone = phoneTextField.editText?.text.toString(),
                                profileImage = data?.profileImage,
                                profileCarImage = data?.profileCarImage
                            )
                            gotoIdentityVerificationFragment()
                        }*/
                gotoIdentityVerificationFragment()
            }
        }
    }

    private fun showDatePicker() {
        datePicker.showDialog(object : DatePickerUtils.Callback {
            override fun onDateSelected(
                dayOfMonth: Int,
                month: Int,
                year: Int,
                dateSelectedFormat: String
            ) {
                binding.apply {
                    dateOfBirthTextInputEditText.setText(dateSelectedFormat)
                }
            }
        })
    }

    private fun validateForm(): Boolean {
        var isValidForm = true
        binding.apply {
            citizenIDTextField.run {
                if (editText?.text?.isEmpty() == true) {
                    this.error =
                        resources.getString(R.string.validate_citizen_identity_number)
                    isValidForm = false
                } else if ((editText?.text?.length ?: 0) < 13) {
                    this.error =
                        resources.getString(R.string.validate_citizen_identity_number_error)
                    isValidForm = false
                } else {
                    this.isErrorEnabled = false
                }
            }

            firstNameTHTextField.run {
                if (editText?.text?.isEmpty() == true) {
                    this.error =
                        resources.getString(R.string.validate_firstname_th)
                    isValidForm = false
                } else {
                    this.isErrorEnabled = false
                }
            }

            lastNameTHTextField.run {
                if (editText?.text?.isEmpty() == true) {
                    this.error =
                        resources.getString(R.string.validate_lastname_th)
                    isValidForm = false
                } else {
                    this.isErrorEnabled = false
                }
            }

            firstNameENTextField.run {
                if (editText?.text?.isEmpty() == true) {
                    this.error =
                        resources.getString(R.string.validate_firstname_en)
                    isValidForm = false
                } else {
                    this.isErrorEnabled = false
                }
            }

            lastNameEBTextField.run {
                if (editText?.text?.isEmpty() == true) {
                    this.error =
                        resources.getString(R.string.validate_lastname_en)
                    isValidForm = false
                } else {
                    this.isErrorEnabled = false
                }
            }

            dateOfBirthTextField.run {
                if (editText?.text?.isEmpty() == true) {
                    this.error =
                        resources.getString(R.string.validate_date_of_birth)
                    isValidForm = false
                } else {
                    this.isErrorEnabled = false
                }
            }

            phoneTextField.run {
                if (editText?.text?.isEmpty() == true) {
                    this.error =
                        resources.getString(R.string.validate_phone_number)
                    isValidForm = false
                } else if (!this.editText?.isPhoneValid()!! ?: false) {
                    this.error =
                        resources.getString(R.string.validate_phone_number_error)
                    isValidForm = false
                } else {
                    this.isErrorEnabled = false
                }
            }

            eMailTextField.run {
                if (editText?.text?.isEmpty() == true) {
                    this.error =
                        resources.getString(R.string.validate_email)
                    isValidForm = false
                } else if (!this.editText?.isEmailValid()!! ?: false) {
                    this.error =
                        resources.getString(R.string.validate_email_error)
                    isValidForm = false
                } else {
                    this.isErrorEnabled = false
                }
            }
        }
        return isValidForm
    }

    private fun gotoIdentityVerificationFragment() {

//        MainActivity().replaceFragment(IdentityVerificationFragment.newInstance(data))
        activity?.let {
            try {

                childFragmentManager.beginTransaction().add(
                    R.id.multipleFragmentContainer,
                    IdentityVerificationFragment.newInstance(data)
                ).addToBackStack(IdentityVerificationFragment.TAG).commit()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun Context.hideKeyboard(view: View) {
        val inputMethodManager =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    override fun updateData(receiverData: RegistrationDataClass) {
        this.data = receiverData
    }
}