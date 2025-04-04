package cordova.kathya.mypokedex_kathyacordova

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.widget.ListView
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() {

    private lateinit var pokemonListView: ListView
    private val pokemonList = mutableListOf<Pokemon>()
    private lateinit var adapter: PokemonAdapter
    private val pokemonRef = FirebaseDatabase.getInstance().getReference("Pokemon")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val registerPokemon : Button = findViewById(R.id.addPokemon)
        pokemonListView = findViewById(R.id.pokemonList) as ListView
        adapter = PokemonAdapter(this, pokemonList)
        pokemonListView.adapter = adapter

        registerPokemon.setOnClickListener {
            val intent: Intent = Intent(this, RegisterPokemon::class.java)
            startActivity(intent)
        }

        loadPokemonFromFirebase()
    }

    private fun loadPokemonFromFirebase() {
        pokemonRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                pokemonList.clear()
                for (data in snapshot.children) {
                    val pokemon = data.getValue(Pokemon::class.java)
                    if (pokemon != null) {
                        pokemonList.add(pokemon)
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Error al leer datos", error.toException())
            }
        })
    }
}
