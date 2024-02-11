package com.dojan.mijuego;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.dojan.mijuego.pantallas.PantallaCarga;
import com.dojan.mijuego.utiles.Recursos;
import com.dojan.mijuego.utiles.Render;

public class Mijuego extends Game {

	@Override
	public void create() {
		Render.app = this;
		Render.batch = new SpriteBatch();
		setScreen(new PantallaCarga());
	}

	@Override
	public void render() {
		super.render();
	}

	@Override
	public void dispose() {
		screen.dispose();
	}

	public void cambiarCursor(boolean cambiar) {
		if (cambiar) {
			Pixmap pixmap = new Pixmap(Gdx.files.internal(Recursos.CURSOR));
			int xHotspot = 15, yHotspot = 15;
			Cursor cursor = Gdx.graphics.newCursor(pixmap, xHotspot, yHotspot);
			pixmap.dispose();
			Gdx.graphics.setCursor(cursor);
		}else {
			Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);
		}

	}
}
