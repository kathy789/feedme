package com.food.feedme

import android.support.v4.app.Fragment
import android.app.FragmentTransaction
import android.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle


class RecipeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe)
        if (savedInstanceState == null) {
            addFragment(SelectIngredientsFragment())
        }
    }

    fun addFragment(fragment: Fragment) {
        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()
        transaction.add(R.id.frame_layout, fragment)
        transaction.commit()

    }
    fun replaceFragment(fragment: Fragment) {
        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()
        transaction.replace(R.id.frame_layout, fragment)
        transaction.addToBackStack(null)
        transaction.commit()

    }
}
