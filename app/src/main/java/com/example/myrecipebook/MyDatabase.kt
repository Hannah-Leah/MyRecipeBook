package com.example.myrecipebook

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

private val TAG:String = "OeBB"
private val DATABASE_VERSION: Int = 1
private val DATABASE_NAME:String = "RecipeDB"


private val TABLE_NAME:String = "Recipes"
private val COLUMN_ID: String = "RecipeID"
private val COLUMN_IMAGE: String = "RecipeImage"
private val COLUMN_TITLE: String = "RecipeTitle"
private val COLUMN_INGREDIENTS: String = "RecipeIngredients"
private val COLUMN_COOKTIME: String = "RecipeCookTime"
private val COLUMN_CATEGORY: String = "RecipeCategory"
private val COLUMN_DESCRIPTION: String = "RecipeDescription"

private val COLUMN_FAVORITE = "FavoriteRecipe"

class MyDatabase (context: Context?) :
    SQLiteOpenHelper(context,
        com.example.myrecipebook.DATABASE_NAME, null,
        com.example.myrecipebook.DATABASE_VERSION
    ) {
    override fun onCreate(db: SQLiteDatabase?) {
        val sqlQuery =
            "CREATE TABLE ${com.example.myrecipebook.TABLE_NAME}(${com.example.myrecipebook.COLUMN_ID} INTEGER PRIMARY KEY AUTOINCREMENT, $COLUMN_IMAGE TEXT, $COLUMN_TITLE TEXT, $COLUMN_INGREDIENTS TEXT, $COLUMN_COOKTIME INTEGER, $COLUMN_CATEGORY TEXT, $COLUMN_DESCRIPTION TEXT, $COLUMN_FAVORITE INTEGER DEFAULT 0)"

        if (db is SQLiteDatabase) {
            db.execSQL(sqlQuery)
        } else {
            Log.e(com.example.myrecipebook.TAG, "${this.javaClass.name} Fehler beim exec ")
        }
    }

    override fun onUpgrade(
        db: SQLiteDatabase?,
        oldVersion: Int,
        newVersion: Int
    ) {
        TODO("Not yet implemented")
    }

    fun addToDb(recipe: MyRecipe) {
        val values = ContentValues()
        values.put(COLUMN_IMAGE, recipe.RecipeImage)
        values.put(COLUMN_TITLE, recipe.RecipeTitle)
        values.put(COLUMN_INGREDIENTS, recipe.RecipeIngredients)
        values.put(COLUMN_COOKTIME, recipe.RecipeCookTime)
        values.put(COLUMN_CATEGORY, recipe.RecipeCategory)
        values.put(COLUMN_DESCRIPTION, recipe.RecipeDescription)
        values.put(COLUMN_FAVORITE, if (recipe.isFavorite) 1 else 0)

        writableDatabase.insert(TABLE_NAME, null, values)


    }

    fun getCount() : Int {
        val query = "SELECT  * FROM ${com.example.myrecipebook.TABLE_NAME}"
        val db = readableDatabase
        val myCursor : Cursor = db.rawQuery(query, null)
        val count = myCursor.count
        myCursor.close()
        return count
    }

    fun createDefaults() {

        if(getCount() == 0) {

            val recipeSchnizel = MyRecipe(0, "https://thefoodbarrel.com/cdn/shop/files/Chicken_Schnitzel_90a8889c-5a06-487d-aa3b-28aa668e8a48.jpg?v=1720608284&width=1500", "Schnitzel", "chicken, rice, veggies", 60, "Chicken", "mix chicken with things")
            val recipeWok = MyRecipe(1, "https://www.wellplated.com/wp-content/uploads/2019/07/Ginger-Teriyaki-Chicken-Stir-Fry.jpg", "Wok Chicken", "chicken, rice, veggies, sauce", 120, "Asian", "Asian chicken rice food...")


            addToDb(recipeSchnizel)
            addToDb(recipeWok)
        }

    }

    // Read

    fun getAll():List<MyRecipe> {
        val list: MutableList<MyRecipe> = mutableListOf()
        val query = "SELECT * FROM ${com.example.myrecipebook.TABLE_NAME}"
        val db = readableDatabase
        val cursor: Cursor = db.rawQuery(query, null)
        if (cursor.moveToFirst()) {
            do {
                val recipe: MyRecipe = MyRecipe()
                recipe.RecipeID = cursor.getInt(0)
                recipe.RecipeImage = cursor.getString(1)
                recipe.RecipeTitle = cursor.getString(2)
                recipe.RecipeIngredients = cursor.getString(3)
                recipe.RecipeCookTime = cursor.getInt(4)
                recipe.RecipeCategory = cursor.getString(5)
                recipe.RecipeDescription = cursor.getString(6)
                recipe.isFavorite = cursor.getInt(7) == 1

                list.add(recipe)

            } while (cursor.moveToNext())
        }
        cursor.close()
        return list
    }

    // Delete
    fun deleteFromDb(recipe: MyRecipe) {
        val query = "DELETE FROM ${com.example.myrecipebook.TABLE_NAME} WHERE ${com.example.myrecipebook.COLUMN_ID} = ${recipe.RecipeID}"
        val db = writableDatabase

        try {
            db.execSQL(query)
        } catch(ex: Exception) {
            Log.e(com.example.myrecipebook.TAG,"Error with deleting ${recipe.RecipeTitle} ${ex.message}")
        }

    }

    // update
    fun editDB(recipe: MyRecipe) {
        val db = writableDatabase
        val values = ContentValues()
        values.put(COLUMN_IMAGE, recipe.RecipeImage)
        values.put(COLUMN_TITLE, recipe.RecipeTitle)
        values.put(COLUMN_INGREDIENTS, recipe.RecipeIngredients)
        values.put(COLUMN_COOKTIME, recipe.RecipeCookTime)
        values.put(COLUMN_CATEGORY, recipe.RecipeCategory)
        values.put(COLUMN_DESCRIPTION, recipe.RecipeDescription)
        values.put(COLUMN_FAVORITE, if (recipe.isFavorite) 1 else 0)

        db.update(
            com.example.myrecipebook.TABLE_NAME,
            values,
            "${com.example.myrecipebook.COLUMN_ID} = ?",
            arrayOf(recipe.RecipeID.toString())
        )
    }
}