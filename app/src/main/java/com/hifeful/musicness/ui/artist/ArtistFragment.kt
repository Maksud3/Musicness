package com.hifeful.musicness.ui.artist

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.*
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.doOnPreDraw
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.hifeful.musicness.R
import com.hifeful.musicness.data.model.Artist
import com.hifeful.musicness.ui.base.BaseFragment

class ArtistFragment : BaseFragment(), ArtistContract.View {
    private val TAG = ArtistFragment::class.qualifiedName

    // UI
    private lateinit var mNavController: NavController

    private lateinit var mFavouriteMenuItem: MenuItem
    private lateinit var mAppBarLayout: AppBarLayout
    private lateinit var mCollapsingToolbarLayout: CollapsingToolbarLayout
    private lateinit var mToolbar: Toolbar
    private lateinit var mArtistImage: ImageView

    // Variables
    private lateinit var mPresenter: ArtistContract.Presenter
    private val mArgs: ArtistFragmentArgs by navArgs()

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

        showArtistImage()
        setUpCollapsingToolbar()
    }

    override fun onResume() {
        super.onResume()

        mPresenter = ArtistPresenter(this)
        mPresenter.getArtistById(mArgs.id)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_artist, menu)
        mFavouriteMenuItem = menu.findItem(R.id.action_favourite)
        mFavouriteMenuItem.isVisible = false
        super.onCreateOptionsMenu(menu, inflater)
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

    override fun showArtistImage() {
        mArtistImage = requireView().findViewById(R.id.artist_toolbar_image)
        Glide.with(this)
            .load(mArgs.imageUrl)
            .into(mArtistImage)
    }

    override fun showArtistDetails(artist: Artist) {
        mCollapsingToolbarLayout.title = artist.name
    }
}