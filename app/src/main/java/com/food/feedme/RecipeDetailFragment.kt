package com.food.feedme

import android.app.AlertDialog
import android.support.v4.app.Fragment
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.ImageButton

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

import java.util.ArrayList


class RecipeDetailFragment : Fragment() {

    var details: JSONObject? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        println("inside webcreate function")
        val v = inflater!!.inflate(R.layout.fragment_recipe_detail, container, false)
        val wView = v.findViewById<View>(R.id.web_view) as WebView
        wView.settings.javaScriptEnabled = true
        wView.webViewClient = WebViewClient()
        println("args:" + arguments)
        wView.loadUrl(arguments.getString("webView"))

        // for back button
//        val back = v.findViewById<ImageButton>(R.id.back_to_recipes)
//        back.setOnClickListener { fragmentManager.popBackStack() }


        return v
    }

    companion object {

        fun newInstance(url: String): RecipeDetailFragment {
            val fragment = RecipeDetailFragment()
            val args = Bundle()
            args.putString("webUrl", url)
            fragment.arguments = args
            return fragment
        }
    }


}// Required empty public constructor
