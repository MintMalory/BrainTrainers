package ua.mintmalory.braintrainers;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Class represents square sprite with number.
 */
public class NumberedSquareSprite extends SquareSprite {
    /**
     * Number of sprite.
     */
    private int number;

    public int getNumber() {
        return number;
    }

    public NumberedSquareSprite(Bitmap bmp, int x, int y, int size, int number) {
        super(bmp, x, y, size);
        this.number = number;
    }

    public void onDraw(Canvas canvas, Paint paint) {
        onDraw(canvas);

        canvas.drawText(String.valueOf(number), getX() + getSize() / 2, getY() + getSize() / 4
                * 3, paint);
    }
}