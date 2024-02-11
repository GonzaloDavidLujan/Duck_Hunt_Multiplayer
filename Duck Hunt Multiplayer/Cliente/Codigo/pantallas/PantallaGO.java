package com.dojan.mijuego.pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.dojan.mijuego.elementos.Texto;
import com.dojan.mijuego.elementos.perro.Perro;
import com.dojan.mijuego.elementos.perro.PerroRisa;
import com.dojan.mijuego.io.Entradas;
import com.dojan.mijuego.utiles.Config;
import com.dojan.mijuego.utiles.Recursos;
import com.dojan.mijuego.utiles.Render;

public class PantallaGO implements Screen {

	private Texto[] texto = new Texto[4];
	private Entradas entradas = new Entradas();
	private String[] txt = { "Perdiste!", "hiciste: " + Recursos.puntos + " puntos", "Volver a Jugar",
			"Volver al Menu" };
	private Perro perroRisa;
	private long risaID;
	private Rectangle bbMenu, bbMouse, bbVolver;
	private OrthographicCamera cam;
	private ShapeRenderer sr;

	@Override
	public void show() {
		sr = new ShapeRenderer();

		Render.app.cambiarCursor(false);
		perroRisa = new PerroRisa();
		perroRisa.setPosicion((Config.anchoVP - perroRisa.getAncho()) / 2, 350);
		try {
///////////////////////////////////////////////////////////////////////

			texto[0] = new Texto(Recursos.FUENTE_MENU, 70);
			texto[0].setTexto(txt[0]);
			texto[0].setColor(Color.FOREST);
			texto[0].setPosicion((Config.anchoVP - texto[0].getAncho()) / 2, (int) (0.975f * Config.altoVP));

///////////////////////////////////////////////////////////////////////

			texto[1] = new Texto(Recursos.FUENTE_MENU, 70);
			texto[1].setTexto(txt[1]);
			texto[1].setColor(Color.FOREST);
			texto[1].setPosicion((Config.anchoVP - texto[1].getAncho()) / 2, (int) (0.9f * Config.altoVP));

///////////////////////////////////////////////////////////////////////

			texto[2] = new Texto(Recursos.FUENTE_MENU, 70);
			texto[2].setTexto(txt[2]);
			texto[2].setColor(Color.GREEN);
			texto[2].setPosicion((Config.anchoVP - texto[2].getAncho()) / 2, (int) (0.45f * Config.altoVP));

///////////////////////////////////////////////////////////////////////

			texto[3] = new Texto(Recursos.FUENTE_MENU, 70);
			texto[3].setTexto(txt[3]);
			texto[3].setColor(Color.GREEN);
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
				risaID = perroRisa.hacerSonido();
				perroRisa.getSonido().setLooping(risaID, true);

			}
		} catch (Exception e) {
			System.out.println(e.toString());
		}

	}

	@Override
	public void render(float delta) {
		try {

			Render.limpiarPantalla(0, 0, 0);

			Vector3 mouseCoordinates = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
			cam.unproject(mouseCoordinates);
			float mouseX = mouseCoordinates.x;
			float mouseY = mouseCoordinates.y;

			actualizarBbMouse(mouseX, mouseY);

			Render.batch.begin();
			for (int i = 0; i < texto.length; i++) {
				texto[i].dibujar();
			}
			perroRisa.dibujar();
			Render.batch.end();

			if (bbMenu.overlaps(bbMouse)) {
				texto[3].setColor(Color.FIREBRICK);
			} else {
				texto[3].setColor(Color.GREEN);
				if (bbVolver.overlaps(bbMouse)) {
					texto[2].setColor(Color.FIREBRICK);
				} else {
					texto[2].setColor(Color.GREEN);
				}
			}

			if (entradas.isEsc() || (entradas.isClick() && bbMenu.overlaps(bbMouse))) {
				perroRisa.getSonido().stop();
				Recursos.puntos = 0;
				Render.app.setScreen(new PantallaMenu());
			} else if (entradas.isClick() && bbVolver.overlaps(bbMouse)) {
				Recursos.puntos = 0;
				perroRisa.getSonido().stop();
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

	}

}
