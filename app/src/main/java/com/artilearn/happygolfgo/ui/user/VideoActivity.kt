package com.artilearn.happygolfgo.ui.user

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.net.toUri
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.ui.BaseActivity
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import kotlinx.android.synthetic.main.activity_video.*

class VideoActivity : BaseActivity() {

    private var path: String? = null
    private var playWhenReady = true
    private var player: SimpleExoPlayer? = null
    private var currentWindow = 0
    private var playbackPosition = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video)

        path = intent.getStringExtra("path")
    }

    private fun initializePlayer(path: String) {
        player = ExoPlayerFactory.newSimpleInstance(this)
        exoPlayer.player = player

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
        path?.let {
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