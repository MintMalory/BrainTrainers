package ua.mintmalory.braintrainers;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;

public class SquareSprite {
	private int x;
	private int y;
	private int size;
	private Bitmap bmp;

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getSize() {
		return size;
	}

	public void setBmp(Bitmap bmp){
		this.bmp = Bitmap.createScaledBitmap(bmp, size, size, false);
	}

	public  SquareSprite(Bitmap bmp, int x, int y, int size) {
		this.size = size;
		this.bmp = Bitmap.createScaledBitmap(bmp, size, size, false);
		this.x = x;
		this.y = y;
	}


	public void onDraw(Canvas canvas) {
		canvas.drawBitmap(bmp, x, y, null);
	}
	
}
