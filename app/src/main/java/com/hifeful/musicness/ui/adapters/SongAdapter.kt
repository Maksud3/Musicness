package com.hifeful.musicness.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hifeful.musicness.R
import com.hifeful.musicness.data.model.Song

class SongAdapter : RecyclerView.Adapter<SongAdapter.SongViewHolder>() {
    var mSongList = mutableListOf<Song>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var mOnSongClickListener: OnSongClickListener? = null

    inner class SongViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.item_song, parent, false)) {
        private val mImage: ImageView = itemView.findViewById(R.id.song_image)
        private val mArtist: TextView = itemView.findViewById(R.id.song_artist)
        private val mTitle: TextView = itemView.findViewById(R.id.song_name)

        fun bind(song: Song) {
            mImage.transitionName = song.id.toString()
            Glide.with(itemView.context)
                .load(song.image)
                .override(300)
                .centerCrop()
                .into(mImage)

            mArtist.text = song.primary_artist
            mTitle.text = song.title
            itemView.setOnClickListener { mOnSongClickListener?.onSongClick(song, mImage) }
        }

    }

    interface OnSongClickListener {
        fun onSongClick(song: Song, imageView: ImageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return SongViewHolder(
            inflater,
            parent
        )
    }

    override fun getItemCount(): Int = mSongList.size

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        holder.bind(mSongList[position])
    }

}