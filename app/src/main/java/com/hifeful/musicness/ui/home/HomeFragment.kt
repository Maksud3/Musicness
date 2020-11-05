package com.hifeful.musicness.ui.home

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.hifeful.musicness.R
import com.hifeful.musicness.data.model.Artist
import com.hifeful.musicness.data.model.Song
import com.hifeful.musicness.ui.adapters.ArtistAdapter
import com.hifeful.musicness.ui.base.BaseFragment
import com.hifeful.musicness.ui.adapters.CustomSearchAdapter
import com.hifeful.musicness.ui.adapters.SongAdapter
import com.hifeful.musicness.ui.artist.ArtistFragmentDirections
import com.hifeful.musicness.util.SpacesItemDecoration
import moxy.ktx.moxyPresenter
import com.miguelcatalan.materialsearchview.MaterialSearchView

class HomeFragment : BaseFragment(), HomeView,
    ArtistAdapter.OnArtistClickListener {
    private val TAG = HomeFragment::class.qualifiedName

    // UI
    private lateinit var mToolbar: Toolbar
    private lateinit var mArtistRecyclerView: RecyclerView
    private lateinit var mArtistAdapter: ArtistAdapter
    private lateinit var mFavouriteHeader: TextView
    private lateinit var mFavoriteArtistRecyclerView: RecyclerView
    private lateinit var mFavoriteArtistAdapter: ArtistAdapter

    private lateinit var mSearchView: MaterialSearchView

    private lateinit var mNavController: NavController

    // Variables
    private val mPresenter by moxyPresenter { HomePresenter() }
    private var mSongs: List<Song> = listOf()
    private var mSongNames: Array<String> = mutableListOf<String>().toTypedArray()
    private lateinit var mSearchAdapter: CustomSearchAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        setHasOptionsMenu(true)
        setUpToolbar(view)
        return view
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_home, menu)
        val searchItem = menu.findItem(R.id.action_search)
        setUpSearch(searchItem)

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun setUpToolbar(view: View) {
        mToolbar = view.findViewById(R.id.home_toolbar)
        (activity as AppCompatActivity).setSupportActionBar(mToolbar)
    }

    override fun setUpSearch(searchItem: MenuItem) {
        mSearchView = requireView().findViewById(R.id.home_search_view)
        mSearchView.setMenuItem(searchItem)
        mSearchAdapter = CustomSearchAdapter(
            requireContext(), mSongNames,
            ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_music_note_24), false
        )
        mSearchView.setAdapter(mSearchAdapter)
        mSearchView.setOnQueryTextListener(object : MaterialSearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                mPresenter.searchSongsThrottled(newText.toString())
                return false
            }
        })
        mSearchView.setOnItemClickListener { _, _, i, _ ->
            val action = HomeFragmentDirections.actionHomeFragmentToSongFragment(mSongs[i])
            mNavController.navigate(action)
        }
    }

    override fun showSearchResults(songs: List<Song>) {
        val songNames = mutableListOf<String>()
        mSongs = songs
        songs.forEach { songNames.add("${it.primary_artist} - ${it.title}") }
        mSongNames = songNames.toTypedArray()
        Log.i(TAG, "showSearchResults: ${mSongNames.size}")
//        mSearchAdapter.setSuggestions(mSongNames)
        mSearchView.setAdapter(CustomSearchAdapter(requireContext(), mSongNames))
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

    override fun onArtistClick(artist: Artist) {
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