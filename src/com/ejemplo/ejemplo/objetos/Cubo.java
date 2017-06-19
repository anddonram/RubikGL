package com.ejemplo.ejemplo.objetos;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import com.ejemplo.ejemplo.MyRenderer;

import android.opengl.GLES20;

public class Cubo {
	private static final String vertexShader = "uniform mat4 uMVPMatrix;"
			+ "uniform vec3 lightPos;" + "attribute vec4 vPosition;"
			+ "attribute vec4 vColor;" + "attribute vec3 vNormal;"
			+ "varying vec4 v_Color;" + "void main() {"
			+ "vec3 mVVertex=vec3(uMVPMatrix*vPosition);"
			+ "vec3 mVNormal=vec3(uMVPMatrix*vec4(vNormal,0.0));"
			+ "float distance=length(lightPos-mVVertex);"
			+ "vec3 lightVector=normalize(lightPos-mVVertex);"
			+ "float diffuse=max(dot(mVNormal,lightVector),0.1);"
			+ "diffuse=diffuse*(1.0/(1.0+(0.1 * distance * distance)));"
			+ "v_Color= vColor * diffuse;"
			+ "gl_Position = uMVPMatrix * vPosition;" + "}";

	private static final String fragmentShader = "precision mediump float;"
			+ "varying vec4 v_Color;" + "void main() {"
			+ "  gl_FragColor = v_Color;" + "}";

	private static final float[] coord = { -0.5f, 0.5f, 0, -0.5f, -0.5f, 0,
			0.5f, -0.5f, 0, 0.5f, 0.5f, 0, -0.5f, 0.5f, -1, -0.5f, -0.5f, -1,
			0.5f, -0.5f, -1, 0.5f, 0.5f, -1 };
	private static final short[] drawOrder = { 0, 1, 2, 0, 2, 3, 3, 2, 6, 3, 6,
			7, 4, 0, 3, 4, 3, 7, 1, 5, 6, 1, 6, 2, 4, 5, 1, 4, 1, 0, 7, 6, 5,
			7, 5, 4 };
	private static final float[] normal = { 0, 0, 1, 0, 0, 1, 1, 0, 0, 1, 0, 0,
			0, 1, 0, 0, 1, 0, 0, -1, 0, 0, -1, 0, -1, 0, 0, -1, 0, 0, 0, 0, -1,
			0, 0, -1 };
	// private static final float[] color = { 0, 0.f, 0.3f, 1, 1, 0, 0, 1, 0, 1,
	// 0, 1, 0.7f, 0, 1, 1, 0.8f, 0.5f, 0.3f, 1, 0.2f, 0.5f, 0.3f, 1, 0,
	// 0.6f, 0.5f, 1, 0.8f, 0, 0.1f, 1, };
	private static final float[] color = { 0, 0, 0, 1, 0, 0, 1, 1, 0, 1, 1, 1,
			0, 0, 1, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, };
	private static final int COORDS_PER_VERTEX = 3;
	private static final int V_COUNT = drawOrder.length;
	private static final int V_STRIDER = COORDS_PER_VERTEX * 4;

	private int program;
	private FloatBuffer fb;
	private FloatBuffer cb;
	private FloatBuffer nb;
	private ShortBuffer sb;

	public Cubo() {
		fb = ByteBuffer.allocateDirect(coord.length * 4)
				.order(ByteOrder.nativeOrder()).asFloatBuffer();
		fb.put(coord).position(0);

		sb = ByteBuffer.allocateDirect(drawOrder.length * 2)
				.order(ByteOrder.nativeOrder()).asShortBuffer();
		sb.put(drawOrder).position(0);

		cb = ByteBuffer.allocateDirect(color.length * 4)
				.order(ByteOrder.nativeOrder()).asFloatBuffer();
		cb.put(color).position(0);

		nb = ByteBuffer.allocateDirect(normal.length * 4)
				.order(ByteOrder.nativeOrder()).asFloatBuffer();
		nb.put(normal).position(0);

		program = MyRenderer.getProgram(vertexShader, fragmentShader);
	}

	public void draw(float[] mPVMatrix) {
		GLES20.glUseProgram(program);

		int mPositionHandle = GLES20.glGetAttribLocation(program, "vPosition");
		int mColorHandle = GLES20.glGetAttribLocation(program, "vColor");
		int nVectorHandle = GLES20.glGetAttribLocation(program, "vNormal");
		int lightPosHandle = GLES20.glGetUniformLocation(program, "lightPos");
		int mVPMatrixHandle = GLES20
				.glGetUniformLocation(program, "uMVPMatrix");

		GLES20.glEnableVertexAttribArray(mPositionHandle);
		GLES20.glEnableVertexAttribArray(mColorHandle);
		GLES20.glEnableVertexAttribArray(nVectorHandle);

		GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX,
				GLES20.GL_FLOAT, false, V_STRIDER, fb);
		GLES20.glVertexAttribPointer(mColorHandle, 4, GLES20.GL_FLOAT, false,
				4 * 4, cb);
		GLES20.glUniform3fv(lightPosHandle, 1, Luz.coords, 0);

		GLES20.glVertexAttribPointer(nVectorHandle, 3, GLES20.GL_FLOAT, false,
				3 * 4, nb);
		// GLES20.glVertexAttrib3f(nVectorHandle, 0, 0, 1);

		GLES20.glUniformMatrix4fv(mVPMatrixHandle, 1, false, mPVMatrix, 0);

		GLES20.glDrawElements(GLES20.GL_TRIANGLES, V_COUNT,
				GLES20.GL_UNSIGNED_SHORT, sb);

	}

}
