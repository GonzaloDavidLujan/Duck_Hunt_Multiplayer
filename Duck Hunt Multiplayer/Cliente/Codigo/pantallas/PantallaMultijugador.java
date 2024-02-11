package com.dojan.mijuego.pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.dojan.mijuego.elementos.Imagen;
import com.dojan.mijuego.elementos.Texto;
import com.dojan.mijuego.elementos.perro.Perro;
import com.dojan.mijuego.elementos.perro.PerroEmpate;
import com.dojan.mijuego.elementos.perro.PerroPatos;
import com.dojan.mijuego.elementos.perro.PerroRisa;
import com.dojan.mijuego.io.Entradas;
import com.dojan.mijuego.red.Estados;
import com.dojan.mijuego.red.HiloCliente;
import com.dojan.mijuego.utiles.Config;
import com.dojan.mijuego.utiles.Recursos;
import com.dojan.mijuego.utiles.Render;

public class PantallaMultijugador implements Screen {

	private Texto conectando, esperandoPj, pjDesconectado, empiezaJuego, volviendoAlMenu, salaLlena, serverDesconectado,
			serverNoEncontrado;
	private HiloCliente hc;
	private Imagen fondo, pasto;
	private float timer = 3f, timerJuego = 3f;
	private Entradas entradas = new Entradas();
	private boolean clickEnviado = false;
	private Texto[] hud = new Texto[2];
	private String[] txtHud = { "SCORE", "BALAS: " };
	private Texto[] pantallaGO = new Texto[4];
	private String[] txtGO = { "Perdiste!", "hiciste: " + Recursos.puntos + " puntos", "Volver a Jugar",
			"Volver al Menu" };
	private Texto[] pantallaVictoria = new Texto[4];
	private String[] txtVictoria = { "Ganaste!", "hiciste: " + Recursos.puntos + " puntos", "Volver a Jugar",
			"Volver al Menu" };
	private String[] txtEmpate = { "Empataron!", "hicieron: " + Recursos.puntos + " puntos", "Volver a Jugar",
			"Volver al Menu" };
	private Texto[] pantallaEmpate = new Texto[4];
	private Perro perroRisa, perroPatos, perroEmpate;
	private Rectangle bbMouse, bbMenuVictoria, bbVolverVictoria, bbMenuGO, bbVolverGO, bbMenuEmpate, bbVolverEmpate;
	private ShapeRenderer sr;
	private OrthographicCamera cam;
	private float timerTimeOut = 10f;
	private boolean loop = false;
	private Music musicaVictoria = Gdx.audio.newMusic(Gdx.files.internal(Recursos.MUSICA_VICTORIA));
	private Music musicaDerrota = Gdx.audio.newMusic(Gdx.files.internal(Recursos.MUSICA_GO));
	private Sound sonidoSeleccion = Gdx.audio.newSound(Gdx.files.internal(Recursos.ELEGIR_OPC_MENU));

	@Override
	public void show() {
		System.out.println("CLIENTE");

		conectando = new Texto(Recursos.FUENTE_MENU, 50);
		conectando.setTexto("CONECTANDO AL SERVIDOR");
		conectando.setPosicion((Config.anchoVP - conectando.getAncho()) / 2, 600);
		conectando.setColor(Color.WHITE);

		esperandoPj = new Texto(Recursos.FUENTE_MENU, 50);
		esperandoPj.setTexto("Esperando al rival");
		esperandoPj.setPosicion((Config.anchoVP - esperandoPj.getAncho()) / 2, 600);

		empiezaJuego = new Texto(Recursos.FUENTE_MENU, 50);
		empiezaJuego.setTexto("Empezando el juego");
		empiezaJuego.setPosicion((Config.anchoVP - empiezaJuego.getAncho()) / 2, 600);

		pjDesconectado = new Texto(Recursos.FUENTE_MENU, 50);
		pjDesconectado.setTexto("El otro jugador se desconectó");
		pjDesconectado.setPosicion((Config.anchoVP - pjDesconectado.getAncho()) / 2, 600);

		volviendoAlMenu = new Texto(Recursos.FUENTE_MENU, 50);
		volviendoAlMenu.setTexto("Volviendo al menu");
		volviendoAlMenu.setPosicion((Config.anchoVP - volviendoAlMenu.getAncho()) / 2,
				(empiezaJuego.getY() - volviendoAlMenu.getAlto()) - 10);

		salaLlena = new Texto(Recursos.FUENTE_MENU, 50);
		salaLlena.setTexto("Sala Llena Papi\n");
		salaLlena.setPosicion((Config.anchoVP - salaLlena.getAncho()) / 2, 600);

		serverDesconectado = new Texto(Recursos.FUENTE_MENU, 50);
		serverDesconectado.setTexto("Te has desconectado del server\n");
		serverDesconectado.setPosicion((Config.anchoVP - serverDesconectado.getAncho()) / 2, 600);

		serverNoEncontrado = new Texto(Recursos.FUENTE_MENU, 50);
		serverNoEncontrado.setTexto("No se encontró servidor");
		serverNoEncontrado.setPosicion((Config.anchoVP - serverNoEncontrado.getAncho()) / 2, 600);

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		// PANTALLA GAME OVER
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

		pantallaGO[0] = new Texto(Recursos.FUENTE_MENU, 70);
		pantallaGO[0].setTexto(txtGO[0]);
		pantallaGO[0].setColor(Color.FOREST);
		pantallaGO[0].setPosicion((Config.anchoVP - pantallaGO[0].getAncho()) / 2, (int) (0.975f * Config.altoVP));

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

		pantallaGO[1] = new Texto(Recursos.FUENTE_MENU, 70);
		pantallaGO[1].setTexto(txtGO[1]);
		pantallaGO[1].setColor(Color.FOREST);
		pantallaGO[1].setPosicion((Config.anchoVP - pantallaGO[1].getAncho()) / 2,
				pantallaGO[0].getY() - pantallaGO[1].getAlto() - 10);

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

		perroRisa = new PerroRisa();
		perroRisa.setPosicion((Config.anchoVP - perroRisa.getAncho()) / 2,
				(int) (pantallaGO[1].getY() - perroRisa.getAlto() - 60));

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

		pantallaGO[2] = new Texto(Recursos.FUENTE_MENU, 70);
		pantallaGO[2].setTexto(txtGO[2]);
		pantallaGO[2].setColor(Color.GREEN);
		pantallaGO[2].setPosicion((Config.anchoVP - pantallaGO[2].getAncho()) / 2,
				perroRisa.getY() - pantallaGO[2].getAlto());

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

		pantallaGO[3] = new Texto(Recursos.FUENTE_MENU, 70);
		pantallaGO[3].setTexto(txtGO[3]);
		pantallaGO[3].setColor(Color.GREEN);
		pantallaGO[3].setPosicion((Config.anchoVP - pantallaGO[3].getAncho()) / 2,
				pantallaGO[2].getY() - pantallaGO[3].getAlto() - 40);

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

		bbVolverGO = new Rectangle(pantallaGO[2].getX(), pantallaGO[2].getY() - pantallaGO[2].getAlto(),
				pantallaGO[2].getAncho(), pantallaGO[2].getAlto());
		bbMenuGO = new Rectangle(pantallaGO[3].getX(), pantallaGO[3].getY() - pantallaGO[3].getAlto(),
				pantallaGO[3].getAncho(), pantallaGO[3].getAlto());

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		// PANTALLA VICTORIA
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

		pantallaVictoria[0] = new Texto(Recursos.FUENTE_MENU, 70);
		pantallaVictoria[0].setTexto(txtVictoria[0]);
		pantallaVictoria[0].setColor(Color.WHITE);
		pantallaVictoria[0].setPosicion((Config.anchoVP - pantallaVictoria[0].getAncho()) / 2,
				(int) (0.975f * Config.altoVP));

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

		pantallaVictoria[1] = new Texto(Recursos.FUENTE_MENU, 70);
		pantallaVictoria[1].setTexto(txtVictoria[1]);
		pantallaVictoria[1].setColor(Color.WHITE);
		pantallaVictoria[1].setPosicion((Config.anchoVP - pantallaVictoria[1].getAncho()) / 2,
				pantallaVictoria[0].getY() - pantallaVictoria[1].getAlto() - 10);

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

		perroPatos = new PerroPatos();
		perroPatos.setPosicion((Config.anchoVP - perroPatos.getAncho()) / 2,
				(int) (pantallaVictoria[1].getY() - perroPatos.getAlto() - 60));

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

		pantallaVictoria[2] = new Texto(Recursos.FUENTE_MENU, 70);
		pantallaVictoria[2].setTexto(txtVictoria[2]);
		pantallaVictoria[2].setColor(Color.WHITE);
		pantallaVictoria[2].setPosicion((Config.anchoVP - pantallaVictoria[2].getAncho()) / 2,
				perroPatos.getY() - pantallaVictoria[2].getAlto());

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

		pantallaVictoria[3] = new Texto(Recursos.FUENTE_MENU, 70);
		pantallaVictoria[3].setTexto(txtVictoria[3]);
		pantallaVictoria[3].setColor(Color.WHITE);
		pantallaVictoria[3].setPosicion((Config.anchoVP - pantallaVictoria[3].getAncho()) / 2,
				pantallaVictoria[2].getY() - pantallaVictoria[3].getAlto() - 40);

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

		bbVolverVictoria = new Rectangle(pantallaVictoria[2].getX(),
				pantallaVictoria[2].getY() - pantallaVictoria[2].getAlto(), pantallaVictoria[2].getAncho(),
				pantallaVictoria[2].getAlto());
		bbMenuVictoria = new Rectangle(pantallaVictoria[3].getX(),
				pantallaVictoria[3].getY() - pantallaVictoria[3].getAlto(), pantallaVictoria[3].getAncho(),
				pantallaVictoria[3].getAlto());

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		// PANTALLA EMPATE
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

		pantallaEmpate[0] = new Texto(Recursos.FUENTE_MENU, 70);
		pantallaEmpate[0].setTexto(txtEmpate[0]);
		pantallaEmpate[0].setColor(Color.FOREST);
		pantallaEmpate[0].setPosicion((Config.anchoVP - pantallaEmpate[0].getAncho()) / 2, Config.altoVP - 10);

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

		pantallaEmpate[1] = new Texto(Recursos.FUENTE_MENU, 70);
		pantallaEmpate[1].setTexto(txtEmpate[1]);
		pantallaEmpate[1].setColor(Color.FOREST);
		pantallaEmpate[1].setPosicion((Config.anchoVP - pantallaEmpate[1].getAncho()) / 2,
				pantallaEmpate[0].getY() - pantallaEmpate[1].getAlto() - 10);

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

		perroEmpate = new PerroEmpate();
		perroEmpate.setPosicion((Config.anchoVP - perroRisa.getAncho()) / 2,
				(int) (pantallaEmpate[1].getY() - perroEmpate.getAlto() - 100));

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

		pantallaEmpate[2] = new Texto(Recursos.FUENTE_MENU, 70);
		pantallaEmpate[2].setTexto(txtEmpate[2]);
		pantallaEmpate[2].setColor(Color.GREEN);
		pantallaEmpate[2].setPosicion((Config.anchoVP - pantallaEmpate[2].getAncho()) / 2,
				perroEmpate.getY() - pantallaEmpate[2].getAlto());

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

		pantallaEmpate[3] = new Texto(Recursos.FUENTE_MENU, 70);
		pantallaEmpate[3].setTexto(txtEmpate[3]);
		pantallaEmpate[3].setColor(Color.GREEN);
		pantallaEmpate[3].setPosicion((Config.anchoVP - pantallaEmpate[3].getAncho()) / 2,
				pantallaEmpate[2].getY() - pantallaEmpate[3].getAlto() - 40);

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		bbVolverEmpate = new Rectangle(pantallaEmpate[2].getX(), pantallaEmpate[2].getY() - pantallaEmpate[2].getAlto(),
				pantallaEmpate[2].getAncho(), pantallaEmpate[2].getAlto());
		bbMenuEmpate = new Rectangle(pantallaEmpate[3].getX(), pantallaEmpate[3].getY() - pantallaEmpate[3].getAlto(),
				pantallaGO[3].getAncho(), pantallaEmpate[3].getAlto());
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

		bbMouse = new Rectangle(0, 0, 5, 5);

		sr = new ShapeRenderer();
		cam = new OrthographicCamera(Config.anchoVP, Config.altoVP);
		cam.position.set(Config.anchoVP / 2, Config.altoVP / 2, 0);
		cam.update(); // Actualiza la cámara

		sr.setProjectionMatrix(cam.combined);
		for (int i = 0; i < hud.length; i++) {
			hud[i] = new Texto(Recursos.FUENTE_MENU, 30);
			hud[i].setColor(Color.YELLOW);
			hud[i].setPosicion(10, ((i == 0) ? 0 : hud[i - 1].getY()) + hud[i].getAlto() + 30);
			hud[i].setTexto(txtHud[i]);
		}

		hc = new HiloCliente();
		hc.start();
		fondo = new Imagen(Recursos.FONDO_JUEGO);
		fondo.setTamano(Config.anchoVP, Config.altoVP);
		pasto = new Imagen(Recursos.PASTO);
		pasto.setTamano(Config.anchoVP, Config.altoVP);
		Gdx.input.setInputProcessor(entradas);

	}

	@Override
	public void render(float delta) {
		if (hc.isGanado()) {
			Render.limpiarPantallaArcoiris();
		} else {
			Render.limpiarPantalla(0, 0, 0);
		}

		actualizarBbMouse(entradas.getMousePosX(), (Config.altoVP - entradas.getMousePosY()) - 2);

		if (Recursos.juegoEmpezado) {
			Render.batch.begin();
			timerJuego -= 0.017f;
			empiezaJuego.dibujar();
			Render.batch.end();
			if (timerJuego < 0) {
				Render.batch.begin();
				fondo.dibujar();
				if (Recursos.cantPatos.size() != 0) {

					Recursos.cantPatos.get(0).dibujar();
				}

				pasto.dibujar();
				hud[0].setTexto("SCORE: " + Recursos.puntos);
				for (int i = 0; i < hud.length; i++) {
					hud[i].dibujar();
				}
				float xOffset = 0; // Variable para almacenar el desplazamiento horizontal entre balas

				for (int i = 0; i < Recursos.balasMultijugador.size(); i++) {
					float x = hud[1].getX() + hud[1].getAncho() + xOffset;
					float y = hud[1].getY() - hud[1].getAlto();

					Recursos.balasMultijugador.get(i).setPosicion(x, y);
					Recursos.balasMultijugador.get(i).setTamano(20, 20);

					xOffset += Recursos.balasMultijugador.get(i).getAncho(); // Sumar el ancho de la bala actual para el
																				// próximo desplazamiento
					Recursos.balasMultijugador.get(i).dibujar();
				}
				Recursos.oponente.dibujar();
				Render.batch.end();
				enviarPosicionMouse(entradas.getMousePosX(), entradas.getMousePosY());

				if (entradas.isClick() && !clickEnviado) {
					enviarClick();
					clickEnviado = true;
				}
				if (!entradas.isClick()) {
					clickEnviado = false;
				}

			}

		} else if (hc.isGanado()) {
			// PANTALLA VICTORIA

			if (!musicaVictoria.isLooping() && Recursos.volMusica) {
				musicaVictoria.setLooping(true);
				musicaVictoria.play();
			}

//			sr.begin(ShapeType.Line);
//			sr.setColor(Color.RED);
//			sr.rect(bbVolverVictoria.x, bbVolverVictoria.y, bbVolverVictoria.width, bbVolverVictoria.height);
//			sr.rect(bbMenuVictoria.x, bbMenuVictoria.y, bbMenuVictoria.width, bbMenuVictoria.height);
//			sr.setColor(Color.WHITE);
//			sr.rect(bbMouse.x, bbMouse.y, bbMouse.width, bbMouse.height);
//			sr.end();

			Render.batch.begin();
			pantallaVictoria[1].setTexto("hiciste: " + Recursos.puntos + " puntos");
			for (int i = 0; i < pantallaVictoria.length; i++) {
				pantallaVictoria[i].dibujar();
			}
			perroPatos.dibujar();
			Render.batch.end();

			if (bbVolverVictoria.overlaps(bbMouse)) {

				pantallaVictoria[2].setColor(Color.BLACK);
				if (entradas.isClick()) {
					Recursos.puntos = 0;
					Recursos.juegoEmpezado = false;
					Recursos.juegoTerminado = false;

					if (musicaVictoria.isPlaying()) {
						musicaVictoria.stop();
					}

					if (Recursos.volSonidos) {
						sonidoSeleccion.play();	
					}
					
					finalizar();

					Render.app.setScreen(new PantallaMultijugador());
				}
			} else {
				pantallaVictoria[2].setColor(Color.WHITE);
			}

			if (bbMenuVictoria.overlaps(bbMouse)) {
				pantallaVictoria[3].setColor(Color.BLACK);


				if (entradas.isClick()) {
					Recursos.puntos = 0;
					Recursos.juegoEmpezado = false;
					Recursos.juegoTerminado = false;

					if (musicaVictoria.isPlaying()) {
						musicaVictoria.stop();
					}

					if (Recursos.volSonidos) {
						sonidoSeleccion.play();
					}

					finalizar();

					Render.app.setScreen(new PantallaMenu());
				}
			} else {
				pantallaVictoria[3].setColor(Color.WHITE);
			}

		} else if (hc.isPerdido()) {
			// PANTALLA GAMEOVER

			if (!musicaDerrota.isLooping() && Recursos.volMusica) {
				musicaDerrota.setLooping(true);
				musicaDerrota.play();
			}

			if (!loop && Recursos.volSonidos) {
				perroRisa.getSonido().loop();
				loop = true;
			}
//			sr.begin(ShapeType.Line);
//			sr.setColor(Color.RED);
//			sr.rect(bbVolverGO.x, bbVolverGO.y, bbVolverGO.width, bbVolverGO.height);
//			sr.rect(bbMenuGO.x, bbMenuGO.y, bbMenuGO.width, bbMenuGO.height);
//			sr.setColor(Color.WHITE);
//			sr.rect(bbMouse.x, bbMouse.y, bbMouse.width, bbMouse.height);
//			sr.end();

			Render.batch.begin();
			for (int i = 0; i < pantallaGO.length; i++) {
				pantallaGO[i].dibujar();
			}

			perroRisa.dibujar();
			Render.batch.end();

			if (bbVolverGO.overlaps(bbMouse)) {
				pantallaGO[2].setColor(Color.FIREBRICK);

				if (entradas.isClick()) {
					Recursos.puntos = 0;
					Recursos.juegoEmpezado = false;
					Recursos.juegoTerminado = false;

					if (musicaDerrota.isPlaying()) {
						musicaDerrota.stop();
					}
					if (Recursos.volSonidos) {
						perroRisa.getSonido().stop();
					}

					if (Recursos.volSonidos) {
						sonidoSeleccion.play();
					}

					finalizar();

					Render.app.setScreen(new PantallaMultijugador());
				}
			} else {
				pantallaGO[2].setColor(Color.GREEN);
			}
			if (bbMenuGO.overlaps(bbMouse)) {
				pantallaGO[3].setColor(Color.FIREBRICK);


				if (entradas.isClick()) {
					Recursos.puntos = 0;
					Recursos.juegoEmpezado = false;
					Recursos.juegoTerminado = false;

					if (musicaDerrota.isPlaying()) {
						musicaDerrota.stop();
					}
					if (Recursos.volSonidos) {
						perroRisa.getSonido().stop();
					}

					if (Recursos.volSonidos) {
						sonidoSeleccion.play();
					}

					finalizar();

					Render.app.setScreen(new PantallaMenu());
				}
			} else {
				pantallaGO[3].setColor(Color.GREEN);
			}

		} else if (hc.isEmpatado()) {
			// PANTALLA EMPATE
			Render.batch.begin();
			perroEmpate.dibujar();
			for (int i = 0; i < pantallaEmpate.length; i++) {
				pantallaEmpate[i].dibujar();
			}
			Render.batch.end();

			if (bbVolverEmpate.overlaps(bbMouse)) {
				pantallaEmpate[2].setColor(Color.FIREBRICK);


				if (entradas.isClick()) {
					Recursos.puntos = 0;
					Recursos.juegoEmpezado = false;
					Recursos.juegoTerminado = false;

					if (Recursos.volSonidos) {
						sonidoSeleccion.play();
					}

					finalizar();

					Render.app.setScreen(new PantallaMultijugador());
				}
			} else {
				pantallaEmpate[2].setColor(Color.GREEN);
			}

			if (bbMenuEmpate.overlaps(bbMouse)) {
				pantallaEmpate[3].setColor(Color.FIREBRICK);

				if (entradas.isClick()) {
					Recursos.puntos = 0;
					Recursos.juegoEmpezado = false;
					Recursos.juegoTerminado = false;

					if (Recursos.volSonidos) {
						sonidoSeleccion.play();
					}

					finalizar();

					Render.app.setScreen(new PantallaMenu());
				}
			} else {
				pantallaEmpate[3].setColor(Color.GREEN);
			}

		} else if (hc.isJugadorDesconectado()) {
			Render.batch.begin();
			pjDesconectado.dibujar();
			volviendoAlMenu.dibujar();
			timer -= 0.017;
			if (timer < 0) {
				Render.app.setScreen(new PantallaMenu());
				finalizar();
			}
			Render.batch.end();
		} else if (hc.getEstado() == Estados.CONECTADO && !Recursos.juegoTerminado) {
			Render.batch.begin();
			esperandoPj.dibujar();
			Render.batch.end();
		} else if (hc.isSalaLlena()) {
			Render.batch.begin();
			salaLlena.dibujar();
			timer -= 0.017f;
			if (timer < 0) {
				Render.app.setScreen(new PantallaMenu());
			}
			Render.batch.end();
		} else if (hc.isServerDesconectado()) {
			Render.batch.begin();
			serverDesconectado.dibujar();
			volviendoAlMenu.dibujar();
			timer -= 0.017;
			if (timer < 0) {
				Render.app.setScreen(new PantallaMenu());
				finalizar();
			}
			Render.batch.end();
		} else {
			Render.batch.begin();
			conectando.dibujar();
			Render.batch.end();

			if (timerTimeOut > 0) {
				timerTimeOut -= 0.017f;
			}
			if (timerTimeOut < 0) {
				conectando.setColor(Color.BLACK);
				Render.batch.begin();
				serverNoEncontrado.dibujar();
				volviendoAlMenu.dibujar();
				Render.batch.end();

				if (timer > 0) {
					timer -= 0.017f;
				}

				if (timer < 0) {
					Render.app.setScreen(new PantallaMenu());
				}
			}
		}

		if (entradas.isEsc()) {
			synchronized (Recursos.balasMultijugador) {
				Recursos.balasMultijugador.clear();
			}
			if (musicaDerrota.isPlaying()) {
				musicaDerrota.stop();
			}
			if (musicaVictoria.isPlaying()) {
				musicaVictoria.stop();
			}
			if (Recursos.volSonidos) {
				perroRisa.getSonido().stop();
			}
			finalizar();
			Render.app.setScreen(new PantallaMenu());

		}
	}

	private void enviarClick() {
		hc.enviarMensaje("Click#");
	}

	private void enviarPosicionMouse(int mouseX, int mouseY) {
		hc.enviarMensaje("PosMouse#" + (mouseX - 15) + "#" + ((Config.altoVP - mouseY) - 17));
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void hide() {
	}

	@Override
	public void dispose() {
		System.out.println("DISPOSE MULTIJUGADOR");

		musicaDerrota.dispose();
		musicaVictoria.dispose();
		sonidoSeleccion.dispose();
		perroRisa.getSonido().dispose();

		if (hc.getEstado() == Estados.CONECTADO) {
			finalizar();

		}

	}

	public void finalizar() {

		// Envía el mensaje de desconexion
		if (!hc.isJugadorDesconectado() && hc.getEstado() == Estados.CONECTADO) {
			hc.enviarMensaje("Desconexion#");
		}

		// Termina el hilo del cliente
		hc.terminar();

		// Limpia la lista de patos
		synchronized (Recursos.cantPatos) {
			Recursos.cantPatos.clear();
		}
		synchronized (Recursos.balasMultijugador) {
			Recursos.balasMultijugador.clear();
		}
	}

	private void actualizarBbMouse(float mouseX, float mouseY) {
		bbMouse.x = mouseX - 2;
		bbMouse.y = mouseY;
	}
}
