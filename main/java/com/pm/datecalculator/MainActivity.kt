package com.pm.datecalculator

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import androidx.navigation.fragment.NavHostFragment
import com.pm.datecalculator.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        var currentFragment = 0

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_content_main) as NavHostFragment
        val navController = navHostFragment.navController
        appBarConfiguration = AppBarConfiguration(setOf(
            R.id.FirstFragment, R.id.SecondFragment, R.id.ThirdFragment))
        setupActionBarWithNavController(navController, appBarConfiguration)

        binding.fab.setOnClickListener {
            if (currentFragment==0){
            findNavController(R.id.nav_host_fragment_content_main).navigate(R.id.action_FirstFragment_to_SecondFragment)
            currentFragment=1
            } else if (currentFragment==1) {
                findNavController(R.id.nav_host_fragment_content_main).navigate(R.id.action_SecondFragment_to_FirstFragment)
                currentFragment=0
                //findNavController(R.id.nav_host_fragment_content_main).navigate(R.id.action_SecondFragment_to_ThirdFragment)
                //currentFragment=2
            } else {
                findNavController(R.id.nav_host_fragment_content_main).navigate(R.id.action_ThirdFragment_to_FirstFragment)
                currentFragment=0
            }
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        val settingsItem = menu.findItem(R.id.action_settings)
        settingsItem.isVisible = false
        return false
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
/*
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

 */
}