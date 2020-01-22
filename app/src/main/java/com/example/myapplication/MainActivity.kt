package com.example.myapplication

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.Navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration

import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.actionbar_profile,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.info_button )
            findNavController(R.id.nav_host_fragment).navigate(R.id.action_home2_to_aboutpage)
        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp() = findNavController(R.id.nav_host_fragment).navigateUp()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(findViewById(R.id.toolbar))



        supportFragmentManager.beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit()

        val navView : BottomNavigationView = findViewById(R.id.bottomnav)

        val navController = findNavController((R.id.nav_host_fragment))
        navController.setGraph(R.navigation.nav_graph)

        val appBarConfiguration = AppBarConfiguration(
                setOf(
                        R.id.home2, R.id.mainProfile, R.id.play2
                )
        )

        setupActionBarWithNavController(navController,appBarConfiguration)
        navView.setupWithNavController(navController)



    }
}