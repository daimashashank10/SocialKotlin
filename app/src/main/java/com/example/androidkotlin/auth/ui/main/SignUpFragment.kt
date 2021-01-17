package com.example.androidkotlin.auth.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.androidkotlin.R
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SignUpFragment: Fragment() {
    private lateinit var mAuth: FirebaseAuth
    companion object {
        fun newInstance() = SetupFragment()
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view= inflater.inflate(R.layout.signup_fragment,container,false)
        mAuth= FirebaseAuth.getInstance();
        view.findViewById<Button>(R.id.signup_button).setOnClickListener{
            val email:String=view.findViewById<EditText>(R.id.signup_email).text.toString();
            val password:String=view.findViewById<EditText>(R.id.signup_password).text.toString();
            val confirmPassword:String=view.findViewById<EditText>(R.id.signup_confirm_password).text.toString();
            if(email.isEmpty()){
                Toast.makeText(activity,"Email is Empty",Toast.LENGTH_SHORT).show();

            }
            else if(password.isEmpty()){
                Toast.makeText(activity,"Password is Empty",Toast.LENGTH_SHORT).show();

            }
            else if(confirmPassword.isEmpty()){
                Toast.makeText(activity,"Confirm Password is Empty",Toast.LENGTH_SHORT).show();

            }
            else if(! password.equals(confirmPassword)){
                Toast.makeText(activity,"Password Mismatch",Toast.LENGTH_SHORT).show();

            }

            else{
                GlobalScope.launch(Dispatchers.IO) {
                    mAuth.signInWithEmailAndPassword(email,password).addOnSuccessListener {
                        GlobalScope.launch (Dispatchers.Main){
                            toSetupFragment();
                        }
                    }.addOnFailureListener{
                        Snackbar.make(view, it.message.toString(), Snackbar.LENGTH_SHORT)
                            .show()
                    }
                }

            }
        }
        return view;
    }

    private fun toSetupFragment() {
        activity?.supportFragmentManager?.beginTransaction()
            ?.replace(R.id.container, SetupFragment.newInstance())
            ?.commitNow()
    }
}