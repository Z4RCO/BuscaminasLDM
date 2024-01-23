package com.example.practica4.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.telephony.ims.RegistrationManager;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.practica4.R;
import com.example.practica4.androidimpl.AndroidJuego;
import com.example.practica4.juego.AdminSQLiteOpenHelper;
import com.example.practica4.juego.Buscaminas;
import com.example.practica4.juego.JuegoBuscaminas;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Buscaminas j = new Buscaminas(9, 9, 10);
        j.comprobar(0, 0);
    }

    /**
     * Método para iniciar sesión
     * Comprueba que ambos campos no están vacíos
     * Calcula el hash de la contraseña
     * Si usuario y hash están en la base de datos, crea objeto usuario y navega a la activity menú
     * En cualquier otro caso avisa del error de inicio de sesión
     * @param view referencia a la view que hace onClick
     */
    public void login(View view){
        EditText user = (EditText)findViewById(R.id.editTextText);
        EditText pass = (EditText)findViewById(R.id.editTextTextPassword);

        String userText = user.getText().toString();

        if(userText.equals("") || pass.getText().toString().equals("")){
            Toast.makeText(this, "Los campos deben rellenarse", Toast.LENGTH_SHORT).show();
            return;
        }
        try{

            MessageDigest hash = MessageDigest.getInstance("SHA-256");

            byte[] passHash = hash.digest(pass.getText().toString().getBytes());
            String password = String.format("%01x", new java.math.BigInteger(1, passHash));


            AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "BDusuarios", null, 1);
            SQLiteDatabase db = admin.getWritableDatabase();

            Cursor c = db.rawQuery("SELECT * FROM usuarios WHERE user = '" + userText + "' AND pass ='" + password + "'", null);
            if(c.moveToFirst()){
                String username = c.getString(0);
                int easy = Integer.parseInt(c.getString(2));
                int medium = Integer.parseInt(c.getString(3));
                int hard = Integer.parseInt(c.getString(4));
                db.close();


                Intent i = new Intent(this, MenuActivity.class);
                i.putExtra("username", username);
                i.putExtra("easy", easy);
                i.putExtra("medium", medium);
                i.putExtra("hard", hard);
                startActivity(i);
            }
            else{
                Toast.makeText(this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();
                db.close();
            }
            c.close();


        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Método para navegar a la Activity a ayuda
     * @param view View en la que se ejecuta onClick
     */
    public void ayuda(View view){
        Intent i = new Intent(this, AyudaActivity.class);
        startActivity(i);
    }

    /**
     * Método para navegar a la Activity de registro
     * @param view View en la que se ejecuta onClick
     */
    public void registro(View view){
        Intent i = new Intent(this, RegisterActivity.class);
        startActivity(i);
    }
}