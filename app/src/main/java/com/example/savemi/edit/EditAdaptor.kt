package com.example.savemi.edit


import android.util.Log
import android.view.*
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.savemi.R

class EditAdaptor(): RecyclerView.Adapter<EditAdaptor.ViewHolder>() {

    private val logtag = EditAdaptor::class.simpleName
    val list = mutableListOf<EditDataElement>()
    var onItemClick: (Int) -> Unit = {}


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val text:TextView = itemView.findViewById(R.id.edit_item_text)
        val title:TextView = itemView.findViewById(R.id.edit_item_title)
        val image: ImageView = itemView.findViewById(R.id.edit_item_image_view)
        val edit: ImageView = itemView.findViewById(R.id.edit_edit_imageView)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.edit_list_item,parent,false))

    }

    override fun getItemCount() = list.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val element = list[position]
        holder.text.text = element.text
        Log.d("EditApaptor","elementText: "+element.text)
        Log.d("EditApaptor", "elementPos: $position")
        holder.edit.setOnClickListener{
            Log.d(logtag, "TEXT:")
            onItemClick(holder.adapterPosition)

        }

        when(element.type){
            EditDataType.NAME -> {
                holder.title.text = "Navn"
                holder.image.setImageResource(R.drawable.navn_ind_24px)
            }
            EditDataType.CPR -> {
                holder.title.text = "CPR"
                holder.image.setImageResource(R.drawable.cpr_24px)
            }
            EditDataType.BLOD -> {
                holder.title.text = "Blodtype"
                holder.image.setImageResource(R.drawable.blood_24px)
            }
            EditDataType.MEDICIN -> {
                holder.title.text = "Medicin"
                holder.image.setImageResource(R.drawable.medicin_24px)
            }
            EditDataType.ALLERGIE -> {
                holder.title.text = "Allergi"
                holder.image.setImageResource(R.drawable.allergi_24px)
            }
            EditDataType.DONER -> {
                holder.title.text = "Donor"
                holder.image.setImageResource(R.drawable.doner_24px)
            }
            EditDataType.EMERGENCY -> {
                holder.title.text = "Nødkontakt information"
                holder.image.setImageResource(R.drawable.contact_24px)
            }
            EditDataType.OTHER -> {
                holder.title.text = "Sygdomstilstand og andet"
                holder.image.setImageResource(R.drawable.medical_condition_and_other_24px_outlined)
            }
        }


    }
}



