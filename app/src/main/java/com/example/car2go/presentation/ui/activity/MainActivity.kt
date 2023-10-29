package com.example.car2go.presentation.ui.activity

import android.Manifest
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.car2go.R
import com.example.car2go.databinding.ActivityMainBinding
import com.example.car2go.presentation.ui.fragment.RegistrationFragment
import com.example.car2go.presentation.viewmodel.RegistrationViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    companion object {
        const val REQUEST_WRITE_PERMISSION = 111
    }

    private lateinit var binding: ActivityMainBinding
    private val viewModel: RegistrationViewModel by viewModels()
    private lateinit var preferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
/*
        preferences = getSharedPreferences("androidEncryption", MODE_PRIVATE)
        viewModel.getUserInfoById("")
        getPermission()
        setObserve()*/
        Log.e("ASADA", "MainActivity  onCreate: ")

        binding.clickButton.setOnClickListener {
            /*         val intent = Intent(this, SummaryActivity::class.java)
                     startActivity(intent)
                     finish()*/
//            if (savedInstanceState != null) {
            replaceFragment( RegistrationFragment.newInstance())

        }
    }

    override fun onStart() {
        super.onStart()
        Log.e("ASADA", "MainActivity  onStart: ")
    }

    override fun onResume() {
        super.onResume()
        Log.e("ASADA", "MainActivity  onResume: ")
    }

    override fun onPause() {
        super.onPause()
        Log.e("ASADA", "MainActivity  onPause: ")
    }

    override fun onStop() {
        super.onStop()
        Log.e("ASADA", "MainActivity  onStop: ")
    }

    override fun onRestart() {
        super.onRestart()
        Log.e("ASADA", "MainActivity  onRestart: ")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e("ASADA", "MainActivity  onDestroy: ")
    }

    private fun getPermission() {
        val permissionIsGranted = viewModel.permissionIsGranted(this)
        if (permissionIsGranted) {
            Toast.makeText(this, "permissionIsGranted", Toast.LENGTH_SHORT).show()
        } else {
            viewModel.getRequestPermission(requestPermission = true)
        }
    }

    private fun setObserve() {
        viewModel.isRequestPermission.observe(this) { isRequestPermission ->
            if (isRequestPermission) {
                requestPermissionPermission()
            }
        }

        viewModel.alreadyRegisLiveData.observe(this) { isAlreadyRegister ->

            /* if (isAlreadyRegister != null) {
                 val intent = Intent(this, SummaryActivity::class.java)
                 startActivity(intent)
                 finish()
             } else {
                 supportFragmentManager
                     .beginTransaction()
                     .add(R.id.mainContainer, RegistrationFragment.newInstance())
                     .addToBackStack(RegistrationFragment.TAG)
                     .commit()
             }*/
        }
    }

    override fun onBackPressed() {
        val isFragmentPopped = handleNestedFragmentBackStack(supportFragmentManager)
        if (!isFragmentPopped) {
            super.onBackPressed()
        }
    }

    private fun handleNestedFragmentBackStack(fragmentManager: FragmentManager): Boolean {
        val childFragmentList = fragmentManager.fragments
        if (childFragmentList.size > 0) {
            for (index in childFragmentList.size - 1 downTo 0) {
                val fragment = childFragmentList[index]
                val isPopped = handleNestedFragmentBackStack(fragment.childFragmentManager)
                return when {
                    isPopped -> true
                    fragmentManager.backStackEntryCount > 0 -> {
                        fragmentManager.popBackStack()
                        true
                    }
                    else -> false
                }
            }
        }
        return false
    }

    private fun requestPermissionPermission() {
        requestPermissions(
            arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            ),
            REQUEST_WRITE_PERMISSION
        )
    }

    fun replaceFragment(newInstance: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .add(R.id.mainContainer, newInstance)
            .commit()
    }
}