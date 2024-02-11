package com.dojan.mijuego.elementos.patos;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.dojan.mijuego.utiles.Recursos;
import com.dojan.mijuego.utiles.Render;

public abstract class Pato {
	private int  puntos, cantFrames;
	private Animation<TextureRegion> animacion;
	private float tiempo,x, y;
	private TextureRegion[] regionesPato = new TextureRegion[4];
	private Texture t;
	private TextureRegion frameActual, frameDetenido;
	private AssetManager manager;
	private Sound cuac, aletear, caida, golpe;
	private Rectangle bb;
	private boolean sonidoEnReproduccion = false, vivo = true, finalizado = false;
	private int frecuencia = 0;
	public Direccion dir;
	private float multVel;

	public Pato(String ruta, int puntos, int cantFrames, float multVel) {
		manager = new AssetManager();
		manager.load(Recursos.CUAC, Sound.class);
		manager.load(Recursos.VOLAR, Sound.class);
		manager.load(Recursos.CAIDA, Sound.class);
		manager.load(Recursos.GOLPE, Sound.class);
		manager.finishLoading();

		cuac = manager.get(Recursos.CUAC, Sound.class);
		aletear = manager.get(Recursos.VOLAR, Sound.class);
		caida = manager.get(Recursos.CAIDA, Sound.class);
		golpe = manager.get(Recursos.GOLPE, Sound.class);

		this.cantFrames = cantFrames;
		t = new Texture(ruta);
		TextureRegion[][] temp = TextureRegion.split(t, t.getWidth() / this.cantFrames, t.getHeight());
		frameDetenido = temp[0][0];
		for (int i = 0; i < regionesPato.length; i++) {
			regionesPato[i] = temp[0][i];
		}
		animacion = new Animation<>(0.05f, regionesPato);
		tiempo = 0;
		this.puntos = puntos;

		this.bb = new Rectangle(this.x, this.y, this.getAncho(), this.getAlto());
		this.multVel = multVel;
	}

	public void actualizarDir() {

		for (int i = 0; i < Recursos.cantPatos.size() - 1; i++) {
			if (Recursos.cantPatos.get(i).isFlipX()) {
				Recursos.cantPatos.get(i).dir = Direccion.IZQUIERDA;
			} else {
				Recursos.cantPatos.get(i).dir = Direccion.DERECHA;
			}
		}
	}

	public Rectangle getBb() {
		return bb;
	}

	public boolean isFlipX() {
		boolean flip = false;
		for (int i = 0; i < regionesPato.length; i++) {
			if (regionesPato[i].isFlipX()) {
				flip = true;
			}
		}
		return flip;
	}

	public void setFlipX() {
		for (int i = 0; i < regionesPato.length; i++) {
			this.regionesPato[i].flip(true, false);
		}

	}

	public TextureRegion getFrameActual() {
		return frameActual;
	}

	public int getPuntos() {
		return puntos;
	}

	public Sound getAletear() {
		return aletear;
	}

	public boolean isVivo() {
		return vivo;
	}

	public void setPosicion(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public int getAncho() {
		return t.getWidth() / this.cantFrames;
	}

	public int getAlto() {
		return t.getHeight();
	}

	public boolean isFinalizado() {
		return finalizado;
	}

	public void dibujar() {
		if (!sonidoEnReproduccion && frecuencia % 15 == 0 && this.vivo && !Recursos.multiplayer) {
			sonidoEnReproduccion = true;
			aletear();
			if (frecuencia % 40 == 0) {
				graznar();
			}
		} else {
			sonidoEnReproduccion = false;
		}
		frecuencia++;
		tiempo += Gdx.graphics.getDeltaTime();
		frameActual = animacion.getKeyFrame(tiempo, true);
		if (this.vivo) {
			Render.batch.draw(frameActual, x, y);
		} else {
			Render.batch.draw(frameDetenido, x, y);
		}
	}

	public void graznar() {
		if (Recursos.volSonidos) {
			cuac.play();
		}
	}

	public void aletear() {
		if (Recursos.volSonidos) {
			aletear.play();
		}
	}

	public void actualizarBb(float x, float y) {
		this.bb.height = this.getAlto();
		this.bb.width = this.getAncho();
		this.bb.x = x;
		this.bb.y = y;
	}

	public void morir() {
		if (this.vivo) {
			this.vivo = false;
			if (Recursos.volSonidos && !Recursos.multiplayer) {
				caida.play();
			}

		}

	}

	public void verificarPegarselaContraElPiso() {
		if (!this.vivo && this.y <= 5 && !this.finalizado && !Recursos.multiplayer) {
			this.finalizado = true;
			caida.stop();
			if (Recursos.volSonidos) {
				golpe.play();
			}
			Recursos.cantPatos.remove(0);
		}
	}

	public Direccion getDir() {
		return dir;
	}

	public float getMultVel() {
		return multVel;
	}
	
}
