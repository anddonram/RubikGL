package com.ejemplo.ejemplo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.ejemplo.ejemplo.objetos.Cuadra2;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.Matrix;

public class RendererCuadrados implements Renderer {
	private float[] viewM = new float[16];
	private float[] projectionM = new float[16];
	private float[] MVPMatrix = new float[16];
	private float[] rotM = new float[16];
	private float[] transM = new float[16];
	private List<Cuadra2> cuadrados;
	private static final Random rnd = new Random();

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		GLES20.glClearColor(0, 0, 0, 1);
		GLES20.glDisable(GLES20.GL_CULL_FACE);
		GLES20.glDisable(GLES20.GL_BLEND);
		cuadrados = new ArrayList<Cuadra2>();
		for (int i = 0; i < 1; i++)
			cuadrados.add(new Cuadra2(rnd.nextFloat() * 2, rnd.nextFloat(), rnd
					.nextFloat(), rnd.nextFloat()));
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		GLES20.glViewport(0, 0, width, height);
		float ratio = (float) width / height;
		Matrix.frustumM(projectionM, 0, -ratio, ratio, -1, 1, 3, 7);
	}

	private float x = -0.01f;
	private float y = 0.01f;
	private float a = 0;
	private float b = 0.25f;

	@Override
	public void onDrawFrame(GL10 gl) {
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

		if (a > 6 || a < -6)
			b = -b;
		a += b;
		Matrix.setLookAtM(viewM, 0, a, 0, (float) Math.sqrt((6 + a) * (6 - a)),
				0, 0, 0, 0, 1, 0);
		Matrix.multiplyMM(MVPMatrix, 0, projectionM, 0, viewM, 0);

		for (Cuadra2 c : cuadrados) {
			float[] scratch = new float[16];
			if (c.isArriba())
				y = -y;
			if (c.isIzquierda())
				x = -x;
			c.addX(x);
			c.addY(y);
			c.addAngle(1f);
			Matrix.setIdentityM(transM, 0);
			Matrix.translateM(transM, 0, c.getX(), c.getY(), 0);
			Matrix.setRotateM(rotM, 0, c.getAngle(), 0, 0, 1);
			Matrix.multiplyMM(rotM, 0, transM, 0, rotM, 0);
			Matrix.multiplyMM(scratch, 0, MVPMatrix, 0, rotM, 0);
			c.draw(scratch);
		}
	}
}
