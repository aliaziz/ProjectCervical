package cure.medicine.find.cervical.Views

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import android.widget.Toast
import cure.medicine.find.cervical.R
import cure.medicine.find.cervical.Utils.KEYS
import cure.medicine.find.cervical.ViewModels.PredictionViewModel
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_home.*
import java.io.File
import java.io.IOException

class Home : AppCompatActivity() {

    private var imageAbsolutePath: String = ""
    private val predictionViewModel = PredictionViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        if (!validateCameraPresence()) finish()

        registerButtonListener()
    }

    /**
     * Validates if user device has camera hardware.
     */
    private fun validateCameraPresence(): Boolean
            = packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)

    /**
     * Launches the camera
     */
    private fun launchCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (cameraIntent.resolveActivity(packageManager) != null) {
            var photoFile: File? = null
            try {
              photoFile = imageStorageLocation()
            } catch (error: IOException) {
                toast("Failed to create photo file with error ${error.localizedMessage}")
            }

            photoFile?.let {
                val uri = FileProvider.getUriForFile(this,
                        getString(R.string.authority),
                        it)
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
            }
            startActivityForResult(cameraIntent, KEYS.cameraId)
        }
    }

    /**
     * Register listeners
     */
    private fun registerButtonListener() {
        scanImage.setOnClickListener {
            launchCamera()
        }
    }

    /**
     * Creates and returns image file name
     *
     * @return location [String]
     */
    private fun imageStorageLocation(): File {
        val file = File.createTempFile(
                "ProjectCVFD_${System.currentTimeMillis()}",
                ".jpg",
                getExternalFilesDir(Environment.DIRECTORY_PICTURES))
        file?.let {
            imageAbsolutePath = it.absolutePath
            it.createNewFile()
        }
        return file!!
    }

    /**
     * Toasts given message
     *
     * @param message [String]
     */
    private fun toast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == KEYS.cameraId && resultCode == Activity.RESULT_OK) {
            predictableImage.setImageURI(Uri.parse(imageAbsolutePath))
                predictionViewModel.predict(imageAbsolutePath).observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                    it?.let { resultsText.text = it }
                },{
                            print(it)
                        })
        } else {
            toast("Failed to take pic")
        }
    }
}
