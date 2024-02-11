package com.dojan.mijuego.elementos.perro; 

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.dojan.mijuego.utiles.Render;

public abstract class Perro {
	private int cantFrames;
	private Animation<TextureRegion> animacion;
	private float tiempo;
	private TextureRegion[] regionesPerro;
	private Texture textura;
	private TextureRegion frameActual;
	private int x, y;
	private AssetManager manager;
	private Sound sonido;
	
	public Perro(String rutaTextura, String rutaSonido, int cantFrames, float tiempoCiclo) {
		this.cantFrames = cantFrames;
		manager = new AssetManager();
		manager.load(rutaSonido, Sound.class);
		manager.finishLoading();
		
		sonido = manager.get(rutaSonido, Sound.class);
		
		textura = new Texture(rutaTextura);
		TextureRegion[][] temp = TextureRegion.split(textura, textura.getWidth() / this.cantFrames, textura.getHeight());

		regionesPerro = new TextureRegion[cantFrames];
		for (int i = 0; i < regionesPerro.length; i++) {
			regionesPerro[i] = temp[0][i];
		}

		animacion = new Animation<TextureRegion>(tiempoCiclo, regionesPerro);
		tiempo = 0;
	}

	public void setPosicion(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public Sound getSonido() {
		return sonido;
	}
	
	public void dibujar() {
		tiempo += Gdx.graphics.getDeltaTime();

		frameActual = animacion.getKeyFrame(tiempo, true);
		Render.batch.draw(frameActual, x, y);
	}

	public long hacerSonido() {
		return sonido.play();
	}
	
	public int getAncho() {
		return textura.getWidth()/cantFrames;
	}
	public int getAlto() {
		return textura.getHeight();
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
	
	
	
}
