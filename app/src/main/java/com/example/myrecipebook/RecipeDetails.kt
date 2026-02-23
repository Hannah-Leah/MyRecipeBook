package com.example.myrecipebook

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.io.File
import com.bumptech.glide.Glide


class RecipeDetails : AppCompatActivity() {

    private lateinit var db: MyDatabase
    private var recipeID: Int = -1

    private var isFavorite = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_recipe_details)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        db = MyDatabase(this)

        // get the elements

        val textviewTitle = findViewById<TextView>(R.id.textviewTitle)
        val imageviewRecipe = findViewById<ImageView>(R.id.imageviewRecipe)
        val textviewCategory = findViewById<TextView>(R.id.textviewCategory)
        val textviewIngredients = findViewById<TextView>(R.id.textviewIngredients)
        val textviewCookingTime = findViewById<TextView>(R.id.textviewCookingTime)
        val textviewDescription = findViewById<TextView>(R.id.textviewDescription)
        val favoriteButton = findViewById<ImageButton>(R.id.buttonFavorite)

        isFavorite = intent.getBooleanExtra("RecipeFavorite", false)
        updateFavoriteIcon(favoriteButton)

        val editButton = findViewById<Button>(R.id.editButton)
        val returnButton = findViewById<Button>(R.id.returnButton)

        // replace the elements with the data of that recipe
        recipeID = intent.getIntExtra("RecipeID", -1)
        textviewTitle.text = intent.getStringExtra("RecipeTitle")
        textviewCookingTime.text = intent.getIntExtra("RecipeCookTime", 0).toString() + " minutes"
        textviewIngredients.text = intent.getStringExtra("RecipeIngredients")
        textviewCategory.text = intent.getStringExtra("RecipeCategory")
        textviewDescription.text = intent.getStringExtra("RecipeDescription")

        favoriteButton.setOnClickListener {
            isFavorite = !isFavorite
            db.updateFavorite(recipeID, isFavorite)
            updateFavoriteIcon(favoriteButton)
        }

        editButton.setOnClickListener {
            val editIntent = Intent(this, AddEditRecipe::class.java)

            editIntent.putExtra("editMode", true)
            editIntent.putExtra("RecipeID", intent.getIntExtra("RecipeID", -1))
            editIntent.putExtra("RecipeTitle", intent.getStringExtra("RecipeTitle"))
            editIntent.putExtra("RecipeImage", intent.getStringExtra("RecipeImage"))
            editIntent.putExtra("RecipeIngredients", intent.getStringExtra("RecipeIngredients"))
            editIntent.putExtra("RecipeCookTime", intent.getIntExtra("RecipeCookTime", 0))
            editIntent.putExtra("RecipeCategory", intent.getStringExtra("RecipeCategory"))
            editIntent.putExtra("RecipeDescription", intent.getStringExtra("RecipeDescription"))

            startActivityForResult(editIntent, 1)

        }



        /*
        val imgFile = File("RecipeImage")
        val myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath())
        imageviewRecipe.setImageBitmap(myBitmap)

         */

        val imageUrl = intent.getStringExtra("RecipeImage")

        Glide.with(this)
            .load(imageUrl)
            .into(imageviewRecipe)

        returnButton.setOnClickListener {
            finish()

        }

    }

    // Forward result back to MainActivity
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            setResult(RESULT_OK, data)
            finish()
        }
    }

    private fun updateFavoriteIcon(button: ImageButton) {
        button.setImageResource(
            if (isFavorite)
                android.R.drawable.btn_star_big_on
            else
                android.R.drawable.btn_star_big_off
        )
    }
}