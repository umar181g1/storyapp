package com.umar.storyapp.ui.story

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import com.umar.storyapp.R
import com.umar.storyapp.databinding.ActivityAddStoryBinding
import com.umar.storyapp.factoryviewmodel.StroyFactoryViewModel
import com.umar.storyapp.model.Result
import com.umar.storyapp.ui.login.LoginActivity
import com.umar.storyapp.ui.reduceFileImage
import com.umar.storyapp.ui.uriToFile
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class AddStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddStoryBinding
    private lateinit var viewModel: StoryViewModel
    private lateinit var token: String
    private lateinit var curetPahtPoto: String
    private var getFile: File? = null

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionGranted()) {
                Toast.makeText(this, "tidak ada permission", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun allPermissionGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!allPermissionGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        this.title = "add story"

        setupViewMOdel()
        binding.buttonCam.setOnClickListener { startTakePhoto() }
        binding.button.setOnClickListener { startGalary() }
        binding.btnUpload.setOnClickListener { upload() }


    }

    private fun startTakePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(packageManager)

        com.umar.storyapp.ui.createTempFile(application).also {
            val photoUri: Uri = FileProvider.getUriForFile(
                this@AddStoryActivity,
                "com.umar.storyapp.ui.stroy",
                it
            )
            curetPahtPoto = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
            launcherIntentCamera.launch(intent)
        }
    }

    private fun upload() {
        if (getFile != null) {
            val exdestText = binding.etDesc.text.toString()
            if (exdestText.isEmpty()) {
                binding.etDesc.error = resources.getString(R.string.input_des)

            } else {
                val deskripsi = exdestText.toRequestBody("text/plain".toMediaType())
                val file = reduceFileImage(getFile as File)
                val requstImage = file.asRequestBody("image/jpg".toMediaTypeOrNull())
                val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                    "photo",
                    file.name,
                    requstImage
                )
                viewModel.addStory(token, imageMultipart, deskripsi).observe(this) { result ->
                    if (result != null) {
                        when (result) {
                            is Result.Loading -> {
                                sholoading(true)
                            }
                            is Result.Success -> {
                                sholoading(false)
                                Toast.makeText(this, result.data.message, Toast.LENGTH_LONG).show()
                                finish()
                            }
                            is Result.Error -> {
                                sholoading(false)
                                Toast.makeText(this, "Error: ${result.error}", Toast.LENGTH_LONG)
                                    .show()
                            }
                        }
                    }
                }
            }
        } else {
            Toast.makeText(
                this@AddStoryActivity,
                resources.getString(R.string.no_image),
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun startGalary() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, resources.getString(R.string.choose_a_picture))
        launcherIntentGallery.launch(chooser)
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val selectedImg: Uri = it.data?.data as Uri

            val myFile = uriToFile(selectedImg, this)

            getFile = myFile

            binding.imageUp.setImageURI(selectedImg)
        }

    }
    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val myFile = File(curetPahtPoto)
            getFile = myFile

            val result = BitmapFactory.decodeFile(getFile?.path)
            binding.imageUp.setImageBitmap(result)
        }
    }

    private fun setupViewMOdel() {
        val factory: StroyFactoryViewModel = StroyFactoryViewModel.getInstance(this)
        viewModel = ViewModelProvider(this, factory)[StoryViewModel::class.java]

        viewModel.getToken().observe(this) { token ->
            if (token.isEmpty()) {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            } else {
                this.token = token
            }
        }
    }

    private fun sholoading(isLoading: Boolean) {
        binding.progressBarLayout.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}