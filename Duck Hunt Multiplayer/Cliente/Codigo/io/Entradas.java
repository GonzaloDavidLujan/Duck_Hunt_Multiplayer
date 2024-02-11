package com.dojan.mijuego.io;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;

public class Entradas implements InputProcessor {

	private boolean click, abajo, izq, der, arriba, enter, esc, w, a, s, d, scrolArriba, scrolAbajo;
	private int clickPosX, clickPosY, mousePosX, mousePosY;

	@Override
	public boolean keyDown(int keycode) {
		if (keycode == Keys.DOWN) {
			abajo = true;
		}
		if (keycode == Keys.UP) {
			arriba = true;
		}
		if (keycode == Keys.RIGHT) {
			der = true;
		}
		if (keycode == Keys.LEFT) {
			izq = true;
		}
		if (keycode == Keys.ENTER) {
			enter = true;
		}
		if (keycode == Keys.ESCAPE) {
			esc = true;
		}
		if (keycode == Keys.W) {
			w = true;
		}
		if (keycode == Keys.A) {
			a = true;
		}
		if (keycode == Keys.S) {
			s = true;
		}
		if (keycode == Keys.D) {
			d = true;
		}
		return false;
	}

	public boolean isIzq() {
		return izq;
	}

	public boolean isDer() {
		return der;
	}

	@Override
	public boolean keyUp(int keycode) {
		if (keycode == Keys.DOWN) {
			abajo = false;
		}
		if (keycode == Keys.UP) {
			arriba = false;
		}
		if (keycode == Keys.RIGHT) {
			der = false;
		}
		if (keycode == Keys.LEFT) {
			izq = false;
		}
		if (keycode == Keys.ENTER) {
			enter = false;
		}
		if (keycode == Keys.ESCAPE) {
			esc = false;
		}
		if (keycode == Keys.W) {
			w = false;
		}
		if (keycode == Keys.A) {
			a = false;
		}
		if (keycode == Keys.S) {
			s = false;
		}
		if (keycode == Keys.D) {
			d = false;
		}
		return false;
	}

	public boolean isW() {
		return w;
	}

	public boolean isA() {
		return a;
	}

	public boolean isS() {
		return s;
	}

	public boolean isD() {
		return d;
	}

	public boolean isEsc() {
		return esc;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		clickPosX = screenX;
		clickPosY = screenY;
		if (button == 0) {
			click = true;
		}
		return false;
	}

	public int getClickPosX() {
		return clickPosX;
	}

	public int getClickPosY() {
		return clickPosY;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		click = false;
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {

		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		mousePosX = screenX;
		mousePosY = screenY;
		
		return false;
	}

	public int getMousePosX() {
		return mousePosX;
	}

	public int getMousePosY() {
		return mousePosY;
	}

	@Override
	public boolean scrolled(int amount) {
		scrolArriba = (amount < 0) ? true : false;
		scrolAbajo = (amount > 0) ? true : false;
		return false;
	}

	public void setScrolArriba(boolean scrolArriba) {
		this.scrolArriba = scrolArriba;
	}

	public void setScrolAbajo(boolean scrolAbajo) {
		this.scrolAbajo = scrolAbajo;
	}

	public boolean isScrolArriba() {
		return scrolArriba;
	}

	public boolean isScrolAbajo() {
		return scrolAbajo;
	}

	public boolean isClick() {
		return click;
	}

	public boolean isAbajo() {
		return abajo;
	}

	public boolean isArriba() {
		return arriba;
	}

	public boolean isEnter() {
		return enter;
	}

}
