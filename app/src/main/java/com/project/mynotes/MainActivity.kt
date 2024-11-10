package com.project.mynotes

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.project.mynotes.ui.HomeFragment
import com.project.mynotes.ui.authUI.LoginPage

class MainActivity : AppCompatActivity() {
    private lateinit var frameLayout: FrameLayout
    private lateinit var firebaseAuth: FirebaseAuth

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar1212)
        setSupportActionBar(toolbar)
//        toolbar.navigationIcon?.setTint(getColor(android.R.attr.colorPrimary))
        supportActionBar!!.setDisplayShowTitleEnabled(false)


        frameLayout = findViewById(R.id.fragment_container_main)
        firebaseAuth = FirebaseAuth.getInstance()

        // Check if user is logged in through FirebaseAuth, and load the appropriate fragment
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser != null) {
            loadHomeFragment()
        } else {
            loadLoginFragment()
        }
    }

    private fun loadHomeFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container_main, HomeFragment())
            .commit()
    }

    private fun loadLoginFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container_main, LoginPage())
            .commit()
    }

    // This is called from LoginPage upon successful login
    fun onLoginSuccess() {
        loadHomeFragment()
    }

    // This is called from HomeFragment when user logs out
    fun onLogout() {
        firebaseAuth.signOut()
        loadLoginFragment()
    }

    override fun onBackPressed() {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container_main)
        if (currentFragment is LoginPage) {
            finishAffinity() // Close the app if back is pressed on the login screen
        } else {
            super.onBackPressed()
        }
    }
}
