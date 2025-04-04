package garcia.roberto.mypokedexrobertogarcia

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback

class RegisterPokemonActivity : AppCompatActivity() {

    private lateinit var etPokemonName: EditText
    private lateinit var etPokemonNumber: EditText
    private lateinit var btnSelectImage: Button
    private lateinit var btnSavePokemon: Button
    private lateinit var ivPokemonImage: ImageView
    private lateinit var tvMessage: TextView

    private val REQUEST_IMAGE_GET = 1
    private val CLOUD_NAME = "dzdna0wcu"
    private val UPLOAD_PRESET = "pokemon-upload"
    private var imageUri: Uri? = null

    private var imageUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register_pokemon)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        etPokemonName = findViewById(R.id.et_pokemon_name)
        etPokemonNumber = findViewById(R.id.et_pokemon_number)
        btnSelectImage = findViewById(R.id.btn_select_image)
        btnSavePokemon = findViewById(R.id.btn_save_pokemon)
        ivPokemonImage = findViewById(R.id.iv_pokemon_thumbnail)
        tvMessage = findViewById(R.id.tv_message)

        btnSelectImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            startActivityForResult(intent, REQUEST_IMAGE_GET)
        }

        btnSavePokemon.setOnClickListener {
            if (!validateFields()) {
                return@setOnClickListener
            }
            savePokemon()
        }
        initCloudinary()

    }

    private fun savePokemon() {
        if (imageUri == null) {
            Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show()
            return
        }
        MediaManager.get().upload(imageUri).unsigned(UPLOAD_PRESET)
            .callback(object : UploadCallback {
                override fun onStart(requestId: String?) {
                    Log.d("Cloudinary", "Upload started")
                    tvMessage.text = "Uploading pokemon..."
                }

                override fun onProgress(requestId: String?, bytes: Long, totalBytes: Long) {

                }

                override fun onSuccess(requestId: String?, resultData: Map<*, *>) {
                    Log.d("Cloudinary", "Upload successful: $resultData")
                    imageUrl = resultData["secure_url"] as String
                    persistPokemon()
                }

                override fun onError(
                    requestId: String?,
                    error: ErrorInfo?
                ) {
                    Log.e("Cloudinary", "Upload error: $error")
                    Toast.makeText(
                        this@RegisterPokemonActivity,
                        "Error uploading image: ${error?.description}",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onReschedule(
                    requestId: String?,
                    error: ErrorInfo?
                ) {
                    Log.e("Cloudinary", "Upload rescheduled: $error")
                }
            }).dispatch()
    }

    private fun persistPokemon() {
        val pokemon = Pokemon(
            name = etPokemonName.text.toString(),
            number = etPokemonNumber.text.toString().toInt(),
            imageUrl = imageUrl!!
        )

        PokemonDAO.savePokemon(pokemon) { error ->
            if (error == null) {
                Toast.makeText(this, "Pokemon: "+ pokemon.name + ", saved successfully", Toast.LENGTH_SHORT)
                    .show()
                finish()
            } else {
                Toast.makeText(this, "Error saving Pokemon: ${error.message}", Toast.LENGTH_SHORT)
                    .show()
            }
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_GET && resultCode == RESULT_OK) {
            val uri = data?.data

            if (uri != null) {
                changeImage(uri)
            } else {
                Toast.makeText(this, "Error selecting image", Toast.LENGTH_SHORT).show()
            }


        }
    }

    private fun changeImage(uri: Uri) {
        try {
            Glide.with(this)
                .load(uri)
                .placeholder(R.drawable.ic_launcher_foreground)
                .error(R.drawable.ic_launcher_foreground)
                .transform(RoundedCorners(16))
                .into(ivPokemonImage)
            imageUri = uri
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Error loading image", Toast.LENGTH_SHORT).show()
        }
    }

    private fun initCloudinary() {
        try {
            MediaManager.get()
            Log.d ("Cloudinary", "Cloudinary already initialized: " + MediaManager.get().toString())
        } catch (e: Exception) {
            val config = HashMap<String, String>()
            config["cloud_name"] = CLOUD_NAME
            MediaManager.init(this, config)
            Log.d("Cloudinary", "Cloudinary initialized")
        }
    }

    private fun validateFields(): Boolean {
        if (etPokemonName.text.isEmpty() || etPokemonName.text.isBlank()) {
            Toast.makeText(this, "Please enter a name", Toast.LENGTH_SHORT).show()
            return false
        }
        if (etPokemonNumber.text.isEmpty() || etPokemonNumber.text.isBlank()) {
            Toast.makeText(this, "Please enter a number", Toast.LENGTH_SHORT).show()
            return false
        }
        if (imageUri == null) {
            Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

}