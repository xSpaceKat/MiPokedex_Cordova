package cordova.kathya.mypokedex_kathyacordova

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide

class PokemonAdapter(private val context: Context, private val pokemonList: List<Pokemon>) : BaseAdapter() {

    override fun getCount(): Int {
        return pokemonList.size
    }

    override fun getItem(position: Int): Any {
        return pokemonList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.pokemon_item, parent, false)

        val nameTextView: TextView = view.findViewById(R.id.pokemonName)
        val numberTextView: TextView = view.findViewById(R.id.pokemonNumber)
        val imageView: ImageView = view.findViewById(R.id.pokemonImage)

        val pokemon = pokemonList[position]

        nameTextView.text = pokemon.nombrePokemon
        numberTextView.text = "No. ${pokemon.numPokedex}"

        Glide.with(context)
            .load(pokemon.imgPokemon)
            .into(imageView)

        return view
    }
}