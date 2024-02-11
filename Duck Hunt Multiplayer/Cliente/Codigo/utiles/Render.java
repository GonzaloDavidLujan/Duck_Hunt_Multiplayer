package com.dojan.mijuego.utiles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.dojan.mijuego.Mijuego;

public class Render {

	public static SpriteBatch batch;
	public static Mijuego app;
	private static float time = 0;

	public static void limpiarPantalla(float r, float g, float b) {

		Gdx.gl.glClearColor(r, g, b, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	}

	public static void limpiarPantallaArcoiris() {
		time += Gdx.graphics.getDeltaTime();

		// Fórmulas para obtener componentes de color que cambian con el tiempo
		float r = (float) (Math.sin(0.5f * time + 0) * 0.5f + 0.5f);
		float g = (float) (Math.sin(0.5f * time + 2) * 0.5f + 0.5f);
		float b = (float) (Math.sin(0.5f * time + 4) * 0.5f + 0.5f);
		Gdx.gl.glClearColor(r, g, b, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	}
}
