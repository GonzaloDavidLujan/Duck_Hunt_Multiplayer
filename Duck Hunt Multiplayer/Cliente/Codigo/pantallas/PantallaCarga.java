package com.dojan.mijuego.pantallas;

import com.badlogic.gdx.Screen;
import com.dojan.mijuego.elementos.Imagen;
import com.dojan.mijuego.utiles.Config;
import com.dojan.mijuego.utiles.Recursos;
import com.dojan.mijuego.utiles.Render;

public class PantallaCarga implements Screen {
	private Imagen logo;
	private boolean fadeInTerminado = false;
	private float a = 0, contTiempo = 0, tiempoEspera = 5;

	@Override
	public void show() {
		logo = new Imagen(Recursos.LOGO);
		logo.setPosicion((Config.anchoVP - logo.getAncho()) / 2,
				(Config.altoVP - logo.getAlto()) / 2);
		a  = 0;
		logo.setTransparencia(a);
		
	}

	@Override
	public void render(float delta) {

		Render.limpiarPantalla(0, 0, 0);

		procesarFade();

		Render.batch.begin();
		logo.dibujar();
		Render.batch.end();

	}

	public void procesarFade() {
		if (!fadeInTerminado) {
			a += 0.01f;
			if (a > 1) {
				a = 1;
				fadeInTerminado = true;
			}
		} else {
			contTiempo += 0.05f;
			if (contTiempo > tiempoEspera) {
				a -= 0.01f;
				if (a < 0) {
					a = 0;
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					Render.app.setScreen(new PantallaMenu());
				}
			}
		}

		logo.setTransparencia(a);

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

	@Override
	public void dispose() {
		Render.batch.dispose();
	}

}
