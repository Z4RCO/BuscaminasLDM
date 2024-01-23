package com.example.practica4;

import com.example.practica4.juego.Usuario;

public interface Juego {
    public Input getInput();

    public FileIO getFileIO();

    public Graficos getGraphics();

    public Audio getAudio();

    public void setScreen(Pantalla pantalla);

    public Pantalla getCurrentScreen();

    public Pantalla getStartScreen();

    public void volverMenu(boolean victoria);

    public Usuario getUser();

    public int getDificultad();

    public Musica getMusica();
}
