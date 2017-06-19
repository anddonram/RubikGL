package com.ejemplo.rubik;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLES20;
import android.opengl.Matrix;

public class CRenderer implements Renderer {

	private static float[] projectionMatrix = new float[16];
	public volatile static float[] viewMatrix = new float[16];
	private static float[] MVPMatrix = new float[16];
	public volatile static Cubo3[] cubos = new Cubo3[27];

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		GLES20.glEnable(GLES20.GL_DEPTH_TEST);
		GLES20.glEnable(GLES20.GL_CULL_FACE);
		GLES20.glClearColor(.0f, 0.0f, 0.f, 1);
		crearCubos();
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		GLES20.glViewport(0, 0, width, height);
		float ratio = (float) width / height;
		Matrix.frustumM(projectionMatrix, 0, -ratio, ratio, -1, 1, 3, 100);
		Matrix.setLookAtM(viewMatrix, 0, 0, 0, 9, 0, 0, 0, 0, 1, 0);
	}

	@Override
	public void onDrawFrame(GL10 gl) {
		Matrix.multiplyMM(MVPMatrix, 0, projectionMatrix, 0, viewMatrix, 0);
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
		for (Cubo3 c : cubos) {
			c.draw(MVPMatrix);
		}
	}

	private void crearCubos() {
		Cubo3.iniciar();

		final int z = 3;
		cubos = new Cubo3[z * z * z];
		for (int i = 0; i < z; i++)
			for (int j = 0; j < z; j++)
				for (int k = 0; k < z; k++) {
					cubos[9 * i + 3 * j + k] = new Cubo3(i, j, -k);
				}
	}

}
