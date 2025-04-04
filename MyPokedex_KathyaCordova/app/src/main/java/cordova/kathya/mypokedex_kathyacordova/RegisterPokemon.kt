package cordova.kathya.mypokedex_kathyacordova

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback

class RegisterPokemon : AppCompatActivity() {

    val REQUEST_IMAGE_GET = 1
    val CLOUD_NAME = "dspqrquds"
    val UPLOAD_PRESET = "pokemon-upload"
    var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register_pokemon)

        val name: EditText = findViewById(R.id.pokemonName)
        val pNumber: EditText = findViewById(R.id.pokemonNumber)
        val select: Button = findViewById(R.id.selectImage)
        val save: Button = findViewById(R.id.savePokemon)

        select.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            startActivityForResult(intent, REQUEST_IMAGE_GET)
        }

        save.setOnClickListener {
            savePokemon { url ->
                if (url.isNotEmpty()) {
                    Log.d("MiApp", "Imagen subida con éxito: $url")
                } else {
                    Log.d("MiApp", "Error al subir la imagen")
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_GET && resultCode == Activity.RESULT_OK) {
            val fullImageUri: Uri? = data?.data

            if (fullImageUri != null) {
                changeImage(fullImageUri)
                imageUri = fullImageUri
            }
        }
    }

    private fun initCloudinary() {
        val config: MutableMap<String, String> = HashMap()
        config["cloud_name"] = CLOUD_NAME
        MediaManager.init(this, config)
    }

    fun savePokemon(callback: (String) -> Unit) {
        if (imageUri != null) {
            MediaManager.get().upload(imageUri)
                .unsigned(UPLOAD_PRESET)
                .callback(object : UploadCallback {
                    override fun onStart(requestId: String) {
                        Log.d("Cloudinary Quickstart", "Upload start")
                    }

                    override fun onProgress(requestId: String, bytes: Long, totalBytes: Long) {
                        Log.d("Cloudinary Quickstart", "Upload progress")
                    }

                    override fun onSuccess(requestId: String, resultData: Map<*, *>) {
                        val url = resultData["secure_url"] as? String ?: ""
                        Log.d("Cloudinary Quickstart", "Upload success: $url")
                        callback(url)  // Llamamos al callback con la URL
                    }

                    override fun onError(requestId: String, error: ErrorInfo) {
                        Log.d("Cloudinary Quickstart", "Upload failed: ${error.description}")
                        callback("") // En caso de error, pasamos un string vacío
                    }

                    override fun onReschedule(requestId: String, error: ErrorInfo) {
                        Log.d("Cloudinary Quickstart", "Upload rescheduled")
                    }
                }).dispatch()
        } else {
            callback("")
        }
    }

    fun changeImage(uri: Uri) {
        val thumbnail: ImageView = findViewById(R.id.thumbnail)
        try {
            thumbnail.setImageURI(uri)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
