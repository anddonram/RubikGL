package com.ejemplo.ejemplo;

import com.ejemplo.ejemplo.R.layout;
import com.ejemplo.rubik.MySurface;

import android.support.v7.app.ActionBarActivity;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

public class MainActivity extends ActionBarActivity {
	private MyGL mygl;
	private MySurface mySurface;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(mySurface = new MySurface(this));
		addContentView(getLayoutInflater().inflate(layout.main_layout, null),
				new LayoutParams(LayoutParams.WRAP_CONTENT,
						LayoutParams.WRAP_CONTENT));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected void onResume() {
		super.onResume();
		mySurface.onResume();
		try {
			mygl.onResume();
		} catch (NullPointerException e) {

		}

	}

	@Override
	protected void onPause() {
		super.onPause();
		mySurface.onPause();
		try {
			mygl.onPause();
		} catch (NullPointerException e) {

		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		mySurface.onTouchEvent(event);
		return super.onTouchEvent(event);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void giro(View view) {
		mySurface.rotar(view);
	}

	private class MyGL extends GLSurfaceView {
		private MyRenderer myRenderer;
		private RendererCuadrados rC;

		public MyGL(Context context) {
			super(context);
			setEGLContextClientVersion(2);
			myRenderer = new MyRenderer();
			setRenderer(myRenderer);
			setRenderMode(RENDERMODE_WHEN_DIRTY);
			// rC = new RendererCuadrados();
			// setRenderer(rC);
		}

		private float pX = 0;
		private float pY = 0;
		private static final float Scale_Factor = 180.0f / 320;

		@Override
		public boolean onTouchEvent(MotionEvent event) {
			if (myRenderer == null)
				return true;
			float x = event.getX();
			float y = event.getY();
			switch (event.getAction()) {
			case MotionEvent.ACTION_MOVE:
				float dx = x - pX;
				float dy = y - pY;
				myRenderer.nx += dx * Scale_Factor;
				myRenderer.ny += dy * Scale_Factor;
				myRenderer.setX(myRenderer.getX() + dx / getWidth());
				myRenderer.setY(myRenderer.getY() - dy / getWidth());
				if (y < getHeight() / 2) {
					dx = dx * -1;
				}
				if (x > getWidth() / 2) {
					dy = dy * -1;
				}
				myRenderer.setrAngle(myRenderer.getrAngle()
						+ ((dx + dy) * Scale_Factor));

				requestRender();
				break;
			case MotionEvent.ACTION_DOWN:
				queueEvent(new Runnable() {
					@Override
					public void run() {
						myRenderer.cambiarBlending();
					}
				});
				break;
			case MotionEvent.ACTION_UP:
				if (event.getX() > getWidth() * 4 / 5
						&& event.getY() > getHeight() * 4 / 5)
					;
			}
			pX = x;
			pY = y;
			return true;
		}

	}
}
