package com.example.savemi

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ToggleButton
import androidx.navigation.fragment.findNavController
import com.example.savemi.R.id.action_scanForWristbandFragment_to_HomeFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.fragment_registrer.*
import kotlinx.coroutines.NonCancellable.cancel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


/**
 * A simple [Fragment] subclass.
 * Use the [ScanForWristbandFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ScanForWristbandFragment : Fragment() {

    private val beaconID = "c0efeb679e7f817c1443aa05b53d5703"
    private val beaconMajor = 62521
    private val beaconMinor = 408


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<ToggleButton>(R.id.scanwristband).setOnClickListener{
            //beginScanWristband()  // begin search of beaconID
            findNavController().navigate(R.id.action_scanForWristbandFragment_to_confirmWristbandFragment)
            //navigate to next page layout testcode
        }
        view.findViewById<ImageButton>(R.id.back_from_scanWristband).setOnClickListener {
            findNavController().navigate(action_scanForWristbandFragment_to_HomeFragment)
            } //leeds back from page
        }

    /*private fun beginScanWristband() {
     beaconID
        if ( val baconId = null) {
            MaterialAlertDialogBuilder(context)
                //.setTitle(resources.getString(R.string.title))
                .setMessage(resources.getString(R.string.Intet_armbånd_fundet_text))
                .setNeutralButton(resources.getString(R.string.Fortryd)) { dialog, which ->
                    // Respond to neutral button press
                }
                .setNegativeButton(resources.getString(R.string.Prøv_igen)) { dialog, which ->
                    // Respond to negative button press
                }
                .setPositiveButton(resources.getString(R.string.OK)) { dialog, which ->
                    // Respond to positive button press
                }
                .show() //show dialog alert message if their is no wristband nearby
        TODO("Not yet implemented")
    }*/

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_scan_for_wristband, container, false)
    }




    /*companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ScanForWristbandFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ScanForWristbandFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    } Tjek if this can be removed*/
}