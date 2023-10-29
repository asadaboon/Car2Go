package com.example.car2go.presentation.ui.fragment

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.car2go.R
import com.example.car2go.databinding.FragmentIdentityVerificationBinding
import com.example.car2go.domain.model.RegistrationDataClass
import com.example.car2go.presentation.viewmodel.RegistrationViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class IdentityVerificationFragment : Fragment() {

    companion object {
        const val TAG = "IdentityVerificationFragment"
        const val REQUEST_CODE_PROFILE = 200
        const val REQUEST_CODE = 201
        const val REGISTRATION_ARG = "ARG_PARAM1"

        fun newInstance(registration: RegistrationDataClass?): IdentityVerificationFragment {
            val fragment = IdentityVerificationFragment()
            fragment.arguments = Bundle().apply {
                putParcelable(REGISTRATION_ARG, registration)
            }
            return fragment
        }
    }

    lateinit var binding: FragmentIdentityVerificationBinding
    private val viewModel: RegistrationViewModel by viewModels()
    private var receiverData: RegistrationDataClass? = null
    private var identityVerificationListener: IdentityVerificationListener? = null
    private var hasProfileImage = false
    private var hasProfileCarImage = false

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.e("ASADA", "IdentityVerificationFragment  onAttach: ")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            receiverData = it.getParcelable(REGISTRATION_ARG)
        }
        Log.e("ASADA", "IdentityVerificationFragment  onCreate: ")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.e("ASADA", "IdentityVerificationFragment  onCreateView: ")
        binding = FragmentIdentityVerificationBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initListener()
        receiverData?.let {
            viewModel.setRegistrationData(receiverData)
        }
        Log.e("ASADA", "IdentityVerificationFragment  onViewCreated: ")
        setUpView()
        setUpObserve()
    }

    override fun onStart() {
        super.onStart()
        Log.e("ASADA", "IdentityVerificationFragment  onStart: ")
    }

    override fun onResume() {
        super.onResume()
        Log.e("ASADA", "IdentityVerificationFragment  onResume: ")
    }

    override fun onPause() {
        super.onPause()
        Log.e("ASADA", "IdentityVerificationFragment  onPause: ")
    }

    override fun onStop() {
        super.onStop()
        Log.e("ASADA", "IdentityVerificationFragment  onStop: ")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.e("ASADA", "IdentityVerificationFragment  onDestroyView: ")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e("ASADA", "IdentityVerificationFragment  onDestroy: ")
    }

    override fun onDetach() {
        super.onDetach()
        Log.e("ASADA", "IdentityVerificationFragment  onDetach: ")
    }

    private fun initListener() {
        try {
            parentFragment?.let {
                identityVerificationListener =
                    it as IdentityVerificationListener
            } ?: run {
                identityVerificationListener =
                    activity as IdentityVerificationListener
            }
        } catch (e: Exception) {
            //do nothing.
        }
    }

    private fun setUpView() {
        binding.apply {
            titleRegisterTextView.text =
                context?.resources?.getString(R.string.regis_title).plus(" 2/2")

            profileRegisterImageView.setOnClickListener {
                val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(cameraIntent, REQUEST_CODE_PROFILE)
            }

            profileCarRegisterImageView.setOnClickListener {
                val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(cameraIntent, REQUEST_CODE)
            }

            nextButton.setOnClickListener {
                if (validateImage()) {
                    activity?.let {
                        try {
                            childFragmentManager.beginTransaction()
                                .add(
                                    R.id.multipleFragmentContainer,
                                    SummaryFragment.newInstance(receiverData)
                                ).addToBackStack(SummaryFragment.TAG)
                                .commit()
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                } else {
                    Toast.makeText(
                        context,
                        context?.resources?.getString(R.string.validate_image_number_error),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun setUpObserve() {
        viewModel.registrationLiveData.observe(viewLifecycleOwner) { registration ->
            identityVerificationListener?.updateData(registration)
            binding.apply {
                registration.profileImage?.let { image ->
                    hasProfileImage = true
                    val stringToBitmap = viewModel.convertStringToBitmap(image)
                    context?.let {
                        Glide.with(it)
                            .load(stringToBitmap)
                            .placeholder(R.mipmap.ic_icon_profile)
                            .error(R.mipmap.ic_icon_profile)
                            .into(profileRegisterImageView)
                    }
                }

                registration.profileCarImage?.let { image ->
                    hasProfileCarImage = true
                    val stringToBitmap =
                        viewModel.convertStringToBitmap(image)
                    profileCarRegisterImageView.setImageBitmap(stringToBitmap)
                    context?.let {
                        Glide.with(it)
                            .load(stringToBitmap)
                            .placeholder(R.mipmap.ic_icon_profile)
                            .error(R.mipmap.ic_icon_profile)
                            .into(profileCarRegisterImageView)
                    }
                }
                hasProfileCarImage = true
            }
        }
    }

    private fun validateImage() = hasProfileImage && hasProfileCarImage

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_PROFILE) {
                val imageBitmap = data?.extras?.get("data") as Bitmap
                viewModel.updateImage(
                    profileImage = imageBitmap
                )
            } else if (requestCode == REQUEST_CODE) {
                val imageBitmap = data?.extras?.get("data") as Bitmap
                viewModel.updateImage(
                    profileCarImage = imageBitmap
                )
            }
        }
    }
}

interface IdentityVerificationListener {
    fun updateData(receiverData: RegistrationDataClass)
}