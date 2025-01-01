package com.shubham.movie_mania_upgrade.ui.movie

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.shubham.movie_mania_upgrade.communicator.IItemClickListener
import com.shubham.movie_mania_upgrade.data.Search
import com.shubham.movie_mania_upgrade.databinding.FragmentMovieBinding
import com.shubham.movie_mania_upgrade.databinding.ItemMovieDetailBinding
import com.shubham.movie_mania_upgrade.remote.CommonResponseManager
import com.shubham.movie_mania_upgrade.ui.MainViewModel
import com.shubham.movie_mania_upgrade.ui.adapter.MovieAdapter
import com.shubham.movie_mania_upgrade.utils.closeKeyBoard
import com.shubham.movie_mania_upgrade.utils.hideView
import com.shubham.movie_mania_upgrade.utils.runOnBackgroundThread
import com.shubham.movie_mania_upgrade.utils.showToast
import com.shubham.movie_mania_upgrade.utils.showView
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MovieFragment : Fragment(), IItemClickListener {

    private val binding: FragmentMovieBinding? by lazy {
        FragmentMovieBinding.inflate(layoutInflater)
    }

    private val itemDetailBinding: ItemMovieDetailBinding? by lazy {
        ItemMovieDetailBinding.inflate(layoutInflater)
    }

    private val bottomSheet: BottomSheetDialog? by lazy {
        BottomSheetDialog(requireContext())
    }

    private val viewModel: MainViewModel by viewModels()


    private val moviePageAdapter: MovieAdapter by lazy {
        MovieAdapter(this)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setRecyclerView()
        setUpListeners()
        itemDetailBinding?.root?.let { bottomSheet?.setContentView(it) }
    }

    private fun setRecyclerView() {
        binding?.movieRecycler?.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = moviePageAdapter
        }
    }

    private fun setUpListeners() {
        binding?.apply {

            searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    query?.let {
                        viewModel.setQuery(it)
                    }
                    closeKeyBoard()
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }

            })


            viewModel.list.observe(viewLifecycleOwner) {
                moviePageAdapter.submitData(lifecycle, it)
            }

            viewModel.movieDetails.observe(viewLifecycleOwner) { states ->
                progressBar.hideView()
                when (states) {
                    is CommonResponseManager.Error -> {
                        states.errorMessage.showToast(requireContext())
                    }

                    is CommonResponseManager.Loading -> {
                        progressBar.apply {
                            if (states.isLoading) showView() else hideView()
                        }
                    }

                    is CommonResponseManager.Success -> {
                        itemDetailBinding?.data = states.data
                        bottomSheet?.show()
                    }
                }

            }
        }
    }

    override fun onItemClicked(data: Search?) {
        data ?: return
        runOnBackgroundThread {
            viewModel.getTheMovieDetails(data.imdbID)
        }
    }

}