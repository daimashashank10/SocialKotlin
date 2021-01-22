package com.example.androidkotlin.auth.ui.main

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.androidkotlin.MainActivity
import com.example.androidkotlin.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext


class LoginFragment : Fragment() {
    private val RC_SIGN_IN: Int=1234;
    lateinit var googleSignInClient: GoogleSignInClient;
    lateinit var mauth:FirebaseAuth;


    companion object {
        fun newInstance() = LoginFragment()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view:View= inflater.inflate(R.layout.login_fragment, container, false)
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
        mauth=FirebaseAuth.getInstance();

        googleSignInClient= activity?.let { GoogleSignIn.getClient(it,gso) }!!
        view.findViewById<SignInButton>(R.id.google_signin).setOnClickListener{
            signIn()
        }
        view.findViewById<Button>(R.id.login_button).setOnClickListener{
            GlobalScope.launch (Dispatchers.IO){
                val email:String=view.findViewById<EditText>(R.id.login_email).text.toString().trim();
                val password:String=view.findViewById<EditText>(R.id.login_password).text.toString().trim();

                mauth.signInWithEmailAndPassword(email,password).addOnSuccessListener {
                    Snackbar.make(view, "Welcome", Snackbar.LENGTH_LONG).setAction("Action", null /* replace with your action or leave null to just display text*/).show();
                    GlobalScope.launch(Dispatchers.Main){
                       sendUsertoMainActivity();

                    }


                }.addOnFailureListener {
                    GlobalScope.launch(Dispatchers.Main){
                        Toast.makeText(context,"Error:"+it.message,Toast.LENGTH_SHORT).show()

                    }


                }

            }
        }
        return view
    }

    private fun sendUsertoMainActivity() {
        val intentToMain=Intent(context,MainActivity::class.java)
        intentToMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intentToMain)
        activity?.finish()

    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account =
                completedTask.getResult(ApiException::class.java)!!
            Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
            firebaseAuthWithGoogle(account.idToken!!)
        } catch (e: ApiException) {
            Log.w(TAG, "signInResult:failed code=" + e.statusCode)

        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)

        GlobalScope.launch(Dispatchers.IO) {
            val auth = mauth.signInWithCredential(credential).await()
            val firebaseUser = auth.user
            withContext(Dispatchers.Main) {
                updateUI(firebaseUser)
            }
        }

    }

    private fun updateUI(firebaseUser: FirebaseUser?) {
        if(firebaseUser != null) {
            val mainActivityIntent = Intent(activity, MainActivity::class.java)
            startActivity(mainActivityIntent)
            activity?.finish()
        }
    }


}