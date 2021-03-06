package ua.mintmalory.braintrainers.gameviews;

import java.util.Random;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.media.MediaPlayer;

import ua.mintmalory.braintrainers.GameDifficulty;
import ua.mintmalory.braintrainers.R;
import ua.mintmalory.braintrainers.StateSquareSprite;

public class LightsOffGameView extends AbstractGameView {
    /**
     * Image for cell with turned on bulb.
     */
    private Bitmap mLightOnBmp;
    /**
     * Image for cell with turned off bulb.
     */
    private Bitmap mLightOffBmp;

    /**
     * Field width/height which appropriates for beginner game difficulty.
     */
    private static final int FIELD_SIZE_BEGINNER = 5;
    /**
     * Field width/height which appropriates for advanced game difficulty.
     */
    private static final int FIELD_SIZE_ADVANCED = 6;
    /**
     * Field width/height which appropriates for expert game difficulty.
     */
    private static final int FIELD_SIZE_EXPERT = 7;

    /**
     * Array which represents game field.
     */
    private StateSquareSprite[][] mField;
    /**
     * Set of game sounds.
     */
    private MediaPlayer[] actionSounds;
    /**
     * Padding from left side of the screen. It's used for centring the field.
     */
    private int paddingLeftField;

    public LightsOffGameView(Context context, GameDifficulty difficulty) {
        super(context);
        int size = calcFieldSize(difficulty);
        mField = new StateSquareSprite[size][size];
        initPictures();
    }

    @Override
    protected boolean rebuildField(int row, int column) {
        for (int i = row - 1; i < mField.length && i <= row + 1; i++) {
            if (i >= 0) {
                invertValueOfCell(mField[column][i]);

            }
        }

        for (int i = column - 1; i < mField.length && i <= column + 1; i++) {
            if (i >= 0) {
                invertValueOfCell(mField[i][row]);

            }
        }

        invertValueOfCell(mField[column][row]);

        return true;
    }

    @Override
    protected boolean isGameOver() {
        return countOutedLights() == mField.length * mField.length;
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

    /**
     * Initializes images
     */
    private void initPictures() {
        mLightOnBmp = BitmapFactory.decodeResource(getResources(),
                R.drawable.bulb_on);
        mLightOffBmp = BitmapFactory.decodeResource(getResources(),
                R.drawable.bulb_off);
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
     * Calculates amount of turned off lights on the game field.
     *
     * @return amount of turned off lights on the game field.
     */
    private int countOutedLights() {
        int outed = 0;

        for (int i = 0; i < mField.length; i++)
            for (int j = 0; j < mField.length; j++) {
                if (!mField[i][j].isActive()) {
                    outed++;
                }
            }

        return outed;
    }

    /**
     * Changes state of the cell to the opposite.
     *
     * @param spr sprite of the cell, which is needed to be inverted.
     */
    private void invertValueOfCell(StateSquareSprite spr) {
        if (spr.isActive()) {
            spr.setState(mLightOffBmp, false);
        } else {
            spr.setState(mLightOnBmp, true);
        }
    }

    @Override
    protected void initField(int cellSize, int screenWidth, int screenHeight) {
        int fieldWidth = cellSize * mField.length;
        paddingLeftField = (screenWidth - fieldWidth) / 2;

        for (int i = 0; i < mField.length; i++)
            for (int j = 0; j < mField.length; j++) {
                mField[i][j] = new StateSquareSprite(mLightOffBmp, j * cellSize + paddingLeftField, i * cellSize + mPaddingTopField, cellSize, false);
            }

        Random rand = new Random();
        int counter;

        do {
            counter = rand.nextInt(100);
        } while (counter < 20);

        for (int iter = 0; iter <= counter; iter++) {
            int row = rand.nextInt(mField.length);
            int column = rand.nextInt(mField.length);

            for (int i = row - 1; i < mField.length && i <= row + 1; i++) {
                if (i >= 0) {
                    invertValueOfCell(mField[i][column]);
                }
            }

            for (int i = column - 1; i < mField.length && i <= column + 1; i++) {
                if (i >= 0) {
                    invertValueOfCell(mField[row][i]);
                }
            }

            invertValueOfCell(mField[row][column]);
        }

    }

    @Override
    protected int checkCellSize(int cellSize) {
        if (cellSize > mLightOffBmp.getWidth()) {
            cellSize = mLightOffBmp.getWidth();
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

        actionSounds[0] = MediaPlayer.create(getContext(), R.raw.tumbler_1);
        actionSounds[1] = MediaPlayer.create(getContext(), R.raw.tumbler_2);
        actionSounds[2] = MediaPlayer.create(getContext(), R.raw.tumbler_3);
    }

}
