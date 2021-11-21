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

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mLocation: TextView = itemView.findViewById(R.id.location)
        var mEstTime: TextView = itemView.findViewById(R.id.estTime)
        var mLine: View = itemView.findViewById(R.id.divider)
        var mImageView: ImageView = itemView.findViewById(R.id.imageView)

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_route, parent, false)
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
            holder.mImageView.setColorFilter(
                ResourcesCompat.getColor(
                    mContext.resources,
                    R.color.green_dark,
                    null
                )
            )
        }
    }

    override fun getItemCount(): Int {
        return mRouteList.size
    }

}