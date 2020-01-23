package com.example.myapplication

import android.R.attr
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth

class Splash : AppCompatActivity() {
    private val RC_SIGN_IN = 1822
    private val SPLASH_DISPLAY_LENGTH = 1000
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        Handler().postDelayed({
            /* Create an Intent that will start the Menu-Activity. */
            // Choose authentication providers
            val providers = arrayListOf(
                    AuthUI.IdpConfig.EmailBuilder().build(),
                    AuthUI.IdpConfig.PhoneBuilder().build()
            )

            //startActivity(Intent(this, MainActivity::class.java))

// Create and launch sign-in intent


            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(providers)
                            .setLogo(R.mipmap.start)
                            .setTheme(R.style.AuthenticationTheme)
                            .build(),
                    RC_SIGN_IN)




        }, SPLASH_DISPLAY_LENGTH.toLong())


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)
            if (resultCode == Activity.RESULT_OK) {
                val user = FirebaseAuth.getInstance().currentUser
                startActivity(Intent(this, MainActivity::class.java))
                Toast.makeText(this, "Welcome! " + user!!.displayName, Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "" + response!!.error!!.message, Toast.LENGTH_LONG).show()
            }
        }


    }


}