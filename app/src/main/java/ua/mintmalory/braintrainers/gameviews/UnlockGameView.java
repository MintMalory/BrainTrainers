package ua.mintmalory.braintrainers.gameviews;

import java.util.LinkedHashMap;
import java.util.Random;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.media.MediaPlayer;

import ua.mintmalory.braintrainers.GameDifficulty;
import ua.mintmalory.braintrainers.R;
import ua.mintmalory.braintrainers.StateSquareSprite;

public class UnlockGameView extends AbstractGameView {
    /**
     * Image for cell with locked lock.
     */
    private Bitmap mLockedBmp;
    /**
     * Image for cell with unlocked lock.
     */
    private Bitmap mUnlockedBmp;

    /**
     * Field width/height which appropriates for beginner game difficulty.
     */
    private static final int FIELD_SIZE_BEGINNER = 4;
    /**
     * Field width/height which appropriates for advanced game difficulty.
     */
    private static final int FIELD_SIZE_ADVANCED = 6;
    /**
     * Field width/height which appropriates for expert game difficulty.
     */
    private static final int FIELD_SIZE_EXPERT = 8;

    /**
     * Padding from left side of the screen. It's used for centring the field.
     */
    private int paddingLeftField;

    /**
     * Array which represents game field.
     */
    private StateSquareSprite[][] mField;

    /**
     * Set of game sounds.
     */
    private MediaPlayer[] actionSounds;

    public UnlockGameView(Context context, GameDifficulty difficulty) {
        super(context);
        int size = calcFieldSize(difficulty);
        mField = new StateSquareSprite[size][size];
        initPictures();
        LinkedHashMap h;
    }

    @Override
    protected boolean rebuildField(int row, int column) {
        for (int i = 0; i < mField.length; i++) {
            invertValueOfCell(mField[i][row]);
        }

        for (int i = 0; i < mField.length; i++) {
            invertValueOfCell(mField[column][i]);
        }

        invertValueOfCell(mField[column][row]);
        return true;
    }

    @Override
    protected boolean isGameOver() {
        return countOpenedCells() == mField.length * mField.length;
    }


    @Override
    protected void drawField(Canvas canvas) {
        for (int i = 0; i < mField.length; i++)
            for (int j = 0; j < mField.length; j++) {
                mField[i][j].onDraw(canvas);
            }
    }


    @Override
    protected int getFieldSize() {
        if (mField != null) {
            return mField.length;
        }

        return -1;
    }

    @Override
    protected int getBeginnerFieldSize() {
        return FIELD_SIZE_BEGINNER;
    }

    @Override
    protected int getAdvancedFieldSize() {
        return FIELD_SIZE_ADVANCED;
    }

    @Override
    protected int getExpertFieldSize() {
        return FIELD_SIZE_EXPERT;
    }

    private void initPictures() {
        mLockedBmp = BitmapFactory.decodeResource(getResources(), R.drawable.x);
        mUnlockedBmp = BitmapFactory.decodeResource(getResources(), R.drawable.o);
    }

    @Override
    protected boolean isFieldTouched(float touchedY, float touchedX) {
        boolean vertical = (touchedY > mField[0][0].getY())
                && (touchedY < (mField[mField.length - 1][mField.length - 1]
                .getY() + mField[mField.length - 1][mField.length - 1].getSize()));

        boolean horizontal = (touchedX > mField[0][0].getX())
                && (touchedX < (mField[0][mField.length - 1]
                .getX() + mField[0][0].getSize()));

        return vertical && horizontal;
    }

    @Override
    protected int calcTouchedRow(float touchedX) {
        return (int) ((touchedX - mField[0][0].getX()) / mField[0][0].getSize());
    }

    @Override
    protected int calcTouchedColumn(float touchedY) {
        return (int) ((touchedY - mField[0][0].getY()) / mField[0][0].getSize());
    }


    /**
     * Calculates amount of unlocked locks on the game field.
     *
     * @return amount of unlocked locks on the game field.
     */
    private int countOpenedCells() {
        int opened = 0;

        for (int i = 0; i < mField.length; i++)
            for (int j = 0; j < mField.length; j++) {
                if (mField[i][j].isActive()) {
                    opened++;
                }
            }

        return opened;
    }

    /**
     * Changes state of the cell to the opposite.
     *
     * @param spr sprite of the cell, which is needed to be inverted.
     */
    private void invertValueOfCell(StateSquareSprite spr) {
        if (spr.isActive()) {
            spr.setState(mLockedBmp, false);
        } else {
            spr.setState(mUnlockedBmp, true);
        }
    }

    @Override
    protected void initField(int cellSize, int screenWidth, int screenHeight) {
        int fieldWidth = cellSize * mField.length;
        paddingLeftField = (screenWidth - fieldWidth) / 2;


        Random rand = new Random();
        for (int i = 0; i < mField.length; i++)
            for (int j = 0; j < mField.length; j++) {
                if (rand.nextBoolean() == true) {
                    mField[i][j] = new StateSquareSprite(mUnlockedBmp, j * cellSize + paddingLeftField, i * cellSize
                            + mPaddingTopField, cellSize, true);
                } else {
                    mField[i][j] = new StateSquareSprite(mLockedBmp, j * cellSize + paddingLeftField, i * cellSize
                            + mPaddingTopField, cellSize, false);
                }
            }
    }

    @Override
    protected int checkCellSize(int cellSize) {
        if (cellSize > mUnlockedBmp.getWidth()) {
            cellSize = mUnlockedBmp.getWidth();
        }
        return cellSize;
    }

    @Override
    protected void playSound() {
        Random r = new Random();
        actionSounds[r.nextInt(actionSounds.length)].start();
    }

    @Override
    protected void initActionSounds() {
        actionSounds = new MediaPlayer[3];

        actionSounds[0] = MediaPlayer.create(getContext(), R.raw.lock_1);
        actionSounds[1] = MediaPlayer.create(getContext(), R.raw.lock_2);
        actionSounds[2] = MediaPlayer.create(getContext(), R.raw.lock_3);
    }
}
