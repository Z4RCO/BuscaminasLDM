package com.example.practica4.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.practica4.R;
import com.example.practica4.fragment.FragmentAyuda1;
import com.example.practica4.fragment.FragmentAyuda2;
import com.example.practica4.fragment.FragmentAyuda3;

public class AyudaActivity extends AppCompatActivity {
    int pagina;
    FragmentAyuda1 a1;
    FragmentAyuda2 a2;
    FragmentAyuda3 a3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pagina = 0;
        a1 = new FragmentAyuda1();
        a2 = new FragmentAyuda2();
        a3 = new FragmentAyuda3();
        setContentView(R.layout.activity_ayuda);
    }

    /**
     * Método para avanzar a la siguiente página de ayuda
     * Funciona de forma cíclica
     * @param view Referencia a la View que hace onClick
     */
    public void next(View view){
        ImageView imagen = findViewById(R.id.imageView7);
        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        switch (pagina){
            case 0:
                imagen.setImageDrawable(getResources().getDrawable(R.drawable.ayuda2, getTheme()));
                break;
            case 1:
                imagen.setImageDrawable(getResources().getDrawable(R.drawable.ayuda3, getTheme()));
                break;
            case 2:
                imagen.setImageDrawable(getResources().getDrawable(R.drawable.ayuda4, getTheme()));

                break;
            case 3:
                t.replace(R.id.fragmentContainerView, a2).commit();
                break;
            case 4:
                t.replace(R.id.fragmentContainerView, a3).commit();
                break;
            case 5:
                t.replace(R.id.fragmentContainerView, a1).commit();
                break;
        }
        pagina = (pagina + 1) % 6;
    }

    /**
     * Método para retroceder a la página de ayuda anterior
     * Funciona de forma cíclica
     * @param view referencia a la View que hace onClick
     */
    public void previous(View view){
        ImageView imagen = findViewById(R.id.imageView7);
        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        switch (pagina){
            case 0:
                t.replace(R.id.fragmentContainerView, a3).commit();
                break;
            case 1:
                imagen.setImageDrawable(getResources().getDrawable(R.drawable.ayuda1, getTheme()));
                break;
            case 2:
                imagen.setImageDrawable(getResources().getDrawable(R.drawable.ayuda2, getTheme()));

                break;
            case 3:
                imagen.setImageDrawable(getResources().getDrawable(R.drawable.ayuda3, getTheme()));
                break;
            case 4:
                t.replace(R.id.fragmentContainerView, a1).commitNow();

                imagen = findViewById(R.id.imageView7);
                imagen.setImageDrawable(getResources().getDrawable(R.drawable.ayuda4, getTheme()));

                break;
            case 5:
                t.replace(R.id.fragmentContainerView, a2).commitNow();
        }
        pagina = (--pagina > 0 ) ? pagina : 5;
    }

    /**
     * Método para volver al menú
     * @param view Referencia a la view que hace onClick
     */
    public void menu(View view){
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
    }

}