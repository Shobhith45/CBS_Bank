package com.cbsindia.cbs.ui.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.cbsindia.cbs.R
import com.cbsindia.cbs.databinding.ActivityMainBinding
import com.cbsindia.cbs.util.SessionManager

class MainActivity : AppCompatActivity() {

    private val navController by lazy {
        Navigation.findNavController(this, R.id.nav_host_fragment)
    }
    private lateinit var binding: ActivityMainBinding
    private lateinit var configuration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.appBarMain.toolbar)
        setupDrawerLayout()
        observeSessionExpiry()
    }

    private fun observeSessionExpiry() {
        SessionManager.isTokenExpired.observe(this) {
            it.getContentIfNotHandled()?.let { isExpired ->
                if (isExpired) {
                    navigateToLoginScreen()
                }
            }
        }
    }

    private fun navigateToLoginScreen() {
        Toast.makeText(
            applicationContext,
            getString(R.string.err_session_expired),
            Toast.LENGTH_SHORT
        ).show()
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }

    private fun setupDrawerLayout() {
        binding.apply {
            configuration = AppBarConfiguration(
                setOf(
                    R.id.homeFragment,
                    R.id.depositFragment,
                    R.id.withdrawFragment,
                    R.id.moneyTransferFragment,
                    R.id.transactionFragment
                ),
                mainDrawerLayout
            )
            mainNavView.setupWithNavController(navController)
            appBarMain.toolbar.setupWithNavController(navController, configuration)
            NavigationUI.setupActionBarWithNavController(
                this@MainActivity,
                navController,
                configuration
            )
            addDrawerDestinationChangeListener()
            addLogoutClickListener()
        }
    }

    private fun addLogoutClickListener() {
        binding.apply {
            mainNavView.setNavigationItemSelectedListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.logout -> {
                        navController.navigate(R.id.action_global_logoutDialogFragment)
                    }
                }
                NavigationUI.onNavDestinationSelected(menuItem, navController)
                mainDrawerLayout.closeDrawer(GravityCompat.START)
                true
            }
        }
    }

    private fun addDrawerDestinationChangeListener() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.introFragment ||
                destination.id == R.id.loginFragment ||
                destination.id == R.id.createAccountFragment
            ) {
                lockDrawer()
            } else {
                unlockDrawer()
            }
        }
    }

    private fun lockDrawer() {
        binding.apply {
            appBarMain.toolbar.isVisible = false
            mainDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        }
    }

    private fun unlockDrawer() {
        binding.apply {
            appBarMain.toolbar.isVisible = true
            mainDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
        }
    }
}
