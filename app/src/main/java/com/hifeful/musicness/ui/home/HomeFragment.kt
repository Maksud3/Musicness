package com.hifeful.musicness.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hifeful.musicness.R
import com.hifeful.musicness.data.model.Artist
import com.hifeful.musicness.common.adapters.ArtistAdapter
import com.hifeful.musicness.common.adapters.OnArtistClickListener
import com.hifeful.musicness.ui.base.BaseFragment
import com.hifeful.musicness.util.SpacesItemDecoration

class HomeFragment : BaseFragment(), HomeContract.View, OnArtistClickListener {
    private val TAG = HomeFragment::class.qualifiedName

    // UI
    private lateinit var mArtistRecyclerView: RecyclerView
    private lateinit var mArtistAdapter: ArtistAdapter

    private lateinit var mNavController: NavController

    // Variables
    private lateinit var mPresenter: HomePresenter
    private val mArtistList = mutableListOf<Artist>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mNavController = Navigation.findNavController(view)

        mArtistRecyclerView = view.findViewById(R.id.artist_recycler)
        mArtistAdapter = ArtistAdapter()
        mArtistRecyclerView.adapter = mArtistAdapter

        mArtistRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        mArtistRecyclerView.addItemDecoration(
            SpacesItemDecoration(
                requireContext(),
                R.dimen.mobile_margin
            )
        )
        mArtistRecyclerView.isNestedScrollingEnabled = false

        mArtistAdapter.mArtistList = mArtistList
        mArtistAdapter.mOnArtistClickListener = this
    }

    override fun onResume() {
        super.onResume()

        mPresenter = HomePresenter(this)
        mPresenter.getRandomArtists(50)
    }

    override fun onArtistClick() {
        Log.d(TAG, "onArtistClick: puk")

        mNavController.navigate(R.id.action_homeFragment_to_artistFragment)
    }

    override fun showArtist(artist: Artist) {
        Log.d(TAG, artist.toString())

        mArtistList.add(artist)
        mArtistAdapter.notifyDataSetChanged()
    }
}