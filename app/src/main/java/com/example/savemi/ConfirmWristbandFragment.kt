package com.example.savemi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.fragment_confirm_wristband.*

class ConfirmWristbandFragment : Fragment() {
    private val beaconID = "c0efeb679e7f817c1443aa05b53d5703"
    private val beaconMajor = 62521
    private val beaconMinor = 408



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        view.findViewById<Button>(R.id.connectWristband).setOnClickListener{
            //connectWristband()  // begin search of beaconID
            findNavController().navigate(R.id.action_confirmWristbandFragment_to_connectedWritsbandFragment)
            //navigate to next page layout testcode
        }

        /*view.findViewById<ImageButton>(R.id.cancel).setOnClickListener {
            findNavController().navigate(R.id.action_confirmWristbandFragment_to_HomeFragment)
        } //cancel connection to wristband*/
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_confirm_wristband, container, false)
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
