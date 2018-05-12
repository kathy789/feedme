package com.food.feedme

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView

import com.squareup.picasso.Picasso

class ListAdapterRecipeInfo(private val parentContext: Context, private val recipeInfos: List<RecipeInfo>)// Notes: without passing list of items, the constructor does not get called.
    : ArrayAdapter<RecipeInfo>(parentContext, 0, recipeInfos) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        if (convertView == null) {

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.recipe_info_row, parent, false)
        }

        val textView = convertView!!.findViewById<TextView>(R.id.recipe_title)
        val imageView = convertView.findViewById<ImageView>(R.id.recipe_image)

        val item = recipeInfos[position]
        textView.text = item.title

        Picasso.with(parentContext)
                .load(item.image)
                .into(imageView)
        /*
        try {
            URL url = new URL(item.getImage());
            Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            imageView.setImageBitmap(bmp);

        } catch (Exception e) {
            e.printStackTrace();
        }
        */
        return convertView
    }
}
