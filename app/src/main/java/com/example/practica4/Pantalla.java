package com.example.practica4;

public abstract class Pantalla {
    protected final Juego juego;

    public Pantalla (Juego juego) {
        this.juego = juego;
    }

    public abstract void update(float deltaTime);

    public abstract void present(float deltaTime);

    public abstract void pause();

    public abstract void resume();

    public abstract void dispose();
}

