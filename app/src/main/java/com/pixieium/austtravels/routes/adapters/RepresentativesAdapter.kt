package com.pixieium.austtravels.routes.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pixieium.austtravels.databinding.ItemRepresentativeBinding
import com.pixieium.austtravels.models.Representative
import timber.log.Timber
import android.content.Intent
import android.net.Uri


class RepresentativesAdapter(repList: ArrayList<Representative>) :
    RecyclerView.Adapter<RepresentativesAdapter.ViewHolder>() {
    private val mRepresentativeList: ArrayList<Representative> = repList
    private lateinit var mContext: Context

    class ViewHolder(binding: ItemRepresentativeBinding) : RecyclerView.ViewHolder(binding.root) {
        var mBinding: ItemRepresentativeBinding = binding
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemRepresentativeBinding = ItemRepresentativeBinding.inflate(
            LayoutInflater.from(viewGroup.context),
            viewGroup,
            false
        )
        mContext = viewGroup.context
        println("size:: " + mRepresentativeList.size)
        if (mRepresentativeList.size > 0) {
            binding.root.visibility = View.VISIBLE
        } else {
            binding.root.visibility = View.GONE
        }
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (mRepresentativeList.size > 0) {
            holder.mBinding.group1.visibility = View.VISIBLE
            holder.mBinding.textView5.text = mRepresentativeList[0].name
            holder.mBinding.imageView.setOnClickListener {
                // open intent
                Timber.d(mRepresentativeList[0].contact)
                val intent = Intent(
                    Intent.ACTION_DIAL,
                    Uri.fromParts("tel", mRepresentativeList[0].contact, null)
                )
                mContext.startActivity(intent)
            }
        }

        if (mRepresentativeList.size > 1) {
            holder.mBinding.group2.visibility = View.VISIBLE
            holder.mBinding.textView4.text = mRepresentativeList[1].name
            holder.mBinding.imageView7.setOnClickListener {
                // open intent
                Timber.d(mRepresentativeList[1].contact)
                val intent = Intent(
                    Intent.ACTION_DIAL,
                    Uri.fromParts("tel", mRepresentativeList[1].contact, null)
                )
                mContext.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int = 1

}