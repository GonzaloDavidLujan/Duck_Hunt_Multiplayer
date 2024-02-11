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
import com.dojan.mijuego.elementos.Imagen;
import com.dojan.mijuego.elementos.Texto;
import com.dojan.mijuego.elementos.patos.Direccion;
import com.dojan.mijuego.elementos.patos.PatoAzul;
import com.dojan.mijuego.elementos.patos.PatoDorado;
import com.dojan.mijuego.elementos.patos.PatoRojo;
import com.dojan.mijuego.elementos.patos.PatoVerde;
import com.dojan.mijuego.io.Entradas;
import com.dojan.mijuego.utiles.Config;
import com.dojan.mijuego.utiles.Recursos;
import com.dojan.mijuego.utiles.Render;

public class PantallaUnJugador implements Screen {

	private AssetManager manager;
	private Imagen fondo, pasto;
	private boolean maxAlto, minAlto = true, maxAncho, minAncho = true;
	private Entradas entradas = new Entradas();
	private OrthographicCamera cam;
	private Sound disparo, scoreUP;
	private Music musica;
	private boolean sonidoEnReproduccion = false;
	private Rectangle bbMouse;
	private int nroDisparos;
	private Imagen[] balas;
	private ShapeRenderer sr;
	private Texto[] hud = new Texto[2];
	private String[] txt = { "SCORE", "BALAS:" };

	@Override
	public void show() {
		try {
			nroDisparos = Recursos.cantInicialPatos + 3;
			balas = new Imagen[nroDisparos];
			for (int i = 0; i < Recursos.cantInicialPatos; i++) {
				int patoRandom = Recursos.r.nextInt(100) + 1;
				if (patoRandom <= 50) {
					Recursos.cantPatos.add(new PatoVerde());
				} else if (patoRandom <= 80) {
					Recursos.cantPatos.add(new PatoAzul());
				} else if (patoRandom <= 95) {
					Recursos.cantPatos.add(new PatoRojo());
				} else {
					Recursos.cantPatos.add(new PatoDorado());
				}
			}

			Render.app.cambiarCursor(true);
			bbMouse = new Rectangle(0, 0, 32, 32);
			manager = new AssetManager();
			manager.load(Recursos.CANCION, Music.class);
			manager.load(Recursos.DISPARO, Sound.class);
			manager.load(Recursos.SCOREUP, Sound.class);
			manager.finishLoading();

			for (int i = 0; i < hud.length; i++) {
				hud[i] = new Texto(Recursos.FUENTE_MENU, 30);
				hud[i].setColor(Color.YELLOW);
				hud[i].setPosicion(10, ((i == 0) ? 0 : hud[i - 1].getY()) + hud[i].getAlto() + 30);
				hud[i].setTexto(txt[i]);
			}

			for (int i = 0; i < balas.length; i++) {
				balas[i] = new Imagen(Recursos.BALA);
				balas[i].setTamano(20, 20);
				balas[i].setPosicion(hud[1].getX() + hud[1].getAncho() + ((i == 0) ? 0 : balas[i - 1].getAncho() * i),
						hud[1].getY() - hud[1].getAlto());
			}

			musica = manager.get(Recursos.CANCION, Music.class);
			disparo = manager.get(Recursos.DISPARO, Sound.class);
			scoreUP = manager.get(Recursos.SCOREUP, Sound.class);

			sr = new ShapeRenderer();

			fondo = new Imagen(Recursos.FONDO_JUEGO);
			pasto = new Imagen(Recursos.PASTO);
			fondo.setTamano(Config.anchoVP, Config.altoVP);
			pasto.setTamano(Config.anchoVP, Config.altoVP);

			cam = new OrthographicCamera(Config.anchoVP, Config.altoVP);
			cam.position.set(Config.anchoVP / 2, Config.altoVP / 2, 0);
			cam.update(); // Actualiza la cámara

			if (Recursos.volMusica) {
				 musica.setLooping(true);
				 musica.play();
			}

			// cam.zoom = 2f;
			sr.setProjectionMatrix(cam.combined);

			Gdx.input.setInputProcessor(entradas);
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}

	@Override
	public void render(float delta) {
		try {
			Recursos.cantPatos.get(0).actualizarDir();
			if (Recursos.cantPatos.get(0).getY() > Config.altoVP
					- Recursos.cantPatos.get(0).getAlto()) {
				maxAlto = true;
				minAlto = false;
			}
			if (Recursos.cantPatos.get(0).getY() < 0) {
				maxAlto = false;
				minAlto = true;
			}
			if (Recursos.cantPatos.get(0).getX() > Config.anchoVP
					- Recursos.cantPatos.get(0).getAncho()) {
				maxAncho = true;
				Recursos.derecha = false;
				Recursos.izquierda = true;
				minAncho = false;

				for (int i = 0; i < Recursos.cantPatos.size(); i++) {
					if (Recursos.cantPatos.get(i).dir != null) {
						if (Recursos.cantPatos.get(i).dir == Direccion.DERECHA
								&& !Recursos.cantPatos.get(i).isFlipX()) {
							Recursos.cantPatos.get(i).actualizarDir();
							Recursos.cantPatos.get(i).setFlipX();
						} else if (Recursos.cantPatos.get(i).dir == Direccion.IZQUIERDA
								&& Recursos.cantPatos.get(i).isFlipX()) {
							Recursos.cantPatos.get(i).actualizarDir();
							Recursos.cantPatos.get(i).setFlipX();
						}
					} else {
						Recursos.cantPatos.get(i).actualizarDir();
						Recursos.cantPatos.get(i).setFlipX();
					}
				}
			}
			if (Recursos.cantPatos.get(0).getX() < 0) {
				maxAncho = false;
				Recursos.derecha = true;
				Recursos.izquierda = false;
				minAncho = true;
				for (int i = 0; i < Recursos.cantPatos.size(); i++) {
					if (Recursos.cantPatos.get(i).dir != null) {
						if (Recursos.cantPatos.get(i).dir == Direccion.DERECHA
								&& !Recursos.cantPatos.get(i).isFlipX()) {
							Recursos.cantPatos.get(i).actualizarDir();
							Recursos.cantPatos.get(i).setFlipX();
						} else if (Recursos.cantPatos.get(i).dir == Direccion.IZQUIERDA
								&& Recursos.cantPatos.get(i).isFlipX()) {
							Recursos.cantPatos.get(i).actualizarDir();
							Recursos.cantPatos.get(i).setFlipX();
						}
					} else {
						Recursos.cantPatos.get(i).actualizarDir();
						Recursos.cantPatos.get(i).setFlipX();
					}
				}
			}
			if (Recursos.cantPatos.get(0).isVivo()) {
				if (maxAlto) {
					Recursos.contY = -5;
				} else if (minAlto) {
					Recursos.contY = 5;
				}
				if (maxAncho) {
					Recursos.contX = -5;
				} else if (minAncho) {
					Recursos.contX = 5;
				}
			} else {
				Recursos.contY = -5;
			}

			Render.limpiarPantalla(1, 1, 1);
			Render.batch.setProjectionMatrix(cam.combined);
			cam.update();

			if (Recursos.cantPatos.get(0).isVivo()) {
				Recursos.cantPatos.get(0).setPosicion(Recursos.cantPatos.get(0).getX() + Recursos.contX*Recursos.cantPatos.get(0).getMultVel() ,Recursos.cantPatos.get(0).getY() + Recursos.contY);
			} else {
				Recursos.cantPatos.get(0)
						.setPosicion(Recursos.cantPatos.get(0).getX(),Recursos.cantPatos.get(0).getY() + Recursos.contY);
			}
			Recursos.cantPatos.get(0).actualizarBb((float)Recursos.cantPatos.get(0).getX(), (float) Recursos.cantPatos.get(0).getY());

			Vector3 mouseCoordinates = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
			cam.unproject(mouseCoordinates);
			float mouseX = mouseCoordinates.x;
			float mouseY = mouseCoordinates.y;
			actualizarBbMouse(mouseX, mouseY);

			Render.batch.begin();
			fondo.dibujar();
			Recursos.cantPatos.get(0).dibujar();
			pasto.dibujar();
			hud[0].setTexto("SCORE: " + Recursos.puntos);

			for (int i = 0; i < hud.length; i++) {
				hud[i].dibujar();
			}
			for (int i = 0; i < nroDisparos; i++) {
				balas[i].dibujar();
			}
			Render.batch.end();

//			sr.begin(ShapeRenderer.ShapeType.Line);
//			sr.setColor(Color.RED);
//			
//			sr.rect(pVerde.getBb().getX(),pVerde.getBb().getY(),pVerde.getBb().getWidth(),pVerde.getBb().getHeight());
//			
//			sr.setColor(Color.WHITE);
//			sr.rect(bbMouse.getX(),bbMouse.getY(),bbMouse.getWidth(),bbMouse.getHeight());
//			
//			
//			sr.end();

			if (entradas.isClick() && !sonidoEnReproduccion && Recursos.cantPatos.get(0).isVivo()) {
				if (Recursos.cantPatos.get(0).getBb().overlaps(bbMouse)) {
					Recursos.cantPatos.get(0).morir();
					Recursos.puntos += Recursos.cantPatos.get(0).getPuntos();
					if (Recursos.volSonidos) {
						scoreUP.play(0.5f);
					}

				}
				if (Recursos.volSonidos) {
					disparo.play();
				}
				nroDisparos--;
				sonidoEnReproduccion = true;
			} else if (!entradas.isClick()) {
				sonidoEnReproduccion = false;
			}

			Recursos.cantPatos.get(0).verificarPegarselaContraElPiso();
			if (Recursos.cantPatos.size() == 0) {

				Render.app.setScreen(new PantallaVictoria());
				terminarJuego();
			}

			if (nroDisparos == 0 && Recursos.cantPatos.get(0).isVivo()) {
				Recursos.cantPatos.get(0).getAletear().stop();
				Render.app.setScreen(new PantallaGO());
				terminarJuego();
			}

			if (entradas.isEsc()) {
				terminarJuego();
				Render.app.setScreen(new PantallaMenu());
			}

		} catch (Exception e) {
			System.out.println(e.toString());
		}

	}

	private void actualizarBbMouse(float mouseX, float mouseY) {
		bbMouse.x = mouseX - 15;
		bbMouse.y = mouseY - 16;
//		System.out.println("mouseX:" + bbMouse.getX());
//		System.out.println("mouseY:" + bbMouse.getY());
//		System.out.println("Pato X:" + pVerde.getX());
//		System.out.println("Pato Y:" + pVerde.getY());
	}

	private void terminarJuego() {
		for (int i = 0; i < Recursos.cantPatos.size(); i++) {
			Recursos.cantPatos.remove(i);
		}
	}

	@Override
	public void dispose() {
		Render.batch.dispose();
		disparo.dispose();
		musica.dispose();
		scoreUP.dispose();
		terminarJuego();
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

}
