package com.example.musicapp

import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.example.musicapp.models.SongModel

object MyExoplayer {

    private var exoPlayer : ExoPlayer? = null
    private var currentSong : SongModel? = null

    fun getCurrentSong() : SongModel?{
        return currentSong
    }

    fun getInstance() : ExoPlayer?{
        return exoPlayer
    }

    fun startPlaying(context: Context, song: SongModel){
        if(exoPlayer==null)
            exoPlayer = ExoPlayer.Builder(context).build()

        if(currentSong!=song){
            //it's a new song so start playing
            currentSong = song

            currentSong?.url?.apply {
                //start playing the media
                //assign media item
                val mediaItem = MediaItem.fromUri(this)
                //set media item to exoplayer
                exoPlayer?.setMediaItem(mediaItem)
                exoPlayer?.prepare()
                exoPlayer?.play()
            }
        }

    }
}