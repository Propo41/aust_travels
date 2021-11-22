package com.pixieium.austtravels.volunteers

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.pixieium.austtravels.R
import com.pixieium.austtravels.models.UserInfo

class VolunteerAdapter(routeList: ArrayList<UserInfo>) :
    RecyclerView.Adapter<VolunteerAdapter.ViewHolder>() {
    private val mRouteList: ArrayList<UserInfo> = routeList
    private lateinit var mContext: Context

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name: TextView = itemView.findViewById(R.id.name)
        var id: TextView = itemView.findViewById(R.id.university_id)
        var imageUrl: ImageView = itemView.findViewById(R.id.profile_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_volunteer, parent, false)
        mContext = view.context
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem: UserInfo = mRouteList[position]
        holder.name.text = currentItem.name
        holder.id.text = currentItem.universityId
        holder.imageUrl.loadSvg(currentItem.userImage)
    }

    /**
     * By default, ImageViews don't support SVG formats.
     * So, instead we are using the coil library to render svg files
     */
    private fun ImageView.loadSvg(url: String) {
        val imageLoader = ImageLoader.Builder(this.context)
            .componentRegistry { add(SvgDecoder(this@loadSvg.context)) }
            .build()

        val request = ImageRequest.Builder(this.context)
            .crossfade(true)
            .crossfade(2)
            .data(url)
            .target(this)
            .build()

        imageLoader.enqueue(request)
    }

    override fun getItemCount(): Int {

        return mRouteList.size
    }
}