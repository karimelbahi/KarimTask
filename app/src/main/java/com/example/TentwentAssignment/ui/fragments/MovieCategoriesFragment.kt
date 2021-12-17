package com.example.TentwentAssignment.ui.movie.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.TentwentAssignment.data.remote.response.movie.categores.CategoriesResponse
import com.example.TentwentAssignment.data.remote.response.movie.detail.Genre
import com.example.TentwentAssignment.databinding.FragmentMovieCategoriesBinding
import com.example.TentwentAssignment.ui.movie.MovieViewModel
import com.example.TentwentAssignment.ui.movie.adapter.MoviesCategoriesAdapter
import com.example.TentwentAssignment.util.Constants
import com.example.TentwentAssignment.util.Resource
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MovieCategoriesFragment : Fragment(), MoviesCategoriesAdapter.IMovie {

    val TAG: String = "MovieListingFragment"
    val viewModel: MovieViewModel by viewModels()
    @Inject
    lateinit var gson: Gson
    lateinit var binding: FragmentMovieCategoriesBinding

    private lateinit var moviesCategoriesAdapter: MoviesCategoriesAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMovieCategoriesBinding.inflate(layoutInflater)
        viewModel.categoriesLiveData.observe(viewLifecycleOwner, {
            when (it.status) {
                Resource.Status.LOADING -> {
                    Log.d(TAG, "LOADING ${it.message}")
                    binding.progressBar.visibility = View.VISIBLE
                    binding.movieRv.visibility = View.GONE
                }
                Resource.Status.SUCCESS -> {
                    Log.i(TAG, "SUCCESS ${it.data}")
                    it.data?.let { categoryEntity ->
                        binding.movieRv.visibility = View.VISIBLE
                        val categoriesResponse =
                            gson.fromJson(categoryEntity.response, CategoriesResponse::class.java)
                        moviesCategoriesAdapter=MoviesCategoriesAdapter(
                            requireContext(),
                            this@MovieCategoriesFragment,
                            categoriesResponse.genres
                        )
                        binding.movieRv.adapter = moviesCategoriesAdapter
                        binding.progressBar.visibility = View.GONE
                    }
                }
                Resource.Status.ERROR -> {
                    Log.e(TAG, "ERROR ${it.message}")
                    binding.progressBar.visibility = View.GONE
                }
                else -> {
                    Log.e(TAG, "INVALID - No status found.")
                    binding.progressBar.visibility = View.GONE
                }
            }
        })

        binding.searchBar.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
            override fun onQueryTextChange(query: String): Boolean {

                moviesCategoriesAdapter.search(query)
                return true
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }
})

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Call API
        viewModel.getCategoriesList(
            apiKey = Constants.API_KEY
        )

    }

    override fun onMovieClick(item: Genre, position: Int) {
        findNavController().navigate(
            MovieCategoriesFragmentDirections.actionMovieCategoriesFragmentToSearchByMovieFragment(
                item.name
            )
        )
    }



}
