package com.example.practica4.androidimpl;

import java.io.IOException;

import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;

import com.example.practica4.Musica;


public class AndroidMusica implements Musica, OnCompletionListener {
    private MediaPlayer mediaPlayer;
    private boolean isPrepared = false;
    private boolean isDisposed = false;

    public AndroidMusica(AssetFileDescriptor assetDescriptor) {
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(assetDescriptor.getFileDescriptor(),
                    assetDescriptor.getStartOffset(),
                    assetDescriptor.getLength());
            mediaPlayer.prepare();
            isPrepared = true;
            mediaPlayer.setOnCompletionListener(this);
        } catch (Exception e) {
            throw new RuntimeException("No se ha podido cargar la música");
        }
    }

    @Override
    public void dispose() {
        if (!isDisposed) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.release();
            isDisposed = true;
        }
    }

    @Override
    public boolean isLooping() {
        return mediaPlayer.isLooping();
    }

    @Override
    public boolean isPlaying() {
        return !isDisposed && mediaPlayer.isPlaying();
    }

    @Override
    public boolean isStopped() {
        return !isPrepared;
    }

    @Override
    public void pause() {
        if (!isDisposed && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    @Override
    public void play() {
        if (!isDisposed && !mediaPlayer.isPlaying()) {
            try {
                synchronized (this) {
                    if (!isPrepared) {
                        mediaPlayer.prepare();
                        isPrepared = true;
                    }
                    mediaPlayer.start();
                }
            } catch (IllegalStateException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void setLooping(boolean isLooping) {
        mediaPlayer.setLooping(isLooping);
    }

    @Override
    public void setVolume(float volume) {
        mediaPlayer.setVolume(volume, volume);
    }

    @Override
    public void stop() {
        if (!isDisposed) {
            mediaPlayer.stop();
            synchronized (this) {
                isPrepared = false;
            }
        }
    }

    @Override
    public void onCompletion(MediaPlayer player) {
        synchronized (this) {
            isPrepared = false;
        }
    }
}
