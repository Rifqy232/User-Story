package com.mry.userstory.ui.addStory

import android.Manifest
import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.mry.userstory.data.CustomResult
import com.mry.userstory.databinding.ActivityAddStoryBinding
import com.mry.userstory.ui.home.HomeActivity
import com.mry.userstory.utils.ViewModelFactory
import com.mry.userstory.utils.createCustomTempFile
import com.mry.userstory.utils.reduceFileImage
import com.mry.userstory.utils.rotateFile
import com.mry.userstory.utils.uriToFile
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class AddStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddStoryBinding
    private var getFile: File? = null
    private val addStoryViewModel: AddStoryViewModel by viewModels {
        ViewModelFactory(this)
    }

    companion object {
        const val CAMERA_X_RESULT = 200

        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            Toast.makeText(
                this,
                "Can't get permissions",
                Toast.LENGTH_SHORT
            ).show()
            finish()
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }
        binding.btnCamerax.setOnClickListener { startCameraX() }
        binding.btnCamera.setOnClickListener { startTakePhoto() }
        binding.btnGallery.setOnClickListener { startGallery() }
        binding.btnUpload.setOnClickListener { uploadPhoto() }
    }

    private fun startCameraX() {
        val intent = Intent(this, CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERA_X_RESULT) {
            val myFile = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it.data?.getSerializableExtra("picture", File::class.java)
            } else {
                @Suppress("DEPRECATION")
                it.data?.getSerializableExtra("picture")
            } as? File

            val isBackCamera = it.data?.getBooleanExtra("isBackCamera", true) as Boolean

            myFile?.let { file ->
                rotateFile(file, isBackCamera)
                getFile = file
                binding.ivPreview.setImageBitmap(BitmapFactory.decodeFile(file.path))
            }
        }
    }

    private fun startTakePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(packageManager)

        createCustomTempFile(application).also {
            val photoURI: Uri = FileProvider.getUriForFile(
                this@AddStoryActivity,
                "com.mry.userstory",
                it
            )
            currentPhotoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            launcherIntentCamera.launch(intent)
        }
    }

    private lateinit var currentPhotoPath: String
    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val myFile = File(currentPhotoPath)

            myFile.let { file ->
                getFile = file
                binding.ivPreview.setImageBitmap(BitmapFactory.decodeFile(file.path))
            }
        }
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg = result.data?.data as Uri
            selectedImg.let { uri ->
                val myFile = uriToFile(uri, this@AddStoryActivity)
                getFile = myFile
                binding.ivPreview.setImageURI(uri)
            }
        }
    }

    private fun uploadPhoto() {
        if (getFile != null) {
            val file = reduceFileImage(getFile as File)

            val description =
                binding.etDescription.text.toString().toRequestBody("text/plain".toMediaType())
            val requestImageFile = file.asRequestBody("image/jpeg".toMediaType())
            val imageMultiPart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "photo",
                file.name,
                requestImageFile,
            )
            addStoryViewModel.uploadStory(imageMultiPart, description).observe(this) { result ->
                if (result != null) {
                    when (result) {
                        is CustomResult.Loading -> showLoading(true)
                        is CustomResult.Success -> {
                            showLoading(false)
                            Toast.makeText(
                                this,
                                result.data.message,
                                Toast.LENGTH_SHORT
                            ).show()
                            val intent = Intent(this, HomeActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                        }

                        is CustomResult.Error -> {
                            showLoading(false)
                            Toast.makeText(
                                this,
                                "Error happened: " + result.error,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        } else {
            Toast.makeText(this, "Please insert a picture first", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.apply {
                progressBar.visibility = View.VISIBLE
                ivPreview.visibility = View.GONE
                btnGallery.visibility = View.GONE
                btnCamerax.visibility = View.GONE
                btnUpload.visibility = View.GONE
                btnCamera.visibility = View.GONE
                etDescription.visibility = View.GONE
            }
        } else {
            binding.apply {
                progressBar.visibility = View.GONE
                ivPreview.visibility = View.VISIBLE
                btnGallery.visibility = View.VISIBLE
                btnUpload.visibility = View.VISIBLE
                btnCamerax.visibility = View.VISIBLE
                btnCamera.visibility = View.VISIBLE
                etDescription.visibility = View.VISIBLE
            }
        }
    }
}