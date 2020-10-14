package com.hifeful.musicness.ui.artist

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.hifeful.musicness.R
import com.hifeful.musicness.data.model.Artist
import com.hifeful.musicness.ui.base.BaseFragment

class ArtistFragment : BaseFragment(), ArtistContract.View {
    private val TAG = ArtistFragment::class.qualifiedName

    // UI
    private lateinit var mCollapsingToolbarLayout: CollapsingToolbarLayout
    private lateinit var mArtistImage: ImageView

    // Variables
    private lateinit var mPresenter: ArtistContract.Presenter
    private val mArgs: ArtistFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_artist, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mCollapsingToolbarLayout = view.findViewById(R.id.artist_toolbar_layout)
        mArtistImage = view.findViewById(R.id.artist_toolbar_image)
    }

    override fun onResume() {
        super.onResume()

        mPresenter = ArtistPresenter(this)
        mPresenter.getArtistById(mArgs.id)
    }

    override fun setUpArtistDetails(artist: Artist) {
        mCollapsingToolbarLayout.title = artist.name
        Glide.with(this)
            .load(artist.image_url)
            .into(mArtistImage)
    }
}