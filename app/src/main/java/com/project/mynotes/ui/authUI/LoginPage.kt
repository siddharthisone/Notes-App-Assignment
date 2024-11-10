package com.project.mynotes.ui.authUI

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.project.mynotes.MainActivity
import com.project.mynotes.utils.NetworkUtils.isNetworkAvailable
import com.project.mynotes.R

class LoginPage : Fragment() {

    private lateinit var loginEmail : TextInputEditText
    private lateinit var loginPassword : TextInputEditText
    private lateinit var loginButton : MaterialButton
    private lateinit var loginToSignUp : TextView
    private lateinit var forgetPass : TextView
    private lateinit var loginGoogleButton : MaterialButton

    private lateinit var googleSignInClient: GoogleSignInClient
    private val RC_SIGN_IN = 9001

    private lateinit var firebaseAuth : FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        loginEmail = view.findViewById(R.id.loginEmail)
        loginPassword = view.findViewById(R.id.loginPassword)
        loginButton = view.findViewById(R.id.loginButton)
        loginToSignUp = view.findViewById(R.id.LoginToSignUp)
        forgetPass = view.findViewById(R.id.forgetPassword)

        loginGoogleButton = view.findViewById(R.id.loginWithGoogleButton)

        firebaseAuth = FirebaseAuth.getInstance()

        forgetPass.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.fragment_container_main, ForgetPassFragment())?.addToBackStack(null)?.commit()
        }


        googleSignInClient = GoogleSignIn.getClient(requireContext(), GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build())


        val account = GoogleSignIn.getLastSignedInAccount(requireContext())
        if (account != null) {
            // Already signed in, proceed to MainActivity
            (activity as MainActivity).onLoginSuccess()
        }




        loginButton.setOnClickListener {
            val email = loginEmail.text.toString().trim()
            val password = loginPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty())
            {
                loginEmail.error = "Email is required"
                loginPassword.error = "Password is required"
                loginEmail.requestFocus()
                loginPassword.requestFocus()
            }
            else {
                if (!isNetworkAvailable(requireContext())) {
                    Toast.makeText(
                        context,
                        "No network connection. Please check your internet and try again.",
                        Toast.LENGTH_LONG
                    ).show()
                    return@setOnClickListener
                }
                    //to do
                    firebaseAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->

                            if (task.isSuccessful) {
                                checkMailVerification()
                            } else Toast.makeText(
                                requireContext(),
                                "Invalid credentials",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    //intent to notes activity
                }
        }

        loginToSignUp.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.fragment_container_main, SignUpPage())?.addToBackStack(null)?.commit()
        }

        loginGoogleButton.setOnClickListener {
            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }

    }

    private fun checkMailVerification() {

        val firebaseUser = firebaseAuth.currentUser!!
        if (firebaseUser.isEmailVerified)
        {
//            Toast.makeText(requireContext(), "Login Successful", Toast.LENGTH_SHORT).show()
//            activity?.finish()
            (activity as MainActivity).onLoginSuccess()
        }
        else
        {
            Toast.makeText(requireContext(), "Please verify your email", Toast.LENGTH_SHORT).show()
            firebaseAuth.signOut()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account)
            } catch (e: ApiException) {
                Log.w("LoginPage", "Google sign in failed", e)
                Toast.makeText(requireContext(), "Google sign-in failed.", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount?) {

        if (account != null) {
            val credential = GoogleAuthProvider.getCredential(account.idToken, null)
            firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(requireContext(), "Google Login Successful", Toast.LENGTH_SHORT).show()
                        (activity as MainActivity).onLoginSuccess()
                    } else {
                        Toast.makeText(requireContext(), "Authentication Failed.", Toast.LENGTH_SHORT).show()
                    }
                }
        }

    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LoginPage().apply {
                arguments = Bundle().apply {

                }
            }
    }
}