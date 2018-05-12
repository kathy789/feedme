package com.food.feedme

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast

import java.util.ArrayList


class ListAdapterStringCheckbox(context: Context, list: List<String>) : ArrayAdapter<String>(context, 0, list) {
    private var ingre_count = 0
    private val checkedItems: MutableList<String>

    init {
        checkedItems = ArrayList()
    }

    fun getCheckedItems(): List<String> {
        return checkedItems
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.select_ingredients_row, parent, false)
        }
        val ingredient_name = convertView!!.findViewById<View>(R.id.ingredient_name) as TextView
        val ingredient_checkbox = convertView.findViewById<View>(R.id.ingredient_checkbox) as CheckBox
        val ingredient_row = convertView.findViewById<View>(R.id.select_ingredients_row) as RelativeLayout

        ingredient_row.setOnClickListener { ingredient_checkbox.performClick() }

        ingredient_checkbox.setOnCheckedChangeListener { compoundButton, isChecked ->
            if (isChecked) {
                if (ingre_count < INGRE_LIMIT) {
                    ingre_count++
                    checkedItems.add(getItem(position))
                } else {
                    val msg = "Ingredients Limit is " + INGRE_LIMIT
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                    compoundButton.isChecked = false
                }

            } else {
                ingre_count--
                checkedItems.remove(getItem(position))
            }
        }

        ingredient_name.text = getItem(position)
        ingredient_checkbox.isChecked = false

        return convertView
    }

    companion object {
        private val INGRE_LIMIT = 3 // change to 5 later  // the limit of selected ingredients
    }
}
