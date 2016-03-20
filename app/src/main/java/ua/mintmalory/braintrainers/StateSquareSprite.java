package ua.mintmalory.braintrainers;

import android.graphics.Bitmap;

/**
 * Class represents square sprite with binary state.
 */
public class StateSquareSprite extends SquareSprite {
    /**
     * State of the sprite.
     */
    private boolean active;

    public StateSquareSprite(Bitmap bmp, int x, int y, int size, boolean active) {
        super(bmp, x, y, size);
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }

    public void setState(Bitmap newBmp, boolean active) {
        setBmp(Bitmap.createScaledBitmap(newBmp, getSize(), getSize(), false));
        this.active = active;
    }

}
