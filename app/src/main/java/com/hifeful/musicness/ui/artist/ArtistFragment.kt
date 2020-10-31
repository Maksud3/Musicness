package com.hifeful.musicness.ui.artist

import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.hifeful.musicness.R
import com.hifeful.musicness.data.model.Artist
import com.hifeful.musicness.data.model.Song
import com.hifeful.musicness.ui.adapters.SongAdapter
import com.hifeful.musicness.ui.base.BaseFragment
import moxy.ktx.moxyPresenter
import java.util.*

class ArtistFragment : BaseFragment(), ArtistView {
    private val TAG = ArtistFragment::class.qualifiedName

    // UI
    private lateinit var mFavouriteMenuItem: MenuItem
    private lateinit var mAppBarLayout: AppBarLayout
    private lateinit var mCollapsingToolbarLayout: CollapsingToolbarLayout
    private lateinit var mToolbar: Toolbar
    private lateinit var mArtistImage: ImageView
    private lateinit var mSongRecyclerView: RecyclerView
    private lateinit var mSongAdapter: SongAdapter
    private lateinit var mFavouriteFab: FloatingActionButton

    // Variables
    private val mPresenter by moxyPresenter { ArtistPresenter() }
    private lateinit var mNavController: NavController
    private val mArgs: ArtistFragmentArgs by navArgs()
    private lateinit var mCurrentArtist: Artist
    private var mIsCurrentArtistCached = false
    private var mIsFavouriteMenuItemPressed = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_artist, container, false)
        setHasOptionsMenu(true)
        setUpToolbar(view)
        showDisplayHomeUp()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mNavController = findNavController()
        mCurrentArtist = mArgs.artist

        setUpSongRecycler()
        setUpFab()
        mPresenter.getArtistPopularSongs(mCurrentArtist.id)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_artist, menu)
        mFavouriteMenuItem = menu.findItem(R.id.action_favourite)
        mFavouriteMenuItem.isVisible = false
        initArtist()
        setUpCollapsingToolbar()
        showArtistDetails()
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                mNavController.popBackStack()
                return true
            }
            R.id.action_favourite -> {
                onFavouriteButtonClick()
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun setUpToolbar(view: View) {
        mToolbar = view.findViewById(R.id.artist_toolbar)
        (activity as AppCompatActivity).setSupportActionBar(mToolbar)
    }

    override fun setUpCollapsingToolbar() {
        mAppBarLayout = requireView().findViewById(R.id.artist_app_bar)
        mCollapsingToolbarLayout = requireView().findViewById(R.id.artist_toolbar_layout)

        mAppBarLayout.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
            var isShow = false
            var scrollRange = -1
            override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
                if (appBarLayout != null && scrollRange == -1) {
                    scrollRange = appBarLayout.totalScrollRange
                }

                if (scrollRange + verticalOffset == 0) {
                    isShow = true
                    mFavouriteMenuItem.isVisible = true
                } else if (isShow) {
                    isShow = false
                    mFavouriteMenuItem.isVisible = false
                }
            }
        })
    }

    override fun showDisplayHomeUp() {
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setHomeButtonEnabled(true)
    }

    override fun showArtistDetails() {
        mCollapsingToolbarLayout.title = mCurrentArtist.name
        mArtistImage = requireView().findViewById(R.id.artist_toolbar_image)
        showImage(this, mCurrentArtist.image_url, mArtistImage)
    }

    override fun setUpSongRecycler() {
        mSongRecyclerView = requireView().findViewById(R.id.artist_songs_recycler)
        mSongAdapter = SongAdapter()

        mSongRecyclerView.adapter = mSongAdapter

        mSongRecyclerView.layoutManager = LinearLayoutManager(requireContext(),
            LinearLayoutManager.VERTICAL, false)
    }

    override fun initArtist() {
        if (mArgs.artist.isFavourite) {
            Log.i(TAG, "initArtist: isFavourite")
            mIsCurrentArtistCached = true
            mIsFavouriteMenuItemPressed = true
            enableFavouriteButton()
        } else {
            Log.i(TAG, "initArtist: isNotFavourite")
            mPresenter.isFavouriteArtistExist(mArgs.artist.id)
        }
    }

    override fun isFavouriteArtistCached(isCached: Boolean) {
        mIsCurrentArtistCached = isCached
    }

    override fun initFavouriteButton(isPressed: Boolean) {
        Log.i(TAG, "initFavouriteButton: $isPressed")
        mIsFavouriteMenuItemPressed = isPressed
        if (mIsFavouriteMenuItemPressed) enableFavouriteButton() else disableFavouriteButton()
    }

    override fun onFavouriteButtonClick() {
        if (mIsFavouriteMenuItemPressed) {
            mIsFavouriteMenuItemPressed = false
            mPresenter.updateFavouriteArtist(mArgs.artist.id, mIsFavouriteMenuItemPressed)
            disableFavouriteButton()
        } else {
            mIsFavouriteMenuItemPressed = true
            val timestamp = Calendar.getInstance().time
            if (!mIsCurrentArtistCached) {
                mArgs.artist.isFavourite = mIsFavouriteMenuItemPressed
                mArgs.artist.timestamp = timestamp
                mPresenter.addFavouriteArtist(mArgs.artist)
                mIsCurrentArtistCached = true
            } else {
                mPresenter.updateFavouriteArtist(mArgs.artist.id, mIsFavouriteMenuItemPressed, timestamp)
            }
            enableFavouriteButton()
        }
    }

    override fun enableFavouriteButton() {
        mFavouriteMenuItem.icon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_favorite_pressed)
        mFavouriteFab.imageTintList = ColorStateList.valueOf(ContextCompat
            .getColor(requireContext(), R.color.favourite_pressed))
    }

    override fun disableFavouriteButton() {
        mFavouriteMenuItem.icon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_favorite)
        mFavouriteFab.imageTintList = ColorStateList.valueOf(ContextCompat
            .getColor(requireContext(), R.color.white))
    }

    override fun setUpFab() {
        mFavouriteFab = requireView().findViewById(R.id.artist_favourite)
        mFavouriteFab.setOnClickListener { onFavouriteButtonClick() }
    }

    override fun showArtistPopularSongs(songs: List<Song>) {
        mSongAdapter.mSongList = songs.toMutableList()
    }
}