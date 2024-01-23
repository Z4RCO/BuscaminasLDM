package com.example.practica4.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.practica4.R;
import com.example.practica4.juego.AdminSQLiteOpenHelper;
import com.example.practica4.juego.Usuario;

public class UserActivity extends AppCompatActivity {
    private Usuario user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        Intent i = getIntent();
        String username = i.getStringExtra("username");
        int easy = i.getIntExtra("easy", 0);
        int medium = i.getIntExtra("medium", 0);
        int hard = i.getIntExtra("hard", 0);
        user = new Usuario(username, easy, medium, hard);

        TextView userText = (TextView)findViewById(R.id.userActivityUser);
        userText.setText(user.getUsername());

        TextView easyText = (TextView)findViewById(R.id.userAvtivityEasy);
        easyText.append(": " + user.getEasy());

        TextView mediumText = (TextView)findViewById(R.id.userACtivityMedium);
        mediumText.append(": " + user.getMedium());

        TextView hardText = (TextView)findViewById(R.id.useractivityHard);
        hardText.append(": " + user.getHard());


    }


    /**
     * Método para cerrar sesión
     * Almacena la información del usuario en la base de datos
     * Navega a la activity de login
     * @param view referencia a la view que hace onClick
     */
    public void cerrarSesion(View view){
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "BDusuarios", null, 1);
        SQLiteDatabase db = admin.getWritableDatabase();

        ContentValues registro = new ContentValues();
        registro.put("user", user.getUsername());
        registro.put("easy", user.getEasy());
        registro.put("medium", user.getMedium());
        registro.put("hard", user.getHard());
        db.update("usuarios", registro, "user='"+user.getUsername()+"'", null);
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
    }

    /**
     * Método para navegar a la activity menu
     * Añade al intent la información del usuario
     * @param view referencia a la view que hace onClick
     */
    public void volver(View view){
        Intent i = new Intent(this, MenuActivity.class);
        i.putExtra("username", user.getUsername());
        i.putExtra("easy", user.getEasy());
        i.putExtra("medium", user.getMedium());
        i.putExtra("hard", user.getHard());
        startActivity(i);
    }
}