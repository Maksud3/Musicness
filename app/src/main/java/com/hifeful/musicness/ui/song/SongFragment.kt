package com.hifeful.musicness.ui.song

import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.transition.TransitionInflater
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.hifeful.musicness.R
import com.hifeful.musicness.data.model.Song
import com.hifeful.musicness.data.model.SongCredits
import com.hifeful.musicness.ui.base.BaseFragment
import moxy.ktx.moxyPresenter

class SongFragment : BaseFragment(), SongView {
    private val TAG = SongFragment::class.qualifiedName

    // UI
    private lateinit var mAppBarLayout: AppBarLayout
    private lateinit var mCollapsingToolbarLayout: CollapsingToolbarLayout
    private lateinit var mToolbar: Toolbar
    private lateinit var mSongImage: ImageView
    private lateinit var mSongArtist: TextView
    private lateinit var mSongCredits: TextView
    private lateinit var mSongLyrics: TextView

    // Variables
    private val mPresenter by moxyPresenter { SongPresenter() }
    private lateinit var mNavController: NavController
    private val mArgs: SongFragmentArgs by navArgs()
    private lateinit var mCurrentSong: Song

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = TransitionInflater.from(requireContext())
            .inflateTransition(android.R.transition.move)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_song, container, false)
        setHasOptionsMenu(true)
        setUpToolbar(view)
        showDisplayHomeUp()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mNavController = findNavController()
        mCurrentSong = mArgs.song

        setUpCollapsingToolbar()
        showSongDetails()
        mPresenter.getSongCredits(mCurrentSong.id)
        if (savedInstanceState == null) {
            mPresenter.getSongLyrics(mCurrentSong.url)
        }
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
        mToolbar = view.findViewById(R.id.song_toolbar)
        (activity as AppCompatActivity).setSupportActionBar(mToolbar)
    }

    override fun setUpCollapsingToolbar() {
        mAppBarLayout = requireView().findViewById(R.id.song_app_bar)
        mCollapsingToolbarLayout = requireView().findViewById(R.id.song_toolbar_layout)
        mSongCredits = requireView().findViewById(R.id.song_toolbar_credits)

        mAppBarLayout.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
            var isShow = false
            var scrollRange = -1
            override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
                if (appBarLayout != null && scrollRange == -1) {
                    hideToolbarInfo()
                    scrollRange = appBarLayout.totalScrollRange
                }

                if (isShow && scrollRange + verticalOffset < scrollRange / 2) {
                    hideToolbarInfo()
                    isShow = false
                } else if (!isShow && scrollRange + verticalOffset > scrollRange / 2) {
                    showToolbarInfo()
                    isShow = true
                }
            }
        })
    }

    override fun showToolbarInfo() {
        mSongArtist.isVisible = true
        mSongCredits.isVisible = true
        mPresenter.mSongCredits?.let { setUpSongCredits(it) }
    }

    override fun hideToolbarInfo() {
        mSongArtist.isVisible = false
        mSongCredits.isVisible = false
    }

    override fun showSongDetails() {
        mCollapsingToolbarLayout.title = mCurrentSong.title
        mSongImage = requireView().findViewById(R.id.song_toolbar_image)
        showImage(this, mCurrentSong.image, mSongImage)
        mSongArtist = requireView().findViewById(R.id.song_toolbar_artist)
        mSongArtist.text = mCurrentSong.primary_artist
    }

    override fun showSongLyrics(lyrics: String) {
        mSongLyrics = requireView().findViewById(R.id.song_lyrics)
        mSongLyrics.text = lyrics
    }

    override fun setUpSongCredits(songCredits: SongCredits) {
        mSongCredits.setOnClickListener {
            val directions = SongFragmentDirections.actionSongFragmentToSongCreditsFragment(songCredits)
            mNavController.navigate(directions)
        }
    }
}