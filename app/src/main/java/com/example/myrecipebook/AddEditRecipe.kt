package com.example.myrecipebook

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class AddEditRecipe : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_edit_recipe)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // taking our elements

        val myTitle = findViewById<EditText>(R.id.ourTitle)
        val myImage = findViewById<EditText>(R.id.ourImage)
        val myIngredients = findViewById<EditText>(R.id.ourIngredients)
        val myCategory = findViewById<EditText>(R.id.ourCategory)
        val myCookingTime = findViewById<EditText>(R.id.ourTime)
        val myDescripton = findViewById<EditText>(R.id.ourDescription)

        val mySaveButton = findViewById<Button>(R.id.ourSaveButton)
        val myCancelButton = findViewById<Button>(R.id.ourCancelButton)

        // editing mode as boolean
        // gets the id of the item that was clicked to capture its data

        val editMode = intent.getBooleanExtra("editMode", false)
        val recipeID = intent.getIntExtra("RecipeID", -1)

        // if edit mode is true, replace the current elements with the data of that item

        if (editMode) {
            myTitle.setText(intent.getStringExtra("RecipeTitle"))
            myImage.setText(intent.getStringExtra("RecipeImage"))
            myIngredients.setText(intent.getStringExtra("RecipeIngredients"))
            myCookingTime.setText(intent.getIntExtra("RecipeCookTime", 0).toString())
            myCategory.setText(intent.getStringExtra("RecipeCategory"))
            myDescripton.setText(intent.getStringExtra("RecipeDescription"))


            // change button text to update

            mySaveButton.text = "Update"
        }

        // onclick event for the save button


        mySaveButton.setOnClickListener {

            // simple error handling to make sure the fields are not empty
            if (myTitle.text.isEmpty() || myImage.text.isEmpty() || myDescripton.text.isEmpty() || myCategory.text.isEmpty() || myIngredients.text.isEmpty() || myCookingTime.text.isEmpty()) {
                Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // passes the elements data to that of the database

            val intent = Intent()
            intent.putExtra("editMode", editMode)
            intent.putExtra("RecipeID", recipeID)
            intent.putExtra("RecipeImage", myImage.text.toString())
            intent.putExtra("RecipeTitle", myTitle.text.toString())
            intent.putExtra("RecipeIngredients", myIngredients.text.toString())
            intent.putExtra("RecipeCookTime", myCookingTime.text.toString().toInt())
            intent.putExtra("RecipeCategory", myCategory.text.toString())
            intent.putExtra("RecipeDescription", myDescripton.text.toString())
            setResult(RESULT_OK, intent)
            finish()


        }

        // closes the view

        myCancelButton.setOnClickListener {
            finish()
        }


    }
}