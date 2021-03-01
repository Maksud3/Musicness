package com.hifeful.musicness.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hifeful.musicness.data.model.Song
import com.hifeful.musicness.databinding.ItemSongBinding

class SongAdapter : RecyclerView.Adapter<SongAdapter.SongViewHolder>() {
    var mSongList = mutableListOf<Song>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var mOnSongClickListener: OnSongClickListener? = null

    inner class SongViewHolder(private val songBinding: ItemSongBinding) :
        RecyclerView.ViewHolder(songBinding.root) {

        fun bind(song: Song) {
            songBinding.songImage.apply {
                Glide.with(itemView.context)
                    .load(song.image)
                    .override(300)
                    .centerCrop()
                    .into(this)
                transitionName = song.id.toString()
            }

            songBinding.songArtist.text = song.primary_artist
            songBinding.songName.text = song.title
            songBinding.root.setOnClickListener { mOnSongClickListener?.onSongClick(song, songBinding.songImage) }
        }

    }

    interface OnSongClickListener {
        fun onSongClick(song: Song, imageView: ImageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        return SongViewHolder(ItemSongBinding.inflate(LayoutInflater.from(parent.context),
            parent, false))
    }

    override fun getItemCount(): Int = mSongList.size

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        holder.bind(mSongList[position])
    }

}