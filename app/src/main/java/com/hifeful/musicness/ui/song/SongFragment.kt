package com.hifeful.musicness.ui.song

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.transition.TransitionInflater
import com.google.android.material.appbar.AppBarLayout
import com.hifeful.musicness.data.model.Song
import com.hifeful.musicness.data.model.SongCredits
import com.hifeful.musicness.databinding.FragmentSongBinding
import com.hifeful.musicness.ui.base.BaseFragment
import moxy.ktx.moxyPresenter

class SongFragment : BaseFragment(), SongView {
    private val TAG = SongFragment::class.qualifiedName

    // UI
    private var _binding: FragmentSongBinding? = null
    private val binding get() = _binding!!

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
        _binding = FragmentSongBinding.inflate(inflater, container, false)
        val view = binding.root

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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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
        (activity as AppCompatActivity).setSupportActionBar(binding.songToolbar)
    }

    override fun setUpCollapsingToolbar() {
        binding.songAppBar.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
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
        binding.songToolbarArtist.isVisible = true
        binding.songToolbarCredits.isVisible = true
        mPresenter.mSongCredits?.let { setUpSongCredits(it) }
    }

    override fun hideToolbarInfo() {
        binding.songToolbarArtist.isVisible = false
        binding.songToolbarCredits.isVisible = false
    }

    override fun showSongDetails() {
        binding.songToolbarLayout.title = mCurrentSong.title
        showImage(this, mCurrentSong.image, binding.songToolbarImage)
        binding.songToolbarArtist.text = mCurrentSong.primary_artist
    }

    override fun showSongLyrics(lyrics: String) {
        binding.songContent.songLyrics.text = lyrics
    }

    override fun setUpSongCredits(songCredits: SongCredits) {
        binding.songToolbarCredits.setOnClickListener {
            val directions = SongFragmentDirections.actionSongFragmentToSongCreditsFragment(songCredits)
            mNavController.navigate(directions)
        }
    }
}