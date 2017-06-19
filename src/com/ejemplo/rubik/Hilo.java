package com.ejemplo.rubik;

import android.opengl.Matrix;

public class Hilo extends Thread {
	public volatile boolean fin = false;
	public volatile boolean clicked = false;
	public volatile int caraRotada;
	public volatile boolean rotar = false;
	public volatile float x = 0;
	public volatile float y = 0;
	private MySurface mySurface;

	public Hilo(MySurface mySurface) {
		super();
		this.mySurface = mySurface;
	}

	@Override
	public void run() {
		while (!fin) {
			if (clicked)
				hacerCambio2();
			if (rotar)
				rotar(x, y);
		}
	}

	private void rotar(float x, float y) {
		Matrix.rotateM(CRenderer.viewMatrix, 0, 1.5f, y, x, 0);
		rotar = false;
		mySurface.requestRender();
	}
	private void hacerCambio2() {
		Cubo3 aux;
		switch (caraRotada) {
		case 0:
			for (int i = 6; i < 27; i += 9) {
				CRenderer.cubos[i].rotarY();
				CRenderer.cubos[i + 1].rotarY();
				CRenderer.cubos[i + 2].rotarY();
			}
			aux = CRenderer.cubos[15];
			CRenderer.cubos[15] = CRenderer.cubos[7];
			CRenderer.cubos[7] = CRenderer.cubos[17];
			CRenderer.cubos[17] = CRenderer.cubos[25];
			CRenderer.cubos[25] = aux;
			aux = CRenderer.cubos[6];
			CRenderer.cubos[6] = CRenderer.cubos[8];
			CRenderer.cubos[8] = CRenderer.cubos[26];
			CRenderer.cubos[26] = CRenderer.cubos[24];
			CRenderer.cubos[24] = aux;
			break;
		case 1:
			for (int i = 0; i < 9; i++)
				CRenderer.cubos[i].rotarX();
			aux = CRenderer.cubos[8];
			CRenderer.cubos[8] = CRenderer.cubos[2];
			CRenderer.cubos[2] = CRenderer.cubos[0];
			CRenderer.cubos[0] = CRenderer.cubos[6];
			CRenderer.cubos[6] = aux;
			aux = CRenderer.cubos[7];
			CRenderer.cubos[7] = CRenderer.cubos[5];
			CRenderer.cubos[5] = CRenderer.cubos[1];
			CRenderer.cubos[1] = CRenderer.cubos[3];
			CRenderer.cubos[3] = aux;
			break;
		case 2:
			for (int i = 2; i < 27; i += 3) {
				CRenderer.cubos[i].rotarZ();
			}
			aux = CRenderer.cubos[5];
			CRenderer.cubos[5] = CRenderer.cubos[17];
			CRenderer.cubos[17] = CRenderer.cubos[23];
			CRenderer.cubos[23] = CRenderer.cubos[11];
			CRenderer.cubos[11] = aux;
			aux = CRenderer.cubos[2];
			CRenderer.cubos[2] = CRenderer.cubos[8];
			CRenderer.cubos[8] = CRenderer.cubos[26];
			CRenderer.cubos[26] = CRenderer.cubos[20];
			CRenderer.cubos[20] = aux;
			break;
		case 3:
			for (int i = 0; i < 27; i += 9) {
				CRenderer.cubos[i].rotarY( );
				CRenderer.cubos[i + 1].rotarY();
				CRenderer.cubos[i + 2].rotarY();
			}
			aux = CRenderer.cubos[1];
			CRenderer.cubos[1] = CRenderer.cubos[11];
			CRenderer.cubos[11] = CRenderer.cubos[19];
			CRenderer.cubos[19] = CRenderer.cubos[9];
			CRenderer.cubos[9] = aux;
			aux = CRenderer.cubos[2];
			CRenderer.cubos[2] = CRenderer.cubos[20];
			CRenderer.cubos[20] = CRenderer.cubos[18];
			CRenderer.cubos[18] = CRenderer.cubos[0];
			CRenderer.cubos[0] = aux;
			break;
		case 4:
			for (int i = 18; i < 27; i++)
				CRenderer.cubos[i].rotarX();
			aux = CRenderer.cubos[19];
			CRenderer.cubos[19] = CRenderer.cubos[21];
			CRenderer.cubos[21] = CRenderer.cubos[25];
			CRenderer.cubos[25] = CRenderer.cubos[23];
			CRenderer.cubos[23] = aux;
			aux = CRenderer.cubos[18];
			CRenderer.cubos[18] = CRenderer.cubos[24];
			CRenderer.cubos[24] = CRenderer.cubos[26];
			CRenderer.cubos[26] = CRenderer.cubos[20];
			CRenderer.cubos[20] = aux;
			break;
		case 5:
			for (int i = 0; i < 27; i += 3) {
				CRenderer.cubos[i].rotarZ();
			}
			aux = CRenderer.cubos[3];
			CRenderer.cubos[3] = CRenderer.cubos[15];
			CRenderer.cubos[15] = CRenderer.cubos[21];
			CRenderer.cubos[21] = CRenderer.cubos[9];
			CRenderer.cubos[9] = aux;
			aux = CRenderer.cubos[0];
			CRenderer.cubos[0] = CRenderer.cubos[6];
			CRenderer.cubos[6] = CRenderer.cubos[24];
			CRenderer.cubos[24] = CRenderer.cubos[18];
			CRenderer.cubos[18] = aux;
			break;
		}
		clicked = false;
		mySurface.requestRender();
	}
	private void hacerCambio() {
		Cubo3 aux;
		switch (caraRotada) {
		case 0:
			for (int i = 6; i < 27; i += 9) {
				CRenderer.cubos[i].rotar(0, 1, 0);
				CRenderer.cubos[i + 1].rotar(0, 1, 0);
				CRenderer.cubos[i + 2].rotar(0, 1, 0);
			}
			aux = CRenderer.cubos[15];
			CRenderer.cubos[15] = CRenderer.cubos[7];
			CRenderer.cubos[7] = CRenderer.cubos[17];
			CRenderer.cubos[17] = CRenderer.cubos[25];
			CRenderer.cubos[25] = aux;
			aux = CRenderer.cubos[6];
			CRenderer.cubos[6] = CRenderer.cubos[8];
			CRenderer.cubos[8] = CRenderer.cubos[26];
			CRenderer.cubos[26] = CRenderer.cubos[24];
			CRenderer.cubos[24] = aux;
			break;
		case 1:
			for (int i = 0; i < 9; i++)
				CRenderer.cubos[i].rotar(1, 0, 0);
			aux = CRenderer.cubos[8];
			CRenderer.cubos[8] = CRenderer.cubos[2];
			CRenderer.cubos[2] = CRenderer.cubos[0];
			CRenderer.cubos[0] = CRenderer.cubos[6];
			CRenderer.cubos[6] = aux;
			aux = CRenderer.cubos[7];
			CRenderer.cubos[7] = CRenderer.cubos[5];
			CRenderer.cubos[5] = CRenderer.cubos[1];
			CRenderer.cubos[1] = CRenderer.cubos[3];
			CRenderer.cubos[3] = aux;
			break;
		case 2:
			for (int i = 2; i < 27; i += 3) {
				CRenderer.cubos[i].rotar(0, 0, 1);
			}
			aux = CRenderer.cubos[5];
			CRenderer.cubos[5] = CRenderer.cubos[17];
			CRenderer.cubos[17] = CRenderer.cubos[23];
			CRenderer.cubos[23] = CRenderer.cubos[11];
			CRenderer.cubos[11] = aux;
			aux = CRenderer.cubos[2];
			CRenderer.cubos[2] = CRenderer.cubos[8];
			CRenderer.cubos[8] = CRenderer.cubos[26];
			CRenderer.cubos[26] = CRenderer.cubos[20];
			CRenderer.cubos[20] = aux;
			break;
		case 3:
			for (int i = 0; i < 27; i += 9) {
				CRenderer.cubos[i].rotar( 0, 1, 0);
				CRenderer.cubos[i + 1].rotar(0, 1, 0);
				CRenderer.cubos[i + 2].rotar(0, 1, 0);
			}
			aux = CRenderer.cubos[1];
			CRenderer.cubos[1] = CRenderer.cubos[11];
			CRenderer.cubos[11] = CRenderer.cubos[19];
			CRenderer.cubos[19] = CRenderer.cubos[9];
			CRenderer.cubos[9] = aux;
			aux = CRenderer.cubos[2];
			CRenderer.cubos[2] = CRenderer.cubos[20];
			CRenderer.cubos[20] = CRenderer.cubos[18];
			CRenderer.cubos[18] = CRenderer.cubos[0];
			CRenderer.cubos[0] = aux;
			break;
		case 4:
			for (int i = 18; i < 27; i++)
				CRenderer.cubos[i].rotar(1, 0, 0);
			aux = CRenderer.cubos[19];
			CRenderer.cubos[19] = CRenderer.cubos[21];
			CRenderer.cubos[21] = CRenderer.cubos[25];
			CRenderer.cubos[25] = CRenderer.cubos[23];
			CRenderer.cubos[23] = aux;
			aux = CRenderer.cubos[18];
			CRenderer.cubos[18] = CRenderer.cubos[24];
			CRenderer.cubos[24] = CRenderer.cubos[26];
			CRenderer.cubos[26] = CRenderer.cubos[20];
			CRenderer.cubos[20] = aux;
			break;
		case 5:
			for (int i = 0; i < 27; i += 3) {
				CRenderer.cubos[i].rotar(0, 0, 1);
			}
			aux = CRenderer.cubos[3];
			CRenderer.cubos[3] = CRenderer.cubos[15];
			CRenderer.cubos[15] = CRenderer.cubos[21];
			CRenderer.cubos[21] = CRenderer.cubos[9];
			CRenderer.cubos[9] = aux;
			aux = CRenderer.cubos[0];
			CRenderer.cubos[0] = CRenderer.cubos[6];
			CRenderer.cubos[6] = CRenderer.cubos[24];
			CRenderer.cubos[24] = CRenderer.cubos[18];
			CRenderer.cubos[18] = aux;
			break;
		}
		clicked = false;
		mySurface.requestRender();
	}
}
