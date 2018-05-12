package com.food.feedme


import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ListView

import org.json.JSONArray
import org.json.JSONException

import java.util.ArrayList
import java.util.Date


class SelectIngredientsFragment : Fragment(), SpoonacularAPI.OnRecipesReturnedInterface {
    private lateinit var parent:RecipeActivity
    //TODO: Think about whether we should include grocery items with expiration date of today. (currently don't).
    val unexpiredNames: List<String>
        get() {
            //val food = MainActivity.db.getDatas()
            val food = ArrayList<String>()
            food.add("potato")
            food.add("tomato")
            val unexpired = ArrayList<String>()


            for (item in food) {
                System.out.println("adding in unexpired: " + item)
                unexpired.add(item)

            }
            return unexpired
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
         System.out.println("I am in fragment")
         val view =  LayoutInflater.from(container?.context).inflate(R.layout.fragment_select_ingredients, container, false)
         val foodList = view.findViewById<ListView>(R.id.unexpiredItems)
         //val foodList = view.findViewById<>(R.id.unexpiredItems)


         // get all the unexpired items on grocery list
         val unexpiredItems = unexpiredNames

         val listAdapter = ListAdapterStringCheckbox(context, unexpiredItems)
         //foodList.setAdapter(listAdapter)
         foodList.adapter = listAdapter

         val searchRecipes = view.findViewById<Button>(R.id.search_recipes_button)
         searchRecipes.setOnClickListener(View.OnClickListener {
             val checkedItems = listAdapter.getCheckedItems()
             println("Printing checked Items")
             for (str in checkedItems) {
                 println(str)
             }
             callSpoonForRecipes(checkedItems)
         })
         return view
    }

    override fun onAttach(context: Context?) {
        if (context != null) {
            parent = activity as RecipeActivity
        }
        super.onAttach(context)
    }

    fun callSpoonForRecipes(checkedItems: List<String>?) {
        // send ingredients to next page
        if (checkedItems != null && checkedItems.size > 0) {
            try {
                val s = SpoonacularAPI(this@SelectIngredientsFragment)
                //s.getRes("285930");
                //String[] ingredients = {"potato", "tomato"};
                //String[] ingredients = (String[]) checkedItems.toArray();  // It doesn't work.
                val ingredients = arrayOfNulls<String>(checkedItems.size)
                for (i in checkedItems.indices) {
                    ingredients[i] = checkedItems[i]
                }
                //s.getRecipeByIngredients(*ingredients)
                s.getRecipeByIngredients("potato")
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }


    override fun onRecipesReturned(recipes: JSONArray) {
        println("I am in fragments")
        println(recipes.toString())
        // convert JSONArray recipes to RecipeInFO class array

        if (recipes.length() > 0) {
            val recipeInfos = arrayOfNulls<RecipeInfo>(recipes.length())
            try {
                for (i in 0 until recipes.length()) {
                    recipeInfos[i] = RecipeInfo(recipes.getJSONObject(i))
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }

            // invoke another fragments
            val recipeListFragment = RecipeListFragment()
            val bundle = Bundle()
            bundle.putSerializable("recipe_info_array", recipeInfos)
            //recipeListFragment.setArguments(bundle)
            recipeListFragment.arguments = bundle

            parent.replaceFragment(recipeListFragment)

            /*
            val transaction = .beginTransaction()
            transaction.replace(R.id.frame_layout, recipeListFragment)
            transaction.addToBackStack(null)
            transaction.commit()
            */
        }


    }
}
