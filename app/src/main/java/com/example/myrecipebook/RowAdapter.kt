package com.example.myrecipebook

import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import java.io.File

//  using glide for the image urls to actually show up as images
import com.bumptech.glide.Glide

class RowAdapter (
    context: Context,

    // creates a list of the recipes

    private val recipes: List<MyRecipe>,

    // here i am making an adapter to change how my list looks like.

) : ArrayAdapter<MyRecipe>(context, 0, recipes) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.recipe_rows, parent, false)

        val imageview = view.findViewById<ImageView>(R.id.imageview)
        val textviewRecipe = view.findViewById<TextView>(R.id.textviewRecipe)

        // the position tag basically gets the ID for that recipe

        val recipe = recipes[position]

        // replaces the text view element with the title of the recipe

        textviewRecipe.text = recipe.RecipeTitle

        /*

        val imgFile = File("RecipeImage")
        val myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath())
        imageview.setImageBitmap(myBitmap)

         */

        // takes th URL path from the recipe and loads the URL into the image view

        Glide.with(context)
            .load(recipe.RecipeImage)
            .into(imageview)

        return view

    }
}