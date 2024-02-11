package com.dojan.mijuego.pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.dojan.mijuego.elementos.Texto;
import com.dojan.mijuego.elementos.perro.Perro;
import com.dojan.mijuego.elementos.perro.PerroPatos;
import com.dojan.mijuego.io.Entradas;
import com.dojan.mijuego.utiles.Config;
import com.dojan.mijuego.utiles.Recursos;
import com.dojan.mijuego.utiles.Render;

public class PantallaVictoria implements Screen {

	private Texto[] texto = new Texto[4];
	private Entradas entradas = new Entradas();
	private String[] txt = { "Ganaste!", "hiciste: " + Recursos.puntos + " puntos", "Volver a Jugar",
			"Volver al Menu" };
	private Perro PerroPatos;
	private long sonidoID;
	private Rectangle bbMenu, bbMouse, bbVolver;
	private AssetManager manager;
	private OrthographicCamera cam;
	private ShapeRenderer sr;

	@Override
	public void show() {
		sr = new ShapeRenderer();

		manager = new AssetManager();
		manager.load(Recursos.MUSICA_VICTORIA, Music.class);
		manager.finishLoading();

		Render.app.cambiarCursor(false);
		PerroPatos = new PerroPatos();
		PerroPatos.setPosicion((Config.anchoVP - PerroPatos.getAncho()) / 2, 350);
		try {
///////////////////////////////////////////////////////////////////////

			texto[0] = new Texto(Recursos.FUENTE_MENU, 70);
			texto[0].setTexto(txt[0]);
			texto[0].setColor(Color.WHITE);
			texto[0].setPosicion((Config.anchoVP - texto[0].getAncho()) / 2, (int) (0.975f * Config.altoVP));

///////////////////////////////////////////////////////////////////////

			texto[1] = new Texto(Recursos.FUENTE_MENU, 70);
			texto[1].setTexto(txt[1]);
			texto[1].setColor(Color.WHITE);
			texto[1].setPosicion((Config.anchoVP - texto[1].getAncho()) / 2, (int) (0.9f * Config.altoVP));

///////////////////////////////////////////////////////////////////////

			texto[2] = new Texto(Recursos.FUENTE_MENU, 70);
			texto[2].setTexto(txt[2]);
			texto[2].setColor(Color.WHITE);
			texto[2].setPosicion((Config.anchoVP - texto[2].getAncho()) / 2, (int) (0.45f * Config.altoVP));

///////////////////////////////////////////////////////////////////////

			texto[3] = new Texto(Recursos.FUENTE_MENU, 70);
			texto[3].setTexto(txt[3]);
			texto[3].setColor(Color.WHITE);
			texto[3].setPosicion((Config.anchoVP - texto[3].getAncho()) / 2, (int) (0.35f * Config.altoVP));

///////////////////////////////////////////////////////////////////////
			bbMouse = new Rectangle(0, 0, 5, 5);

			bbVolver = new Rectangle(texto[2].getX(), texto[2].getY() - texto[2].getAlto(), texto[2].getAncho(),
					texto[2].getAlto());

			bbMenu = new Rectangle(texto[3].getX(), texto[3].getY() - texto[3].getAlto(), texto[3].getAncho(),
					texto[3].getAlto());

			cam = new OrthographicCamera(Config.anchoVP, Config.altoVP);
			cam.position.set(Config.anchoVP / 2, Config.altoVP / 2, 0);
			cam.update(); // Actualiza la cámara

			sr.setProjectionMatrix(cam.combined);
			Gdx.input.setInputProcessor(entradas);
			if (Recursos.volSonidos) {
				sonidoID = PerroPatos.hacerSonido();
				PerroPatos.getSonido().setLooping(sonidoID, true);
			}
			if (Recursos.volMusica) {
				manager.get(Recursos.MUSICA_VICTORIA, Music.class).setLooping(true);
				manager.get(Recursos.MUSICA_VICTORIA, Music.class).play();
			}
		} catch (Exception e) {
			System.out.println(e.toString());
		}

	}

	@Override
	public void render(float delta) {
		try {

			Render.limpiarPantallaArcoiris();

			Vector3 mouseCoordinates = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
			cam.unproject(mouseCoordinates);
			float mouseX = mouseCoordinates.x;
			float mouseY = mouseCoordinates.y;

			actualizarBbMouse(mouseX, mouseY);

			Render.batch.begin();
			for (int i = 0; i < texto.length; i++) {
				texto[i].dibujar();
			}
			PerroPatos.dibujar();
			Render.batch.end();

			if (bbVolver.overlaps(bbMouse)) {
				texto[2].setColor(Color.BLACK);
			} else {
				texto[2].setColor(Color.WHITE);
				if (bbMenu.overlaps(bbMouse)) {
					texto[3].setColor(Color.BLACK);
				} else {
					texto[3].setColor(Color.WHITE);
				}
			}

			if (entradas.isEsc() || (entradas.isClick() && bbMenu.overlaps(bbMouse))) {
				Recursos.puntos = 0;
				manager.get(Recursos.MUSICA_VICTORIA, Music.class).stop();
				Render.app.setScreen(new PantallaMenu());
			} else if (entradas.isClick() && bbVolver.overlaps(bbMouse)) {
				Recursos.puntos = 0;
				manager.get(Recursos.MUSICA_VICTORIA, Music.class).stop();
				Render.app.setScreen(new PantallaUnJugador());
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
		Render.batch.dispose();
		manager.get(Recursos.MUSICA_VICTORIA, Music.class).dispose();
	}

}
