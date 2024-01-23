package com.example.practica4.androidimpl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import android.content.res.AssetManager;
import android.os.Environment;

import com.example.practica4.FileIO;

public class AndroidFileIO implements FileIO {
    AssetManager assets;
    String rutaAlmacenamientoExterno;

    public AndroidFileIO(AssetManager assets) {
        this.assets = assets;
        this.rutaAlmacenamientoExterno = Environment
                .getExternalStorageDirectory().getAbsolutePath()
                + File.separator;
    }

    @Override
    public InputStream leerAsset(String nombreArchivo) throws IOException {
        return assets.open(nombreArchivo);
    }

    @Override
    public InputStream leerArchivo(String nombreArchivo) throws IOException {
        return new FileInputStream(rutaAlmacenamientoExterno + nombreArchivo);
    }

    @Override
    public OutputStream escribirArchivo(String nombreArchivo) throws IOException {
        return new FileOutputStream(rutaAlmacenamientoExterno + nombreArchivo);
    }

}

