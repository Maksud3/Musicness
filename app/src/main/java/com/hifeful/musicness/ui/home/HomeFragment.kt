package com.hifeful.musicness.ui.home

import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.hifeful.musicness.R
import com.hifeful.musicness.data.model.Artist
import com.hifeful.musicness.data.model.Song
import com.hifeful.musicness.databinding.FragmentHomeBinding
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
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    // Variables
    private val mPresenter by moxyPresenter { HomePresenter() }
    private lateinit var mNavController: NavController
    private lateinit var mArtistAdapter: ArtistAdapter
    private lateinit var mFavoriteArtistAdapter: ArtistAdapter
    private lateinit var mSearchAdapter: CustomSearchAdapter
    private var mIsKeyboardShowing = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(this, object :
            OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (binding.homeSearchView.isSearchOpen) binding.homeSearchView.closeSearch()
                else activity?.finish()
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root

        setHasOptionsMenu(true)
        setUpToolbar(view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.root.viewTreeObserver.addOnGlobalLayoutListener(this)
        mNavController = Navigation.findNavController(view)

        setUpFavoriteSection()
        setUpRandomArtistsSection()
        setUpArtistRecycler()

        mPresenter.getFavoriteArtists()
        mPresenter.getRandomArtists()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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
        _binding?.root?.getWindowVisibleDisplayFrame(r)
        val screenHeight = _binding?.root?.rootView?.height

        // r.bottom is the position above soft keypad or device button.
        // if keypad is shown, the r.bottom is smaller than that before.
        if (screenHeight != null) {
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
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        _binding?.homeSearchView?.setViewRequested(this.mIsKeyboardShowing)
    }

    override fun setUpToolbar(view: View) {
        (activity as AppCompatActivity).setSupportActionBar(binding.homeToolbar)
    }

    override fun setUpSearch(searchItem: MenuItem) {
        binding.homeBackgroundBlur.isVisible = mPresenter.mIsSearchViewVisible

        mSearchAdapter = CustomSearchAdapter(requireContext(), mPresenter.mSongNamesSearchView,
            ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_music_note_24),
            true)
        binding.homeSearchView.apply {
            setMenuItem(searchItem)
            setAdapter(mSearchAdapter)
            setOnSearchViewListener(object : MaterialSearchView.SearchViewListener {
                override fun onSearchViewClosed() {
                    binding.homeSearchView.isVisible = false
                    binding.homeToolbar.isVisible = true

                    mPresenter.mIsSearchViewVisible = false
                    binding.homeBackgroundBlur.isVisible = false
                }

                override fun onSearchViewShown() {
                    binding.homeSearchView.isVisible = true
                    binding.homeToolbar.isVisible = false

                    mPresenter.mIsSearchViewVisible = true
                    binding.homeBackgroundBlur.isVisible = true
                }
            })
            setOnQueryTextListener(object : MaterialSearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    dismissSuggestions()
                    mPresenter.searchSongsThrottled(newText.toString())
                    return false
                }
            })
            setOnItemClickListener { _, _, i, _ ->
                setViewRequested(mIsKeyboardShowing)
                val action = HomeFragmentDirections.actionHomeFragmentToSongFragment(mPresenter.mSongsSearchView[i])
                mNavController.navigate(action)
                clearFocus()
            }
        }
    }

    override fun showSearchResults(songs: List<Song>) {
        val songNames = mutableListOf<String>()
        mPresenter.mSongsSearchView = songs
        mPresenter.mSongsSearchView.forEach { songNames.add("${it.primary_artist} - ${it.title}") }
        mPresenter.mSongNamesSearchView = songNames.toTypedArray()

        binding.homeSearchView.setSuggestions(mPresenter.mSongNamesSearchView)
    }

    override fun setUpArtistRecycler() {
        mArtistAdapter = ArtistAdapter().apply { mOnArtistClickListener = this@HomeFragment }
        binding.homeContent.homeArtistRecycler.apply {
            adapter = mArtistAdapter
            layoutManager = GridLayoutManager(requireContext(), 2)
            addItemDecoration(
                SpacesItemDecoration(
                    0, 0,
                    resources.getDimensionPixelSize(R.dimen.mobile_margin),
                    resources.getDimensionPixelSize(R.dimen.mobile_margin)
                )
            )
            isNestedScrollingEnabled = false
        }
    }

    override fun setUpFavoriteSection() {
        mFavoriteArtistAdapter = ArtistAdapter().apply { mOnArtistClickListener = this@HomeFragment }
        binding.homeContent.homeFavoriteArtistsRecycler.apply {
            adapter = mFavoriteArtistAdapter
            layoutManager = LinearLayoutManager(requireContext(),
                LinearLayoutManager.HORIZONTAL, false)
            addItemDecoration(
                SpacesItemDecoration(
                    0, 0,
                    resources.getDimensionPixelSize(R.dimen.mobile_margin), 0
                )
            )
        }
    }

    override fun setUpRandomArtistsSection() {
        binding.homeContent.homeArtistRefresh.setOnClickListener {
            binding.homeContent.homeScrollView.smoothScrollTo(0, 0, 1000)
            mPresenter.mArtists?.clear()
            mArtistAdapter.clearArtists()
            mPresenter.getRandomArtists()
        }
    }

    override fun showFavoriteArtists(artists: List<Artist>) {
        if (artists.isNotEmpty()) binding.homeContent.homeFavoriteArtistHeader.visibility = View.VISIBLE
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