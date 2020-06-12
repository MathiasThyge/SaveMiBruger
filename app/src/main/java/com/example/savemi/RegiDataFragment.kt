package com.example.savemi


import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.TranslateAnimation
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.*
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


    private var totalFields = 0
    //private val scrollLayout = view?.findViewById<ConstraintLayout>(R.id.RegiData_ScrollView_ContraintLayout)



    // Create user in db
    data class User(
        var Navn: String = "",
        var PersId: String = ""//,
        //var Donor: Boolean = FALSE,
        //var BeaconId: String?
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_regi_data,container,false)
    }

    override fun onViewCreated( view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.regiData_confirmButton).setOnClickListener {
            createUser()
            pluralInput("Allergier")
            pluralInput("Medicin")
            pluralInput("Other")
            pluralInput("")
            //findNavController().navigate(R.id.action_regiDataFragment_to_homefragment)       Use to control layout
        }

        var addField = true
        view.findViewById<ImageButton>(R.id.regiData_add).setOnClickListener(){
            if(addField) {
                val animationUp = AnimationUtils.loadAnimation(context, R.anim.slide_up)
                regiData_overlay_view.startAnimation(animationUp)
                regiData_overlay_view.visibility = LinearLayout.VISIBLE
                addField = !addField
            }
        }
        view.findViewById<ConstraintLayout>(R.id.RegiData_ScrollView_ContraintLayout).setOnClickListener(){
            if (!addField) {
                val animationDown = AnimationUtils.loadAnimation(context, R.anim.slide_down)
                regiData_overlay_view.startAnimation(animationDown)
                regiData_overlay_view.visibility = LinearLayout.GONE
            addField = !addField
            }
        }

        // Before when it was textView
        /*view.findViewById<TextView>(R.id.regiData_overlay_add_Medicine).setOnClickListener() {
            addMore()
            Log.d(logtag, "Knap trykket")
        }*/

        // Test with button instead of textView
        view.findViewById<Button>(R.id.regiData_overlay_add_Medicine).setOnClickListener() {
            val linearLayout = view?.findViewById<LinearLayout>(R.id.RegiData_ScrollView_ConstraintLayout_2)


            //val changeAddButton = view?.findViewById<ImageView>(regiData_add)
            RegiData_ScrollView_ConstraintLayout_2.visibility = ConstraintLayout.VISIBLE
            var newButton = EditText(activity)

            var layoutparam : LinearLayout.LayoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT)

            newButton.setText("Ny knap")

            linearLayout?.addView(newButton, layoutparam)
            //addMore()
            Log.d(logtag, "Knap trykket")
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
        if (RegiData_ScrollView_ContraintLayout.regiDataPersonalID.length() >= 12 && RegiData_ScrollView_ContraintLayout.regiDataPersonalID.length() <= 9) {
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


    fun addMore(){
        // Check amount of fields
        Log.d(logtag, "Total: " + totalFields)
        totalFields++
        if(totalFields > 100) {
            Log.d(logtag,"Total over 100" + totalFields)
            return
        }

        // New edittext
        //var editText  = EditText(this.context)

        // UNCOMMENT FOR REAL TEST
        //scrollLayout?.addView(editText)
        //Log.d(logtag,"EditText tilføjet")


        // Define parameters for ScrollLayout
        //var layoutparam : ConstraintLayout.LayoutParams = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT,100)
        //layoutparam.setMargins(30,20,30,0)
        //layoutparam.width = ConstraintLayout.LayoutParams.MATCH_PARENT

        // Test with LinearLayout
        var layoutparam : LinearLayout.LayoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,100)
        layoutparam.setMargins(30,20,30,0)
        layoutparam.width = LinearLayout.LayoutParams.MATCH_PARENT


        // Set parameters
        /*
        editText.layoutParams = layoutparam
        editText.id=totalFields
        val hejsa = "Dette fungerer$totalFields"
        editText.setText(hejsa)
        editText.setTag("EditText" + totalFields)
        */


        // Test for knowing how to do

    }
}
