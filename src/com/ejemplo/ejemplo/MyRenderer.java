package com.ejemplo.ejemplo;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.ejemplo.ejemplo.objetos.Cuadrado;
import com.ejemplo.ejemplo.objetos.Cubo;
import com.ejemplo.ejemplo.objetos.Luz;
import com.ejemplo.ejemplo.objetos.RCubo;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.Matrix;
import android.os.SystemClock;

public class MyRenderer implements Renderer {
	private volatile float rAngle = 0;
	private volatile float x = 0;
	private volatile float y = 0;
	public volatile float nx = 0;
	public volatile float ny = 0;

	private boolean blend;
	private Cuadrado c;
	private Cubo cubo;
	private Luz luz;
	private RCubo rcubo;
	private float[] mProjection = new float[16];
	private float[] mView = new float[16];
	private float[] mPVMatrix = new float[16];
	private float[] mRotation = new float[16];
	private float[] mTranslation = new float[16];

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		blend = false;
		GLES20.glEnable(GLES20.GL_DEPTH_TEST);
		GLES20.glEnable(GLES20.GL_CULL_FACE);
		GLES20.glClearColor(.0f, 0.0f, 0.f, 1);
		// c = new Cuadrado();
		// cubo = new Cubo();
		luz = new Luz();
		rcubo = new RCubo();
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		GLES20.glViewport(0, 0, width, height);
		float ratio = (float) width / height;
		Matrix.frustumM(mProjection, 0, -ratio, ratio, -1, 1, 3, 15);
	}

	@Override
	public void onDrawFrame(GL10 gl) {
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
		Matrix.setLookAtM(mView, 0, 0f, 0f, 9, 0f, 0f, 0, 0, 1, 0);
		Matrix.multiplyMM(mPVMatrix, 0, mProjection, 0, mView, 0);
		float[] scratch = new float[16];
		if (cubo != null) {
			long time = SystemClock.uptimeMillis() % 4000;
			float angle = 0.09f * time;
			Matrix.setRotateM(mRotation, 0, angle, 0, 0.5f, 0f);
			Matrix.multiplyMM(scratch, 0, mPVMatrix, 0, mRotation, 0);
			cubo.draw(scratch);
			luz.draw(mPVMatrix);
		}
		if (c != null) {
			Matrix.setRotateM(mRotation, 0, rAngle, 0, 0, 1);
			Matrix.setIdentityM(mTranslation, 0);
			Matrix.translateM(mTranslation, 0, x, y, 0);
			Matrix.multiplyMM(mRotation, 0, mTranslation, 0, mRotation, 0);
			Matrix.multiplyMM(scratch, 0, mPVMatrix, 0, mRotation, 0);
			c.draw(scratch);
		}
		if (rcubo != null) {
			Matrix.setIdentityM(mTranslation, 0);
			Matrix.setRotateM(mRotation, 0, nx, 0, 1, 0f);
			Matrix.rotateM(mRotation, 0, ny, 1, 0, 0f);
			Matrix.translateM(mTranslation, 0, -1, -1, 1.5f);
			Matrix.multiplyMM(mRotation, 0, mRotation, 0, mTranslation, 0);
			Matrix.multiplyMM(scratch, 0, mPVMatrix, 0, mRotation, 0);
			rcubo.draw(scratch);
		}
	}

	public static int loadShader(int type, String shaderCode) {
		int shader = GLES20.glCreateShader(type);
		GLES20.glShaderSource(shader, shaderCode);
		GLES20.glCompileShader(shader);
		return shader;
	}

	public static int getProgram(String vertexShader, String fragmentShader) {
		int vS = MyRenderer.loadShader(GLES20.GL_VERTEX_SHADER, vertexShader);
		int fS = MyRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER,
				fragmentShader);
		int program = GLES20.glCreateProgram();
		GLES20.glAttachShader(program, fS);
		GLES20.glAttachShader(program, vS);
		GLES20.glLinkProgram(program);
		return program;
	}

	public void cambiarBlending() {
		blend = !blend;
		if (blend && rcubo == null) {
			GLES20.glDisable(GLES20.GL_DEPTH_TEST);
			GLES20.glDisable(GLES20.GL_CULL_FACE);
			GLES20.glEnable(GLES20.GL_BLEND);
			GLES20.glBlendFunc(GLES20.GL_ONE, GLES20.GL_ONE);
		} else {
			GLES20.glEnable(GLES20.GL_DEPTH_TEST);
			GLES20.glEnable(GLES20.GL_CULL_FACE);
			GLES20.glDisable(GLES20.GL_BLEND);
		}
	}

	public float getrAngle() {
		return rAngle;
	}

	public void setrAngle(float rAngle) {
		this.rAngle = rAngle;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	
}
