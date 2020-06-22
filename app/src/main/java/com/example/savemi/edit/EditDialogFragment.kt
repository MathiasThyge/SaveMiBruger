package com.example.savemi.edit



import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.DialogInterface.BUTTON_POSITIVE
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.example.savemi.R
import kotlinx.android.synthetic.main.edit_dialog.*
import org.w3c.dom.Text


class EditDialogFragment : DialogFragment() {
    private lateinit var input: String

    @SuppressLint("ResourceAsColor", "InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        return activity?.let {
            // Use the Builder class for convenient dialog construction

            val builder = AlertDialog.Builder(it)

            val inflater = requireActivity().layoutInflater
            val view = inflater.inflate(R.layout.edit_dialog,null)
            builder.setMessage("Ændrer eller slet")
            builder.setTitle("Slet eller ændrer")
            builder.setView(view)
            builder.setCancelable(true)
            builder.setPositiveButton("Ændrer", DialogInterface.OnClickListener() { dialog, id ->

                Log.d("EDITDIALOG", "inpu:${view.findViewById<TextView>(R.id.edit_dialog_input).text.toString()}")
                input = view.findViewById<TextView>(R.id.edit_dialog_input).text.toString()

                        })
            builder.setNegativeButton("Slet", DialogInterface.OnClickListener { dialog, id ->
                input ="DELETE"
                        })
            builder.setNeutralButton(R.string.Annuller, DialogInterface.OnClickListener() {dialog,id ->
                        dialog.cancel()
            })




            // Create the AlertDialog object and return it
           builder.create();

        } ?: throw IllegalStateException("Activity cannot be null")
    }



    fun getInput(onInput: (String) -> Unit) {
        if (input != "null") onInput.invoke(input)


    }

}