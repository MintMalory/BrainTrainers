package ua.mintmalory.braintrainers;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Class represents simple square sprite.
 */
public class SquareSprite {
    /**
     * X coordinate of left top corner.
     */
    private int x;
    /**
     * Y coordinate of left top corner.
     */
    private int y;
    /**
     * Size of one side of the sprite.
     */
    private int size;
    /**
     * Background image of the sprite.
     */
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

    public void setBmp(Bitmap bmp) {
        this.bmp = Bitmap.createScaledBitmap(bmp, size, size, false);
    }

    public SquareSprite(Bitmap bmp, int x, int y, int size) {
        this.size = size;
        this.bmp = Bitmap.createScaledBitmap(bmp, size, size, false);
        this.x = x;
        this.y = y;
    }

    /**
     * Draws sprite on canvas.
     *
     * @param canvas canvas for drawing on.
     */
    public void onDraw(Canvas canvas) {
        canvas.drawBitmap(bmp, x, y, null);
    }

}
