package com.ejemplo.ejemplo.objetos;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import android.opengl.GLES20;

import com.ejemplo.ejemplo.MyRenderer;

public class Cuadra2 {
	private static final String vertexShader = "uniform mat4 uMVPMatrix;"
			+ "attribute vec4 vPosition;" + "void main() {"
			+ "  gl_Position = uMVPMatrix * vPosition;" + "}";

	private static final String fragmentShader = "precision mediump float;"
			+ "uniform vec4 vColor;" + "void main() {"
			+ "  gl_FragColor = vColor;" + "}";
	private float x = 0;
	private float y = 0;
	private float angle = 0;
	private int program;
	private FloatBuffer fb;
	private ShortBuffer sb;

	private int mPositionHandle;
	private int mColorHandle;
	private int mVPMatrixHandle;
	private float[] color;

	public Cuadra2(float esquina, float r, float g, float b) {
		final float[] coord = { esquina - 0.5f, esquina + 0.5f, 0,
				esquina - 0.5f, esquina - 0.5f, 0, esquina + 0.5f,
				esquina - 0.5f, 0, esquina + 0.5f, esquina + 0.5f, 0 };
		final short[] drawOrder = { 0, 1, 2, 0, 2, 3 };
		color = new float[4];
		color[0] = r;
		color[1] = g;
		color[2] = b;
		color[3] = 1;
		fb = ByteBuffer.allocateDirect(coord.length * 4)
				.order(ByteOrder.nativeOrder()).asFloatBuffer();
		fb.put(coord).position(0);

		sb = ByteBuffer.allocateDirect(drawOrder.length * 2)
				.order(ByteOrder.nativeOrder()).asShortBuffer();
		sb.put(drawOrder).position(0);

		program = MyRenderer.getProgram(vertexShader, fragmentShader);

		mPositionHandle = GLES20.glGetAttribLocation(program, "vPosition");
		mColorHandle = GLES20.glGetUniformLocation(program, "vColor");
		mVPMatrixHandle = GLES20.glGetUniformLocation(program, "uMVPMatrix");

	}

	public void draw(float[] mMVPMatrix) {
		GLES20.glUseProgram(program);
		GLES20.glEnableVertexAttribArray(mPositionHandle);

		GLES20.glVertexAttribPointer(mPositionHandle, 3, GLES20.GL_FLOAT,
				false, 12, fb);
		GLES20.glUniform4fv(mColorHandle, 1, color, 0);
		GLES20.glUniformMatrix4fv(mVPMatrixHandle, 1, false, mMVPMatrix, 0);

		GLES20.glDrawElements(GLES20.GL_TRIANGLES, 6, GLES20.GL_UNSIGNED_SHORT,
				sb);
	}

	public void addX(float x) {
		this.x += x;
	}

	public void addY(float y) {
		this.y += y;
	}

	public boolean isIzquierda() {
		return x <= -2 || x >= 2;
	}

	public boolean isArriba() {
		return y <= -1 || y >= 1;
	}

	public float getAngle() {
		return angle;
	}

	public void addAngle(float angle) {
		this.angle += angle;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

}
