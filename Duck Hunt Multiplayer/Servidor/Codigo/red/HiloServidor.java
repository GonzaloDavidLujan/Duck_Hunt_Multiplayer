package com.dojan.mijuego.red;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.dojan.mijuego.elementos.patos.PatoAzul;
import com.dojan.mijuego.elementos.patos.PatoDorado;
import com.dojan.mijuego.elementos.patos.PatoRojo;
import com.dojan.mijuego.elementos.patos.PatoVerde;
import com.dojan.mijuego.utiles.Config;
import com.dojan.mijuego.utiles.Recursos;

public class HiloServidor extends Thread {

	private DatagramSocket socket;
	private int puerto = 52000;
	private boolean fin = false;
	private int indiceCliente = 0;
	private int[] puntajeJugadores = { 0, 0 };
	private Rectangle[] bbMouseJugadores = new Rectangle[2];
	private ArrayList<Conexion> conexiones = new ArrayList<Conexion>();
	public float timerJuego = 3f;
	private int[] cantBalasJugadores = new int[2];
	
	public HiloServidor() {
		try {
			fin = false;
			for (int i = 0; i < cantBalasJugadores.length; i++) {
				cantBalasJugadores[i] = Recursos.CANT_INICIAL_PATOS + 3;
			}
			
			socket = new DatagramSocket(puerto);
			System.out.println("EMPEZÓ EL SERVER");
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		System.out.println("Inició el server");
		while (!fin) {
			if ((Recursos.cantPatos.isEmpty() && Recursos.juegoTerminado && !conexiones.isEmpty()) || (Recursos.juegoEmpezado && !verificarBalas() )) {
				if (puntajeJugadores[0] > puntajeJugadores[1]) {
					enviarMensajeATodos("Gano#" + conexiones.get(0).getNroJugador());
				} else if (puntajeJugadores[1] > puntajeJugadores[0]) {
					enviarMensajeATodos("Gano#" + conexiones.get(1).getNroJugador());
				} else {
					enviarMensajeATodos("Empate#");
				}
				Recursos.juegoEmpezado = false;
				Recursos.juegoTerminado = false;
				timerJuego = 3f;

				for (int i = 0; i < puntajeJugadores.length; i++) {
					puntajeJugadores[i] = 0;
				}				
				
				synchronized (conexiones) {
					conexiones.clear();
				}


			}
			byte[] data = new byte[1024];
			DatagramPacket dp = new DatagramPacket(data, data.length);
			try {
				socket.receive(dp);
				procesarMensaje(dp);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void procesarMensaje(DatagramPacket dp) {
		String msg = (new String(dp.getData())).trim();
		String[] msgCompuesto = msg.split("#");

		switch (msgCompuesto[0]) {

		case "Conexion":
			indiceCliente = -1;

			if (conexiones.size() > 0) {
				indiceCliente = verificarExisteCliente(dp);
				Recursos.juegoEmpezado = false;
			}

			if (conexiones.size() < 2) {
				if (indiceCliente == -1) {
					conexiones.add(new Conexion(dp.getAddress(), dp.getPort(), (conexiones.size() + 1)));
					enviarMensaje("Conectado#" + conexiones.get(conexiones.size() - 1).getNroJugador(), dp.getAddress(),
							dp.getPort());
					bbMouseJugadores[conexiones.size() - 1] = new Rectangle();
					if (conexiones.size() == 2) {
						enviarMensajeATodos("Empieza#");
						Recursos.juegoEmpezado = true;
						Recursos.minAlto = true;
						Recursos.minAncho = true;
						enviarMensajeATodos("CantPatos#" + Recursos.CANT_INICIAL_PATOS);
						
						for (int i = 0; i < cantBalasJugadores.length; i++) {
							if (cantBalasJugadores[i] == 0) {
								cantBalasJugadores[i] = Recursos.CANT_INICIAL_PATOS + 3;
							}
						}
						
						enviarMensajeATodos("SumarBalas#" + (Recursos.CANT_INICIAL_PATOS + 3));
						for (int i = 0; i < Recursos.CANT_INICIAL_PATOS; i++) {
							final int PATO_RANDOM = Recursos.r.nextInt(100) + 1;

							Gdx.app.postRunnable(new Runnable() {
								@Override
								public void run() {
									synchronized (Recursos.cantPatos) {
										enviarMensajeATodos("Nrandom#" + PATO_RANDOM);
										if (PATO_RANDOM <= 50) {
											Recursos.cantPatos.add(new PatoVerde());
										} else if (PATO_RANDOM <= 80) {
											Recursos.cantPatos.add(new PatoAzul());
										} else if (PATO_RANDOM <= 95) {
											Recursos.cantPatos.add(new PatoRojo());
										} else {
											Recursos.cantPatos.add(new PatoDorado());
										}
										Recursos.r = new Random();
										Recursos.cantPatos.get(Recursos.cantPatos.size() - 1)
												.setPosicion((Recursos.r.nextInt(Config.anchoVP) - 35) + 5, 0);

									}
								}
							});
						}

					}
				}
			} else {
				enviarMensaje("salaLlena#", dp.getAddress(), dp.getPort());
			}
			break;

		case "Desconexion":
			
			if (conexiones.size() > 0) {
				indiceCliente = verificarExisteCliente(dp);
				enviarMensajeATodos("Desconexion#" + conexiones.get(indiceCliente).getNroJugador());
				enviarMensajeATodos("Desconectado#");

				conexiones.remove(indiceCliente);

			}
			for (int i = 0; i < cantBalasJugadores.length; i++) {
				cantBalasJugadores[i] = 0;
			}
			for (int i = 0; i < puntajeJugadores.length; i++) {
				puntajeJugadores[i] = 0;
			}
			Recursos.juegoEmpezado = false;
			synchronized (Recursos.cantPatos) {
				Recursos.cantPatos.clear();
			}
			timerJuego = 3f;
			break;

		case "PosMouse":
			if (!Recursos.juegoTerminado && conexiones.size() > 0) {
				indiceCliente = verificarExisteCliente(dp);
				if (Recursos.juegoEmpezado) {
					if (indiceCliente == 0) {
						enviarMensaje(msg, conexiones.get(1).getIp(), conexiones.get(1).getPuerto());
					} else {
						enviarMensaje(msg, conexiones.get(0).getIp(), conexiones.get(0).getPuerto());
					}
					bbMouseJugadores[indiceCliente].height = 32;
					bbMouseJugadores[indiceCliente].width = 32;
					bbMouseJugadores[indiceCliente].x = Float.parseFloat(msgCompuesto[1]);
					bbMouseJugadores[indiceCliente].y = Float.parseFloat(msgCompuesto[2]);

				}
			}

			break;

		case "Click":
			if (!Recursos.juegoTerminado) {
				indiceCliente = verificarExisteCliente(dp);

				if (Recursos.juegoEmpezado) {
					if (verificarBalas()) {
						if (cantBalasJugadores[indiceCliente] != 0) {
							enviarMensajeATodos("Disparo#");
							cantBalasJugadores[indiceCliente]--;
							enviarMensaje("RestarBalas#", conexiones.get(indiceCliente).getIp(),
									conexiones.get(indiceCliente).getPuerto());
							if (Recursos.cantPatos.get(0).getBb().overlaps(bbMouseJugadores[indiceCliente])) {
								Recursos.cantPatos.get(0).morir(this);
								puntajeJugadores[indiceCliente] += Recursos.cantPatos.get(0).getPuntos();
								enviarMensaje("ScoreUp#" + puntajeJugadores[indiceCliente],
										conexiones.get(indiceCliente).getIp(),
										conexiones.get(indiceCliente).getPuerto());
							}
						}
					}else {
						Recursos.juegoTerminado = true;
					}
				}
			}

			break;
		}

	}

	private boolean verificarBalas() {
		boolean tienenBalas = true;

		if (cantBalasJugadores[0] <= 0 && cantBalasJugadores[1] <= 0) {
			tienenBalas = false;
		}

		return tienenBalas;
	}

	public void enviarMensajeATodos(String msg) {
		for (int i = 0; i < conexiones.size(); i++) {
			enviarMensaje(msg, conexiones.get(i).getIp(), conexiones.get(i).getPuerto());
		}
	}

	private int verificarExisteCliente(DatagramPacket dp) {
		int indice = -1;
		int i = 0;
		do {
			int puertoConex = conexiones.get(i).getPuerto();
			InetAddress ipConex = conexiones.get(i).getIp();
			if (ipConex.equals(dp.getAddress()) && puertoConex == dp.getPort()) {
				indice = i;
			}
			i++;
		} while (i < conexiones.size() && indice == -1);
		return indice;
	}

	public void enviarMensaje(String mensaje, InetAddress ipDestino, int puerto) {
		byte[] data = mensaje.getBytes();
		DatagramPacket dp = new DatagramPacket(data, data.length, ipDestino, puerto);
		try {
			socket.send(dp);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void terminar() {
		for (int i = 0; i < puntajeJugadores.length; i++) {
			puntajeJugadores[i] = 0;
		}
		for (int i = 0; i < cantBalasJugadores.length; i++) {
			cantBalasJugadores[i] = 0;
		}
		fin = true;
	}

}
