package com.example.savemi

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.fragment.findNavController
import android.widget.Button


/**
 * A simple [Fragment] subclass.
 * Use the [SettingsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SettingsFragment : Fragment() {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.connectWristbandSetbutton).setOnClickListener {
            findNavController().navigate(R.id.action_settingsFragment_to_scanForWristbandFragment)
        }

        view.findViewById<Button>(R.id.permissionsbutton).setOnClickListener {
            findNavController().navigate(R.id.action_settingsFragment_to_permissionsFragment)
        }

        view.findViewById<ImageView>(R.id.back_from_settings).setOnClickListener {
            findNavController().navigate(R.id.action_settingsFragment_to_HomeFragment)
        } //leeds back from page
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }




}