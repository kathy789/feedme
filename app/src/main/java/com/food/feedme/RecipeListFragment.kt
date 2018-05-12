package com.food.feedme

import android.content.Context
import android.support.v4.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageButton
import android.widget.ListView

import org.json.JSONException
import org.json.JSONObject

import java.util.Arrays

class RecipeListFragment : Fragment(), SpoonacularAPI.OnRecipeDetailsInterface {

    private lateinit var recipeInfos: List<RecipeInfo>
    private lateinit var parent:RecipeActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_recipe_list, container, false)

        // for back button
//        val back = view.findViewById<ImageButton>(R.id.back_to_ingredients)
//        back.setOnClickListener { fragmentManager.popBackStack() }

        val args = arguments
        val recipes = args.getSerializable("recipe_info_array") as Array<RecipeInfo>
        if (recipes != null) {
            recipeInfos = Arrays.asList(*recipes)
            /* testing purpose
            for (RecipeInfo recipeInfo : recipeInfos) {
                System.out.println("id : " + recipeInfo.getId());
            }
            */
            val listView = view.findViewById<ListView>(R.id.recipe_info_list)
            val recipeList = ListAdapterRecipeInfo(context, recipeInfos)
            listView.adapter = recipeList

            listView.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, i, l ->
                val item = recipeInfos[i]
                println("I am clicking " + item.title)
                // get Recipe details for this recipe
                getRecipeDetails(item.id)
            }
        }

        return view
    }
    // TODO: if the recipe-info  page wait a little bit long time, it will crash. problem with serialible

    fun getRecipeDetails(recipeId: Int) {
        val spoon = SpoonacularAPI(this)
        try {
            spoon.getRecipeInfo(recipeId.toString())
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun onAttach(context: Context?) {
        if (context != null) {
            parent = activity as RecipeActivity
        }
        super.onAttach(context)
    }

    override fun onRecipeDetailsReturned(recipeDetail: JSONObject) {


        if (recipeDetail != null) {
            try {
                val detailUrl = recipeDetail.getString("spoonacularSourceUrl")
                println(recipeDetail)
                println("RecipeListURL:" + detailUrl)

        		val recipeDetailFragment = RecipeDetailFragment()
        		val bundle = Bundle()
        		bundle.putString("webView", detailUrl)
                recipeDetailFragment.arguments = bundle
                parent.replaceFragment(recipeDetailFragment)


            } catch (e: JSONException) {
                e.printStackTrace()
            }

        }

    }

}
