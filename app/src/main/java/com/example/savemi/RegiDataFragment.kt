package com.example.savemi


import android.os.Bundle
import android.util.Log
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
import kotlin.concurrent.timerTask

/**
 * A simple [Fragment] subclass.
 */
class RegiDataFragment : Fragment() {
    private val logtag = RegiDataFragment::class.simpleName
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    // Create user in db
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

            pluralInput("Allergier")
            pluralInput("Medicin")
            pluralInput("Other")
            pluralInput("")

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
        if (RegiData_ScrollView_ContraintLayout.regiDataPersonalID.length() >= 14 && RegiData_ScrollView_ContraintLayout.regiDataPersonalID.length() <= 11) {
            Log.d(logtag, "Length of CPR in:" + RegiData_ScrollView_ContraintLayout.regiDataPersonalID.length())
            RegiData_ScrollView_ContraintLayout.regiDataPersonalID.error = "Angiv korrekt CPR"
            RegiData_ScrollView_ContraintLayout.regiDataPersonalID.requestFocus()
            return
        }
        if( currentUser != null ) {
            Log.d(logtag, "Length of CPR:" + RegiData_ScrollView_ContraintLayout.regiDataPersonalID.length())

            val uid = auth.currentUser?.uid.toString()
            val Navn = RegiData_ScrollView_ContraintLayout.regiDataName.text.toString()
            val PersId = RegiData_ScrollView_ContraintLayout.regiDataPersonalID.text.toString()
            Log.d(logtag, "Indtastet nav og cpr: " + Navn + " " + PersId)
            val user = User( Navn, PersId )
            Log.d(logtag, "User: " + user)
            database.child("users").child( uid ).setValue(user).addOnCompleteListener(){ task ->
                if( task.isSuccessful){
                    //findNavController().navigate(R.id.action_regiDataFragment_to_homefragment)
                } else{
                    Toast.makeText(activity,"Der skete en fejl",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun pluralInput(input: String) {
        var list = ArrayList<String>()
        auth = FirebaseAuth.getInstance()
        database = Firebase.database.reference
        val uid = auth.currentUser?.uid.toString()

        when (input) {
            "Allergier" -> {
                if (RegiData_ScrollView_ContraintLayout.regiDataAllergie.text.toString().isEmpty() ||
                    RegiData_ScrollView_ContraintLayout.regiDataAllergie2.text.toString().isEmpty()) {
                    return
                } else {
                    list.add(RegiData_ScrollView_ContraintLayout.regiDataAllergie.text.toString())
                    list.add(RegiData_ScrollView_ContraintLayout.regiDataAllergie2.text.toString())
                }
            }
            "Medicin" -> {
                if (RegiData_ScrollView_ContraintLayout.regiDataMedicin.text.toString().isEmpty()) {
                    return
                } else {
                    list.add(RegiData_ScrollView_ContraintLayout.regiDataMedicin.text.toString())
                }
            }
            "Other" -> {
                if (RegiData_ScrollView_ContraintLayout.regiDataOther.text.toString().isEmpty()) {
                    return
                } else {
                    list.add(RegiData_ScrollView_ContraintLayout.regiDataOther.text.toString())
                }
            }
            else -> findNavController().navigate(R.id.action_regiDataFragment_to_scanForWristbandFragment)
        }
        Log.d(logtag, "Liste er: " + list)
        if (list.size > 0) {
            database.child("users").child(uid).child("$input").setValue(list)
                //Dette kan fjernes
                .addOnCompleteListener() { task ->
                    if (task.isSuccessful) {
                        Log.d(logtag, "Liste gennemlæst")
                        //findNavController().navigate(R.id.action_regiDataFragment_to_scanForWristbandFragment)
                    } else {
                        Toast.makeText(activity, "Der skete en fejl", Toast.LENGTH_SHORT).show()
                    }
                }   // Fjerne-slut
        }
    }
}
