package proetsch.mygame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.widget.Toast;

public class Engine extends Thread {
	
	// Base rate (in milliseconds) for determining how quickly certain things occur
	//  e.g. updating background image to simulate movement
	private final int RATE = 100;
	
	// How many pixels go by every RATE milliseconds for the background
	// Background will proceed by this many pixels every 1/3rd of a second
	private final int backgroundRate = 3;
	
	// Handle to the SurfaceHolder that contains the Canvas we will paint to
	private SurfaceHolder SHolder;
	
	// Handle to the current Context
	private Context context;
	
	// Hold screen width and height
	private Rect dimensionRect;
	
	// Background image
	private Bitmap background;
	
	// Portion of the background image we are currently showing
	private Rect backgroundRect;
	
	// Player's ship
	private Ship ship;
	
	// Are we running?
	private boolean running;
	
	// Does the user currently have a finger on the screen?
	private boolean fingerDown;
	
	// ID of the active pointer
	private int pointerId;
	
	// Previous pointer position
	private int oldX;
	private int oldY;
	
	// Keep track of change in time
	private long dTime;
	
	// Keep track of when the game started
	private long startTime;
	
	public Engine(SurfaceHolder sHolder, Context c) {
		SHolder = sHolder;
		context = c;
		dimensionRect = SHolder.getSurfaceFrame();
		background = BitmapFactory.decodeResource(c.getResources(), R.drawable.background_lens_flare_2);
		backgroundRect = new Rect();
		ship = new Ship(context.getResources(), R.drawable.ship);
		ship.setBounds(45, 45, 45+(ship.getIntrinsicWidth()), 45+(ship.getIntrinsicHeight()));
		running = false;
		fingerDown = false;
		startTime = System.currentTimeMillis();
	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
		startTime = System.currentTimeMillis();
	}
	
	// Screen size has changed (probably orientation)
	public void resizeEvent(Rect newDimensions) {
		dimensionRect = newDimensions;
	}
	
	public void doDraw(Canvas canvas) {
		canvas.drawBitmap(background, backgroundRect, dimensionRect, null);
		ship.draw(canvas);
	}
	
	/** called as often as possible by this thread
	 *  the idea is to use time deltas to simulate a realistic environment
	 *  with regards to moving the background bitmaps and enemies
	 */
	public void modifyGameState() {
		// Update the background bitmap to simulate movement
		dTime = (int) ((System.currentTimeMillis() - startTime));
		backgroundRect.set(	(int) dTime/RATE * backgroundRate,
							0,
							(int) dTime/RATE * backgroundRate + dimensionRect.right,
							background.getHeight());
	}
	
	public void touchEvent(MotionEvent event) {
		int eventType = event.getActionMasked();
		
		if (eventType == MotionEvent.ACTION_DOWN && fingerDown == false) {
			
			fingerDown = true;
			pointerId = event.getPointerId(event.getActionIndex());
			
			oldX = (int) event.getX(event.findPointerIndex(pointerId));
			oldY = (int) event.getY(event.findPointerIndex(pointerId));
		}
		if (eventType == MotionEvent.ACTION_UP) {
			
			fingerDown = false;
			
		}
		if (eventType == MotionEvent.ACTION_MOVE &&
				event.getActionIndex() == pointerId) {
			int pIndex = event.findPointerIndex(pointerId);
			
			// TODO: Fix this dirty try/catch! When main pointer goes down,
			//  and secondary goes down, and main comes up, program crashes...
			try {
				int newX;
				int newY;
				for (int i = 0; i < event.getHistorySize(); ++i) {
					newX = (int) event.getHistoricalX(pIndex, i);
					newY = (int) event.getHistoricalY(pIndex, i);
					
					// Move the player based on the touch
					ship.move(newX - oldX, newY - oldY, dimensionRect);
					
					oldX = newX;
					oldY = newY;
				}
				
				newX = (int) event.getX(pIndex);
				newY = (int) event.getY(pIndex);
				ship.move(newX - oldX, newY - oldY, dimensionRect);
				
				oldX = newX;
				oldY = newY;
				
				}
			

			catch (IllegalArgumentException e) {
				Toast.makeText(context, "I can't find a pointer with that ID", Toast.LENGTH_SHORT).show();
				pointerId = event.getPointerId(event.getActionIndex());
			}
		}
	}
	
	@Override
	public void run() {
		while (running) {
			Canvas canvas = null;
			try {
				/**** change state ****/
				modifyGameState();
				
				canvas = SHolder.lockCanvas();
				synchronized (SHolder) {
					/**** draw things ****/
					try {
					doDraw(canvas);
					} catch (NullPointerException e) {
						continue;
					}
				}
			} finally {
				if (canvas != null)
				SHolder.unlockCanvasAndPost(canvas);
			}
		}
	}

}
