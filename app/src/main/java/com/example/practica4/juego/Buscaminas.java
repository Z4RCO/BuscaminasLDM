package com.example.practica4.juego;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class Buscaminas {
    private Set<Coordenada> minas;
    private int[][] tablero;
    private boolean[][] comprobado;

    private int minasRestantes;
    private Set<Coordenada> banderas;
    private int x, y;

    public Buscaminas(int sizeX, int sizeY, int nMinas){
        if(sizeX < 0 || sizeY < 0 || nMinas <= 0)throw new IllegalArgumentException("No puede haber parametros negativos");
        if(nMinas > sizeX*sizeY)throw new IllegalArgumentException("No puede haber mas minas que casillas");
        tablero = new int[sizeY][sizeX];
        comprobado = new boolean[sizeY][sizeX];
        x = sizeX;
        y = sizeY;


        minas = new HashSet<>();
        banderas = new HashSet<>();
        minasRestantes = nMinas;
        Random r = new Random();
        for(int i = 0; i < nMinas; ++i){
            boolean correcto = false;
            while(!correcto){
                int rX = r.nextInt(sizeX);
                int rY = r.nextInt(sizeY);
                correcto = minas.add(new Coordenada(rX, rY));
            }
        }
        initTablero();
    }

    /**
     * Método para inicializar las estructuras de la clase
     * Crea a partir de la matriz del tablero, coloca en ella las minas
     * Además, pone los números correspondientes en cada casilla
     */
    private void initTablero(){
        for(Coordenada m: minas){
           tablero[m.getY()][m.getX()] = -1;
            for(int i = -1; i <= 1; ++i){
                for(int j = -1; j <= 1; ++j){
                    try{
                        if(tablero[m.getY() + i][m.getX() + j] != -1)tablero[m.getY() + i][m.getX() + j]++;
                    }catch(ArrayIndexOutOfBoundsException ignored){}
                }
            }
        }
        System.out.println("Inicializando tablero...");
        for(int i = 0; i < y; ++i){
            for(int j = 0; j < x; ++j){
               if(tablero[i][j] != -1) System.out.print(tablero[i][j] + " ");
               else System.out.print("* ");

            }
            System.out.println();
        }
    }

    /**
     * Método para comprobar el estado de una casilla
     * Si hay mina, pierdes la partida
     * Sino, marca como comprobadas todas las casillas adyacentes que sean 0 o número
     * @param x coordenada x del tablero
     * @param y coordenada y del tablero
     * @return false si pierdes la partida, true en caso contrario
     */
    public boolean comprobar(int x, int y){
        if(x > getX() ||y > getY())throw new RuntimeException("Coordenadas invalidas");

        Coordenada c = new Coordenada(x, y);

        if(banderas.contains(c))return true;
        if(minas.contains(c)){
            return false;
        }

        if(comprobado[y][x])return true;
        else{
            comprobado[y][x] = true;
            if(tablero[y][x] == 0){
                Set<Coordenada> t = obtenerCasillasAdyacentes(tablero, y, x, new ArrayList<>());
                if(t != null)
                    for(Coordenada coordenada: t){
                        comprobado[coordenada.getX()][coordenada.getY()] = true;
                    }
            }
        }
        return true;
    }

    /**
     * Método para buscar las casillas adyacentes a una que tengan 0 o un número (no mina)
     * @param matriz matriz en la que buscar las casillas
     * @param p1 coordenada 1 de la casilla de inicio
     * @param p2 coordenada 2 de la casiila de inicio
     * @param visitadas lista de casillas visitadas para no hacer bucle infinito
     * @return Copnjunto de casillas encontradas
     */
    private static Set<Coordenada> obtenerCasillasAdyacentes(int[][] matriz, int p1, int p2, List<Coordenada> visitadas) {
        if(visitadas.contains(new Coordenada(p1, p2)))return null;
        int valorInicial = matriz[p1][p2];
        int filas = matriz.length;
        int columnas = matriz[0].length;


        Set<Coordenada> adyacentes = new HashSet<>();

        // Verificar cada dirección (arriba, abajo, izquierda, derecha y diagonales)
        int[][] direcciones = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}, {-1, -1}, {-1, 1}, {1, -1}, {1, 1}};

        for (int[] direccion : direcciones) {
            int nuevaFila = p1 + direccion[0];
            int nuevaColumna = p2 + direccion[1];

            // Verificar si la nueva posición está dentro de los límites de la matriz
            if (0 <= nuevaFila && nuevaFila < filas && 0 <= nuevaColumna && nuevaColumna < columnas) {
                if (matriz[nuevaFila][nuevaColumna] == valorInicial) {
                    adyacentes.add(new Coordenada(nuevaFila, nuevaColumna));
                    visitadas.add(new Coordenada(p1, p2));
                    Set<Coordenada> res = obtenerCasillasAdyacentes(matriz, nuevaFila, nuevaColumna, visitadas);
                    if(res != null)adyacentes.addAll(res);
                }
                else if(matriz[nuevaFila][nuevaColumna] > 0){
                    adyacentes.add(new Coordenada(nuevaFila, nuevaColumna));
                }
            }
        }

        return adyacentes;
    }

    /**
     * Método para poner y quitar banderas
     * Si no hay bandera, la pone y resta 1 a las minas restantes
     * Si hay bandera, la quita y suma 1 a las minas restantes
     * @param x coordenada x del tablero
     * @param y coordenada y del tablero
     * @return true si ganas la partida, false en caso contrario
     */
    public boolean bandera(int x, int y){
        if(x > getX() ||y > getY())throw new IllegalArgumentException("Coordenadas invalidas");
        if(comprobado[y][x])return false;
        Coordenada c = new Coordenada(x, y);
        boolean correcto = banderas.contains(c);
        if(!correcto){
            banderas.add(c);
            minasRestantes--;
            if(minasRestantes <= 0){
                return minas.equals(banderas);
            }
            else return false;
        }
        else{
            minasRestantes++;
            banderas.remove(c);
            return false;
        }
    }
    public void printTablero(){
        for(int i = 0; i < y; ++i){
            for(int j = 0; j < x; ++j){
                if(tablero[i][j] != -1)System.out.print(tablero[i][j] + " ");
                else System.out.print("* ");
            }
            System.out.println();
        }
    }
    public void printComprobado(){
        for(int i = 0; i < y; ++i){
            for(int j = 0; j < x; ++j){
                if(comprobado[i][j])System.out.print(1);
                else System.out.print(0);
            }
            System.out.println();
        }
    }

    public int[][] getTablero() {
        return tablero;
    }

    public boolean[][] getComprobado() {
        return comprobado;
    }

    public Set<Coordenada> getBanderas() {
        return banderas;
    }
    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }

    public int getMinasRestantes() {
        return minasRestantes;
    }
}


