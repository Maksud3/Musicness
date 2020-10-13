package com.hifeful.musicness.common.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hifeful.musicness.R
import com.hifeful.musicness.data.model.Artist

class ArtistAdapter
    : RecyclerView.Adapter<ArtistAdapter.ArtistViewHolder>() {

    var mArtistList = mutableListOf<Artist>()
        set(value) {
        field = value
        notifyDataSetChanged()
    }

    var mOnArtistClickListener: OnArtistClickListener? = null

    inner class ArtistViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.item_artist, parent, false)) {
        private val mImage: ImageView = itemView.findViewById(R.id.artist_image)
        private val mName: TextView = itemView.findViewById(R.id.artist_name)

        fun bind(artist: Artist) {
            Glide.with(itemView.context)
                .load(artist.image_url)
                .centerCrop()
                .into(mImage)

            mName.text = artist.name
            itemView.setOnClickListener { mOnArtistClickListener?.onArtistClick() }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtistViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ArtistViewHolder(
            inflater,
            parent
        )
    }

    override fun getItemCount(): Int = mArtistList.size

    override fun onBindViewHolder(holder: ArtistViewHolder, position: Int) {
        holder.bind(mArtistList[position])
    }
}

interface OnArtistClickListener {
    fun onArtistClick()
}