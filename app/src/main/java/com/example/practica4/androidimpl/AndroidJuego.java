package com.example.practica4.androidimpl;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;
import android.view.WindowManager;


import com.example.practica4.Audio;
import com.example.practica4.FileIO;
import com.example.practica4.Graficos;
import com.example.practica4.Input;
import com.example.practica4.Juego;
import com.example.practica4.Musica;
import com.example.practica4.Pantalla;
import com.example.practica4.R;
import com.example.practica4.activity.MenuActivity;
import com.example.practica4.juego.PantallaJuego;
import com.example.practica4.juego.Usuario;

import java.io.IOException;

public abstract class AndroidJuego extends Activity implements Juego {
    AndroidFastRenderView renderView;
    Graficos graficos;
    Audio audio;
    Input input;
    FileIO fileIO;
    Pantalla pantalla;
    WakeLock wakeLock;

    Musica musica;

    Usuario user;
    int dificultad;



    private void initMusic() {

        musica.setLooping(true); // Establecer si se desea que se reproduzca en bucle
        musica.setVolume(0.5f); // Establecer el volumen según sea necesario
        musica.play();

    }




    @SuppressLint("InvalidWakeLockTag")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_juego);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        boolean isLandscape = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
        int frameBufferWidth = isLandscape ? 480 : 320;
        int frameBufferHeight = isLandscape ? 320 : 480;
        Bitmap frameBuffer = Bitmap.createBitmap(frameBufferWidth,
                frameBufferHeight, Config.RGB_565);

        float scaleX = (float) frameBufferWidth
                / getWindowManager().getDefaultDisplay().getWidth();
        float scaleY = (float) frameBufferHeight
                / getWindowManager().getDefaultDisplay().getHeight();


        renderView = new AndroidFastRenderView(this, frameBuffer);
        graficos = new AndroidGraficos(getAssets(), frameBuffer);
        fileIO = new AndroidFileIO(getAssets());
        audio = new AndroidAudio(this);

        AssetFileDescriptor musicDescriptor;
        try {
            musicDescriptor = getAssets().openFd("musicafondo.mp3");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        musica = new AndroidMusica(musicDescriptor);


        input = new AndroidInput(this, renderView, scaleX, scaleY);

        Intent i = getIntent();
        dificultad = i.getIntExtra("dificultad", 0);
        String username = i.getStringExtra("username");
        int easy = i.getIntExtra("easy", 0);
        int medium = i.getIntExtra("medium", 0);
        int hard = i.getIntExtra("hard", 0);
        Log.d("TAG", "onCreate: " + dificultad);
        user = new Usuario(username, easy, medium, hard);

        pantalla = getStartScreen();
        setContentView(renderView);

        PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "GLGame");
    }

    @Override
    public void onResume() {
        super.onResume();
        wakeLock.acquire();
        pantalla.resume();
        renderView.resume();

        initMusic();
    }

    @Override
    public void onPause() {
        super.onPause();
        wakeLock.release();
        renderView.pause();
        pantalla.pause();

        if (!musica.isPlaying()) {
            musica.pause();
        }
        if (isFinishing())
            pantalla.dispose();
    }

    @Override
    public Input getInput() {
        return input;
    }

    @Override
    public FileIO getFileIO() {
        return fileIO;
    }

    @Override
    public Graficos getGraphics() {
        return graficos;
    }

    @Override
    public Audio getAudio() {
        return audio;
    }

    @Override
    public void setScreen(Pantalla pantalla) {
        if (pantalla == null) {
            throw new IllegalArgumentException("Pantalla no debe ser null");
        }

        // Verificar si la nueva pantalla es PantallaJuego
        if (pantalla instanceof PantallaJuego) {
            // Detener la música solo si la nueva pantalla es PantallaJuego
            musica.setVolume(0.3f);
        }

        this.pantalla.pause();
        this.pantalla.dispose();
        pantalla.resume();
        pantalla.update(0);
        this.pantalla = pantalla;
    }

    public void volverMenu(boolean victoria){
        if(victoria)user.victoria(dificultad);
        Intent i = new Intent(this, MenuActivity.class);
        i.putExtra("username", user.getUsername());
        i.putExtra("easy", user.getEasy());
        i.putExtra("medium", user.getMedium());
        i.putExtra("hard", user.getHard());
        startActivity(i);
    }


    public Pantalla getCurrentScreen() {
        return pantalla;
    }

    @Override
    public int getDificultad() {
        return dificultad;
    }

    @Override
    public Usuario getUser() {
        return user;
    }

    @Override
    public Musica getMusica() {
        return musica;
    }
}