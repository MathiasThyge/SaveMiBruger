package com.example.savemi

import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_registrer.*


class RegistrerFragment : Fragment() {

    private lateinit var auth: FirebaseAuth


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_registrer, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()

       view.findViewById<Button>(R.id.regi_button).setOnClickListener{
           signup()
       }


        view.findViewById<ImageView>(R.id.back_from_registrer).setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_loginFragment)
        }


    }

    private fun signup() {


        if (regi_email.text.toString().isEmpty()) {
            regi_email.error = "Indtast email"
            regi_email.requestFocus()
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(regi_email.text.toString()).matches()) {
            regi_email.error = "Den indtastede email er ikke en email"
            regi_email.requestFocus()
            return
        }

        if (regi_password.text.toString().isEmpty()) {
            regi_password.error = "Indtast kodeord"
            regi_password.requestFocus()
            return
        }

        auth.createUserWithEmailAndPassword(
            regi_email.text.toString(),
            regi_password.text.toString()
        )
            .addOnCompleteListener() { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    findNavController().navigate(R.id.action_registrerFragement_to_HomeFragment)

                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(
                        activity,
                        "Registrering fejlede, prøv igen efter noget tid",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

}



