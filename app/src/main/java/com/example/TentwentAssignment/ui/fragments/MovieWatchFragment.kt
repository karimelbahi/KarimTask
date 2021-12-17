package com.example.TentwentAssignment.ui.movie.fragments

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.TentwentAssignment.data.remote.response.movie.video.VideoResponse
import com.example.TentwentAssignment.databinding.FragmentWatchMovieBinding
import com.example.TentwentAssignment.ui.movie.MovieViewModel
import com.example.TentwentAssignment.util.Constants
import com.example.TentwentAssignment.util.Resource
import com.google.gson.Gson
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MovieWatchFragment : Fragment() {

    val TAG: String = "MovieWatchFragment"
    lateinit var binding: FragmentWatchMovieBinding
    val viewModel: MovieViewModel by viewModels()
    @Inject
    lateinit var gson: Gson

    private val args: MovieWatchFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        binding = FragmentWatchMovieBinding.inflate(layoutInflater)


        binding.watchBack.setOnClickListener {
            activity?.onBackPressed()
        }

        binding.watchDone.setOnClickListener {
           activity?.onBackPressed()
        }


        if (args.movieId > 0) {
            viewModel.getVideo(
                apiKey = Constants.API_KEY,
                movieId = args.movieId
            )
        }
        viewModel.videoLiveData.observe(viewLifecycleOwner, {
            when (it.status) {
                Resource.Status.LOADING -> {
                    Log.d(TAG, "LOADING ${it.message}")
                }
                Resource.Status.SUCCESS -> {
                    Log.i(TAG, "SUCCESS ${it.data}")
                    it.data?.let { videoEntity ->
                        val videoResponse =
                            gson.fromJson(videoEntity.response, VideoResponse::class.java)
                        videoResponse?.let { video ->
                            onPlayVideo(video.results[0].key)
                        }
                    }
                }
                Resource.Status.ERROR -> {
                    Log.e(TAG, "ERROR ${it.message}")
                }
                else -> {
                    Log.e(TAG, "INVALID - No status found.")
                }
            }
        })

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()

        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

    }

    private fun onPlayVideo(videoId: String) {
        lifecycle.addObserver(binding.youtubePlayerView)
        binding.youtubePlayerView.addYouTubePlayerListener(object :
            AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                youTubePlayer.loadVideo(videoId, 0F)
            }
        })
    }
}