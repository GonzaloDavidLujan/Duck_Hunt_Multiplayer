package com.dojan.mijuego.red;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import com.badlogic.gdx.Gdx;
import com.dojan.mijuego.elementos.Imagen;
import com.dojan.mijuego.elementos.patos.Direccion;
import com.dojan.mijuego.elementos.patos.PatoAzul;
import com.dojan.mijuego.elementos.patos.PatoDorado;
import com.dojan.mijuego.elementos.patos.PatoRojo;
import com.dojan.mijuego.elementos.patos.PatoVerde;
import com.dojan.mijuego.utiles.Recursos;
import com.dojan.mijuego.utiles.Render;

public class HiloCliente extends Thread {

	private DatagramSocket socket;
	private InetAddress ipServidor;
	private int puertoServidor = 52000;
	private boolean fin = false, jugadorDesconectado = false, salaLlena = false, ganado = false, perdido = false,
			empatado = false;
	private Estados estado;
	
	private int nroJugador;
	private boolean serverDesconectado = false;

	public HiloCliente() {
		try {
			jugadorDesconectado = false;
			salaLlena = false;
			ganado = false;
			perdido = false;
			empatado = false;
			estado = Estados.DESCONECTADO;
			
			System.out.println("SE  EJECUTO EL HILO CLIENTE");
			String mensaje = "Conexion#";
			byte[] data = mensaje.getBytes();
			DatagramPacket dp = new DatagramPacket(data, data.length, InetAddress.getByName("255.255.255.255"),
					this.puertoServidor);
			socket = new DatagramSocket();
			socket.send(dp);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		while (!fin) {
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

		case "Conectado":
			estado = Estados.CONECTADO;
			this.ipServidor = dp.getAddress();
			Render.app.cambiarCursor(false);
			Recursos.multiplayer = true;
			nroJugador = Integer.parseInt(msgCompuesto[1]);
			break;
		case "Desconectado":
			Render.app.cambiarCursor(false);
			estado = Estados.DESCONECTADO;
			Recursos.multiplayer = false;
			synchronized (Recursos.balasMultijugador) {
				Recursos.balasMultijugador.clear();
			}
			break;

		case "salaLlena":
			salaLlena = true;
			break;

		case "Empieza":
			Recursos.juegoEmpezado = true;
			Render.app.cambiarCursor(true);
			break;

		case "Desconexion":
			Render.app.cambiarCursor(false);
			Recursos.juegoEmpezado = false;
			jugadorDesconectado = true;
			break;

		case "Nrandom":
			final int[] NUMERO_RANDOM = { Integer.parseInt(msgCompuesto[1]) };

			// Utiliza Application.postRunnable() para ejecutar el código en el hilo
			// principal.
			Gdx.app.postRunnable(new Runnable() {
				@Override
				public void run() {
					if (NUMERO_RANDOM[0] <= 50) {
						Recursos.cantPatos.add(new PatoVerde());
					} else if (NUMERO_RANDOM[0] <= 80) {
						Recursos.cantPatos.add(new PatoAzul());
					} else if (NUMERO_RANDOM[0] <= 95) {
						Recursos.cantPatos.add(new PatoRojo());
					} else {
						Recursos.cantPatos.add(new PatoDorado());
					}
				}
			});
			break;

		case "CantPatos":
			Recursos.cantInicialPatos = Integer.parseInt(msgCompuesto[1]);
			break;

		case "SumarBalas":

			final int CANT_BALAS = Integer.parseInt(msgCompuesto[1]);
			Gdx.app.postRunnable(new Runnable() {
				@Override
				public void run() {
					for (int i = 0; i < CANT_BALAS; i++) {
						Recursos.balasMultijugador.add(new Imagen(Recursos.BALA));
					}

				}
			});
			break;

		case "RestarBalas":
			Recursos.balasMultijugador.remove(0);
			break;

		case "Disparo":
			if (Recursos.volSonidos) {
				Recursos.disparo.play();
			}
			break;

		case "PosPato":
			if (Recursos.juegoEmpezado && Recursos.cantPatos.size() > 0) {

				if (Recursos.cantPatos.get(0).getX() < Float.parseFloat(msgCompuesto[1])) {
					Recursos.cantPatos.get(0).dir = Direccion.DERECHA;
				} else {
					Recursos.cantPatos.get(0).dir = Direccion.IZQUIERDA;
				}
				Recursos.cantPatos.get(0).setPosicion(Float.parseFloat(msgCompuesto[1]),
						Float.parseFloat(msgCompuesto[2]));

			}
			break;

		case "PosMouse":
			if (Recursos.juegoEmpezado && Recursos.cantInicialPatos > 0
					&& Recursos.cantInicialPatos <= Recursos.cantPatos.size()) {
				Recursos.oponente.setPosicion(Integer.parseInt(msgCompuesto[1]), Integer.parseInt(msgCompuesto[2]));
			}
			break;

		case "Flip":

			for (int i = 0; i < Recursos.cantPatos.size(); i++) {
				if (Recursos.cantPatos.get(i).dir != null) {
					if (Recursos.cantPatos.get(i).dir == Direccion.DERECHA && !Recursos.cantPatos.get(i).isFlipX()) {
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
			break;

		case "MatarPato":
			Recursos.cantPatos.remove(0);
			break;

		case "ScoreUp":
			if (Recursos.volSonidos) {
				Recursos.scoreUp.play();
			}
			Recursos.puntos = Integer.parseInt(msgCompuesto[1]);
			break;

		case "PlayAletear":
			if (Recursos.volSonidos) {
				Recursos.aletear.play();
			}
			break;

		case "PlayGraznar":
			if (Recursos.volSonidos) {
				Recursos.graznar.play();
			}
			break;

		case "PlayCaida":
			if (Recursos.volSonidos) {
				Recursos.caida.loop();
			}
			break;

		case "StopCaida":
			if (Recursos.volSonidos) {
				Recursos.caida.stop();
				Recursos.golpe.play();
			}
			break;

		case "TerminarJuego":
			Recursos.juegoEmpezado = false;
			Recursos.juegoTerminado = true;
			break;

		case "Gano":
			Recursos.juegoEmpezado = false;
			Recursos.juegoTerminado = true;
			Render.app.cambiarCursor(false);
		
			
			if (this.nroJugador == Integer.parseInt(msgCompuesto[1])) {
				this.ganado = true;
				this.perdido = false;
				this.empatado = false;
			} else {
				this.ganado = false;
				this.perdido = true;
				this.empatado = false;
			}
			estado = Estados.DESCONECTADO;
			break;

		case "Empate":
			Recursos.juegoEmpezado = false;
			Recursos.juegoTerminado = true;
			Render.app.cambiarCursor(false);
			
			this.ganado = false;
			this.perdido = false;
			this.empatado = true;
			estado = Estados.DESCONECTADO;

			break;
		case "Server Desconectado":
			estado = Estados.DESCONECTADO;
			serverDesconectado  = true;
			Recursos.juegoEmpezado = false;
			Recursos.juegoTerminado = true;
			System.out.println("Se cayo/cerro el server");
			break;
		}

	}

	public void enviarMensaje(String mensaje) {
		byte[] data = mensaje.getBytes();
		DatagramPacket dp = new DatagramPacket(data, data.length, this.ipServidor, this.puertoServidor);
		try {
			socket.send(dp);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Estados getEstado() {
		return estado;
	}

	public boolean isJugadorDesconectado() {
		return jugadorDesconectado;
	}

	public void terminar() {
		fin = true;
	}

	public boolean isSalaLlena() {
		return salaLlena;
	}

	public boolean isGanado() {
		return ganado;
	}

	public boolean isPerdido() {
		return perdido;
	}

	public boolean isEmpatado() {
		return empatado;
	}

	public boolean isServerDesconectado() {
		return serverDesconectado;
	}

	
	
}
