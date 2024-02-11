package com.dojan.mijuego.elementos.patos;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.dojan.mijuego.red.HiloServidor;
import com.dojan.mijuego.utiles.Recursos;

public abstract class Pato {
	private int puntos, cantFrames;
	private Animation<TextureRegion> animacion;
	private float tiempo, x, y;
	private TextureRegion[] regionesPato = new TextureRegion[4];
	private Texture t;
	private TextureRegion frameActual, frameDetenido;
	private Rectangle bb;
	private boolean sonidoEnReproduccion = false, vivo = true, finalizado = false;
	private int frecuencia = 0;
	private Direccion dir;
	private float multVel;

	public Pato(String ruta, int puntos, int cantFrames, float multVel) {
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

	public void enviarSonidosPato(HiloServidor hs) {
		if (!sonidoEnReproduccion && frecuencia % 15 == 0 && this.vivo) {
			sonidoEnReproduccion = true;
			hs.enviarMensajeATodos("PlayAletear#");
			if (frecuencia % 40 == 0) {
				hs.enviarMensajeATodos("PlayGraznar#");
			}
		}

		// Reiniciar sonidoEnReproduccion si la condición ya no se cumple
		if (sonidoEnReproduccion && !(frecuencia % 15 == 0 && this.vivo)) {
			sonidoEnReproduccion = false;
		}

		frecuencia++;
	}

	public void actualizarBb() {
		this.bb.height = this.getAlto();
		this.bb.width = this.getAncho();
		this.bb.x = this.x;
		this.bb.y = this.y;
	}

	public void morir(HiloServidor hs) {
		if (Recursos.cantPatos.get(0).vivo) {
			Recursos.cantPatos.get(0).vivo = false;
			hs.enviarMensajeATodos("PlayCaida#");
		}

	}

	public void verificarPegarselaContraElPiso(HiloServidor hs) {
		if (!Recursos.cantPatos.get(0).vivo && this.y <= 5 && !Recursos.cantPatos.get(0).finalizado) {
			Recursos.cantPatos.get(0).finalizado = true;
			hs.enviarMensajeATodos("StopCaida#");
			hs.enviarMensajeATodos("MatarPato#");
			Recursos.cantPatos.remove(0);

			if (Recursos.cantPatos.isEmpty()) {
				Recursos.juegoTerminado = true;
				Recursos.juegoEmpezado = false;
				hs.enviarMensajeATodos("TerminarJuego#");
			}
		}
	}

	public Direccion getDir() {
		return dir;
	}

	public float getMultVel() {
		return multVel;
	}
}
