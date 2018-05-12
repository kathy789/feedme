package com.food.feedme

import android.app.Fragment
import android.content.Context
import android.os.AsyncTask
import android.util.Log
import android.widget.Toast
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

/**
 * Created by yuanyuanji on 5/12/18.
 */
package com.food.feedme.activities

import android.app.Fragment
import android.content.Context
import android.os.AsyncTask
import android.util.Log
import android.widget.Toast

import com.mashape.unirest.http.HttpResponse
import com.mashape.unirest.http.JsonNode
import com.mashape.unirest.http.Unirest

import org.json.JSONArray
import org.json.JSONObject

import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class SpoonacularAPI {

    var clickedRecipe: JSONObject
    var recipes: JSONArray? = null
    private var handleRecipesReturned: OnRecipesReturnedInterface ?= null
    private var onRecipeDetailsInterface: OnRecipeDetailsInterface ?=null
    private var context: Context? = null

    constructor(handleRecipesReturned: OnRecipesReturnedInterface) {
        this.handleRecipesReturned = handleRecipesReturned
        this.clickedRecipe = JSONObject()
        this.context = (handleRecipesReturned as Fragment) as Context
    }

    constructor(onRecipeDetailsInterface: OnRecipeDetailsInterface) {
        this.onRecipeDetailsInterface = onRecipeDetailsInterface
        this.clickedRecipe = JSONObject()
        this.context = (onRecipeDetailsInterface as Fragment)as Context
    }

    @Throws(Exception::class)
    fun getRecipeInfo(recipeId: String) {

        GetRecipeInfo().execute(recipeId)
    }

    @Throws(Exception::class)
    fun getRecipeByIngredients(vararg ingredients: String) {
        SearchRecipeByIngredients().execute(*ingredients)
    }


    internal inner class SearchRecipeByIngredients : AsyncTask<String, Void, JSONArray>() {

        override fun onPreExecute() {
            super.onPreExecute()
        }

        override fun doInBackground(vararg ingredients: String): JSONArray? {
            try {
                val startUrl = baseUrl + "findByIngredients?"
                var finalUrl: String
                finalUrl = addQueryParams(startUrl, "ingredients", *ingredients)
                finalUrl += '&'.toString()
                finalUrl = addQueryParams(finalUrl, "number", "3")
                println("urlStr:$finalUrl")


                val url = URL(finalUrl)
                val con = url.openConnection() as HttpURLConnection
                con.requestMethod = "GET"
                con.doInput = true
                con.setRequestProperty("Content-Type", "application/json")
                con.setRequestProperty("X-Mashape-Key", X_Mashape_Key)
                con.setRequestProperty("X-Mashape-Host", X_Mashape_Host)

                val `in` = BufferedReader(
                        InputStreamReader(con.inputStream))
                var inputLine: String
                val response = StringBuffer()
                inputLine = `in`.readLine()
                while (inputLine!=null)
                {
                    response.append(inputLine)
                    response.append('\n')
                    inputLine = `in`.readLine()
                }
                `in`.close()


                return JSONArray(response.toString())

            } catch (e: Exception) {
                e.printStackTrace()
            }

            return null
        }

        override fun onPostExecute(recipes: JSONArray?) {
            if (recipes == null) {
                Log.e(javaClass.name, " API call returned null object. One possible reason is network not enabled.")
                Toast.makeText(context, "Please make sure your network enabled!", Toast.LENGTH_LONG).show()
                return
            }
            handleRecipesReturned?.onRecipesReturned(recipes)
            super.onPostExecute(recipes)
        }


    }

    fun addQueryParams(startUrl: String, name: String, vararg values: String): String {
        if (values.size < 0) {
            return startUrl
        }
        val res = StringBuilder("$startUrl$name=")
        if (values.size > 1) {
            for (i in values.indices) {
                res.append(values[i])
                if (i != values.size - 1) {
                    res.append("%2C+")
                }
            }
            return res.toString()

        } else {

            res.append(values[0])
            return res.toString()
        }
    }

    internal inner class GetRecipeInfo : AsyncTask<String, Void, JSONObject>() {


        override fun onPreExecute() {

        }


        override fun doInBackground(vararg recipeIds: String): JSONObject? {

            try {
                val url = URL(baseUrl + recipeIds[0] + "/information")
                val con = url.openConnection() as HttpURLConnection
                con.requestMethod = "GET"
                con.doInput = true
                con.setRequestProperty("Content-Type", "application/json")
                con.setRequestProperty("X-Mashape-Key", X_Mashape_Key)
                con.setRequestProperty("X-Mashape-Host", X_Mashape_Host)

                val `in` = BufferedReader(
                        InputStreamReader(con.inputStream))
                var inputLine: String
                val response = StringBuffer()
                inputLine = `in`.readLine()
                while (inputLine!=null)  {
                    response.append(inputLine)
                    response.append('\n')
                    inputLine = `in`.readLine()
                }
                `in`.close()

                clickedRecipe = JSONObject(response.toString())

                return clickedRecipe
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return null
        }

        override fun onPostExecute(recipeDetail: JSONObject?) {
            if (recipeDetail == null) {
                Log.e(javaClass.name, " API call returned null object. One possible reason is network not enabled. ")
                Toast.makeText(context, "Please make sure your network enabled!", Toast.LENGTH_LONG).show()
                return
            }
            onRecipeDetailsInterface?.onRecipeDetailsReturned(recipeDetail)
            super.onPostExecute(recipeDetail)
        }
    }


    interface OnRecipesReturnedInterface {
        public fun onRecipesReturned(recipes: JSONArray)
    }

    interface OnRecipeDetailsInterface {
        public fun onRecipeDetailsReturned(recipeDetail: JSONObject)
    }

    companion object {
        private val X_Mashape_Key = "U19lNQuw25msh8MhwobUmfwgYEr9p1zhfcGjsn1AiU3QYpMzcQ"
        private val X_Mashape_Host = "spoonacular-recipe-food-nutrition-v1.p.mashape.com"
        var baseUrl = "https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/recipes/"
    }
}
