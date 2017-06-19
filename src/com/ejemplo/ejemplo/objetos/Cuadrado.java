package com.ejemplo.ejemplo.objetos;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import com.ejemplo.ejemplo.MyRenderer;

import android.opengl.GLES20;

public class Cuadrado {
	private static final String vertexShader = "uniform mat4 uMVPMatrix;"
			+ "attribute vec4 vPosition;" + "void main() {"
			+ "gl_Position = uMVPMatrix * vPosition;" + "}";

	private static final String fragmentShader = "precision mediump float;"
			+ "uniform vec4 vColor;" + "void main() {"
			+ "  gl_FragColor = vColor;" + "}";

	private static final float[] coord = { -0.5f, 0.5f, 0, -0.5f, -0.5f, 0,
			0.5f, -0.5f, 0, 0.5f, 0.5f, 0 };
	private static final short[] drawOrder = { 0, 1, 2, 0, 2, 3 };
	private static final float[] color = { 0.5f, 1f, 0.3f, 1 };

	private static final int COORDS_PER_VERTEX = 3;
	private static final int V_COUNT = drawOrder.length;
	private static final int V_STRIDER = COORDS_PER_VERTEX * 4;

	private int program;
	private FloatBuffer fb;
	private ShortBuffer sb;

	private int mPositionHandle;
	private int mColorHandle;
	private int mVPMatrixHandle;

	public Cuadrado() {
		fb = ByteBuffer.allocateDirect(coord.length * 4)
				.order(ByteOrder.nativeOrder()).asFloatBuffer();
		fb.put(coord).position(0);

		sb = ByteBuffer.allocateDirect(drawOrder.length * 2)
				.order(ByteOrder.nativeOrder()).asShortBuffer();
		sb.put(drawOrder).position(0);

		program = MyRenderer.getProgram(vertexShader, fragmentShader);
		GLES20.glUseProgram(program);

		mPositionHandle = GLES20.glGetAttribLocation(program, "vPosition");
		mColorHandle = GLES20.glGetUniformLocation(program, "vColor");
		mVPMatrixHandle = GLES20.glGetUniformLocation(program, "uMVPMatrix");

		GLES20.glEnableVertexAttribArray(mPositionHandle);

		GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX,
				GLES20.GL_FLOAT, false, V_STRIDER, fb);
		GLES20.glUniform4fv(mColorHandle, 1, color, 0);
	}

	public void draw(float[] mMVPMatrix) {

		GLES20.glUniformMatrix4fv(mVPMatrixHandle, 1, false, mMVPMatrix, 0);

		GLES20.glDrawElements(GLES20.GL_TRIANGLES, V_COUNT,
				GLES20.GL_UNSIGNED_SHORT, sb);

	}
}
