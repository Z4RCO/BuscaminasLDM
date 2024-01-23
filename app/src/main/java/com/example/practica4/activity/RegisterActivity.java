package com.example.practica4.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.practica4.R;
import com.example.practica4.juego.AdminSQLiteOpenHelper;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }


    /**
     * Método para registrar un nuevo usuario en la base de datos
     * Comprueba que ningún campo esté vacío
     * Si es así, comprueba que el usuario no exista ya en la base de datos
     * Si es así, calcula el hash de la contraseña y añade la nueva entrada a la tabla
     * En cualquier otro caso, informa del error de registro
     * @param view referencia a la view que hace onClick
     */
    public void registrar(View view) {
        EditText user = findViewById(R.id.userRegistrar);
        EditText pass = findViewById(R.id.passRegistrar);
        EditText copiaPass = findViewById(R.id.repPassRegistrar);
        if(user.getText().toString().equals("") || pass.getText().toString().equals("") || copiaPass.getText().toString().equals("")){
            Toast.makeText(this, "Los campos no pueden estar vacíos", Toast.LENGTH_SHORT).show();
        }

        if (!pass.getText().toString().equals(copiaPass.getText().toString())) {

            Toast.makeText(this, "Las contraseñas deben coincidir", Toast.LENGTH_SHORT).show();
            return;
        }

        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "BDusuarios", null, 1);
        SQLiteDatabase db = admin.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM usuarios WHERE user = '" + user.getText().toString()+"'", null);
        if (c.getCount() > 0) {
            Toast.makeText(this, "Ese usuario ya está registrado", Toast.LENGTH_SHORT).show();
            db.close();
            return;
        }

        try {
            MessageDigest hash = MessageDigest.getInstance("SHA-256");
            byte[] passHash = hash.digest(pass.getText().toString().getBytes());
            String password = String.format("%01x", new java.math.BigInteger(1, passHash));

            ContentValues registro = new ContentValues();
            registro.put("user", user.getText().toString());
            registro.put("pass", password);
            registro.put("easy", 0);
            registro.put("medium", 0);
            registro.put("hard", 0);
            db.insert("usuarios", null, registro);
            db.close();
            Toast.makeText(this, "Nuevo usuario añadido correctamente", Toast.LENGTH_SHORT).show();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Método para navegar a la acvtivity de login
     * @param view referencia a la view que hace onClick
     */
    public void menu(View view){
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
    }
}