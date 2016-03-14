package ua.mintmalory.braintrainers.gameviews;

import java.util.Random;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import ua.mintmalory.braintrainers.GameDifficulty;
import ua.mintmalory.braintrainers.R;
import ua.mintmalory.braintrainers.StateSquareSprite;

public class LightsOffGameView extends AbstractGameView {
    private Bitmap mLightOnBmp;
    private Bitmap mLightOffBmp;

    private static final int FIELD_SIZE_BEGINNER = 5;
    private static final int FIELD_SIZE_ADVANCED = 6;
    private static final int FIELD_SIZE_EXPERT = 7;

    private StateSquareSprite[][] mField;

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
	protected int getBeginnerFieldSize(){
		return FIELD_SIZE_BEGINNER;
	}
	
	@Override
	protected int getAdvancedFieldSize(){
		return FIELD_SIZE_ADVANCED;
	}
	
	@Override
	protected int getExpertFieldSize(){
		return FIELD_SIZE_EXPERT;
	}
	
	
    private void initPictures() {
        mLightOnBmp = BitmapFactory.decodeResource(getResources(),
                R.drawable.bulb_on);
        mLightOffBmp = BitmapFactory.decodeResource(getResources(),
                R.drawable.bulb_off);
    }


    @Override
    protected boolean isFieldTouched(float touchedY) {
        return (touchedY > PADDING_TOP_FIELD)
                && (touchedY < mField[mField.length - 1][mField.length - 1]
                .getY() + mField[mField.length - 1][mField.length - 1].getSize());
    }

    @Override
    protected int calcTouchedRow(float touchedX) {
        return (int) ((touchedX - mField[0][0].getX()) / mField[0][0].getSize());
    }

    @Override
    protected int calcTouchedColumn(float touchedY) {
        return (int) ((touchedY - mField[0][0].getY()) / mField[0][0].getSize());
    }

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

    private void invertValueOfCell(StateSquareSprite spr) {
        if (spr.isActive()) {
            spr.setState(mLightOffBmp, false);
        } else {
            spr.setState(mLightOnBmp, true);
        }
    }

    @Override
    protected void initField(int size) {
        for (int i = 0; i < mField.length; i++)
            for (int j = 0; j < mField.length; j++) {
                mField[i][j] = new StateSquareSprite(mLightOffBmp, i * size, j * size + PADDING_TOP_FIELD, size, false);
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
            cellSize= mLightOffBmp.getWidth();
        }

        return cellSize;
    }

}
