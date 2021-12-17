package com.example.TentwentAssignment.ui.movie.fragments

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.example.TentwentAssignment.data.remote.response.movie.detail.MovieDetailResponse
import com.example.TentwentAssignment.data.remote.response.movie.images.ImagesResponse
import com.example.TentwentAssignment.databinding.FragmentMovieDetailBinding
import com.example.TentwentAssignment.ui.movie.MovieViewModel
import com.example.TentwentAssignment.util.Constants
import com.example.TentwentAssignment.util.Resource
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MovieDetailFragment : Fragment() {

    val TAG: String = "MovieDetailFragment"
    lateinit var binding: FragmentMovieDetailBinding
    val viewModel: MovieViewModel by viewModels()
    @Inject lateinit var gson: Gson

    private val args: MovieDetailFragmentArgs by navArgs()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentMovieDetailBinding.inflate(layoutInflater)
        viewModel.moviesDetailLiveData.observe(viewLifecycleOwner,{
            when (it.status) {
                Resource.Status.LOADING -> {
                    Log.d(TAG, "LOADING ${it.message}")
                }
                Resource.Status.SUCCESS -> {
                    Log.i(TAG, "SUCCESS ${it.data}")
                    it.data?.let { movieDetailEntity ->
                        val moviesDetailResponse = gson.fromJson(movieDetailEntity.response, MovieDetailResponse::class.java)
                        moviesDetailResponse?.let { m ->
//                            binding.movieDetailGenre.text = m.genre
//                            binding.movieDetailDate.text = m.release_date
                            binding.movieDetailOverview.text = m.overview
                            binding.watchTrailer.setOnClickListener {
                                goWatchTrailerFragment(m)
                            }
                        }
                    }
                }
                Resource.Status.ERROR -> {
                    Log.e(TAG, "ERROR ${it.message}")
                }
                else -> {
                    Log.e(TAG,"INVALID - No status found.")
                }
            }
        })

        viewModel.imageLiveData.observe(viewLifecycleOwner,{
            when (it.status) {
                Resource.Status.LOADING -> {
                    Log.d(TAG, "LOADING ${it.message}")
                    binding.progressBar.visibility = View.VISIBLE
//                    binding.movieDetailContentLl.visibility = View.INVISIBLE
                }
                Resource.Status.SUCCESS -> {
                    Log.i(TAG, "SUCCESS ${it.data}")
                    it.data?.let { imageEntity ->
                        binding.progressBar.visibility = View.GONE
                        val imageResponse = gson.fromJson(imageEntity.response, ImagesResponse::class.java)
                        imageResponse?.let { images ->
                            if(!images.posters.isNullOrEmpty()) {
                                val imageList = ArrayList<SlideModel>()
                                images.posters.forEachIndexed { index, poster ->
                                    if (index < 5) {
                                        imageList.add(
                                            SlideModel(
                                                imageUrl = Constants.IMAGE_URL + poster.file_path,
                                                scaleType = ScaleTypes.FIT
                                            )
                                        )
                                    }
                                }

                                Glide.with(this)
                                    .load(Constants.IMAGE_URL + images.posters.get(0).file_path)
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .into(binding.movieDetailImage)

                            }
                        }
                    }
                }
                Resource.Status.ERROR -> {
                    binding.progressBar.visibility = View.GONE
                    Log.e(TAG, "ERROR ${it.message}")
                }
                else -> {
                    binding.progressBar.visibility = View.GONE
                    Log.e(TAG,"INVALID - No status found.")
                }
            }
        })
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getImage(
            apiKey = Constants.API_KEY,
            movieId = args.movieId
        )
        viewModel.getMovieDetail(
            apiKey = Constants.API_KEY,
            movieId = args.movieId
        )

    }



    private fun goWatchTrailerFragment(item: MovieDetailResponse){

        findNavController().navigate(
            MovieDetailFragmentDirections.actionMovieDetailFragmentToMovieWatchFragment(
                item.id
            )
        )
    }
}