package garcia.roberto.mypokedexrobertogarcia

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    private lateinit var btnAddPokemon: Button
    private lateinit var lvPokemon: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        btnAddPokemon = findViewById(R.id.btn_add_pokemon)
        lvPokemon = findViewById(R.id.lv_pokemon)

        btnAddPokemon.setOnClickListener {
            navigateRegisterPokemon()
        }
        setupListView()

    }

    private fun navigateRegisterPokemon() {
        val intent = Intent(this, RegisterPokemonActivity::class.java)
        startActivity(intent)
    }

    private fun setupListView() {
        // Setup the ListView with an adapter to display Pokémon
        val pokemonList = mutableListOf<Pokemon>()
        val adapter = PokemonAdapter(this, pokemonList)
        lvPokemon.adapter = adapter

        // Load Pokémon from the database
        PokemonDAO.getAllPokemon { pokemons ->
            pokemonList.clear()
            pokemonList.addAll(pokemons)
            adapter.notifyDataSetChanged()
        }
    }
}