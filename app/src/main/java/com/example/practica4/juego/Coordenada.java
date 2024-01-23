package com.example.practica4.juego;

import java.util.Objects;

public class Coordenada {
    private int x;
    private int y;

    public Coordenada(int x, int y) {
        if(x < 0 || y < 0)throw new IllegalArgumentException("Las coordenadas no pueden ser negativas");
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordenada that = (Coordenada) o;
        return x == that.x && y == that.y;
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
    }
}
