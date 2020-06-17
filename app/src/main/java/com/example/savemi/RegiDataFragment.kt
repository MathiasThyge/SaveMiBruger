package com.example.savemi


import android.app.Activity
import android.inputmethodservice.Keyboard
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_regi_data.*
import kotlinx.android.synthetic.main.fragment_regi_data.view.*
import androidx.navigation.fragment.findNavController


/**
 * A simple [Fragment] subclass.
 */
class RegiDataFragment : Fragment() {
    private val logtag = RegiDataFragment::class.simpleName
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    // Used for animation state
    private var addField = true

    // If input is wrong
    private var fejlInput = false

    //  Amount of fields addded of each
    private var totalMedicinFields = 1
    private var totalAllergiFields = 1
    private var totalOtherFields = 1

    // ArrayLists for containing id's to check before adding to database
    private var medicinIdList = ArrayList<EditText>()
    private var allergiIdList = ArrayList<EditText>()
    private var otherIdList = ArrayList<EditText>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_regi_data,container,false)


        return view
    }

    override fun onViewCreated( view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fun hideSoftKeyBoard( view: View) {
            try {
                val imm = context?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        view.findViewById<ImageView>(R.id.back_from_regiData).setOnClickListener(){
            hideSoftKeyBoard(view)
            FirebaseAuth.getInstance().currentUser?.delete()
            findNavController().navigateUp()
            Toast.makeText(activity,"Din bruger er blevet slettet, opret venligst bruger på ny",Toast.LENGTH_LONG).show()
        }




        view.findViewById<Button>(R.id.regiData_confirmButton).setOnClickListener {
            hideSoftKeyBoard(view)
            createUser()
            writeToFireBase("Medicin")
            writeToFireBase("Allergier")
            writeToFireBase("Andet")
            writeToFireBase("Blodtype")
            writeToFireBase("Donor")
            writeToFireBase("Kontaktperson")
            writeToFireBase("")
            //findNavController().navigate(R.id.action_regiDataFragment_to_scanForWristbandFragment)

        }
        view.findViewById<ImageButton>(R.id.regiData_add).setOnClickListener(){
            hideSoftKeyBoard(view)
            slideUp()
        }
        view.findViewById<ConstraintLayout>(R.id.RegiData_ScrollView_ContraintLayout).setOnClickListener(){
            hideSoftKeyBoard(view)
            slideDown()
        }
        var slideDownEditText = ArrayList<EditText>()
        slideDownEditText.add(view?.findViewById<EditText>(R.id.regiDataName))
        slideDownEditText.add(view?.findViewById<EditText>(R.id.regiDataPersonalID))
        slideDownEditText.add(view?.findViewById<EditText>(R.id.regiDataMedicin1))
        slideDownEditText.add(view?.findViewById<EditText>(R.id.regiDataAllergi1))
        slideDownEditText.add(view?.findViewById<EditText>(R.id.regiDataOther1))
        slideDownEditText.add(view?.findViewById<EditText>(R.id.regiDataKontaktNavn))
        slideDownEditText.add(view?.findViewById<EditText>(R.id.regiDataKontaktNummer))

        var slideDownSpinner = ArrayList<Spinner>()
        slideDownSpinner.add(view?.findViewById<Spinner>(R.id.regiDataBlodType))
        slideDownSpinner.add(view?.findViewById<Spinner>(R.id.regiDataDonor))



        fun slideAllEditTextDown ( pressed :  EditText) {
            pressed.setOnTouchListener(object : View.OnTouchListener {
                override fun onTouch(view: View, event: MotionEvent?): Boolean {
                    when (event?.action) {
                        MotionEvent.ACTION_UP -> slideDown()
                    }
                    return view.onTouchEvent(event) ?: true
                }
            })
        }
        fun slideAllSpinnerDown ( pressed :  Spinner) {
            pressed.setOnTouchListener(object : View.OnTouchListener {
                override fun onTouch(view: View, event: MotionEvent?): Boolean {
                    when (event?.action) {
                        MotionEvent.ACTION_UP -> slideDown()
                    }
                    hideSoftKeyBoard(view)
                    return view.onTouchEvent(event) ?: true
                }
            })
        }
        for ( EditText in slideDownEditText ) {
            slideAllEditTextDown( EditText )
        }
        for ( Spinner in slideDownSpinner ) {
            slideAllSpinnerDown( Spinner )
        }

        view.findViewById<Button>(R.id.regiData_overlay_add_Medicin).setOnClickListener() {
            hideSoftKeyBoard(view)
            slideDown()
            addMore("Medicin")
        }
        view.findViewById<Button>(R.id.regiData_overlay_add_Allergi).setOnClickListener() {
            hideSoftKeyBoard(view)
            slideDown()
            addMore("Allergier")
        }
        view.findViewById<Button>(R.id.regiData_overlay_add_Other).setOnClickListener() {
            hideSoftKeyBoard(view)
            slideDown()
            addMore("Andet")
        }
    }

    private fun createUser(){
        auth = FirebaseAuth.getInstance()
        database = Firebase.database.reference

        val currentUser = auth.currentUser

        if (RegiData_ScrollView_ContraintLayout.regiDataName.text.toString().isEmpty()) {
            RegiData_ScrollView_ContraintLayout.regiDataName.error = "Indtast navn"
            RegiData_ScrollView_ContraintLayout.regiDataName.requestFocus()
            fejlInput = true
            return
        }
        if (RegiData_ScrollView_ContraintLayout.regiDataPersonalID.text.toString().isEmpty()) {
            RegiData_ScrollView_ContraintLayout.regiDataPersonalID.error = "Angiv CPR"
            RegiData_ScrollView_ContraintLayout.regiDataPersonalID.requestFocus()
            fejlInput = true
            return
        }
        if (RegiData_ScrollView_ContraintLayout.regiDataPersonalID.length() >= 12 || RegiData_ScrollView_ContraintLayout.regiDataPersonalID.length() <= 9) {
            Log.d(logtag, "Length of CPR in:" + RegiData_ScrollView_ContraintLayout.regiDataPersonalID.length())
            RegiData_ScrollView_ContraintLayout.regiDataPersonalID.error = "Angiv korrekt CPR"
            RegiData_ScrollView_ContraintLayout.regiDataPersonalID.requestFocus()
            fejlInput = true
            return
        }   else fejlInput = false

        if( currentUser != null ) {
            data class User(
                var Navn: String = "",
                var PersId: String = ""//,
            )

            Log.d(logtag, "Length of CPR:" + RegiData_ScrollView_ContraintLayout.regiDataPersonalID.length())

            val uid = auth.currentUser?.uid.toString()
            val Navn = RegiData_ScrollView_ContraintLayout.regiDataName.text.toString()
            val PersId = RegiData_ScrollView_ContraintLayout.regiDataPersonalID.text.toString()
            Log.d(logtag, "Indtastet nav og cpr: " + Navn + " " + PersId)
            val user = User( Navn, PersId )
            Log.d(logtag, "User: " + user)

            database.child("users").child( uid ).setValue(user).addOnCompleteListener(){ task ->
                if( !task.isSuccessful){
                    Toast.makeText(activity,"Der skete en fejl",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun writeToFireBase(input: String) {
        auth = FirebaseAuth.getInstance()
        database = Firebase.database.reference
        val uid = auth.currentUser?.uid.toString()
        var list = ArrayList<String>()
        // Test for finding out how to get string from new editText
        /*Log.d(logtag,   "Array " + medicinIdList)
        for (newEditText in medicinIdList) {
            Log.d(logtag, "Array string - " + newEditText.text.toString())
            Log.d(logtag,"Antal felter " + totalMedicinFields)
            Log.d(logtag,"Id felter: " + newEditText.id)
        }*/

        when (input) {
            "Medicin" -> {
                if (totalMedicinFields == 1 && !RegiData_ScrollView_ContraintLayout.regiDataMedicin1.text.toString().isEmpty()) {
                    list.add(RegiData_ScrollView_ContraintLayout.regiDataMedicin1.text.toString())
                }
                if( totalMedicinFields > 1){
                    for (newEditText in medicinIdList){
                        if (!newEditText.text.toString().isEmpty()){
                            list.add(newEditText.text.toString())
                            Log.d(logtag,"Gennemlest tilføjede medicin: " + newEditText.text.toString())
                        } else (Log.d(logtag, "andet felt er tomt"))
                    }
                }
            }
            "Allergier" -> {
                if (totalAllergiFields == 1 && !RegiData_ScrollView_ContraintLayout.regiDataAllergi1.text.toString().isEmpty()) {
                    list.add(RegiData_ScrollView_ContraintLayout.regiDataAllergi1.text.toString())
                }
                if( totalAllergiFields > 1){
                    for (newEditText in allergiIdList){
                        if (!newEditText.text.toString().isEmpty()){
                            list.add(newEditText.text.toString())
                            Log.d(logtag,"Gennemlest tilføjede allergi: " + newEditText.text.toString())
                        } else (Log.d(logtag, "andet felt er tomt"))
                    }
                }
            }
            "Andet" -> {
                if (totalOtherFields == 1 && !RegiData_ScrollView_ContraintLayout.regiDataOther1.text.toString().isEmpty()) {
                    list.add(RegiData_ScrollView_ContraintLayout.regiDataOther1.text.toString())
                }
                if( totalOtherFields > 1 ){
                    for (newEditText in otherIdList){
                        if (!newEditText.text.toString().isEmpty()){
                            list.add(newEditText.text.toString())
                            Log.d(logtag,"Gennemlest tilføjede andet: " + newEditText.text.toString())
                        } else (Log.d(logtag, "andet felt er tomt"))
                    }
                }
            }
            "Blodtype" -> {
                var spinner = view?.findViewById<Spinner>(R.id.regiDataBlodType)?.selectedItem
                if (spinner.toString() == input ){
                    return
                } else {
                    database.child("users").child(uid).child(input).setValue(spinner.toString())
                }
            }
            "Donor" -> {
                var spinner = view?.findViewById<Spinner>(R.id.regiDataDonor)?.selectedItem
                if (spinner.toString().equals(input)){
                    database.child("users").child(uid).child(input).setValue("Nej")
                    return
                } else {
                    database.child("users").child(uid).child(input).setValue(spinner.toString())
                }
            }
            "Kontaktperson" -> {
                data class KontakInfo (
                    var navn : String = "",
                    var nummer : String = ""
                )
                var kontaktNavn = RegiData_ScrollView_ContraintLayout.regiDataKontaktNavn
                var kontaktNummer = RegiData_ScrollView_ContraintLayout.regiDataKontaktNummer

                if ( kontaktNavn.text.toString().isEmpty() && kontaktNummer.text.toString().isEmpty() ) {
                    return
                } else if ( kontaktNavn.text.toString().isEmpty() && !kontaktNummer.text.toString().isEmpty()) {
                    kontaktNavn.error = "Udfyld også navn"
                    kontaktNavn.requestFocus()
                    fejlInput = true
                } else if ( !kontaktNavn.text.toString().isEmpty() && kontaktNummer.text.toString().isEmpty()) {
                    kontaktNummer.error = "Udfyld også nummer"
                    kontaktNummer.requestFocus()
                    fejlInput = true
                } else {
                    var registreretInfo = KontakInfo( kontaktNavn.text.toString() , kontaktNummer.text.toString() )
                    database.child("users").child(uid).child(input).setValue( registreretInfo )
                }
            }
            else -> if(fejlInput == false) findNavController().navigate(R.id.action_regiDataFragment_to_scanForWristbandFragment)
        }
        when (input) {
            "Medicin","Allergier","Andet" -> {
                Log.d(logtag, "Liste er: " + list)
                if (list.size > 0) {
                    database.child("users").child(uid).child(input).setValue(list)
                }
            }
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
            "Allergier"   -> {
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
            "Andet"     -> {
                totalOtherFields++
                if(totalOtherFields > 5) {
                    Log.d(logtag, "TotalOtherFields over 5 er - $totalOtherFields")
                    Toast.makeText(activity,"Du har nået grænsen for antal andet felter",Toast.LENGTH_SHORT).show()
                    return
                }
                linearLayout = requireView().findViewById<LinearLayout>(R.id.regiData_add_Other)
                regiData_add_Other.visibility = LinearLayout.VISIBLE
                newEditText.id = totalOtherFields

                otherIdList.add(newEditText)
            }
        }
        newEditText.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(view: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_UP -> slideDown()
                }
                return view?.onTouchEvent(event) ?: true
            }
        })
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
        if(addField == true) {
            val animationUp = AnimationUtils.loadAnimation(context, R.anim.slide_up)
            regiData_overlay_view.startAnimation(animationUp)

            regiData_overlay_view.visibility = LinearLayout.VISIBLE
            addField = false
        }
    }

    // Animation method
    private fun slideDown(){
        if (addField == false) {
            val animationDown = AnimationUtils.loadAnimation(context, R.anim.slide_down)
            regiData_overlay_view.startAnimation(animationDown)
            regiData_overlay_view.visibility = LinearLayout.GONE
            addField = true
        }
    }
}
