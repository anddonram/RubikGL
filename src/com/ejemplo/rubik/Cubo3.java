package com.ejemplo.rubik;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import com.ejemplo.ejemplo.MyRenderer;

import android.opengl.GLES20;
import android.opengl.Matrix;

public class Cubo3 {
	private static final String vertexShader = "uniform mat4 uMVPMatrix;"
			+ "attribute vec4 vPosition;" + "attribute vec3 vColor;"
			+ "varying vec4 v_Color;" + "void main() {"
			+ "gl_Position = uMVPMatrix * vPosition;"
			+ "v_Color=vec4(vColor,1.0);" + "}";
	private static final String fragmentShader = "precision mediump float;"
			+ "varying vec4 v_Color;" + "void main() {"
			+ "  gl_FragColor = v_Color;" + "}";
	private static final short[] drawOrder = { 0, 1, 2, 0, 2, 3, 3, 2, 6, 3, 6,
			7, 4, 0, 3, 4, 3, 7, 1, 5, 6, 1, 6, 2, 4, 5, 1, 4, 1, 0, 7, 6, 5,
			7, 5, 4 };

	private static ShortBuffer sb;
	private static float[] scratch = new float[16];
	private static int program;
	private static int mPositionHandle;
	private static int mColorHandle;
	private static int mVPMatrixHandle;
	private FloatBuffer fb;
	public FloatBuffer cb;
	public volatile float[] color;
	private float[] rotationMatrix = new float[16];

	private float[] x = { 1, 0, 0 };
	private float[] y = { 0, 1, 0 };
	private float[] z = { 0, 0, 1 };

	public Cubo3(float i, float j, float k) {
		Matrix.setIdentityM(rotationMatrix, 0);
		color = new float[24];
		if (k == 0) {
			for (int index = 0; index < 12;) {
				color[index++] = 1;
				color[index++] = 0;
				color[index++] = 1;
			}
		} else if (k == -2) {
			for (int index = 12; index < 24;) {
				color[index++] = 1;
				color[index++] = 1;
				color[index++] = 1;
			}
		}
		if (j == 0) {
			color[3] = 0;
			color[4] = 0;
			color[5] = 1;
			color[6] = 0;
			color[7] = 0;
			color[8] = 1;
			color[15] = 0;
			color[16] = 0;
			color[17] = 1;
			color[18] = 0;
			color[19] = 0;
			color[20] = 1;
		} else if (j == 2) {
			color[0] = 1;
			color[1] = 0;
			color[2] = 0;
			color[9] = 1;
			color[10] = 0;
			color[11] = 0;
			color[12] = 1;
			color[13] = 0;
			color[14] = 0;
			color[21] = 1;
			color[22] = 0;
			color[23] = 0;
		}
		if (i == 0) {
			color[0] = 1;
			color[1] = 1;
			color[2] = 0;
			color[3] = 1;
			color[4] = 1;
			color[5] = 0;
			color[12] = 1;
			color[13] = 1;
			color[14] = 0;
			color[15] = 1;
			color[16] = 1;
			color[17] = 0;
		} else if (i == 2) {
			color[6] = 0;
			color[7] = 1;
			color[8] = 1;
			color[9] = 0;
			color[10] = 1;
			color[11] = 1;
			color[18] = 0;
			color[19] = 1;
			color[20] = 1;
			color[21] = 0;
			color[22] = 1;
			color[23] = 1;
		}
		final float[] coord = { i - 1.5f, j - 0.5f, k + 1.5f, i - 1.5f,
				j - 1.5f, k + 1.5f, i - .5f, j - 1.5f, k + 1.5f, i - .5f,
				j - 0.5f, k + 1.5f, i - 1.5f, j - 0.5f, k + .5f, i - 1.5f,
				j - 1.5f, k + .5f, i - .5f, j - 1.5f, k + .5f, i - .5f,
				j - 0.5f, k + .5f };

		fb = ByteBuffer.allocateDirect(coord.length * 4)
				.order(ByteOrder.nativeOrder()).asFloatBuffer();
		fb.put(coord).position(0);

		cb = ByteBuffer.allocateDirect(color.length * 4)
				.order(ByteOrder.nativeOrder()).asFloatBuffer();
		cb.put(color).position(0);
	}

	public void draw(float[] mPVMatrix) {
		Matrix.multiplyMM(scratch, 0, mPVMatrix, 0, rotationMatrix, 0);
		GLES20.glVertexAttribPointer(mPositionHandle, 3, GLES20.GL_FLOAT,
				false, 12, fb);
		GLES20.glVertexAttribPointer(mColorHandle, 3, GLES20.GL_FLOAT, false,
				3 * 4, cb);
		GLES20.glUniformMatrix4fv(mVPMatrixHandle, 1, false, scratch, 0);
		GLES20.glDrawElements(GLES20.GL_TRIANGLES, drawOrder.length,
				GLES20.GL_UNSIGNED_SHORT, sb);
	}

	public static void iniciar() {
		sb = ByteBuffer.allocateDirect(drawOrder.length * 2)
				.order(ByteOrder.nativeOrder()).asShortBuffer();
		sb.put(drawOrder).position(0);

		program = MyRenderer.getProgram(vertexShader, fragmentShader);
		GLES20.glUseProgram(program);

		mPositionHandle = GLES20.glGetAttribLocation(program, "vPosition");
		mColorHandle = GLES20.glGetAttribLocation(program, "vColor");
		mVPMatrixHandle = GLES20.glGetUniformLocation(program, "uMVPMatrix");

		GLES20.glEnableVertexAttribArray(mPositionHandle);
		GLES20.glEnableVertexAttribArray(mColorHandle);
	}

	public void rotar(float x, float y, float z) {
		Matrix.rotateM(rotationMatrix, 0, 90, x, y, z);
	}

	public void rotarX() {
		rotar(0);
	}

	public void rotarY() {
		rotar(1);
	}

	public void rotarZ() {
		rotar(2);
	}

	private void rotar(int i) {
		float[] res = { 0, 0, 0, 90 };
		if (x[i] == 1) {
			res[0] = 1;
			float[] aux = { -y[0], -y[1], -y[2] };
			y[0] = z[0];
			y[1] = z[1];
			y[2] = z[2];
			z = aux;
		} else if (y[i] == 1) {
			res[1] = 1;
			float[] aux = { -z[0], -z[1], -z[2] };
			z[0] = x[0];
			z[1] = x[1];
			z[2] = x[2];
			x = aux;
		} else if (z[i] == 1) {
			res[2] = 1;
			float[] aux = { -x[0], -x[1], -x[2] };
			x[0] = y[0];
			x[1] = y[1];
			x[2] = y[2];
			y = aux;
		} else if (x[i] == -1) {
			res[0] = 1;
			float[] aux = { y[0], y[1], y[2] };
			y[0] = -z[0];
			y[1] = -z[1];
			y[2] = -z[2];
			z = aux;
			res[3] = -90;
		} else if (y[i] == -1) {
			res[1] = 1;
			float[] aux = { z[0], z[1], z[2] };
			z[0] = -x[0];
			z[1] = -x[1];
			z[2] = -x[2];
			x = aux;
			res[3] = -90;
		} else if (z[i] == -1) {
			res[2] = 1;
			float[] aux = { x[0], x[1], x[2] };
			x[0] =- y[0];
			x[1] =- y[1];
			x[2] =- y[2];
			y = aux;
			res[3] = -90;
		}
		Matrix.rotateM(rotationMatrix, 0, res[3], res[0], res[1], res[2]);
	}

	public float[] crossProduct(float[] a, float[] b) {
		float[] res = new float[3];
		res[0] = a[1] * b[2] - a[2] * b[1];
		res[1] = a[2] * b[0] - a[0] * b[2];
		res[2] = a[0] * b[1] - a[1] * b[0];
		return res;
	}
}
