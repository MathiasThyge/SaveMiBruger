package com.example.savemi

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_regi_data.*
import kotlinx.android.synthetic.main.fragment_regi_data.view.*

/**
 * A simple [Fragment] subclass.
 */
class RegiDataFragment : Fragment() {
    private val logtag = RegiDataFragment::class.simpleName
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference


    data class User(
        var Navn: String = "",
        var PersId: String = ""//,
        //var Donor: Boolean = FALSE,
        //var BeaconId: String?
    )


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_regi_data,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

         view.findViewById<Button>(R.id.regiData_confirmButton).setOnClickListener {
             createUser()
             //findNavController().navigate(R.id.action_regiDataFragment_to_homefragment)       Use to control layout
         }
    }

    private fun createUser(){
        auth = FirebaseAuth.getInstance()
        database = Firebase.database.reference

        val currentUser = auth.currentUser

        if (RegiData_ScrollView_ContraintLayout.regiDataName.text.toString().isEmpty()) {
            RegiData_ScrollView_ContraintLayout.regiDataName.error = "Indtast navn"
            RegiData_ScrollView_ContraintLayout.regiDataName.requestFocus()
            return
        }

        if (RegiData_ScrollView_ContraintLayout.regiDataPersonalID.text.toString().isEmpty()) {
            RegiData_ScrollView_ContraintLayout.regiDataPersonalID.error = "Angiv CPR"
            RegiData_ScrollView_ContraintLayout.regiDataPersonalID.requestFocus()
            return
        }

        if (RegiData_ScrollView_ContraintLayout.regiDataPersonalID.length() <=13 &&
            RegiData_ScrollView_ContraintLayout.regiDataPersonalID.length() >=12 ) {
            RegiData_ScrollView_ContraintLayout.regiDataPersonalID.error = "Angiv korrekt CPR"
            RegiData_ScrollView_ContraintLayout.regiDataPersonalID.requestFocus()
            return
        }

        if( currentUser != null ) {
            val uid = auth.currentUser?.uid.toString()

            val Navn = RegiData_ScrollView_ContraintLayout.regiDataName.text.toString()
            val PersId = RegiData_ScrollView_ContraintLayout.regiDataPersonalID.text.toString()
            val user = User( Navn, PersId )

            database.child("users").child( uid ).setValue(user).addOnCompleteListener(){ task ->
                if( task.isSuccessful){
                    findNavController().navigate(R.id.action_regiDataFragment_to_homefragment)
                } else{
                    Toast.makeText(activity,"Der skete en fejl",Toast.LENGTH_SHORT).show()
                }
            }

        }
    }


}
