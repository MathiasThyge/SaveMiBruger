package com.example.savemi.edit

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.savemi.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.edit_dialog.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class EditFragment: Fragment() {
    private val logtag = EditFragment::class.simpleName
    private val model: EditViewModel by activityViewModels()
    val auth = FirebaseAuth.getInstance()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_edit, container, false)

        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val currentUser = auth.currentUser!!
        val editListAdaptor = EditAdaptor()
        //Log.d(logtag, "$editListAdaptor")

        val list: RecyclerView = view.findViewById(R.id.edit_RecyclerView)
        list.layoutManager = LinearLayoutManager(view.context, RecyclerView.VERTICAL, false)
        list.adapter = editListAdaptor

        model.editUpdateRepo(currentUser)

        model.getEditData().observe(viewLifecycleOwner, Observer {
            it ?: return@Observer

            editListAdaptor.list.clear()
            Log.d(logtag,"elements: ${it.elements}")
            editListAdaptor.list.addAll(it.elements)
            editListAdaptor.notifyDataSetChanged()
            Log.d(logtag, "in observe")
        })


        view.findViewById<ImageView>(R.id.edit_back_to_home).setOnClickListener {
            findNavController().navigate(R.id.homeFragment3)
        }

         editListAdaptor.onItemClick = { position ->
             val type = editListAdaptor.list[position].type.toString()
            val count = editListAdaptor.list[position].count
             Log.d(logtag, "count: $count")

             showDialog(){
                 model.onChangesValues(auth.uid.toString(),it,type,count)
                 Log.d(logtag,"input: $it")
             }





        }





    }



    private fun showDialog(onInput: (String) -> Unit) {
        // Create an instance of the dialog fragment and show it
        val dialog = EditDialogFragment()
        dialog.show(this.parentFragmentManager, "NoticeDialogFragment")

        dialog.getInput {
             onInput.invoke(it)
            Log.d(logtag, "invoke else: $it")

        }

    }

}