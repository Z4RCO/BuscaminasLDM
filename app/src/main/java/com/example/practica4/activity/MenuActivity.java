package com.example.practica4.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.practica4.Juego;
import com.example.practica4.R;
import com.example.practica4.juego.JuegoBuscaminas;
import com.example.practica4.juego.Usuario;

public class MenuActivity extends AppCompatActivity {
    private Usuario user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Intent i = getIntent();

        String username = i.getStringExtra("username");
        int easy = i.getIntExtra("easy", 0);
        int medium = i.getIntExtra("medium", 0);
        int hard = i.getIntExtra("hard", 0);
        user = new Usuario(username, easy, medium, hard);

        TextView user = findViewById(R.id.userView);
        user.setText(this.user.getUsername());

    }

    /**
     * Método para navegar a la activity de información de usuario
     * Añade al intent la información del usuario
     * @param view Referencia a la view que hace onClick
     */
    public void userPage(View view){
        Intent i = new Intent(this, UserActivity.class);
        i.putExtra("username", user.getUsername());
        i.putExtra("easy", user.getEasy());
        i.putExtra("medium", user.getMedium());
        i.putExtra("hard", user.getHard());
        startActivity(i);
    }


    /**
     * Método para comenzar una partida en dificultad fácil
     * Comienza la activity juegoBuscaminas
     * Añade al intent la información del usuario y la dificultad
     * @param view referencia a la view que hace onClick
     */
    public void jugarEasy(View view){
        Intent i = new Intent(this, JuegoBuscaminas.class);
        i.putExtra("username", user.getUsername());
        i.putExtra("easy", user.getEasy());
        i.putExtra("medium", user.getMedium());
        i.putExtra("hard", user.getHard());
        i.putExtra("dificultad", 0);
        startActivity(i);
    }


    /**
     * Método para comenzar una partida en dificultad media
     * Comienza la activity juegoBuscaminas
     * Añade al intent la información del usuario y la dificultad
     * @param view referencia a la view que hace onClick
     */
    public void jugarMedium(View view){
        Intent i = new Intent(this, JuegoBuscaminas.class);
        i.putExtra("username", user.getUsername());
        i.putExtra("easy", user.getEasy());
        i.putExtra("medium", user.getMedium());
        i.putExtra("hard", user.getHard());
        i.putExtra("dificultad", 1);
        startActivity(i);

    }


    /**
     * Método para comenzar una partida en dificultad dificil
     * Comienza la activity juegoBuscaminas
     * Añade al intent la información del usuario y la dificultad
     * @param view referencia a la view que hace onClick
     */
    public void jugarHard(View view){
        Intent i = new Intent(this, JuegoBuscaminas.class);
        i.putExtra("username", user.getUsername());
        i.putExtra("easy", user.getEasy());
        i.putExtra("medium", user.getMedium());
        i.putExtra("hard", user.getHard());
        i.putExtra("dificultad", 2);
        startActivity(i);

    }
}