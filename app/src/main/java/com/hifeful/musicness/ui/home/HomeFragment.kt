package com.hifeful.musicness.ui.home

import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hifeful.musicness.R
import com.hifeful.musicness.data.model.Artist
import com.hifeful.musicness.data.model.Song
import com.hifeful.musicness.ui.adapters.ArtistAdapter
import com.hifeful.musicness.ui.adapters.CustomSearchAdapter
import com.hifeful.musicness.ui.base.BaseFragment
import com.hifeful.musicness.util.MaterialSearchView
import com.hifeful.musicness.util.SpacesItemDecoration
import moxy.ktx.moxyPresenter

class HomeFragment : BaseFragment(), HomeView,
    ArtistAdapter.OnArtistClickListener, ViewTreeObserver.OnGlobalLayoutListener {
    private val TAG = HomeFragment::class.qualifiedName

    // UI
    private lateinit var mRootLayout: FrameLayout
    private lateinit var mBackgroundBlur: View
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
    private lateinit var mSearchAdapter: CustomSearchAdapter
    private var mIsKeyboardShowing = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(this, object :
            OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (mSearchView.isSearchOpen) mSearchView.closeSearch()
                else activity?.finish()
            }
        })
    }

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

        mRootLayout = view.findViewById(R.id.home_layout)
        mRootLayout.viewTreeObserver.addOnGlobalLayoutListener(this)
        mNavController = Navigation.findNavController(view)

        setUpFavoriteSection()
        setUpArtistRecycler()

        mPresenter.getFavoriteArtists()
        mPresenter.getRandomArtists(50)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_home, menu)
        val searchItem = menu.findItem(R.id.action_search)
        requireView().post { setUpSearch(searchItem) }

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_search -> {
//                mSearchView.openSearch()
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onGlobalLayout() {
        val r = Rect()
        mRootLayout.getWindowVisibleDisplayFrame(r)
        val screenHeight = mRootLayout.rootView.height

        // r.bottom is the position above soft keypad or device button.
        // if keypad is shown, the r.bottom is smaller than that before.
        val keypadHeight = screenHeight - r.bottom

        if (keypadHeight > screenHeight * 0.15) { // 0.15 ratio is perhaps enough to determine keypad height.
            if (!mIsKeyboardShowing) {
                mIsKeyboardShowing = true
            }
        } else {
            if (mIsKeyboardShowing) {
                mIsKeyboardShowing = false
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        mSearchView.setViewRequested(this.mIsKeyboardShowing)
    }

    override fun setUpToolbar(view: View) {
        mToolbar = view.findViewById(R.id.home_toolbar)
        (activity as AppCompatActivity).setSupportActionBar(mToolbar)
    }

    override fun setUpSearch(searchItem: MenuItem) {
        mBackgroundBlur = requireView().findViewById(R.id.home_background_blur)
        mBackgroundBlur.isVisible = mPresenter.mIsSearchViewVisible

        mSearchView = requireView().findViewById(R.id.home_search_view)
        mSearchView.setMenuItem(searchItem)
        mSearchAdapter = CustomSearchAdapter(requireContext(), mPresenter.mSongNamesSearchView,
            ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_music_note_24),
            true)
        mSearchView.setAdapter(mSearchAdapter)
        mSearchView.setOnSearchViewListener(object : MaterialSearchView.SearchViewListener {
            override fun onSearchViewClosed() {
                mPresenter.mIsSearchViewVisible = false
                mBackgroundBlur.isVisible = false
            }

            override fun onSearchViewShown() {
                mPresenter.mIsSearchViewVisible = true
                mBackgroundBlur.isVisible = true
            }
        })
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
            mSearchView.setViewRequested(mIsKeyboardShowing)
            val action = HomeFragmentDirections.actionHomeFragmentToSongFragment(mPresenter.mSongsSearchView[i])
            mNavController.navigate(action)
        }
    }

    override fun showSearchResults(songs: List<Song>) {
        val songNames = mutableListOf<String>()
        mPresenter.mSongsSearchView = songs
        mPresenter.mSongsSearchView.forEach { songNames.add("${it.primary_artist} - ${it.title}") }
        mPresenter.mSongNamesSearchView = songNames.toTypedArray()
        mSearchView.setSuggestions(mPresenter.mSongNamesSearchView)
//        mSearchAdapter.setSuggestions(mPresenter.mSongNamesSearchView)
//        mSearchView.setAdapter(CustomSearchAdapter(requireContext(), mPresenter.mSongNamesSearchView,
//            ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_music_note_24),
//            true))
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
        mArtistAdapter.addArtist(artist)
    }

    override fun showArtists(artists: List<Artist>) {
        mArtistAdapter.initArtists(artists)
    }
}