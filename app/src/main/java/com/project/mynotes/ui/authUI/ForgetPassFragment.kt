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

class ForgetPassFragment : Fragment() {

    private lateinit var backToLogin : TextView

    private lateinit var textInputEditTextEmail: TextInputEditText

    private lateinit var forgetPassContinue : MaterialButton

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        backToLogin = view.findViewById(R.id.backToLoginButton)

        forgetPassContinue = view.findViewById(R.id.forgetPasswordContinueButton)

        textInputEditTextEmail = view.findViewById(R.id.textInputEditTextEmail)

        firebaseAuth = FirebaseAuth.getInstance()



        //BACK TO LOGIN
        backToLogin.setOnClickListener {

        activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.fragment_container_main,
            LoginPage()
        )
            ?.addToBackStack(null)
            ?.commit()

        }


        //FORGET PASSWORD
        forgetPassContinue.setOnClickListener {

            val mail : String  = textInputEditTextEmail.text.toString().trim()

            if (mail.isEmpty()){
                textInputEditTextEmail.error = "Email is required"
                textInputEditTextEmail.requestFocus()
            }
            else{
                if (!NetworkUtils.isNetworkAvailable(requireContext())) {
                    Toast.makeText(
                        context,
                        "No network connection. Please check your internet and try again.",
                        Toast.LENGTH_LONG
                    ).show()
                    return@setOnClickListener
                }
                firebaseAuth.sendPasswordResetEmail(mail).addOnCompleteListener {
                    if (it.isSuccessful)
                    {
                        Toast.makeText(requireContext(),"Reset Password Mail sent",Toast.LENGTH_LONG).show()
                        activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.fragment_container_main,
                            LoginPage()
                        )
                            ?.addToBackStack(null)
                            ?.commit()

                    }else Toast.makeText(requireContext(),"Account doesn't exist",Toast.LENGTH_LONG).show()
                }
            }

        }



    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_forget_pass_fragement, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ForgetPassFragment().apply {
                arguments = Bundle().apply {



                }
            }
    }
}