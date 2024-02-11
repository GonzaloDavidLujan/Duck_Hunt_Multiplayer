package com.dojan.mijuego;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.dojan.mijuego.pantallas.PantallaMultijugador;
import com.dojan.mijuego.utiles.Render;

public class Mijuego extends Game {

	@Override
	public void create() {
		Render.app = this;
		Render.batch = new SpriteBatch();
		setScreen(new PantallaMultijugador());
	}

	@Override
	public void render() {
		super.render();
	}

	@Override
	public void dispose() {
		screen.dispose();
	}

}
