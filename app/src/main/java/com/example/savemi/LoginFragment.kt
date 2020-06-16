package com.example.savemi



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
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.savemi.data.User
import com.example.savemi.home.HomeAdaptor
import com.example.savemi.home.HomeViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch



/**
 * A simple [Fragment] subclass.
 */
class LoginFragment : Fragment() {
    private val logtag = LoginFragment::class.simpleName
    private lateinit var auth: FirebaseAuth
    //private lateinit var firebase: Firebase
    //private lateinit var databse: FirebaseDatabase
    //private lateinit var rep: UserRepository
    private val model: HomeViewModel by activityViewModels()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)
        auth = FirebaseAuth.getInstance()
        val currentUser: FirebaseUser? = auth.currentUser
        Log.d(logtag,"currentuser: $currentUser")

        if(currentUser!=null) {
            Log.d(logtag,"USER EXIST: $currentUser")
            updateUI(currentUser)
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        model.getHomeData().observe(viewLifecycleOwner, Observer{
            Log.d(logtag, "observer")

            if(it != null)
                findNavController().navigate(R.id.homeFragment3)
            else
                Snackbar.make(view, "Not authorized", 50).show()
        })


        Log.d(logtag, "jeg er bruger auth ${auth.currentUser}")
        view.findViewById<Button>(R.id.login_button).setOnClickListener {
            //updateUI(auth.currentUser)
            singIn()
            Log.d(logtag, "jeg er bruger authlast ${auth.currentUser}")
       }
        view.findViewById<TextView>(R.id.createaccounttext).setOnClickListener{
            findNavController().navigate(R.id.action_loginFragment_to_registrerFragment)
        }
    }



    private fun updateUI(currentUser: FirebaseUser?){
        if(currentUser!=null){
            findNavController().navigate(R.id.action_loginFragment_to_homeFragment3)
            Toast.makeText(activity,"Et øjeblik",Toast.LENGTH_LONG).show()

        }else{
            Toast.makeText(activity,"Login fejlede",Toast.LENGTH_SHORT).show()
        }
    }

    private fun singIn() {
        val username = login_email.text.toString()
        val password = login_password.text.toString()
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

        if (login_password.length() < 8) {
            login_password.error = "min. 8 karaktere"
            login_password.requestFocus()
            return
        }

        GlobalScope.launch {
            model.login(username,password)
            Log.d(logtag,"Global Launch")
        }
    }



        /* auth.signInWithEmailAndPassword(login_email.text.toString(), login_password.text.toString())
            .addOnCompleteListener() { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(activity, "Forket login", Toast.LENGTH_SHORT).show()
                    updateUI(null)
                }
            }*/
}






