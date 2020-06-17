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
import androidx.fragment.app.activityViewModels
import com.example.savemi.home.HomeViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase



class SettingsFragment : Fragment() {
    private val logtag = SettingsFragment::class.simpleName
    private lateinit var auth: FirebaseAuth
    private val model: HomeViewModel by activityViewModels()

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
        }

        auth = FirebaseAuth.getInstance()


        view.findViewById<Button>(R.id.logoutfromSettings).setOnClickListener {
            val currentUser: FirebaseUser? = auth.currentUser
            Log.d(logtag,"currentuser: $currentUser")

            model.logout()

            Log.d(logtag,"Bruger logget ud "+ currentUser)

            findNavController().navigate(R.id.loginFragment)
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