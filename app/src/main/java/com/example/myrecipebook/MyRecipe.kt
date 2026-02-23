package com.example.myrecipebook

class MyRecipe {
var RecipeID : Int = 0
        get() = field
        set(value) {
            if(value >= 0)
                field = value
        }

    var RecipeImage : String = ""
        get() = field
        set(value) {
            if(value.isNotEmpty())
                field = value
        }

    var RecipeTitle : String = ""
        get() = field
        set(value) {
            if(value.isNotEmpty())
                field = value
        }

    var RecipeIngredients : String = ""
        get() = field
        set(value) {
            if(value.isNotEmpty())
                field = value
        }

    var RecipeCookTime : Int = 0
        get() = field
        set(value) {
            if(value >= 0)
                field = value
        }

    var RecipeCategory : String = ""
        get() = field
        set(value) {
            if(value.isNotEmpty())
                field = value
        }

    var RecipeDescription : String = ""
        get() = field
        set(value) {
            if(value.isNotEmpty())
                field = value
        }

    var isFavorite: Boolean = false



    constructor() {}

    constructor(ex_RecipeID:Int, ex_RecipeImage:String, ex_RecipeTitle:String, ex_RecipeIngredients:String, ex_RecipeCookTime:Int, ex_RecipeCategory:String, ex_RecipeDescription:String) {
        RecipeID = ex_RecipeID
        RecipeImage = ex_RecipeImage
        RecipeTitle = ex_RecipeTitle
        RecipeIngredients = ex_RecipeIngredients
        RecipeCookTime = ex_RecipeCookTime
        RecipeCategory = ex_RecipeCategory
        RecipeDescription = ex_RecipeDescription
    }

    override fun toString(): String {
        return RecipeTitle
    }
}