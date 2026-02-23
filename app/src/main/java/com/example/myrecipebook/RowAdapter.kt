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

//  using glide for the picture urls to show up
import com.bumptech.glide.Glide

class RowAdapter (
    context: Context,
    private val recipes: List<MyRecipe>,

) : ArrayAdapter<MyRecipe>(context, 0, recipes) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.recipe_rows, parent, false)

        val imageview = view.findViewById<ImageView>(R.id.imageview)
        val textviewRecipe = view.findViewById<TextView>(R.id.textviewRecipe)
        // val buttonFavorite = view.findViewById<Button>(R.id.buttonFavorite)


        val recipe = recipes[position]

        // replace the textview with our data

        textviewRecipe.text = recipe.RecipeTitle

        /*

        val imgFile = File("RecipeImage")
        val myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath())
        imageview.setImageBitmap(myBitmap)

         */

        Glide.with(context)
            .load(recipe.RecipeImage)
            .into(imageview)

        return view

    }
}