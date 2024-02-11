package com.dojan.mijuego.pantallas;

import com.badlogic.gdx.Screen;
import com.dojan.mijuego.red.HiloServidor;
import com.dojan.mijuego.utiles.Config;
import com.dojan.mijuego.utiles.Recursos;
import com.dojan.mijuego.utiles.Render;

public class PantallaMultijugador implements Screen {
	private HiloServidor hs;

	@Override
	public void show() {
		System.out.println("SERVER");
		hs = new HiloServidor();
		hs.start();

	}

	@Override
	public void render(float delta) {
		try {
			Render.limpiarPantalla(0, 0, 0);

			if (hs.timerJuego > 0 && Recursos.juegoEmpezado) {
				hs.timerJuego -= 0.017f;
			}

			if (Recursos.juegoEmpezado && hs.timerJuego < 0) {
				if (!Recursos.cantPatos.isEmpty()) {
					// Acciones que dependen de elementos de Recursos.cantPatos
					if (Recursos.cantPatos.get(0).getY() > Config.altoVP - Recursos.cantPatos.get(0).getAlto()) {
						Recursos.maxAlto = true;
						Recursos.minAlto = false;
					}
					if (Recursos.cantPatos.get(0).getY() < 0) {
						Recursos.maxAlto = false;
						Recursos.minAlto = true;
					}
					if (Recursos.cantPatos.get(0).getX() > Config.anchoVP - Recursos.cantPatos.get(0).getAncho()) {
						Recursos.maxAncho = true;
						hs.enviarMensajeATodos("Flip#");
						Recursos.minAncho = false;
					}
					if (Recursos.cantPatos.get(0).getX() < 0) {
						Recursos.maxAncho = false;
						hs.enviarMensajeATodos("Flip#");
						Recursos.minAncho = true;
					}
					if (Recursos.cantPatos.get(0).isVivo()) {
						if (Recursos.maxAlto) {
							Recursos.contY = -5;
						} else if (Recursos.minAlto) {
							Recursos.contY = 5;
						}
						if (Recursos.maxAncho) {
							Recursos.contX = -5;
						} else if (Recursos.minAncho) {
							Recursos.contX = 5;
						}
					} else {
						Recursos.contY = -5;
					}
					if (Recursos.cantPatos.get(0).isVivo()) {
						Recursos.cantPatos.get(0).setPosicion(
								Recursos.cantPatos.get(0).getX()
										+ Recursos.contX * Recursos.cantPatos.get(0).getMultVel(),
								Recursos.cantPatos.get(0).getY() + Recursos.contY);
					} else {
						Recursos.cantPatos.get(0).setPosicion(Recursos.cantPatos.get(0).getX(),
								Recursos.cantPatos.get(0).getY() + Recursos.contY);
					}

					Recursos.cantPatos.get(0).actualizarBb();
					Recursos.cantPatos.get(0).verificarPegarselaContraElPiso(hs);
					if (Recursos.cantPatos.size() != 0) {
						hs.enviarMensajeATodos(
								"PosPato#" + Recursos.cantPatos.get(0).getX() + "#" + Recursos.cantPatos.get(0).getY());
					}

					if (Recursos.cantPatos.size() != 0) {
						Recursos.cantPatos.get(0).enviarSonidosPato(hs);
					}

				} else {
					Recursos.juegoEmpezado = false;
					Recursos.juegoTerminado = true;
				}

			}
		} catch (Exception e) {
			System.out.println("err " + e.toString());
		}
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
		System.out.println("DISPOSE SERVER");
		hs.enviarMensajeATodos("Server Desconectado#");
		hs.terminar();
	}

}
