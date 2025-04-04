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
    private lateinit var pokemonAdapter: PokemonAdapter
    private val pokemonList = mutableListOf<Pokemon>()

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

        pokemonAdapter = PokemonAdapter(this, pokemonList)
        lvPokemon.adapter = pokemonAdapter

        setupPokemonUpdates()

    }

    private fun navigateRegisterPokemon() {
        val intent = Intent(this, RegisterPokemonActivity::class.java)
        startActivity(intent)
    }

    private fun setupPokemonUpdates() {
        PokemonDAO.listenForPokemonChanges { newPokemon ->
            val existingIndex = pokemonList.indexOfFirst { it.id == newPokemon.id }
            if (existingIndex >= 0) {
                pokemonList[existingIndex] = newPokemon
            } else {
                pokemonList.add(newPokemon)
            }
            pokemonAdapter.updatePokemons(pokemonList) // Updates the entire list (BaseAdapter doesn't support granular updates)
        }

        // Initial load
        PokemonDAO.getAllPokemon { pokemons ->
            pokemonList.clear()
            pokemonList.addAll(pokemons)
            pokemonAdapter.updatePokemons(pokemonList)
        }
    }

}