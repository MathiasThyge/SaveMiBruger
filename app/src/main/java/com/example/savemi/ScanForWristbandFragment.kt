package com.example.savemi

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ToggleButton
import androidx.navigation.fragment.findNavController
import com.example.savemi.R.id.action_scanForWristbandFragment_to_settingsFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_registrer.*
import kotlinx.android.synthetic.main.fragment_scan_for_wristband.*
import kotlinx.coroutines.NonCancellable.cancel




/**
 * A simple [Fragment] subclass.
 * Use the [ScanForWristbandFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ScanForWristbandFragment : Fragment() {

private val logtag = ScanForWristbandFragment::class.simpleName
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    data class BrugerUID(var brugerUID:String = "")


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(logtag, "Du er nu ved at oprette en beacon")


        view.findViewById<Button>(R.id.connectWristband).setOnClickListener{
            //beginScanWristband()  // begin search of beaconID
           Log.d(logtag, "Du har trykket på knappen scanwristband")

            opretBeacon()
            findNavController().navigate(R.id.action_scanForWristbandFragment_to_connectedWritsbandFragment)
            //navigate to next page layout testcode
        }
        view.findViewById<ImageView>(R.id.back_from_scanWristband).setOnClickListener {
            findNavController().navigate(R.id.action_scanForWristbandFragment_to_settingsFragment)
            } //leeds back from page


        }


    private fun opretBeacon(){
        val wristbndID = wristbndIDtextfield.text.toString()
        Log.d(logtag,"dette er wristbandID "+ wristbndID)

        if (wristbndID.isEmpty()) {
                 wristbndIDtextfield.error = "Udfyld venligst feltet"
            wristbndIDtextfield.requestFocus()
           // return
            }
        auth = FirebaseAuth.getInstance()
        database = Firebase.database.reference
        val useruid= auth.currentUser?.uid.toString()
    Log.d(logtag,"Dette er bruger id "+ useruid)
        var brugerUID = BrugerUID(useruid)

        database.child("ibeacon").child(wristbndID).setValue(brugerUID)

    }


    /*private fun sendEmail(wristbndID: String) {
        val mIntent = Intent(Intent.ACTION_SEND)
        /* To send an email you need to specify mailto: as URI using setData()
        method and data type will be to text/plain using settype() method
        */
        mIntent.data= Uri.parse("mailto: ")
        mIntent.type = "text/plain"
        // put email in intent
    }*/


    /* override fun onCreate(savedInstanceState: Bundle?) {
         super.onCreate(savedInstanceState)
         scanwristband.setOnClickListener{
             //var

         }

     }*/


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_scan_for_wristband, container, false)
    }





}