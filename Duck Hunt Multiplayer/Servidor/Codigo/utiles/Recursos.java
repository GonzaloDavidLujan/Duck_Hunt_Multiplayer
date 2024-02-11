package com.dojan.mijuego.utiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.dojan.mijuego.elementos.patos.Pato;

public class Recursos {
	//SPRITES
	public final static String SPRITESHEET_PATOVERDE = "sprites/patoverde.png",
							   SPRITESHEET_PATOAZUL = "sprites/patoazul.png",
							   SPRITESHEET_PATOROJO = "sprites/patorojo.png",
							   SPRITESHEET_PATODORADO = "sprites/patodorado.png"
							   ;

	public static List<Pato> cantPatos = new ArrayList<Pato>();
	public static final int CANT_INICIAL_PATOS = 7;
	public static Random r = new Random();
	public static int contX = 0, contY = 0;
	public static boolean maxAlto, minAlto = true, maxAncho, minAncho = true;
	public static boolean juegoEmpezado = false, juegoTerminado = false;
}
