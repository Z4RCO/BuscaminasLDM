package com.example.practica4.androidimpl;

import android.media.SoundPool;

import com.example.practica4.Sonido;

public class AndroidSonido implements Sonido {
    int soundId;
    SoundPool soundPool;

    public AndroidSonido(SoundPool soundPool, int soundId) {
        this.soundId = soundId;
        this.soundPool = soundPool;

    }

    @Override
    public void play(float volume) {
        soundPool.play(soundId, volume, volume, 0, 0, 1);
    }

    @Override
    public void dispose() {
        soundPool.unload(soundId);
    }

    @Override
    public void parar() {
        soundPool.stop(soundId);
    }

}

