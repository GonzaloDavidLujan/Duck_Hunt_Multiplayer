package com.dojan.mijuego.pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.dojan.mijuego.elementos.Imagen;
import com.dojan.mijuego.elementos.Texto;
import com.dojan.mijuego.io.Entradas;
import com.dojan.mijuego.utiles.Config;
import com.dojan.mijuego.utiles.Recursos;
import com.dojan.mijuego.utiles.Render;

public class PantallaMenu implements Screen {
	private Imagen menu;
	private Texto[] opciones = new Texto[4];
	private String[] texto = { "singleplayer", "multiplayer", "opciones", "salir" };
	private Entradas entradas = new Entradas();
	private int avance = 60, opc = 1;
	private AssetManager manager;
	private Sound seleccionar, mover;
	private boolean sonidoEnReproduccion = false;
	private long moverID;
	private Rectangle bbOpc[] = new Rectangle[opciones.length], bbMouse;
	private ShapeRenderer sr;
	private OrthographicCamera cam;
	private float[] varPitch = { 0.8f, 1f, 1.2f };
	
	
	@Override
	public void show() {
		try {
			sr = new ShapeRenderer();
			cam = new OrthographicCamera();
			menu = new Imagen(Recursos.FONDO_MENU);
			menu.setTamano(Config.ANCHOP, Config.ALTOP);

			for (int i = 0; i < opciones.length; i++) {
				opciones[i] = new Texto(Recursos.FUENTE_MENU, 45);
				opciones[i].setColor(Color.GREEN);
				opciones[i].setTexto(texto[i]);
				opciones[i].setPosicion(0.5f * Config.anchoVP - 0.5f * opciones[i].getAncho(),
						(0.55f * Config.altoVP - 0.5f * opciones[i].getAlto()) - avance * i);
			}

			for (int i = 0; i < bbOpc.length; i++) {
				bbOpc[i] = new Rectangle(opciones[i].getX(), opciones[i].getY() - opciones[i].getAlto(),
						opciones[i].getAncho(), opciones[i].getAlto());
			}
			bbMouse = new Rectangle(0, 0, 5, 5);

			manager = new AssetManager();
			manager.load(Recursos.MUSICA_MENU, Music.class);
			manager.load(Recursos.ELEGIR_OPC_MENU, Sound.class);
			manager.load(Recursos.MOVER_OPC_MENU, Sound.class);
			manager.finishLoading();

			if (Recursos.volMusica) {
				manager.get(Recursos.MUSICA_MENU, Music.class).setLooping(true);
				manager.get(Recursos.MUSICA_MENU, Music.class).play();
			}

			seleccionar = manager.get(Recursos.ELEGIR_OPC_MENU, Sound.class);
			mover = manager.get(Recursos.MOVER_OPC_MENU, Sound.class);
			moverID = mover.play(0);

			cam = new OrthographicCamera(Config.anchoVP, Config.altoVP);
			cam.position.set(Config.anchoVP / 2, Config.altoVP / 2, 0);
			cam.update();
			sr.setProjectionMatrix(cam.combined);

			Gdx.input.setInputProcessor(entradas);
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}

	@Override
	public void render(float delta) {
		try {
			Render.limpiarPantalla(0, 0, 0);
			Render.app.cambiarCursor(false);

			Vector3 mouseCoordinates = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
			cam.unproject(mouseCoordinates);
			float mouseX = mouseCoordinates.x;
			float mouseY = mouseCoordinates.y;

			actualizarBbMouse(mouseX, mouseY);

			Render.batch.begin();
			menu.dibujar();
			for (int i = 0; i < opciones.length; i++) {
				opciones[i].dibujar();
			}
			Render.batch.end();

//		sr.begin(ShapeType.Line);
//		sr.setColor(Color.RED);
//		for (int i = 0; i < bbOpc.length; i++) {
//			sr.rect(bbOpc[i].getX(), bbOpc[i].getY(), bbOpc[i].getWidth(), bbOpc[i].getHeight());
//		}
//		sr.end();

			for (int i = 0; i < opciones.length; i++) {
				if (bbOpc[i].overlaps(bbMouse)) {
					opciones[i].setColor(Color.FIREBRICK);
					opc = i + 1;
					if (!sonidoEnReproduccion && Recursos.volSonidos) {
						sonidoEnReproduccion = true;
						mover.setPitch(moverID, varPitch[Recursos.r.nextInt(3)]);
						mover.play();
					}
				} else {
					if (!bbOpc[opc - 1].overlaps(bbMouse)) {
						sonidoEnReproduccion = false;
					}
					opciones[i].setColor(Color.GREEN);
				}

			}

			switch (opc) {
			case 1:
				if ((entradas.isEnter() || entradas.isClick()) && bbOpc[opc - 1].overlaps(bbMouse)) {
					Render.app.setScreen(new PantallaUnJugador());
					if (Recursos.volSonidos) {
						seleccionar.play();
					}
					manager.get(Recursos.MUSICA_MENU, Music.class).stop();
				}
				break;
			case 2:
				if ((entradas.isEnter() || entradas.isClick()) && bbOpc[opc - 1].overlaps(bbMouse)) {
					Render.app.setScreen(new PantallaMultijugador());
					if (Recursos.volSonidos) {
						seleccionar.play();
					}
					manager.get(Recursos.MUSICA_MENU, Music.class).stop();
				}
				break;
			case 3:
				if ((entradas.isEnter() || entradas.isClick()) && bbOpc[opc - 1].overlaps(bbMouse)) {
					Render.app.setScreen(new PantallaOpciones());
					if (Recursos.volSonidos) {
						seleccionar.play();
					}
					manager.get(Recursos.MUSICA_MENU, Music.class).stop();

				}
				break;
			case 4:
				if ((entradas.isEnter() || entradas.isClick()) && bbOpc[opc - 1].overlaps(bbMouse)) {
					seleccionar.play();

					Gdx.app.exit();
				}
				break;
			default:
				break;
			}

		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}

	private void actualizarBbMouse(float mouseX, float mouseY) {
		bbMouse.x = mouseX - 2;
		bbMouse.y = mouseY;
	}

	@Override
	public void dispose() {
		Render.batch.dispose();
		manager.get(Recursos.MUSICA_MENU, Music.class).dispose();
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

}
