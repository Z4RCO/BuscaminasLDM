package com.example.practica4.juego;


import com.example.practica4.Graficos;
import com.example.practica4.Juego;
import com.example.practica4.Pantalla;

public class LoadingScreen extends Pantalla {

    public LoadingScreen(Juego juego) {
        super(juego);
    }

    @Override
    public void update(float deltaTime) {
        Graficos g = juego.getGraphics();
        Assets.fondo = g.newPixmap("fondo.jpg", Graficos.PixmapFormat.RGB565);
        Assets.finjuego = g.newPixmap("findejuego.png", Graficos.PixmapFormat.ARGB4444);
        Assets.victoria = g.newPixmap("victoria.png", Graficos.PixmapFormat.ARGB4444);
        Assets.menuPausa = g.newPixmap("menuPausa.png", Graficos.PixmapFormat.ARGB4444);
        Assets.preparado = g.newPixmap("preparado.png", Graficos.PixmapFormat.ARGB4444);


        Assets.casillaOculta = g.newPixmap("casillaoculta.png", Graficos.PixmapFormat.ARGB4444);
        Assets.casillaVacia = g.newPixmap("casilla.png", Graficos.PixmapFormat.ARGB4444);
        Assets.casilla1 = g.newPixmap("casilla1.png", Graficos.PixmapFormat.ARGB4444);
        Assets.casilla2 = g.newPixmap("casilla2.png", Graficos.PixmapFormat.ARGB4444);
        Assets.casilla3 = g.newPixmap("casilla3.png", Graficos.PixmapFormat.ARGB4444);
        Assets.casilla4 = g.newPixmap("casilla4.png", Graficos.PixmapFormat.ARGB4444);
        Assets.casilla5 = g.newPixmap("casilla5.png", Graficos.PixmapFormat.ARGB4444);
        Assets.casilla6 = g.newPixmap("casilla6.png", Graficos.PixmapFormat.ARGB4444);
        Assets.casilla7 = g.newPixmap("casilla7.png", Graficos.PixmapFormat.ARGB4444);
        Assets.casilla8 = g.newPixmap("casilla8.png", Graficos.PixmapFormat.ARGB4444);
        Assets.casillaBandera = g.newPixmap("casillabandera.png", Graficos.PixmapFormat.ARGB4444);

        Assets.modoBandera = g.newPixmap("modoBandera.png", Graficos.PixmapFormat.ARGB4444);
        Assets.modoMina = g.newPixmap("modoMina.png", Graficos.PixmapFormat.ARGB4444);
        Assets.pausa = g.newPixmap("pausa.png", Graficos.PixmapFormat.ARGB4444);
        Assets.numeros = g.newPixmap("numeros.png", Graficos.PixmapFormat.ARGB4444);

        Assets.pulsar = juego.getAudio().nuevoSonido("pulsar.ogg");
        Assets.bomba = juego.getAudio().nuevoSonido("bomba.mp3");
        Assets.ganar = juego.getAudio().nuevoSonido("ganar.mp3");

        juego.setScreen(new PantallaJuego(juego));
    }

    @Override
    public void present(float deltaTime) {

    }

    @Override
    public void pause() {

    }


    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }
}