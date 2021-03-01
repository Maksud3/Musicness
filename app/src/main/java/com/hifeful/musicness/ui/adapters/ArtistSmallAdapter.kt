package com.hifeful.musicness.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hifeful.musicness.data.model.Artist
import com.hifeful.musicness.databinding.ItemArtistSmallBinding

class ArtistSmallAdapter : RecyclerView.Adapter<ArtistSmallAdapter.ArtistSmallViewHolder>() {
    var mArtistList = mutableListOf<Artist>()
    var mOnArtistClickListener: OnArtistSmallClickListener? = null
    var mLabel = ""

    inner class ArtistSmallViewHolder(private val artistSmallBinding: ItemArtistSmallBinding) :
        RecyclerView.ViewHolder(artistSmallBinding.root) {

        fun bind(artist: Artist) {
            artistSmallBinding.artistSmallImage.apply {
                Glide.with(itemView.context)
                    .load(artist.image_url)
                    .centerCrop()
                    .into(this)
                transitionName = "${mLabel}_${artist.id}"
            }
            artistSmallBinding.artistSmallName.text = artist.name
            artistSmallBinding.root.setOnClickListener {
                mOnArtistClickListener?.onArtistClick(artist, artistSmallBinding.artistSmallImage) }
        }

    }

    interface OnArtistSmallClickListener {
        fun onArtistClick(artist: Artist, imageView: ImageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtistSmallViewHolder {
        return ArtistSmallViewHolder(ItemArtistSmallBinding.inflate(LayoutInflater.from(parent.context),
            parent, false))
    }

    override fun getItemCount(): Int = mArtistList.size

    override fun onBindViewHolder(holder: ArtistSmallViewHolder, position: Int) {
        holder.bind(mArtistList[position])
    }
}