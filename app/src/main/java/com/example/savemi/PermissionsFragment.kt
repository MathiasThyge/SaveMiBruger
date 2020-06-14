package com.example.savemi

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.navigation.fragment.findNavController
import com.example.savemi.R.id.action_permissionsFragment_to_settingsFragment


/**
 * A simple [Fragment] subclass.
 * Use the [PermissionsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PermissionsFragment : Fragment() {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.back_from_permissions).setOnClickListener {
            findNavController().navigate(R.id.action_permissionsFragment_to_settingsFragment)
        } //leeds back from page

        view.findViewById<Button>(R.id.confirmpermissionsbutton).setOnClickListener {
            findNavController().navigate(R.id.action_permissionsFragment_to_settingsFragment)
        } //test of layout
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_permissions, container, false)
    }


    }
