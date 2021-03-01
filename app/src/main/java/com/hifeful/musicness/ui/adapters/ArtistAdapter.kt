package com.hifeful.musicness.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hifeful.musicness.data.model.Artist
import com.hifeful.musicness.databinding.ItemArtistBinding

class ArtistAdapter
    : RecyclerView.Adapter<ArtistAdapter.ArtistViewHolder>() {
    private var mArtistList = mutableListOf<Artist>()

    var mOnArtistClickListener: OnArtistClickListener? = null

    inner class ArtistViewHolder(private val artistBinding: ItemArtistBinding) :
        RecyclerView.ViewHolder(artistBinding.root) {

        fun bind(artist: Artist) {
            Glide.with(itemView.context)
                .load(artist.image_url)
                .centerCrop()
                .into(artistBinding.artistImage)

            artistBinding.artistName.text = artist.name
            artistBinding.root.setOnClickListener { mOnArtistClickListener?.onArtistClick(artist) }
        }

    }

    interface OnArtistClickListener {
        fun onArtistClick(artist: Artist)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtistViewHolder {
        return ArtistViewHolder(ItemArtistBinding.inflate(LayoutInflater.from(parent.context),
            parent, false))
    }

    override fun getItemCount(): Int = mArtistList.size
    override fun onBindViewHolder(holder: ArtistViewHolder, position: Int) {
        holder.bind(mArtistList[position])
    }

    fun initArtists(artists: List<Artist>) {
        mArtistList.clear()
        mArtistList.addAll(artists)

        notifyDataSetChanged()
    }

    fun addArtist(artist: Artist) {
        mArtistList.add(artist)

        notifyDataSetChanged()
    }

    fun clearArtists() {
        mArtistList.clear()
    }
}
