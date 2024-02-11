package com.dojan.mijuego.elementos;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.dojan.mijuego.utiles.Render;

public class Texto {

	private BitmapFont fuente;
	private float x = 0, y = 0;
	private String texto = "";
	private GlyphLayout layout;
	FreeTypeFontParameter parametros;
	FreeTypeFontGenerator generador;

	
	public Texto(String ruta, int tamanoF) {
		generador = new FreeTypeFontGenerator(Gdx.files.internal(ruta));
		parametros = new FreeTypeFontParameter();

		parametros.size = tamanoF;

		fuente = generador.generateFont(parametros);
		layout = new GlyphLayout();
	}

	public void dibujar() {
		fuente.draw(Render.batch, texto, x, y);
	}

	public void setColor(Color color) {
		fuente.setColor(color);
	}

	public void setPosicion(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public void setTexto(String texto) {
		this.texto = texto;
		layout.setText(fuente, texto);
	}

	public float getAncho() {
		return layout.width;
	}

	public float getAlto() {
		return layout.height;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}
}
