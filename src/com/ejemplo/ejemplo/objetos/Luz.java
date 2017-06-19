package com.ejemplo.ejemplo.objetos;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import com.ejemplo.ejemplo.MyRenderer;

import android.opengl.GLES20;

public class Luz {
	private static final String vertexShader = "uniform mat4 uMVPMatrix;"
			+ "attribute vec4 vPosition;" + "void main(){"
			+ "gl_Position=uMVPMatrix*vPosition;" + "gl_PointSize=5.0;" + "}";
	private static final String fragmentShader = "precision mediump float;"
			+ "void main() {" + "  gl_FragColor = vec4(1.0,1.0,1.0,1.0);" + "}";

	public static final float[] coords = { 0.5f, -0.5f, 0.2f };

	private int program;
	private FloatBuffer fb;

	public Luz() {
		fb = ByteBuffer.allocateDirect(coords.length * 4)
				.order(ByteOrder.nativeOrder()).asFloatBuffer();
		fb.put(coords).position(0);
		
		program = MyRenderer.getProgram(vertexShader, fragmentShader);
	}

	public void draw(float[] uMVPMatrix) {
		GLES20.glUseProgram(program);

		int posHandle = GLES20.glGetAttribLocation(program, "vPosition");
		int mVPMatrixHandle = GLES20
				.glGetUniformLocation(program, "uMVPMatrix");
		GLES20.glEnableVertexAttribArray(posHandle);
		GLES20.glVertexAttribPointer(posHandle, 3, GLES20.GL_FLOAT, false,
				3 * 4, fb);

		GLES20.glUniformMatrix4fv(mVPMatrixHandle, 1, false, uMVPMatrix, 0);

		GLES20.glDrawArrays(GLES20.GL_POINTS, 0, 1);
		GLES20.glDisableVertexAttribArray(posHandle);
	}

}
