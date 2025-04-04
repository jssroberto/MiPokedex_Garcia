package garcia.roberto.mypokedexrobertogarcia

import com.google.firebase.Firebase
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.database

object PokemonDAO {
    private val database = Firebase.database.reference
    private val pokemonRef = database.child("pokemons")

    fun savePokemon(pokemon: Pokemon, onComplete: (Exception?) -> Unit = {}) {
        val newPokemonRef = pokemonRef.push()
        val pokemonWithId = pokemon.copy(id = newPokemonRef.key ?: "")
        newPokemonRef.setValue(pokemonWithId)
            .addOnCompleteListener { task ->
                onComplete(task.exception)
            }
    }

    fun listenForPokemonChanges(onPokemonAdded: (Pokemon) -> Unit) {
        pokemonRef.addChildEventListener(object : SimpleChildEventListener() {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                snapshot.getValue(Pokemon::class.java)?.let { onPokemonAdded(it) }
            }
        })
    }

    fun getAllPokemon(onComplete: (List<Pokemon>) -> Unit) {
        pokemonRef.get()
            .addOnSuccessListener { snapshot ->
                val pokemonList = snapshot.children.mapNotNull { it.getValue(Pokemon::class.java) }
                onComplete(pokemonList)
            }
            .addOnFailureListener {
                onComplete(emptyList())
            }
    }

}

abstract class SimpleChildEventListener : ChildEventListener {
    override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
    override fun onChildRemoved(snapshot: DataSnapshot) {}
    override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
    override fun onCancelled(error: DatabaseError) {}
}