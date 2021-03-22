package com.srs.bookhub

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.FrameLayout
import android.widget.Toast

import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {
    lateinit var drawerLayout:DrawerLayout
    lateinit var coordinatorLayout:CoordinatorLayout
    lateinit var toolbar:Toolbar
    lateinit var frameLayout:FrameLayout
    lateinit var navigationView:NavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        drawerLayout=findViewById(R.id.drawerLayout)
        coordinatorLayout=findViewById(R.id.coordinatorLayout)
        toolbar=findViewById(R.id.toolbar)
        frameLayout=findViewById(R.id.frameLayout)
        navigationView=findViewById(R.id.navigationView)
        setUpToolBar()
        val actionBarDrawerToggle=ActionBarDrawerToggle(this@MainActivity,drawerLayout,R.string.open_drawer,
            R.string.close_drawer)
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()
        navigationView.setNavigationItemSelectedListener {
            when(it.itemId)
            {
                R.id.dashboard->{

                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frameLayout,DashboardFragment())
                        .addToBackStack("Dashboard")
                        .commit()
                    drawerLayout.closeDrawers()

                }
                R.id.favourites->{
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frameLayout,FavouritesFragment())
                        .addToBackStack("Favourites")
                        .commit()
                    drawerLayout.closeDrawers()
                }
                R.id.profile->{
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frameLayout,ProfileFragment())
                        .addToBackStack("Profile")
                        .commit()
                    drawerLayout.closeDrawers()
                }
                R.id.about_app->{
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frameLayout,AboutAppFragment())
                        .addToBackStack("About App")
                        .commit()
                    drawerLayout.closeDrawers()
                }
            }
            return@setNavigationItemSelectedListener true
        }
    }
    fun setUpToolBar()
    {
        setSupportActionBar(toolbar)
        supportActionBar?.title="Toolbar Title"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id=item.itemId
        if(id==android.R.id.home)
        {
            drawerLayout.openDrawer(GravityCompat.START)
        }
        return super.onOptionsItemSelected(item)
    }

}