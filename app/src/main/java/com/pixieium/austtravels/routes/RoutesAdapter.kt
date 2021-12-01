package com.pixieium.austtravels.routes

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

class RoutesAdapter(routeList: ArrayList<Route>) :
    RecyclerView.Adapter<RoutesAdapter.ViewHolder>() {
    private val mRouteList: ArrayList<Route> = routeList
    private lateinit var mContext: Context

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val mLocation: TextView = itemView.findViewById(R.id.location)
        val mEstTime: TextView = itemView.findViewById(R.id.estTime)
        val mLine: View = itemView.findViewById(R.id.divider)
        val mImageView: ImageView = itemView.findViewById(R.id.imageView)

    }


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_route, viewGroup, false)
        mContext = view.context
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem: Route = mRouteList[position]

        holder.mLocation.text = currentItem.place
        holder.mEstTime.text = currentItem.estTime


        /*change color of first and last icon*/
        if (position == mRouteList.size - 1) {
            holder.mLine.visibility = View.GONE
            holder.mImageView.setColorFilter(
                ResourcesCompat.getColor(
                    mContext.resources,
                    R.color.orange,
                    null
                )
            )
        } else if (position == 0) {
            holder.mLine.visibility = View.VISIBLE
            holder.mImageView.setColorFilter(
                ResourcesCompat.getColor(
                    mContext.resources,
                    R.color.green_dark,
                    null
                )
            )
        }else{
            holder.mLine.visibility = View.VISIBLE
            holder.mImageView.setColorFilter(
                ResourcesCompat.getColor(
                    mContext.resources,
                    R.color.black,
                    null
                )
            )
        }
    }

    override fun getItemCount(): Int {
        return mRouteList.size
    }

}