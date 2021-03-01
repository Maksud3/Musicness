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
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.TransitionInflater
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.hifeful.musicness.R
import com.hifeful.musicness.data.model.Artist
import com.hifeful.musicness.data.model.Song
import com.hifeful.musicness.databinding.FragmentArtistBinding
import com.hifeful.musicness.ui.adapters.SongAdapter
import com.hifeful.musicness.ui.base.BaseFragment
import moxy.ktx.moxyPresenter
import java.util.*

class ArtistFragment : BaseFragment(), ArtistView, SongAdapter.OnSongClickListener {
    private val TAG = ArtistFragment::class.qualifiedName

    // UI
    private var _binding: FragmentArtistBinding? = null
    private val binding get() = _binding!!
    private lateinit var mFavouriteMenuItem: MenuItem

    // Variables
    private val mPresenter by moxyPresenter { ArtistPresenter() }
    private lateinit var mNavController: NavController
    private val mArgs: ArtistFragmentArgs by navArgs()

    private lateinit var mSongAdapter: SongAdapter
    private lateinit var mCurrentArtist: Artist
    private var mIsCurrentArtistCached = false
    private var mIsFavouriteMenuItemPressed = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = TransitionInflater.from(requireContext())
            .inflateTransition(android.R.transition.move)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentArtistBinding.inflate(inflater, container, false)
        val view = binding.root

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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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
        (activity as AppCompatActivity).setSupportActionBar(binding.artistToolbar)
    }

    override fun setUpCollapsingToolbar() {
        binding.artistAppBar.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
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

    override fun showArtistDetails() {
        binding.artistToolbarLayout.title = mCurrentArtist.name

        if (mCurrentArtist.image_url.contains("default_avatar")) {
            showImage(this,
                mPresenter.getDrawableImageId(requireContext(), "genius_default_avatar"),
                binding.artistToolbarImage)
        } else {
            showImage(this, mCurrentArtist.image_url,
                binding.artistToolbarImage)
        }
    }

    override fun setUpSongRecycler() {
        mSongAdapter = SongAdapter().apply {
            mOnSongClickListener = this@ArtistFragment
        }

        binding.artistContent.artistSongsRecycler.apply {
            adapter = mSongAdapter
            layoutManager = LinearLayoutManager(requireContext(),
                LinearLayoutManager.VERTICAL, false)
            postponeEnterTransition()
            viewTreeObserver.addOnPreDrawListener {
                startPostponedEnterTransition()
                true
            }
        }
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
            mArgs.artist.isFavourite = false
            mPresenter.updateFavouriteArtist(mArgs.artist.id, mIsFavouriteMenuItemPressed)
            disableFavouriteButton()
        } else {
            mIsFavouriteMenuItemPressed = true
            mArgs.artist.isFavourite = true
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
        binding.artistFavourite.imageTintList = ColorStateList.valueOf(ContextCompat
            .getColor(requireContext(), R.color.favourite_pressed))
    }

    override fun disableFavouriteButton() {
        mFavouriteMenuItem.icon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_favorite)
        binding.artistFavourite.imageTintList = ColorStateList.valueOf(ContextCompat
            .getColor(requireContext(), R.color.white))
    }

    override fun setUpFab() {
        binding.artistFavourite.setOnClickListener { onFavouriteButtonClick() }
    }

    override fun showArtistPopularSongs(songs: List<Song>) {
        mSongAdapter.mSongList = songs.toMutableList()
    }

    override fun onSongClick(song: Song, imageView: ImageView) {
        val extras = FragmentNavigatorExtras(imageView to "song_toolbar_image")
        val action = ArtistFragmentDirections.actionArtistFragmentToSongFragment(song)

        mNavController.navigate(action, extras)
    }
}