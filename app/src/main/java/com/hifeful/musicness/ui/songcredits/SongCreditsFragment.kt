package com.hifeful.musicness.ui.songcredits

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hifeful.musicness.R
import com.hifeful.musicness.data.model.Artist
import com.hifeful.musicness.data.model.SongCredits
import com.hifeful.musicness.ui.adapters.ArtistAdapter
import com.hifeful.musicness.ui.adapters.ArtistSmallAdapter
import com.hifeful.musicness.ui.base.BaseFragment

class SongCreditsFragment : BaseFragment(), SongCreditsView, ArtistSmallAdapter.OnArtistSmallClickListener {
    // UI
    private lateinit var mToolbar: androidx.appcompat.widget.Toolbar
    private lateinit var mReleaseDate: TextView
    private lateinit var mPrimaryArtistLayout: LinearLayout
    private lateinit var mPrimaryArtistImage: ImageView
    private lateinit var mPrimaryArtistName: TextView

    // Variables
    private lateinit var mNavController: NavController
    private val mArgs: SongCreditsFragmentArgs by navArgs()
    private lateinit var mSongCredits: SongCredits

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_song_credits, container, false)
        setHasOptionsMenu(true)
        setUpToolbar(view)
        showDisplayHomeUp()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mNavController = findNavController()
        mSongCredits = mArgs.songCredits
        setUpSongCredits()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                mNavController.popBackStack()
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun showImage(fragment: Fragment, image: String, imageView: ImageView) {
        Glide.with(fragment)
            .load(image)
            .centerCrop()
            .into(imageView)
    }

    override fun setUpToolbar(view: View) {
        mToolbar = view.findViewById(R.id.song_credits_toolbar)
        (activity as AppCompatActivity).setSupportActionBar(mToolbar)
    }

    override fun setUpSongCredits() {
        mReleaseDate = requireView().findViewById(R.id.song_credits_release_date)
        mReleaseDate.text = mSongCredits.releaseDate
        setUpPrimaryArtist()
        setUpStaff(mSongCredits.featuredArtists,
            R.id.song_credits_featured_artists, R.id.song_credits_featured_artists_recycler,
            "featured")
        setUpStaff(mSongCredits.producerArtists,
            R.id.song_credits_producers, R.id.song_credits_producers_recycler,
            "producer")
        setUpStaff(mSongCredits.writerArtists,
            R.id.song_credits_writers, R.id.song_credits_writers_recycler,
            "writer")
    }

    override fun setUpPrimaryArtist() {
        mPrimaryArtistLayout = requireView().findViewById(R.id.song_credits_primary_artist)
        mPrimaryArtistLayout.setOnClickListener { onArtistClick(mSongCredits.primaryArtist, mPrimaryArtistImage) }
        mPrimaryArtistImage = requireView().findViewById(R.id.artist_small_image)
        mPrimaryArtistImage.transitionName = "primary_${mSongCredits.primaryArtist.id}"
        showImage(this, mSongCredits.primaryArtist.image_url, mPrimaryArtistImage)
        mPrimaryArtistName = requireView().findViewById(R.id.artist_small_name)
        mPrimaryArtistName.text = mSongCredits.primaryArtist.name
    }

    override fun setUpStaff(staffList: List<Artist>, headerId: Int, recyclerId: Int, label: String) {
        if (staffList.isNotEmpty()) {
            requireView().findViewById<TextView>(headerId).apply { isVisible = true }

            val staffRecycler = requireView().findViewById<RecyclerView>(recyclerId)
            val staffAdapter = ArtistSmallAdapter().apply {
                mArtistList = staffList.toMutableList()
                mLabel = label
                mOnArtistClickListener = this@SongCreditsFragment
            }

            staffRecycler.apply {
                adapter = staffAdapter
                layoutManager = LinearLayoutManager(requireContext(),
                    LinearLayoutManager.VERTICAL, false)
                postponeEnterTransition()
                viewTreeObserver.addOnPreDrawListener {
                    startPostponedEnterTransition()
                    true
                }
            }
        }
    }

    override fun onArtistClick(artist: Artist, imageView: ImageView) {
        val extras = FragmentNavigatorExtras(imageView to "artist_toolbar_image")
        val action = SongCreditsFragmentDirections.actionSongCreditsFragmentToArtistFragment2(artist)
        mNavController.navigate(action, extras)
    }
}