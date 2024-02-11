package com.dojan.mijuego.utiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.dojan.mijuego.elementos.Imagen;
import com.dojan.mijuego.elementos.patos.Pato;

public class Recursos {
	//SPRITES
	public final static String PASTO = "fondos/Pasto.png",
							   SPRITESHEET_PATOVERDE = "sprites/patoverde.png",
							   SPRITESHEET_PATOAZUL = "sprites/patoazul.png",
							   SPRITESHEET_PATOROJO = "sprites/patorojo.png",
							   SPRITESHEET_PATODORADO = "sprites/patodorado.png",
							   SPRITESHEET_PERRORISA = "sprites/perroRisa.png",
							   SPRITE_PATOVICTORIA = "sprites/perrovictoria.png",
							   BALA = "sprites/bala.png",
							   PERRO_EMPATE = "sprites/Perro Empate.png"
							   ;
	//SFX
	public final static String DISPARO = "SFX/juego/pistola.mp3",
							   CUAC = "SFX/pato/cuac.mp3", 
							   VOLAR = "SFX/pato/volar.mp3", 
							   CAIDA = "SFX/pato/caida.mp3", 
							   GOLPE = "SFX/pato/golpe.mp3",
							   RISA_PERRO = "SFX/perro/risaperro.mp3",
							   ELEGIR_OPC_MENU = "SFX/juego/seleccionarOpcMenu.mp3", 
							   MOVER_OPC_MENU = "SFX/juego/moverOpc.mp3",
							   SILENCIO = "SFX/juego/silencio.mp3",
							   SCOREUP = "SFX/juego/scoreUP.mp3",
							   SONIDO_DERROTA = "SFX/juego/sonidoderrota.mp3"
							   ;
	
	//BGX
	public final static String CANCION = "BGX/BGX.mp3",
							   MUSICA_MENU = "BGX/MusicaMenu.mp3",
							   MUSICA_GO="BGX/MusicaGO.mp3",
							   MUSICA_VICTORIA = "BGX/MusicaVictoria.mp3"
							   ;
	//LOGOS, FONDOS, ETC
	public final static String CURSOR = "cursor/cursor.png",
							   LOGO = "fondos/PatoLogo2.png",
							   FONDO_MENU = "fondos/Menu.png",
							   FUENTE_MENU = "fuentes/VarsityTeam-Bold.otf",
							   FONDO_RANKING = "fondos/Sorry-Out-Of-Order-Sign.jpg",
							   FONDO_JUEGO = "fondos/fondo1_1920x1024.jpg"
							   ;
	
	public static int puntos = 0;
	public static boolean volSonidos = false, volMusica = false; 
	public static List<Pato> cantPatos = new ArrayList<Pato>();
	public static int cantInicialPatos = 10;
	public static Random r = new Random();
	public static int contX = 0, contY = 0;
	public static boolean izquierda = false, derecha = true;
	public static Imagen oponente = new Imagen(CURSOR);
	public static List<Imagen> balasMultijugador = new ArrayList<Imagen>();
	public static boolean multiplayer = false, juegoEmpezado = false, juegoTerminado = false;
	
	//Sonidos del multiplayer
	public static Sound disparo = Gdx.audio.newSound(Gdx.files.internal(DISPARO));
	public static Sound scoreUp = Gdx.audio.newSound(Gdx.files.internal(SCOREUP));
	public static Sound caida = Gdx.audio.newSound(Gdx.files.internal(CAIDA));
	public static Sound golpe = Gdx.audio.newSound(Gdx.files.internal(GOLPE));
	public static Sound aletear = Gdx.audio.newSound(Gdx.files.internal(VOLAR));
	public static Sound graznar = Gdx.audio.newSound(Gdx.files.internal(CUAC));
}
