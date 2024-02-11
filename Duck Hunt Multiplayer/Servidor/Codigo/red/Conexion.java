package com.dojan.mijuego.red;

import java.net.InetAddress;

public class Conexion {

	private InetAddress ip;
	private int puerto, nroJugador;
	
	public Conexion(InetAddress ip, int puerto, int nroJugador) {
		this.ip = ip;
		this.puerto = puerto;
		this.nroJugador = nroJugador;
	}

	public InetAddress getIp() {
		return ip;
	}

	public int getPuerto() {
		return puerto;
	}
	
	public int getNroJugador() {
		return nroJugador;
	}

}
