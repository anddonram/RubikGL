package com.ejemplo.ejemplo.objetos;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import android.opengl.GLES20;

import com.ejemplo.ejemplo.MyRenderer;

public class RCubo {
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

	public Cubo2[] cubos;

	private ShortBuffer sb;
	private int program;
	private int V_COUNT;

	public RCubo() {
		program = MyRenderer.getProgram(vertexShader, fragmentShader);
		GLES20.glUseProgram(program);
		sb = ByteBuffer.allocateDirect(drawOrder.length * 2)
				.order(ByteOrder.nativeOrder()).asShortBuffer();
		sb.put(drawOrder).position(0);

		V_COUNT = drawOrder.length;

		int z = 3;
		cubos = new Cubo2[z * z * z];
		for (int i = 0; i < z; i++)
			for (int j = 0; j < z; j++)
				for (int k = 0; k < z; k++) {
					cubos[9 * i + 3 * j + k] = new Cubo2(i, j, -k);
				}
	}

	public void draw(float[] mPVMatrix) {
		for (Cubo2 c : cubos) {
			c.draw(mPVMatrix);
		}
	}

	public class Cubo2 {
		private float[] color;
		private static final int COORDS_PER_VERTEX = 3;
		private static final int V_STRIDE = COORDS_PER_VERTEX * 4;

		private int mPositionHandle;
		private int mColorHandle;
		private int mVPMatrixHandle;

		private FloatBuffer fb;
		private FloatBuffer cb;

		public Cubo2(float i, float j, float k) {
			this.color = new float[24];
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
			float[] coord = { i - 0.5f, j + 0.5f, k, i - 0.5f, j - 0.5f, k,
					i + 0.5f, j - 0.5f, k, i + 0.5f, j + 0.5f, k, i - 0.5f,
					j + 0.5f, k - 1, i - 0.5f, j - 0.5f, k - 1, i + 0.5f,
					j - 0.5f, k - 1, i + 0.5f, j + 0.5f, k - 1 };

			fb = ByteBuffer.allocateDirect(coord.length * 4)
					.order(ByteOrder.nativeOrder()).asFloatBuffer();
			fb.put(coord).position(0);

			cb = ByteBuffer.allocateDirect(color.length * 4)
					.order(ByteOrder.nativeOrder()).asFloatBuffer();
			cb.put(color).position(0);

			mPositionHandle = GLES20.glGetAttribLocation(program, "vPosition");
			mColorHandle = GLES20.glGetAttribLocation(program, "vColor");
			mVPMatrixHandle = GLES20
					.glGetUniformLocation(program, "uMVPMatrix");

			GLES20.glEnableVertexAttribArray(mPositionHandle);
			GLES20.glEnableVertexAttribArray(mColorHandle);
		}

		public void draw(float[] mPVMatrix) {
			GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX,
					GLES20.GL_FLOAT, false, V_STRIDE, fb);
			GLES20.glVertexAttribPointer(mColorHandle, 3, GLES20.GL_FLOAT,
					false, 3 * 4, cb);
			GLES20.glUniformMatrix4fv(mVPMatrixHandle, 1, false,
					 mPVMatrix , 0);
			GLES20.glDrawElements(GLES20.GL_TRIANGLES, V_COUNT,
					GLES20.GL_UNSIGNED_SHORT, sb);
		}

	}

}