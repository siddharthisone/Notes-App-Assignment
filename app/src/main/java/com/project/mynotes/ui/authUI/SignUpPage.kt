package com.project.mynotes.ui.authUI

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.project.mynotes.utils.NetworkUtils
import com.project.mynotes.R

class SignUpPage : Fragment() {

    private lateinit var signUpNameEmail : TextInputEditText
    private lateinit var signUpNamePassword : TextInputEditText
    private lateinit var signUpNameConfirmPassword : TextInputEditText
    private lateinit var signUpButton : MaterialButton
    private lateinit var backToLogin  : TextView

    private lateinit var firebaseAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        firebaseAuth = FirebaseAuth.getInstance()



        signUpNameEmail = view.findViewById(R.id.signUpEmail)
        backToLogin  = view.findViewById(R.id.signUpToLoginPage)
        signUpNamePassword = view.findViewById(R.id.signUpPassword)
        signUpNameConfirmPassword = view.findViewById(R.id.signUpConfirmPassword)
        signUpButton = view.findViewById(R.id.signUpButton)

        signUpButton.setOnClickListener {
            val email = signUpNameEmail.text.toString().trim()
            val password = signUpNamePassword.text.toString().trim()
            val confirmPass = signUpNameConfirmPassword.text.toString().trim()


            if (email.isEmpty() || password.isEmpty()){
                signUpNameEmail.error = "All fields are required"
                signUpNameEmail.requestFocus()

                signUpNamePassword.error = "All fields are required"
                signUpNamePassword.requestFocus()
            }
            else if (password.length < 6){
                signUpNamePassword.error = "Password must have at least 6 characters"
                signUpNamePassword.requestFocus()
            }
            else if(confirmPass != password)
            {
                signUpNameConfirmPassword.error = "Password doesn't match"
                signUpNameConfirmPassword.requestFocus()
            }
            else {
                //SIGN UP USING FIREBASE
                if (!NetworkUtils.isNetworkAvailable(requireContext())) {
                    Toast.makeText(
                        context,
                        "No network connection. Please check your internet and try again.",
                        Toast.LENGTH_LONG
                    ).show()
                    return@setOnClickListener
                }
                firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener {task->
                    if (task.isSuccessful)
                    {
                        sendEmailVerification()
                    }
                    else{
                        Toast.makeText(context,"Account already exists",Toast.LENGTH_SHORT).show()
                    }
                }
            }

        }

        backToLogin.setOnClickListener {

            activity?.supportFragmentManager!!.beginTransaction().replace(R.id.fragment_container_main, LoginPage()).addToBackStack(null).commit()
        }
    }

    private fun sendEmailVerification() {
        val user = firebaseAuth.currentUser
        if (user != null){
            user.sendEmailVerification().addOnCompleteListener {
                Toast.makeText(requireContext(),"Verify email and Log In",Toast.LENGTH_LONG).show()
                firebaseAuth.signOut()
                activity?.supportFragmentManager!!.beginTransaction().replace(R.id.fragment_container_main, LoginPage()).addToBackStack(null).commit()
            }
        }
        else{
            Toast.makeText(requireContext(),"Error sending email",Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_sign_up_page, container, false)
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SignUpPage().apply {
                arguments = Bundle().apply {
                }
            }
    }
}