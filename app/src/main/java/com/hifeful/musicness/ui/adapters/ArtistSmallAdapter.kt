package com.hifeful.musicness.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hifeful.musicness.R
import com.hifeful.musicness.data.model.Artist

class ArtistSmallAdapter : RecyclerView.Adapter<ArtistSmallAdapter.ArtistSmallViewHolder>() {
    var mArtistList = mutableListOf<Artist>()
    var mOnArtistClickListener: OnArtistSmallClickListener? = null
    var mLabel = ""

    inner class ArtistSmallViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.item_artist_small, parent, false)) {
        private val mImage: ImageView = itemView.findViewById(R.id.artist_small_image)
        private val mName: TextView = itemView.findViewById(R.id.artist_small_name)

        fun bind(artist: Artist) {
            mImage.transitionName = "${mLabel}_${artist.id}"
            Glide.with(itemView.context)
                .load(artist.image_url)
                .centerCrop()
                .into(mImage)

            mName.text = artist.name
            itemView.setOnClickListener { mOnArtistClickListener?.onArtistClick(artist, mImage) }
        }

    }

    interface OnArtistSmallClickListener {
        fun onArtistClick(artist: Artist, imageView: ImageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtistSmallViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ArtistSmallViewHolder(
            inflater,
            parent
        )
    }

    override fun getItemCount(): Int = mArtistList.size

    override fun onBindViewHolder(holder: ArtistSmallViewHolder, position: Int) {
        holder.bind(mArtistList[position])
    }
}