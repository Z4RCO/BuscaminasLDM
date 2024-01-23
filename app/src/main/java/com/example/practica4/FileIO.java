package com.example.practica4;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface FileIO {
    public InputStream leerAsset(String nombreArchivo) throws IOException;

    public InputStream leerArchivo(String nombreArchivo) throws IOException;

    public OutputStream escribirArchivo(String nombreArchivo) throws IOException;

}

