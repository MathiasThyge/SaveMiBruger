package com.example.savemi

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.fragment.findNavController
import android.widget.Button
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


/**
 * A simple [Fragment] subclass.
 * Use the [SettingsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SettingsFragment : Fragment() {
    private val logtag = SettingsFragment::class.simpleName
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.connectWristbandSetbutton).setOnClickListener {
            findNavController().navigate(R.id.action_settingsFragment_to_scanForWristbandFragment)
        }

        view.findViewById<Button>(R.id.permissionsbutton).setOnClickListener {
            findNavController().navigate(R.id.action_settingsFragment_to_permissionsFragment)
        }

        view.findViewById<ImageView>(R.id.back_from_settings).setOnClickListener {
            findNavController().navigateUp()
        } //leeds back from page

        auth = FirebaseAuth.getInstance()
        //database = Firebase.database.reference

        view.findViewById<Button>(R.id.logoutfromSettings).setOnClickListener {
            val currentUser: FirebaseUser? = auth.currentUser
            Log.d(logtag,"currentuser: $currentUser")

            auth.signOut()

            Log.d(logtag,"Bruger logget ud "+ currentUser)

            findNavController().navigate(R.id.action_settingsFragment_to_loginFragment)
        }
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }





}