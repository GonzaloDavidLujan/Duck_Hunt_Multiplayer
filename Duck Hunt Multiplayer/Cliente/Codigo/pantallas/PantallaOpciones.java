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
import com.dojan.mijuego.elementos.Texto;
import com.dojan.mijuego.io.Entradas;
import com.dojan.mijuego.utiles.Config;
import com.dojan.mijuego.utiles.Recursos;
import com.dojan.mijuego.utiles.Render;

public class PantallaOpciones implements Screen {
	private Entradas entradas = new Entradas();
	private AssetManager manager = new AssetManager();
	private String[] texto = { "Musica", "Sonidos", "Volver" };
	private Texto[] opciones = new Texto[3];
	private int avance = 50;
	private Rectangle bbMusica, bbSonidos, bbMouse, bbVolver;
	private OrthographicCamera cam;
	private ShapeRenderer sr;
	private boolean click = false;
	private Music musica;
	private Sound seleccionar;

	@Override
	public void show() {
		try {
			manager.load(Recursos.ELEGIR_OPC_MENU, Sound.class);
			manager.load(Recursos.MUSICA_MENU, Music.class);
			manager.finishLoading();

			seleccionar = manager.get(Recursos.ELEGIR_OPC_MENU, Sound.class);
			musica = manager.get(Recursos.MUSICA_MENU, Music.class);

			sr = new ShapeRenderer();
			for (int i = 0; i < opciones.length; i++) {
				opciones[i] = new Texto(Recursos.FUENTE_MENU, 45);
				opciones[i].setColor(Color.GREEN);
				opciones[i].setTexto(texto[i]);
				opciones[i].setPosicion(500, 450 - avance * i);
			}
			opciones[2].setPosicion(10, Config.ALTOP - opciones[2].getAlto());
			
			bbMusica = new Rectangle(opciones[0].getX() + opciones[1].getAncho() + 10,
					opciones[0].getY() - opciones[0].getAlto(), opciones[0].getAlto(), opciones[0].getAlto());
			bbSonidos = new Rectangle(opciones[1].getX() + opciones[1].getAncho() + 10,
					opciones[1].getY() - opciones[1].getAlto(), opciones[0].getAlto(), opciones[1].getAlto());

			bbMouse = new Rectangle(0, 0, 5, 5);

			bbVolver = new Rectangle(opciones[2].getX(), opciones[2].getY() - opciones[2].getAlto(),
					opciones[2].getAncho(), opciones[2].getAlto());

			cam = new OrthographicCamera(Config.anchoVP, Config.altoVP);
			cam.position.set(Config.anchoVP / 2, Config.altoVP / 2, 0);
			cam.update(); // Actualiza la cámara

			if (Recursos.volMusica) {
				musica.setLooping(true);
				musica.play();
			}

			sr.setProjectionMatrix(cam.combined);
			Gdx.input.setInputProcessor(entradas);
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}

	@Override
	public void render(float delta) {
		Render.limpiarPantalla(0, 0, 0);

		Vector3 mouseCoordinates = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
		cam.unproject(mouseCoordinates);
		float mouseX = mouseCoordinates.x;
		float mouseY = mouseCoordinates.y;

		actualizarBbMouse(mouseX, mouseY);

		sr.begin(ShapeRenderer.ShapeType.Filled);

		if (Recursos.volMusica) {
			sr.setColor(Color.RED);
		} else {
			sr.setColor(Color.WHITE);
		}
		sr.rect(bbMusica.x, bbMusica.y, bbMusica.width, bbMusica.height);

		if (Recursos.volSonidos) {
			sr.setColor(Color.RED);
		} else {
			sr.setColor(Color.WHITE);
		}

		sr.rect(bbSonidos.x, bbSonidos.y, bbSonidos.width, bbSonidos.height);

		sr.end();

//		sr.begin(ShapeRenderer.ShapeType.Filled);
//
//		sr.setColor(Color.GREEN);
//		sr.rect(bbMouse.x, bbMouse.y, bbMouse.width, bbMouse.height);
//
//		sr.end();


		
		if (entradas.isClick() && Recursos.volMusica && bbMusica.overlaps(bbMouse) && !click) {
			if (Recursos.volSonidos) {
				seleccionar.play();
			}
			Recursos.volMusica = false;
			musica.setLooping(false);
			musica.stop();
			click = true;
		} else if (!entradas.isClick()) {
			click = false;
		}
		if (entradas.isClick() && !Recursos.volMusica && bbMusica.overlaps(bbMouse) && !click) {
			if (Recursos.volSonidos) {
				seleccionar.play();
			}
			musica.setLooping(true);
			musica.play();
			Recursos.volMusica = true;
			click = true;
		} else if (!entradas.isClick()) {
			click = false;
		}
		if (entradas.isClick() && Recursos.volSonidos && bbSonidos.overlaps(bbMouse) && !click) {
			seleccionar.play();
			Recursos.volSonidos = false;
			click = true;
		} else if (!entradas.isClick()) {
			click = false;
		}
		if (entradas.isClick() && !Recursos.volSonidos && bbSonidos.overlaps(bbMouse) && !click) {
			seleccionar.play();
			Recursos.volSonidos = true;
			click = true;
		} else if (!entradas.isClick()) {
			click = false;
		}

		Render.batch.begin();
		for (int i = 0; i < opciones.length; i++) {
			opciones[i].dibujar();
		}
		Render.batch.end();

		if (bbVolver.overlaps(bbMouse)) {
			opciones[2].setColor(Color.FIREBRICK);
		}else {
			opciones[2].setColor(Color.GREEN);
		}
		
		if (entradas.isEsc() || (entradas.isClick() && bbVolver.overlaps(bbMouse))) {
			if (Recursos.volMusica)	musica.stop();
			Render.app.setScreen(new PantallaMenu());
		}

	}

	@Override
	public void dispose() {
		Render.batch.dispose();
		musica.dispose();
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

	private void actualizarBbMouse(float mouseX, float mouseY) {
		bbMouse.x = mouseX - 2;
		bbMouse.y = mouseY;
	}

}
