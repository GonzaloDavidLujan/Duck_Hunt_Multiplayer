package com.dojan.mijuego.elementos;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.dojan.mijuego.utiles.Render;

public class Imagen {
	private Texture t;
	private Sprite s;
	private float x, y;

	public Imagen(String ruta) {
		t = new Texture(ruta);
		s = new Sprite(t);
	}
	public void setImagen(String ruta) {
		t = new Texture(ruta);
		s = new Sprite(t);
	}
	public float getAncho() {
		return s.getWidth();
	}
	
	public float getAlto() {
		return s.getHeight();
	}
	public Sprite getS() {
		return s;
	}

	public void dibujar() {
		s.draw(Render.batch);
	}

	public void setPosicion(float x, float y) {
		this.x = x;
		this.y = y;
		s.setPosition(x, y);
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public void setTransparencia(float alpha) {
		s.setAlpha(alpha);
	}

	public void setTamano(float ancho, float alto) {
		s.setSize(ancho, alto);
	}
}
