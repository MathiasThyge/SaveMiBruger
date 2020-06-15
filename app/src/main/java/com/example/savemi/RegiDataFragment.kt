package com.example.savemi


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_regi_data.*
import kotlinx.android.synthetic.main.fragment_regi_data.view.*
import androidx.navigation.fragment.findNavController
import com.example.savemi.R.id.action_regiDataFragment_to_scanForWristbandFragment
import kotlin.math.log

/**
 * A simple [Fragment] subclass.
 */
class RegiDataFragment : Fragment() {
    private val logtag = RegiDataFragment::class.simpleName
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    // Used for animation state
    private var addField = true

    //  Amount of fields addded of each
    private var totalMedicinFields = 1
    private var totalAllergiFields = 1
    private var totalOtherFields = 1

    // ArrayLists for containing id's to check before adding to database
    private var medicinIdList = ArrayList<EditText>()
    private var allergiIdList = ArrayList<EditText>()
    private var otherIdList = ArrayList<EditText>()

    private var slideDownFields = ArrayList<Int>()

    // Create user in db
    data class User(
        var Navn: String = "",
        var PersId: String = ""//,

        // For later use
        //var Donor: Boolean = FALSE,
        //var BeaconId: String?
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_regi_data,container,false)
    }

    override fun onViewCreated( view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //slideDownFields.add(R.id.RegiData_ScrollView)

        view.findViewById<Button>(R.id.regiData_confirmButton).setOnClickListener {
            createUser()
            pluralInput("Allergier")
            pluralInput("Medicin")
            pluralInput("Other")
            pluralInput("")
            //findNavController().navigate(R.id.action_regiDataFragment_to_homefragment)       Use to control layout
        }

        //  Tested but then there needs to be added one for each editText view, and also for the new ones
        view.findViewById<EditText>(R.id.regiDataName).setOnClickListener(){

                slideDown()

        }

        view.findViewById<ImageButton>(R.id.regiData_add).setOnClickListener(){
            slideUp()
        }
        view.findViewById<ConstraintLayout>(R.id.RegiData_ScrollView_ContraintLayout).setOnClickListener(){
            slideDown()
        }

        view.findViewById<Button>(R.id.regiData_overlay_add_Medicin).setOnClickListener() {
            slideDown()
            addMore("Medicin")
        }
        view.findViewById<Button>(R.id.regiData_overlay_add_Allergi).setOnClickListener() {
            slideDown()
            addMore("Allergi")
        }
        view.findViewById<Button>(R.id.regiData_overlay_add_Other).setOnClickListener() {
            slideDown()
            addMore("Other")
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

        // Test for finding out how to get string from new editText
        /*
        Log.d(logtag,   "Array " + medicinIdList)
        for (newEditText in medicinIdList) {
            Log.d(logtag, "Array string - " + newEditText.text.toString())
            Log.d(logtag,"Antal felter " + totalMedicinFields)

        }
        */
        when (input) {
            "Medicin" -> {
                if (totalMedicinFields <= 1 && RegiData_ScrollView_ContraintLayout.regiDataMedicin1.text.toString().isEmpty()) {
                    return
                } else {
                    list.add(RegiData_ScrollView_ContraintLayout.regiDataMedicin1.text.toString())
                }

                // If added field, first must be filled - dont know how to do for the others
                if (totalMedicinFields > 1 && RegiData_ScrollView_ContraintLayout.regiDataMedicin1.text.toString().isEmpty()) {
                    RegiData_ScrollView_ContraintLayout.regiDataMedicin1.error =
                        "Udfyld venligst det første felt inden"
                    RegiData_ScrollView_ContraintLayout.regiDataMedicin1.requestFocus()
                    return
                } else {
                    for (newEditText in medicinIdList){
                        list.add(newEditText.text.toString())
                        Log.d(logtag,"Gennemlest tilføjede medicin: " + newEditText.text.toString())
                    }
                }
            }
            "Allergier" -> {
                if (totalAllergiFields <= 1 && RegiData_ScrollView_ContraintLayout.regiDataAllergi1.text.toString().isEmpty()) {
                    return
                } else {
                    list.add(RegiData_ScrollView_ContraintLayout.regiDataAllergi1.text.toString())
                }

                // If added field, first must be filled - dont know how to do for the others
                if (totalAllergiFields > 1 && RegiData_ScrollView_ContraintLayout.regiDataAllergi1.text.toString().isEmpty()) {
                    RegiData_ScrollView_ContraintLayout.regiDataAllergi1.error =
                        "Udfyld venligst det første felt inden"
                    RegiData_ScrollView_ContraintLayout.regiDataAllergi1.requestFocus()
                    return
                } else {
                    for (newEditText in allergiIdList){
                        list.add(newEditText.text.toString())
                        Log.d(logtag,"Gennemlest tilføjede allergi: " + newEditText.text.toString())
                    }
                }
            }
            "Other" -> {
                if (totalOtherFields <= 1 && RegiData_ScrollView_ContraintLayout.regiDataOther1.text.toString().isEmpty()) {
                    return
                } else {
                    list.add(RegiData_ScrollView_ContraintLayout.regiDataOther1.text.toString())
                }

                // If added field, first must be filled - dont know how to do for the others
                if (totalOtherFields > 1 && RegiData_ScrollView_ContraintLayout.regiDataOther1.text.toString().isEmpty()) {
                    RegiData_ScrollView_ContraintLayout.regiDataOther1.error =
                        "Udfyld venligst det første felt først"
                    RegiData_ScrollView_ContraintLayout.regiDataOther1.requestFocus()
                    return
                } else {
                    for (newEditText in otherIdList){
                        list.add(newEditText.text.toString())
                        Log.d(logtag,"Gennemlest tilføjede andre: " + newEditText.text.toString())
                    }
                }
            }
            else -> findNavController().navigate(action_regiDataFragment_to_scanForWristbandFragment)
        }

        Log.d(logtag, "Liste er: " + list)
        if (list.size > 0) {
            database.child("users").child(uid).child("$input").setValue(list)
                //Dette kan fjernes
                /*.addOnCompleteListener() { task ->
                    if (task.isSuccessful) {
                        Log.d(logtag, "Liste gennemlæst")
                        //findNavController().navigate(R.id.action_regiDataFragment_to_scanForWristbandFragment)
                    } else {
                        Toast.makeText(activity, "Der skete en fejl", Toast.LENGTH_SHORT).show()
                    }
                }*/   // Fjerne-slut
        }
    }

    private fun addMore( choosen: String ){
        var linearLayout = LinearLayout(activity)
        var newEditText = EditText(activity)

        // Calculate pixel value corresponding to wanted dp-value as parameter
        fun calculateDp( dpValue : Int ) : Int {
            var dpRatio = view?.resources!!.displayMetrics.density
            //  Check if value is correct
            //  Log.d(logtag, "Method Margin" + dpValue * dpRatio.toInt())
            return dpValue * dpRatio.toInt()
        }

        //  Find corresponding linearLayout after choosen field to add
        when(choosen){
            "Medicin"   -> {
                totalMedicinFields++
                if(totalMedicinFields > 5) {
                    Log.d(logtag, "TotalMedicinFields over 5 er - $totalMedicinFields")
                    Toast.makeText(activity,"Du har nået grænsen for antal medicin felter",Toast.LENGTH_SHORT).show()
                    return
                }
                linearLayout = requireView().findViewById<LinearLayout>(R.id.regiData_add_Medicin)
                regiData_add_Medicin.visibility = LinearLayout.VISIBLE
                newEditText.id = totalMedicinFields

                medicinIdList.add(newEditText)
            }
            "Allergi"   -> {
                totalAllergiFields++
                if(totalAllergiFields > 5) {
                    Log.d(logtag, "TotalAllergiFields over 5 er - $totalAllergiFields")
                    Toast.makeText(activity,"Du har nået grænsen for antal allergi felter",Toast.LENGTH_SHORT).show()
                    return
                }
                linearLayout = requireView().findViewById<LinearLayout>(R.id.regiData_add_Allergi)
                regiData_add_Allergi.visibility = LinearLayout.VISIBLE
                newEditText.id = totalAllergiFields

                allergiIdList.add(newEditText)
            }
            "Other"     -> {
                totalOtherFields++
                if(totalOtherFields > 5) {
                    Log.d(logtag, "TotalOtherFields over 5 er - $totalOtherFields")
                    Toast.makeText(activity,"Du har nået grænsen for antal andre felter",Toast.LENGTH_SHORT).show()
                    return
                }
                linearLayout = requireView().findViewById<LinearLayout>(R.id.regiData_add_Other)
                regiData_add_Other.visibility = LinearLayout.VISIBLE
                newEditText.id = totalOtherFields

                otherIdList.add(newEditText)
            }
        }

        var layoutParam : LinearLayout.LayoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT)
        layoutParam.height = calculateDp(50)
        layoutParam.setMargins(calculateDp(30),calculateDp(20),calculateDp(30),0)
        newEditText.setPadding(calculateDp(12),0,0,0)
        newEditText.setHint(choosen)
        newEditText.setEms(10)
        newEditText.setBackgroundResource(R.drawable.inputbox)

        linearLayout?.addView(newEditText, layoutParam)
    }
    // Animation method
    private fun slideUp(){
        if(addField) {
            val animationUp = AnimationUtils.loadAnimation(context, R.anim.slide_up)
            regiData_overlay_view.startAnimation(animationUp)
            regiData_overlay_view.visibility = LinearLayout.VISIBLE
            addField = !addField
        }
    }

    // Animation method
    private fun slideDown(){
        if (!addField) {
            val animationDown = AnimationUtils.loadAnimation(context, R.anim.slide_down)
            regiData_overlay_view.startAnimation(animationDown)
            regiData_overlay_view.visibility = LinearLayout.GONE
            addField = !addField
        }
    }
}
