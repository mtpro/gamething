package proetsch.mygame;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class MyGameSurface extends SurfaceView implements SurfaceHolder.Callback {
	
	SurfaceHolder SHolder;
	Engine engine;
	
	public MyGameSurface(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		// We don't want to interface with the Surface directly
		// We will get its Holder then tell it that we would
		// like to receive the SurfaceHolder cb's (changed, created, ~'d)
		SHolder = getHolder();
		SHolder.addCallback(this);
		
		engine = new Engine(SHolder, context);
	}
	
	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		engine.resizeEvent(arg0.getSurfaceFrame());
		
	}

	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		// TODO Auto-generated method stub
		engine.setRunning(true);
		engine.start();
		
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public boolean onTouchEvent (MotionEvent event) {
		engine.touchEvent(event);
		return true;
	}
}
