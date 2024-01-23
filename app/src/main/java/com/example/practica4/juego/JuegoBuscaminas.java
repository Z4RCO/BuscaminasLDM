package com.example.practica4.juego;

import com.example.practica4.Pantalla;
import com.example.practica4.androidimpl.AndroidJuego;

public class JuegoBuscaminas extends AndroidJuego {
    @Override
    public Pantalla getStartScreen() {
        return new LoadingScreen(this);
    }
}
