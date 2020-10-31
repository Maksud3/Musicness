package com.hifeful.musicness.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hifeful.musicness.R
import com.hifeful.musicness.data.model.Artist
import com.hifeful.musicness.ui.adapters.ArtistAdapter
import com.hifeful.musicness.ui.base.BaseFragment
import com.hifeful.musicness.util.SpacesItemDecoration
import moxy.ktx.moxyPresenter

class HomeFragment : BaseFragment(), HomeView,
    ArtistAdapter.OnArtistClickListener {
    private val TAG = HomeFragment::class.qualifiedName

    // UI
    private lateinit var mArtistRecyclerView: RecyclerView
    private lateinit var mArtistAdapter: ArtistAdapter
    private lateinit var mFavouriteHeader: TextView
    private lateinit var mFavoriteArtistRecyclerView: RecyclerView
    private lateinit var mFavoriteArtistAdapter: ArtistAdapter

    private lateinit var mNavController: NavController

    // Variables
    private val mPresenter by moxyPresenter { HomePresenter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mNavController = Navigation.findNavController(view)

        setUpFavoriteSection()
        setUpArtistRecycler()

        if (savedInstanceState == null) {
            mPresenter.getFavoriteArtists()
            mPresenter.getRandomArtists(50)
        }
    }

    override fun setUpArtistRecycler() {
        mArtistRecyclerView = requireView().findViewById(R.id.home_artist_recycler)
        mArtistAdapter = ArtistAdapter()
        mArtistRecyclerView.adapter = mArtistAdapter

        mArtistRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        mArtistRecyclerView.addItemDecoration(
            SpacesItemDecoration(
                0,
                0,
                resources.getDimensionPixelSize(R.dimen.mobile_margin),
                resources.getDimensionPixelSize(R.dimen.mobile_margin)
            )
        )
        mArtistRecyclerView.isNestedScrollingEnabled = false

        mArtistAdapter.mOnArtistClickListener = this
    }

    override fun setUpFavoriteSection() {
        mFavouriteHeader = requireView().findViewById(R.id.home_favorite_artist_header)

        mFavoriteArtistRecyclerView = requireView().findViewById(R.id.home_favorite_artists_recycler)
        mFavoriteArtistAdapter = ArtistAdapter()
        mFavoriteArtistRecyclerView.adapter = mFavoriteArtistAdapter

        mFavoriteArtistRecyclerView.layoutManager = LinearLayoutManager(requireContext(),
            LinearLayoutManager.HORIZONTAL, false)
        mFavoriteArtistRecyclerView.addItemDecoration(SpacesItemDecoration(
            0,
            0,
            resources.getDimensionPixelSize(R.dimen.mobile_margin),
            0
        ))
        mFavoriteArtistAdapter.mOnArtistClickListener = this
    }

    override fun showFavoriteArtists(artists: List<Artist>) {
        if (artists.isNotEmpty()) mFavouriteHeader.visibility = View.VISIBLE
        mFavoriteArtistAdapter.initArtists(artists)
    }

    override fun onArtistClick(artist: Artist, artistImageView: ImageView) {
        val action = HomeFragmentDirections.actionHomeFragmentToArtistFragment(artist)
        Log.i(TAG, "onArtistClick: $artist")
        mNavController.navigate(action)
    }

    override fun showArtist(artist: Artist) {
        Log.d(TAG, artist.toString())

        mArtistAdapter.addArtist(artist)
    }

    override fun showArtists(artists: List<Artist>) {
        mArtistAdapter.initArtists(artists)
    }
}