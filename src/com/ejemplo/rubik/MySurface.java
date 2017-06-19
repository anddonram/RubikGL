package com.ejemplo.rubik;

import com.ejemplo.ejemplo.R.id;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;
import android.view.View;

public class MySurface extends GLSurfaceView {
	private Hilo hilo;

	public MySurface(Context context) {
		super(context);
		hilo = new Hilo(this);
		setEGLContextClientVersion(2);
		setRenderer(new CRenderer());
		setRenderMode(RENDERMODE_WHEN_DIRTY);
		hilo.start();
	}

	@Override
	public void onPause() {
		hilo.fin = true;
		try {
			hilo.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		super.onPause();
	}

	@Override
	public void onResume() {
		if (!hilo.isAlive()) {
			hilo = new Hilo(this);
			hilo.start();
		}
		super.onResume();
	}

	private float x = 0;
	private float y = 0;

	@Override
	public boolean onTouchEvent(MotionEvent e) {
		switch (e.getAction()) {
		case MotionEvent.ACTION_MOVE:
			hilo.x = (e.getX() - x)/2;
			hilo.y = (e.getY() - y)/2;
			hilo.rotar = true;
		}
		x = e.getX();
		y = e.getY();
		return super.onTouchEvent(e);
	}

	public void rotar(View view) {
		switch (view.getId()) {
		case id.button0:
			hilo.caraRotada = 0;
			break;
		case id.button1:
			hilo.caraRotada = 1;
			break;
		case id.button2:
			hilo.caraRotada = 2;
			break;
		case id.button3:
			hilo.caraRotada = 3;
			break;
		case id.button4:
			hilo.caraRotada = 4;
			break;
		case id.button5:
			hilo.caraRotada = 5;
			break;
		}
		hilo.clicked = true;
	}
}
