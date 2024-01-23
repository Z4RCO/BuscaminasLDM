package com.example.practica4.juego;



import static com.example.practica4.Input.TouchEvent.TOUCH_UP;

import android.graphics.Color;

import com.example.practica4.Graficos;
import com.example.practica4.Input;
import com.example.practica4.Juego;
import com.example.practica4.Musica;
import com.example.practica4.Pantalla;

import java.util.List;

public class PantallaJuego extends Pantalla {
    private final boolean BANDERA = false;

    enum EstadoJuego {
        Preparado,
        Ejecutandose,
        Pausado,
        FinJuego
    }

    EstadoJuego estado = EstadoJuego.Preparado;
    Buscaminas buscaminas;

    // Usado para saber si quieres poner banderas o comprobar celdas
    private boolean modo;

  

    // 2 atributos usados para detectar la dirección cuando arrastras
    private int previousX;
    private int previousY;

    // 2 atributos usados para llevar la cuenta del cómo el usuario mueve el tablero
    private int offsetX;
    private int offsetY;

    // Usado para cuando terminas de arrastrar no detecte que tocas la pantalla
    private int lastEvent;

    // Almacena si has ganado o no
    private boolean victoria;



    /**
     * Constructor de la clase
     * Crea el objeto de la partida en base a la dificultad
     * Inicializa la música
     * @param juego Referencia al juego
     */
    public PantallaJuego(Juego juego) {
        super(juego);
        int x, y, minas;
        if (juego.getDificultad() == 0) {
            x = 10;
            y = 10;
            minas = 9;
        } else if (juego.getDificultad() == 1) {
            x = 16;
            y = 16;
            minas = 40;
        } else {
            x = 50;
            y = 16;
            minas = 99;
        }
        

        buscaminas = new Buscaminas(x, y, minas);



        victoria = false;
    }


    /**
     * Método para actualizar la pantalla
     * @param deltaTime Tiempo actual del juego
     */
    @Override
    public void update(float deltaTime) {
        List<Input.TouchEvent> touchEvents = juego.getInput().getTouchEvents();
        juego.getInput().getKeyEvents();

        if (estado == EstadoJuego.Preparado) {
            updateReady(touchEvents);

        }
        if (estado == EstadoJuego.Ejecutandose) {
            updateRunning(touchEvents, deltaTime);
            if (juego.getMusica() != null && !juego.getMusica().isPlaying()) {
                juego.getMusica().play();
            }
        }
        if (estado == EstadoJuego.Pausado) {

            if (juego.getMusica().isPlaying())
                juego.getMusica().pause();
            updatePaused(touchEvents);

        }
        if (estado == EstadoJuego.FinJuego) {
            if (juego.getMusica() != null)
                juego.getMusica().stop();
            updateGameOver(touchEvents);

        }

    }

    /**
     * Método para actualizar la pantalla cuando comienza la partida
     * Espera a que se pulse la pantalla para comenzar la partida
     * @param touchEvents Pulsaciones en la pantalla
     */
    private void updateReady(List<Input.TouchEvent> touchEvents) {
        if (touchEvents.size() > 0 && touchEvents.get(0).type == TOUCH_UP)
            estado = EstadoJuego.Ejecutandose;
    }

    /**
     * Método para actualizar la pantalla cuando se ejecuta el juego
     * Si se pulsa el botón de pausa -> pausa la partida
     * Si se pulsa el botón de modo de juego -> cambia entre comprobar y bandera
     * Si se pulsa el tablero -> en función del modo, pone una bandera o comprueba la casilla
     * En base a eso, comprueba si el usuario gana o pierde la partida, o sigue jugando
     * @param touchEvents lista de pulsaciones de la pantalla
     * @param deltaTime tiempo actual del juego
     */
    private void updateRunning(List<Input.TouchEvent> touchEvents, float deltaTime) {
        for (Input.TouchEvent event : touchEvents) {
            if (event.type == TOUCH_UP) {
                if (lastEvent == Input.TouchEvent.TOUCH_DRAGGED) {
                    lastEvent = event.type;
                    continue;
                }
                Assets.pulsar.play(1);
                if (event.x >= 110 && event.x <= 110 + 90 && event.y >= 400 && event.y < 400 + 50) {
                    modo = !modo;
                } else if (event.x < 40 && event.y < 40) {
                    estado = EstadoJuego.Pausado;
                    return;
                } else if (event.y < 360) {
                    int tableroX = (event.x - offsetX) / 32;
                    int tableroY = (event.y - offsetY - 40) / 32;

                    if (tableroX < 0 || tableroX >= buscaminas.getX() || tableroY < 0 || tableroY >= buscaminas.getY())
                        continue;
                    if (modo == BANDERA) {
                        boolean win = buscaminas.bandera(tableroX, tableroY);
                        if (win) {
                            this.victoria = true;
                            estado = EstadoJuego.FinJuego;
                            Assets.ganar.play(1);
                            return;
                        }
                    } else {
                        if (!buscaminas.comprobar(tableroX, tableroY)) {
                            Assets.bomba.play(1);
                            estado = EstadoJuego.FinJuego;
                            return;
                        }
                    }
                }


            } else if (event.type == Input.TouchEvent.TOUCH_DRAGGED) { // Gestionar desplazamiento del tablero
                if (event.x > previousX) offsetX += 5;
                if (event.x < previousX) offsetX -= 5;
                if (event.y > previousY) offsetY += 5;
                if (event.y < previousY) offsetY -= 5;

                previousX = event.x;
                previousY = event.y;

            }
            lastEvent = event.type;
        }
    }

    /**
     * Método para actualizar la pantalla cuando el juego está pausado
     * Comprueba si se ha pulsado la pantalla para reanudar la partida o salir
     * @param touchEvents pulsaciones en la pantalla
     */
    private void updatePaused(List<Input.TouchEvent> touchEvents) {
        int len = touchEvents.size();
        for (int i = 0; i < len; i++) {
            Input.TouchEvent event = touchEvents.get(i);
            if (event.type == TOUCH_UP) {
                Assets.pulsar.play(1);
                if (event.x > 80 && event.x <= 240) {
                    if (event.y > 100 && event.y <= 148) {
                        estado = EstadoJuego.Ejecutandose;
                        return;
                    }
                    if (event.y > 148 && event.y < 196) {
                        if (juego.getMusica().isPlaying()) juego.getMusica().stop();
                        juego.volverMenu(false);
                        return;
                    }
                }
            }
        }
    }

    private void updateGameOver(List<Input.TouchEvent> touchEvents) {
        int len = touchEvents.size();
        if (len > 0) {
            for(Input.TouchEvent event: touchEvents){
                System.out.println(event.x + ", " + event.y);
                if (event.x >= 80 && event.x <= 200 &&
                        event.y >= 135 && event.y <= 165 && event.type == TOUCH_UP) {
                    if (juego.getMusica().isPlaying()) juego.getMusica().stop();
                    juego.volverMenu(victoria);
                }
            }
        }
    }


    @Override
    public void present(float deltaTime) {
        Graficos g = juego.getGraphics();

        g.drawPixmap(Assets.fondo, 0, 0);
        drawWorld();


        if (estado == EstadoJuego.Preparado) drawReadyUI();
        if (estado == EstadoJuego.Ejecutandose) drawRunningUI();
        if (estado == EstadoJuego.Pausado) drawPausedUI();
        if (estado == EstadoJuego.FinJuego) drawGameOverUI();
        drawText(g, String.valueOf(buscaminas.getMinasRestantes()), 280, 10);
    }


    private void drawWorld() {
        Graficos g = juego.getGraphics();
        int[][] tablero = buscaminas.getTablero();
        boolean[][] comprobado = buscaminas.getComprobado();

        for (int i = 0; i < buscaminas.getX(); ++i) {
            for (int j = 0; j < buscaminas.getY(); j++) {
                if (!comprobado[j][i]) {
                    if (!buscaminas.getBanderas().contains(new Coordenada(i, j))) {
                        g.drawPixmap(Assets.casillaOculta, i * 32 + offsetX, j * 32 + offsetY + 40);
                    } else {
                        g.drawPixmap(Assets.casillaBandera, i * 32 + offsetX, j * 32 + offsetY + 40);
                    }
                    continue;
                }
                switch (tablero[j][i]) {
                    case -1:
                        g.drawPixmap(Assets.casillaOculta, i * 32 + offsetX, j * 32 + offsetY + 40);
                        break;
                    case 0:
                        g.drawPixmap(Assets.casillaVacia, i * 32 + offsetX, j * 32 + offsetY + 40);
                        break;
                    case 1:
                        g.drawPixmap(Assets.casilla1, i * 32 + offsetX, j * 32 + offsetY + 40);
                        break;
                    case 2:
                        g.drawPixmap(Assets.casilla2, i * 32 + offsetX, j * 32 + offsetY + 40);
                        break;
                    case 3:
                        g.drawPixmap(Assets.casilla3, i * 32 + offsetX, j * 32 + offsetY + 40);
                        break;
                    case 4:
                        g.drawPixmap(Assets.casilla4, i * 32 + offsetX, j * 32 + offsetY + 40);
                        break;
                    case 5:
                        g.drawPixmap(Assets.casilla5, i * 32 + offsetX, j * 32 + offsetY + 40);
                        break;
                    case 6:
                        g.drawPixmap(Assets.casilla6, i * 32 + offsetX, j * 32 + offsetY + 40);
                        break;
                    case 7:
                        g.drawPixmap(Assets.casilla7, i * 32 + offsetX, j * 32 + offsetY + 40);
                        break;
                    case 8:
                        g.drawPixmap(Assets.casilla8, i * 32 + offsetX, j * 32 + offsetY + 40);
                        break;
                }
            }
        }
    }

    private void drawReadyUI() {
        Graficos g = juego.getGraphics();
        g.drawPixmap(Assets.preparado, 47, 100);
    }

    private void drawRunningUI() {
        Graficos g = juego.getGraphics();
        g.drawRect(0, 0, 320, 40, 0xFFD6D6D6);
        g.drawRect(0, 360, 320, 120, 0xFFD6D6D6);

        g.drawPixmap(Assets.pausa, 0, 0);
        if (modo == BANDERA) {
            g.drawPixmap(Assets.modoBandera, 0, 360);

        } else {
            g.drawPixmap(Assets.modoMina, 0, 360);
        }
    }

    private void drawPausedUI() {
        Graficos g = juego.getGraphics();

        g.drawPixmap(Assets.menuPausa, 80, 100);
        g.drawLine(0, 416, 480, 416, Color.BLACK);
    }

    private void drawGameOverUI() {
        Graficos g = juego.getGraphics();
        if (victoria) {
            g.drawPixmap(Assets.victoria, 62, 100);
        } else {
            g.drawPixmap(Assets.finjuego, 62, 100);

        }

    }

    public void drawText(Graficos g, String line, int x, int y) {
        int len = line.length();
        for (int i = 0; i < len; i++) {
            char character = line.charAt(i);

            if (character == ' ') {
                x += 20;
                continue;
            }

            int srcX = 0;
            int srcWidth = 0;
            if (character == '.') {
                srcX = 200;
                srcWidth = 10;
            } else {
                srcX = (character - '0') * 20;
                srcWidth = 20;
            }

            g.drawPixmap(Assets.numeros, x, y, srcX, 0, srcWidth, 32);
            x += srcWidth;
        }
    }

    @Override
    public void pause() {
        if (estado == EstadoJuego.Ejecutandose)
            estado = EstadoJuego.Pausado;

        // Pausar la música al pausar el juego
        if (juego.getMusica().isPlaying())
            juego.getMusica().pause();
    }


    @Override
    public void resume() {
        if (estado == EstadoJuego.Ejecutandose && juego.getMusica() != null && !juego.getMusica().isPlaying())
            juego.getMusica().play();
    }

    @Override
    public void dispose() {
        // Liberar recursos al salir de la pantalla
        if (juego.getMusica() != null) {
            juego.getMusica().stop();
            juego.getMusica().dispose();
        }
    }
}
