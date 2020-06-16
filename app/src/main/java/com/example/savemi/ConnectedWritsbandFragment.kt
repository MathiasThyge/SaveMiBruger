package com.example.savemi

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_wristband_connected.*


class ConnectedWritsbandFragment : Fragment() {
    private val logtag = ConnectedWritsbandFragment::class.simpleName

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var wristbandIDtext = confirmconnected.toString()
        Log.d(logtag,"Dette er text fra wristband "+ wristbandIDtext)


        view.findViewById<Button>(R.id.backtoprofile).setOnClickListener{
            //connectWristband()  // begin search of beaconID
            findNavController().navigate(R.id.action_connectedWritsbandFragment_to_HomeFragment)
            //navigate to next page layout testcode
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_wristband_connected, container, false)
    }




    /*companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ConnectedWritsbandFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ConnectedWritsbandFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }*/
}