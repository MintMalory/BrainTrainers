package ua.mintmalory.braintrainers.gameviews;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;

import ua.mintmalory.braintrainers.GameDifficulty;
import ua.mintmalory.braintrainers.NumberedSquareSprite;
import ua.mintmalory.braintrainers.R;

public class NPuzzleGameView extends AbstractGameView {
    /**
     * Image for cell with number.
     */
    private Bitmap mCellBmp;
    /**
     * Image for empty cell.
     */
    private Bitmap mEmptyCellBmp;

    /**
     * Field width/height which appropriates for beginner game difficulty.
     */
    private static final int FIELD_SIZE_BEGINNER = 3;
    /**
     * Field width/height which appropriates for advanced game difficulty.
     */
    private static final int FIELD_SIZE_ADVANCED = 4;
    /**
     * Field width/height which appropriates for expert game difficulty.
     */
    private static final int FIELD_SIZE_EXPERT = 5;

    /**
     * Number of column in which empty cell is situated.
     */
    private int mEmptyCellColumn;
    /**
     * Number of row in which empty cell is situated.
     */
    private int mEmptyCellRow;

    /**
     * Array which represents game field.
     */
    private NumberedSquareSprite[][] mField;

    /**
     * Settings of the brush for writing numbers on tiles, that are in place.
     */
    private Paint mPaintTextCellsInPlace;
    /**
     * Settings of the brush for writing numbers on tiles, that are out of place.
     */
    private Paint mPaintTextCellsOutOfPlace;
    /**
     * Set of game sounds.
     */
    private MediaPlayer actionSounds[];
    /**
     * Padding from left side of the screen. It's used for centring the field.
     */
    private int paddingLeftField;

    public NPuzzleGameView(Context context, GameDifficulty difficulty) {
        super(context);
        initPaintSettings();
        int size = calcFieldSize(difficulty);
        mField = new NumberedSquareSprite[size][size];
        initPictures();
    }

    @Override
    protected boolean rebuildField(int row, int column) {
        if (isEmptyCellsNear(row, column)) {
            swapCells(row, column);
            return true;
        }

        return false;
    }

    @Override
    protected boolean isGameOver() {
        return countCellsInPlace() == mField.length * mField.length;
    }

    @Override
    protected void drawField(Canvas canvas) {
        for (int i = 0; i < mField.length; i++)
            for (int j = 0; j < mField.length; j++) {
                if (mField[i][j].getNumber() == mField.length * mField.length) {
                    mField[i][j].onDraw(canvas);
                } else if (mField[i][j].getNumber() == i * mField.length + j
                        + 1) {
                    mField[i][j].onDraw(canvas, mPaintTextCellsInPlace);
                } else {
                    mField[i][j].onDraw(canvas, mPaintTextCellsOutOfPlace);
                }

            }
    }

    /**
     * Sets font size for writing number on tiles.
     *
     * @param size font size.
     */
    private void setCellsTextSize(float size) {
        mPaintTextCellsInPlace.setTextSize(size);
        mPaintTextCellsOutOfPlace.setTextSize(size);
    }

    /**
     * Checks if {@code cell[row][column]} is near empty cell. Tiles are near, if they have one common side.
     *
     * @param row    number of row, in which checked cell is situated
     * @param column number of column, in which checked cell is situated
     * @return {@code true} if checked tiles are near, and {@code false} otherwise.
     */
    private boolean isEmptyCellsNear(int row, int column) {
        return ((mEmptyCellColumn + 1 == column) && (mEmptyCellRow == row))
                || ((mEmptyCellColumn - 1 == column) && (mEmptyCellRow == row))
                || ((mEmptyCellColumn == column) && (mEmptyCellRow + 1 == row))
                || ((mEmptyCellColumn == column) && (mEmptyCellRow - 1 == row));
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
     * Initializes images for tiles.
     */
    private void initPictures() {
        mCellBmp = BitmapFactory
                .decodeResource(getResources(), R.drawable.cell);
        mEmptyCellBmp = BitmapFactory.decodeResource(getResources(),
                R.drawable.empty_cell);
    }

    @Override
    protected void initPaintSettings() {
        super.initPaintSettings();

        mPaintTextCellsInPlace = new Paint();
        mPaintTextCellsInPlace.setColor(getResources().getColor(
                R.color.orange_color));
        mPaintTextCellsInPlace.setTypeface(getCustomTypeface());
        mPaintTextCellsInPlace.setTextAlign(Paint.Align.CENTER);
        mPaintTextCellsOutOfPlace = new Paint(mPaintTextCellsInPlace);
        mPaintTextCellsOutOfPlace.setColor(Color.WHITE);
    }

    /**
     * Calculates amount of tiles in place on the game field.
     *
     * @return amount of tiles in place on the game field.
     */
    private int countCellsInPlace() {
        int inPlace = 0;

        for (int i = 0; i < mField.length; i++)
            for (int j = 0; j < mField.length; j++) {
                if (mField[i][j].getNumber() == i * mField.length + j + 1) {
                    inPlace++;
                }
            }

        return inPlace;
    }

    /**
     * Swaps {@code cell[row][column]} with empty cell.
     *
     * @param row    row in which cell to swap is situated.
     * @param column column in which cell to swap is situated.
     */
    private void swapCells(int row, int column) {
        NumberedSquareSprite tmp = mField[column][row];
        mField[column][row] = mField[mEmptyCellColumn][mEmptyCellRow];
        mField[mEmptyCellColumn][mEmptyCellRow] = tmp;

        int tmpX;
        int tmpY;

        tmpX = mField[column][row].getX();
        tmpY = mField[column][row].getY();
        mField[column][row].setX(mField[mEmptyCellColumn][mEmptyCellRow]
                .getX());
        mField[column][row].setY(mField[mEmptyCellColumn][mEmptyCellRow]
                .getY());
        mField[mEmptyCellColumn][mEmptyCellRow].setX(tmpX);
        mField[mEmptyCellColumn][mEmptyCellRow].setY(tmpY);

        mEmptyCellColumn = column;
        mEmptyCellRow = row;
    }

    /**
     * Checks if combination of shuffled tiles has a solution according to game's rules.
     *
     * @param arr combination of shuffled tiles.
     * @return {@code true} if combination can be solved, and {@code false} otherwise.
     */
    private boolean isSolution(List<Integer> arr) {
        int tempSum = 0;
        int sum = 0;

        for (int i = 0; i < arr.size() - 1; i++) {
            inner:
            for (int j = i + 1; j < arr.size(); j++) {
                if (arr.get(i) == mField.length * mField.length) {
                    mEmptyCellRow = i % mField.length;
                    mEmptyCellColumn = i / mField.length;
                    if (mField.length % 2 == 0) {
                        tempSum = mEmptyCellRow + 1;
                    }

                    break inner;
                }

                if (arr.get(i) > arr.get(j)) {
                    tempSum++;
                }
            }
            sum = sum + tempSum;
            tempSum = 0;
        }

        if (sum % 2 == 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void initField(int cellSize, int screenWidth, int screenHeight) {
        int fieldWidth = cellSize * mField.length;
        paddingLeftField = (screenWidth - fieldWidth) / 2;

        int lastCellNumber = mField.length * mField.length;
        List<Integer> arrTmp = new ArrayList<Integer>(lastCellNumber);

        for (int i = 1; i <= lastCellNumber; i++) {
            arrTmp.add(i);
        }

        do {
            Collections.shuffle(arrTmp);
        } while (!isSolution(arrTmp));

        for (int i = 0; i < mField.length; i++)
            for (int j = 0; j < mField.length; j++) {
                if (arrTmp.get(i * mField.length + j) != lastCellNumber) {
                    mField[i][j] = new NumberedSquareSprite(mCellBmp,
                            j * cellSize + paddingLeftField, i * cellSize + mPaddingTopField, cellSize, arrTmp.get(i
                            * mField.length + j));
                } else {
                    mField[i][j] = new NumberedSquareSprite(mEmptyCellBmp, j * cellSize + paddingLeftField, i
                            * cellSize + mPaddingTopField, cellSize,
                            arrTmp.get(i * mField.length + j));
                }
            }
    }

    @Override
    protected int checkCellSize(int cellSize) {
        if (cellSize > mCellBmp.getWidth()) {
            cellSize = mCellBmp.getWidth();
        }

        setCellsTextSize((float) (cellSize * 0.5));

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

        actionSounds[0] = MediaPlayer.create(getContext(), R.raw.stone_1);
        actionSounds[1] = MediaPlayer.create(getContext(), R.raw.stone_2);
        actionSounds[2] = MediaPlayer.create(getContext(), R.raw.stone_3);
    }
}
