package garcia.roberto.mypokedexrobertogarcia

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide

class PokemonAdapter(
    private val context: Context,
    private val pokemons: List<Pokemon>
) : BaseAdapter() {

    private val inflater = LayoutInflater.from(context)

    override fun getCount(): Int {
        return pokemons.size
    }

    override fun getItem(position: Int): Any? {
        return if (position in 0 until pokemons.size) {
            pokemons[position]
        } else {
            null
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(
        position: Int,
        convertView: View?,
        parent: ViewGroup?
    ): View? {

        val view: View
        val viewHolder: ViewHolder

        if (convertView == null) {
            view = inflater.inflate(R.layout.pokemon_layout, parent, false)
            viewHolder = ViewHolder()
            viewHolder.image = view.findViewById(R.id.iv_pokemon_image_layout)
            viewHolder.name = view.findViewById(R.id.tv_pokemon_name_layout)
            viewHolder.number = view.findViewById(R.id.tv_pokemon_number_layout)
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ViewHolder
        }

        val currentPokemon = pokemons[position]

        viewHolder.name.text = currentPokemon.name
        viewHolder.number.text = currentPokemon.number.toString()

        Glide.with(context)
            .load(currentPokemon.imageUrl)
            .into(viewHolder.image)

        return view

    }


    private class ViewHolder {
        lateinit var image: ImageView
        lateinit var name: TextView
        lateinit var number: TextView
    }
}