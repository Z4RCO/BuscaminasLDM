package com.example.practica4.juego;

public class Usuario {
    private String username;
    private int easy;
    private int medium;
    private int hard;

    public Usuario(String username, int easy, int medium, int hard) {
        this.username = username;
        this.easy = easy;
        this.medium = medium;
        this.hard = hard;
    }

    public String getUsername() {
        return username;
    }

    public int getEasy() {
        return easy;
    }

    public int getMedium() {
        return medium;
    }

    public int getHard() {
        return hard;
    }

    public void victoria(int tipo){
        switch(tipo){
            case 0:
                easy++;
                break;
            case 1:
                medium++;
                break;
            case 2:
                hard++;
                break;
        }
    }
}
