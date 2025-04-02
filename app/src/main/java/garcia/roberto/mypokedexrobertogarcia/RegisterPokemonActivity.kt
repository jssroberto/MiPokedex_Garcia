package garcia.roberto.mypokedexrobertogarcia

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class RegisterPokemonActivity : AppCompatActivity() {

    private lateinit var etPokemonName: EditText
    private lateinit var etPokemonNumber: EditText
    private lateinit var ivPokemonImage: ImageView
    private lateinit var btnSelectImage: Button
    private lateinit var btnSavePokemon: Button

    private val REQUEST_IMAGE_GET = 1
    private val CLOUD_NAME = "dzdna0wcu"
    private val UPLOAD_PRESET = "pokemon-upload"

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

        btnSelectImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            startActivityForResult(intent, REQUEST_IMAGE_GET)
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
        val imageView = ivPokemonImage
        try {
            imageView.setImageURI(uri)
        } catch (e: Exception) {
            Toast.makeText(this, "Error loading image", Toast.LENGTH_SHORT).show()
        }
    }

}