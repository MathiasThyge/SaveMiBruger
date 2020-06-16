package com.example.savemi.home

import android.util.Log
import android.view.*
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.savemi.R

class HomeAdaptor(): RecyclerView.Adapter<HomeAdaptor.ViewHolder>() {

    val list = mutableListOf<HomeDataElement>()

   inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
       val text:TextView = itemView.findViewById(R.id.home_item_text)
       val title:TextView = itemView.findViewById(R.id.home_item_title)
       val image: ImageView = itemView.findViewById(R.id.home_item_image_view)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.home_list_item,parent,false))

    }

    override fun getItemCount() = list.size



    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val element = list[position]
        holder.text.text = element.text
        Log.d("Apaptor","elementText: "+element.text)
        Log.d("Apaptor", "elementPos: $position")


        when(element.type){
            HomeDataType.NAME -> {
                holder.title.text = "Navn"
                holder.image.setImageResource(R.drawable.navn_ind_24px)

            }
            HomeDataType.CPR -> {
                holder.title.text = "CPR"
                holder.image.setImageResource(R.drawable.cpr_24px)
            }
            HomeDataType.BLOD -> {
                holder.title.text = "Blod type"
                holder.image.setImageResource(R.drawable.blood_24px)
            }
            HomeDataType.MEDICIN -> {
                holder.title.text = "Medicin"
                holder.image.setImageResource(R.drawable.medicin_24px)
            }
            HomeDataType.ALLERGIE -> {
                holder.title.text = "Allergi"
                holder.image.setImageResource(R.drawable.allergi_24px)
            }
            HomeDataType.DONER -> {
                holder.title.text = "Doner"
                holder.image.setImageResource(R.drawable.doner_24px)
            }
            HomeDataType.EMERGENCY -> {
                holder.title.text = "Nød kontak information"
                holder.image.setImageResource(R.drawable.contact_24px)
            }
            HomeDataType.OTHER -> {
                holder.title.text = "Sygdomstilstand og andet"
                holder.image.setImageResource(R.drawable.medical_condition_and_other_24px_outlined)
            }
        }


    }
}



