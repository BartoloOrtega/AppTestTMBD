package com.example.apptesttmbd.ui.activity.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.apptesttmbd.R
import com.example.apptesttmbd.databinding.ActivityMainBinding
import com.example.apptesttmbd.ui.activity.listmovies.ListMoviesActivity
import com.example.apptesttmbd.utils.customDialog
import com.facebook.*
import com.facebook.login.LoginResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mAuth: FirebaseAuth
    lateinit var callbackManager: CallbackManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mAuth = FirebaseAuth.getInstance()
        callbackManager = CallbackManager.Factory.create();
        binding.btnLogin.setOnClickListener {
            if(!binding.loginEmailEditText.text.isNullOrEmpty() && !binding.loginPasswordEditText.text.isNullOrEmpty())
                Login()
            else
                customDialog.instance?.DialogSimple(this,getString(R.string.req_user_pass))
        }

        //binding.loginButton.setReadPermissions("public_profile", "default")
        binding.loginButton.setPermissions("public_profile", "email")
        binding.loginButton.registerCallback(callbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult) {
                    // stuff
                    handleFacebookAccessToken(result.accessToken)
                    /*
                    val request = GraphRequest.newMeRequest(
                        result.accessToken
                    ) { `object`, response ->
                        // stuff
                        Toast.makeText(this@MainActivity, "Se conecto", Toast.LENGTH_LONG).show()
                    }

                    val parameters = Bundle()
                    parameters.putString("fields", "id,name")
                    request.parameters = parameters
                    request.executeAsync()
                    */

                }

                override fun onCancel() {
                    Log.d("FacebookTag", "Facebook onCancel")
                }

                override fun onError(error: FacebookException) {
                    Log.d("FacebookTag", "Facebook onError")
                }

            })
    }

    fun Login() {
        try{
            mAuth.signInWithEmailAndPassword(binding.loginEmailEditText.text?.trim().toString(),
                binding.loginPasswordEditText.text.toString().trim())
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val intent = Intent(this, ListMoviesActivity::class.java).apply {
                            putExtra("user", binding.loginEmailEditText.text.toString())
                        }
                        startActivity(intent)
                        finish()
                        //Toast.makeText(this, "Logueo con exito", Toast.LENGTH_LONG).show();

                    } else customDialog.instance?.DialogSimple(this,getString(R.string.login_error))
                }
        }catch (ex: Exception){
            customDialog.instance?.DialogSimple(this, ex.message)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }
    private fun handleFacebookAccessToken(token: AccessToken) {
        Log.d("", "handleFacebookAccessToken:$token")

        val credential = FacebookAuthProvider.getCredential(token.token)
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("TAG", "signInWithCredential:success")
                    val user = mAuth.currentUser
                    //updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("TAG", "signInWithCredential:failure", task.exception)
                    Toast.makeText(
                        baseContext,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                    //updateUI(null)
                }
            }
    }
}