package proetsch.mygame;

import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;

public class Ship extends BitmapDrawable {
	
	// Keep track of intrinsic properties of child bitmap
	private int intrinsicWidth;
	private int intrinsicHeight;
	
	// Velocity in the x-direction
	private int v_x;
	// Velocity in the y-direction
	private int v_y;
	
	public Ship(Resources res, int resID) {
		
		super(res, BitmapFactory.decodeResource(res, resID));
		intrinsicWidth = getIntrinsicWidth();
		intrinsicHeight = getIntrinsicHeight();
		
		this.setBounds(0, 0, intrinsicWidth, intrinsicHeight);
		
	}
	
	public void move(int dx, int dy, Rect bounds) {
		Rect oldBounds = getBounds();
		
		int newLeft = oldBounds.left+dx;
		int newTop = oldBounds.top+dy;
		int newRight = oldBounds.right+dx;
		int newBottom = oldBounds.bottom+dy;
		
		// setBounds takes left, top, right, bottom
		
		if (newLeft <= bounds.left ||
				newTop <= bounds.top ||
				newRight >= bounds.right ||
				newBottom >= bounds.bottom) {
			// Top-left
			if (newLeft <= bounds.left && newTop <= bounds.top) {
				setBounds(bounds.left, bounds.top, bounds.left + intrinsicWidth, bounds.top + intrinsicHeight);
			}
			// Bottom-left
			else if (newLeft <= bounds.left && newBottom >= bounds.bottom) {
				setBounds(bounds.left, bounds.bottom - intrinsicHeight, bounds.left + intrinsicWidth, bounds.bottom);
			}
			// Bottom-right
			else if (newRight >= bounds.right && newBottom >= bounds.bottom) {
				setBounds(bounds.right - intrinsicWidth, bounds.bottom - intrinsicHeight, bounds.right, bounds.bottom);
			}
			// Top-right
			else if (newRight >= bounds.right && newTop <= bounds.top){
				setBounds(bounds.right - intrinsicWidth, bounds.top, bounds.right, bounds.top + intrinsicHeight);
			}
			// Left side
			else if (newLeft < bounds.left) {
				setBounds(bounds.left, newTop, bounds.left + intrinsicWidth, newBottom);
			}
			// Right side
			else if (newRight >= bounds.right) {
				setBounds(bounds.right - intrinsicWidth, newTop, bounds.right, newBottom);
			}
			// Top side
			else if (newTop <= bounds.top) {
				setBounds(newLeft, bounds.top, newRight, bounds.top+intrinsicHeight);
			}
			// Bottom side
			else if (newBottom >= bounds.bottom) {
				setBounds(newLeft, bounds.bottom - intrinsicHeight, newRight, bounds.bottom);
			}
		}
		else
			setBounds(newLeft, newTop, newRight, newBottom);

	}
}