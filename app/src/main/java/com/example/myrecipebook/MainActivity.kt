package com.example.myrecipebook

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var db: MyDatabase
    private lateinit var recipeList: ListView
    private lateinit var addButton: FloatingActionButton
    private lateinit var favoritesButton: FloatingActionButton
    private lateinit var appTitle : TextView

    private lateinit var searchField: EditText

    private var showingFavorites = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        recipeList = findViewById(R.id.recipeList)
        addButton = findViewById(R.id.addButton)
        favoritesButton = findViewById(R.id.favoritesButton)
        appTitle = findViewById(R.id.appTitle)
        searchField = findViewById(R.id.searchField)

        db = MyDatabase(this)
        db.createDefaults()

        loadData()

        favoritesButton.setOnClickListener {
            showingFavorites = !showingFavorites
            loadData()

        }

        favoritesButton.setImageResource(
            if (showingFavorites)
                android.R.drawable.btn_star_big_on
            else
                android.R.drawable.btn_star_big_off
        )

        addButton.setOnClickListener {
            val myIntent = Intent(this, AddEditRecipe::class.java)
            startActivityForResult(myIntent, 1)
        }

        // search f

        searchField.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                val query = searchField.text.toString().trim()

                val recipes = if (query.isEmpty()) {
                    if (showingFavorites) db.getFavorites() else db.getAll()
                } else {
                    db.searchRecipes(query)
                }

                recipeList.adapter = RowAdapter(this, recipes)
                true
            } else {
                false
            }
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {

            val editMode = data.getBooleanExtra("editMode", false)

            val id = data.getIntExtra("RecipeID", -1)
            val image = data.getStringExtra("RecipeImage") ?: return
            val title = data.getStringExtra("RecipeTitle") ?: ""
            val ingredients = data.getStringExtra("RecipeIngredients") ?: ""
            val cooktime = data.getIntExtra("RecipeCookTime", 0)
            val category = data.getStringExtra("RecipeCategory") ?: return
            val description = data.getStringExtra("RecipeDescription") ?: ""

//            val recipe = if (editMode) {
//                val existing = db.getAll().first { it.RecipeID == id }
//
//                MyRecipe(
//                    id,
//                    image,
//                    title,
//                    ingredients,
//                    cooktime,
//                    category,
//                    description,
//                    existing.isFavorite
//                )
//            } else {
//                MyRecipe(
//                    0, // SQLite AUTOINCREMENT
//                    image,
//                    title,
//                    ingredients,
//                    cooktime,
//                    category,
//                    description,
//                    false
//                )
//
//            }
//
//
//
//            // if shit is false add to the database otherwise call edit function
//            if (editMode) {
//                db.editDB(recipe)
//            } else {
//                db.addToDb(recipe)
//            }
//
//        }
//
//        loadData()
//    }

            if (editMode && id > 0) {
                val existingRecipe = db.getAll().firstOrNull { it.RecipeID == id }
                db.editDB(
                    MyRecipe(
                        id,
                        image,
                        title,
                        ingredients,
                        cooktime,
                        category,
                        description,
                        existingRecipe?.isFavorite ?: false
                    )
                )
            } else {
                db.addToDb(
                    MyRecipe(
                        0,
                        image,
                        title,
                        ingredients,
                        cooktime,
                        category,
                        description,
                        false
                    )
                )
            }

            loadData()
        }
    }

//    private fun loadData() {
//        val recipes = if (showingFavorites) {
//            appTitle.setText("Favorite Recipes")
//            db.getFavorites()
//
//        } else {
//            appTitle.setText("My Recipes")
//            db.getAll()
//        }
//
//        val adapter = RowAdapter(this, recipes)
//        recipeList.adapter = adapter
//
//        recipeList.setOnItemClickListener { _, _, position, _ ->
//            val selectedRecipe = recipes[position]
//
//            val intent = Intent(this, AddEditRecipe::class.java)
//            intent.putExtra("editMode", true)
//            intent.putExtra("RecipeID", selectedRecipe.RecipeID)
//            intent.putExtra("RecipeTitle", selectedRecipe.RecipeTitle)
//            intent.putExtra("RecipeImage", selectedRecipe.RecipeImage)
//            intent.putExtra("RecipeIngredients", selectedRecipe.RecipeIngredients)
//            intent.putExtra("RecipeCookTime", selectedRecipe.RecipeCookTime)
//            intent.putExtra("RecipeCategory", selectedRecipe.RecipeCategory)
//            intent.putExtra("RecipeDescription", selectedRecipe.RecipeDescription)
//            startActivityForResult(intent, 1)
//        }
//    }

    private fun loadData() {
        val recipes = if (showingFavorites) {
            appTitle.text = "Favorite Recipes"
            db.getFavorites()
        } else {
            appTitle.text = "My Recipes"
            db.getAll()
        }

        val adapter = RowAdapter(this, recipes)
        recipeList.adapter = adapter

        recipeList.setOnItemClickListener { _, _, position, _ ->
            val selectedRecipe = recipes[position]
            val intent = Intent(this, RecipeDetails::class.java)
            intent.putExtra("RecipeID", selectedRecipe.RecipeID)
            intent.putExtra("RecipeTitle", selectedRecipe.RecipeTitle)
            intent.putExtra("RecipeIngredients", selectedRecipe.RecipeIngredients)
            intent.putExtra("RecipeCookTime", selectedRecipe.RecipeCookTime)
            intent.putExtra("RecipeCategory", selectedRecipe.RecipeCategory)
            intent.putExtra("RecipeDescription", selectedRecipe.RecipeDescription)
            intent.putExtra("RecipeImage", selectedRecipe.RecipeImage)
            intent.putExtra("RecipeFavorite", selectedRecipe.isFavorite)
            startActivityForResult(intent, 1)
        }

        //  for deleting
        recipeList.setOnItemLongClickListener { _, _, position, _ ->
            val selectedRecipe = recipes[position]
            showDeleteDialog(selectedRecipe.RecipeID)
            true
        }
    }

    private fun showDeleteDialog(recipeId: Int) {
        AlertDialog.Builder(this)
            .setTitle("Delete Recipe")
            .setMessage("Are you sure you want to delete this Recipe?")
            .setPositiveButton("Delete") { _, _ ->
                val recipe = MyRecipe()
                recipe.RecipeID = recipeId
                db.deleteFromDb(recipe)

                loadData()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }


}