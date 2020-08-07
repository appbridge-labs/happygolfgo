package com.artilearn.happygolfgo.deprecated

import android.net.Uri
import android.os.Bundle
import androidx.core.net.toUri
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.base.BaseActivity
import com.artilearn.happygolfgo.constants.VIDEO_URL
import com.artilearn.happygolfgo.databinding.ActivityVideoBinding
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory

class VideoActivity : BaseActivity<ActivityVideoBinding>(R.layout.activity_video) {

    private val videoUrl by lazy { intent.getStringExtra(VIDEO_URL) }
    private var playWhenReady = true
    private var player: SimpleExoPlayer? = null
    private var currentWindow = 0
    private var playbackPosition = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    private fun initializePlayer(path: String) {
        player = ExoPlayerFactory.newSimpleInstance(this)
        binding.exoplayer.player = player

        val uri = path.toUri()
        val mediaSource = buildMediaSource(uri)

        player?.playWhenReady = playWhenReady
        player?.seekTo(currentWindow, playbackPosition)
        player?.prepare(mediaSource, false, false)
    }

    private fun buildMediaSource(uri: Uri): MediaSource {
        val dataSourceFactory = DefaultDataSourceFactory(this, "happygolf_pro")

        return ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(uri)
    }

    private fun releasePlayer() {
        if (player != null) {
            playWhenReady = player!!.playWhenReady
            playbackPosition = player!!.currentPosition
            currentWindow = player!!.currentWindowIndex
            player!!.release()
            player = null
        }
    }

    override fun onStart() {
        super.onStart()
        videoUrl?.let {
            initializePlayer(it)
        }
    }

    override fun onPause() {
        super.onPause()
        releasePlayer()
    }

    override fun onStop() {
        super.onStop()
        releasePlayer()
    }
}