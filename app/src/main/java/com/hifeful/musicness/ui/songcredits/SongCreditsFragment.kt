package com.hifeful.musicness.ui.songcredits

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
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
import com.hifeful.musicness.databinding.FragmentSongCreditsBinding
import com.hifeful.musicness.ui.adapters.ArtistSmallAdapter
import com.hifeful.musicness.ui.base.BaseFragment

class SongCreditsFragment : BaseFragment(), SongCreditsView, ArtistSmallAdapter.OnArtistSmallClickListener {
    // UI
    private var _binding: FragmentSongCreditsBinding? = null
    private val binding get() = _binding!!

    // Variables
    private val mArgs: SongCreditsFragmentArgs by navArgs()
    private lateinit var mNavController: NavController
    private lateinit var mSongCredits: SongCredits

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSongCreditsBinding.inflate(inflater, container, false)
        val view = binding.root

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

    override fun showImage(fragment: Fragment, image: String, imageView: ImageView) {
        Glide.with(fragment)
            .load(image)
            .centerCrop()
            .into(imageView)
    }

    override fun setUpToolbar(view: View) {
        (activity as AppCompatActivity).setSupportActionBar(binding.songCreditsToolbar)
    }

    override fun setUpSongCredits() {
        binding.songCreditsContent.songCreditsReleaseDate.text = mSongCredits.releaseDate
        setUpPrimaryArtist()
        setUpStaff(mSongCredits.featuredArtists,
            binding.songCreditsContent.songCreditsFeaturedArtists,
            binding.songCreditsContent.songCreditsFeaturedArtistsRecycler,
            "featured")
        setUpStaff(mSongCredits.producerArtists,
            binding.songCreditsContent.songCreditsProducers,
            binding.songCreditsContent.songCreditsProducersRecycler,
            "producer")
        setUpStaff(mSongCredits.writerArtists,
            binding.songCreditsContent.songCreditsWriters,
            binding.songCreditsContent.songCreditsWritersRecycler,
            "writer")
    }

    override fun setUpPrimaryArtist() {
        binding.songCreditsContent.songCreditsPrimaryArtist.artistSmallLayout
            .setOnClickListener { onArtistClick(mSongCredits.primaryArtist,
                binding.songCreditsContent.songCreditsPrimaryArtist.artistSmallImage) }

        binding.songCreditsContent.songCreditsPrimaryArtist
            .artistSmallImage.transitionName = "primary_${mSongCredits.primaryArtist.id}"

        showImage(this, mSongCredits.primaryArtist.image_url,
            binding.songCreditsContent.songCreditsPrimaryArtist.artistSmallImage)

        binding.songCreditsContent.songCreditsPrimaryArtist
            .artistSmallName.text = mSongCredits.primaryArtist.name
    }

    override fun setUpStaff(staffList: List<Artist>,
                            headerView: TextView, recyclerView: RecyclerView,
                            label: String) {
        if (staffList.isNotEmpty()) {
            headerView.apply { isVisible = true }

            val staffAdapter = ArtistSmallAdapter().apply {
                mArtistList = staffList.toMutableList()
                mLabel = label
                mOnArtistClickListener = this@SongCreditsFragment
            }

            recyclerView.apply {
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