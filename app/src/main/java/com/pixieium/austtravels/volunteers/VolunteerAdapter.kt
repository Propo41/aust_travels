package com.pixieium.austtravels.volunteers

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.pixieium.austtravels.R
import com.pixieium.austtravels.models.Route
import com.pixieium.austtravels.models.Volunteer
import com.pixieium.austtravels.routes.RoutesAdapter
import com.squareup.picasso.Picasso

class VolunteerAdapter (routeList: ArrayList<Volunteer>) :
        RecyclerView.Adapter<VolunteerAdapter.ViewHolder>(){
    private val mRouteList: ArrayList<Volunteer> = routeList
    private lateinit var mContext: Context

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name: TextView = itemView.findViewById(R.id.name)
        var id: TextView = itemView.findViewById(R.id.university_id)
        var imageUrl: ImageView = itemView.findViewById(R.id.profile_image)


    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view: View =
                LayoutInflater.from(parent.context).inflate(R.layout.volunteer_layout, parent, false)
        mContext = view.context
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val currentItem: Volunteer = mRouteList[position]
        holder.name.text = currentItem.name
        holder.id.text = currentItem.roll
        Picasso.get().load(currentItem.imageUrl).into(holder.imageUrl)




    }
    override fun getItemCount(): Int {

        return mRouteList.size
    }
}