package com.example.savemi

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Button
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_registrer.*

/**
 * A simple [Fragment] subclass.
 */
class LoginFragment : Fragment() {
    private val logtag = LoginFragment::class.simpleName
    private lateinit var auth: FirebaseAuth




    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)
        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser

        if(currentUser!=null) {
            Log.d(logtag,"USER EXIST: $currentUser")
            findNavController().navigate(R.id.action_loginFragment_to_homefragment)
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.login_button).setOnClickListener {
            singIn()
        }
        view.findViewById<TextView>(R.id.createaccounttext).setOnClickListener{
            findNavController().navigate(R.id.action_loginFragment_to_registrerFragment)
        }
    }



    private fun updateUI(currentUser: FirebaseUser?){
        if(currentUser!=null){
            findNavController().navigate(R.id.action_loginFragment_to_homefragment)
        }else{
            Toast.makeText(activity,"Login fejlede",Toast.LENGTH_SHORT).show()
        }
    }

    private fun singIn(){
        auth = FirebaseAuth.getInstance()
        if (login_email.text.toString().isEmpty()) {
            login_email.error = "Indtast email"
            login_email.requestFocus()
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(login_email.text.toString()).matches()) {
            login_email.error = "Den indtastede email er ikke en email"
            login_email.requestFocus()
            return
        }

        if (login_password.text.toString().isEmpty()) {
            login_password.error = "Indtast kodeord"
            login_password.requestFocus()
            println(login_password.length())

            return
        }

        if(login_password.length()<8){
            login_password.error ="min. 8 karaktere"
            login_password.requestFocus()
            return
        }

        auth.signInWithEmailAndPassword(login_email.text.toString(), login_password.text.toString())
            .addOnCompleteListener() { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = auth.currentUser
                    updateUI(user)

                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(
                        activity,
                        "Forket login",
                        Toast.LENGTH_SHORT
                    ).show()
                    updateUI(null)
                }

            }


    }

}

