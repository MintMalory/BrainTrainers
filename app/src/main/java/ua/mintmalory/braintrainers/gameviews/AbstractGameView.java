package ua.mintmalory.braintrainers.gameviews;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import ua.mintmalory.braintrainers.ChalkFontHolder;
import ua.mintmalory.braintrainers.GameDifficulty;
import ua.mintmalory.braintrainers.R;
import ua.mintmalory.braintrainers.SoundAndMusicOptionsState;
import ua.mintmalory.braintrainers.State;

/**
 * Class represents custom view for game with square field.
 */
public abstract class AbstractGameView extends SurfaceView implements
        SurfaceHolder.Callback, DialogInterface.OnClickListener {
    /**
     * Padding from the top of the screen. Used for positioning of the game field.
     */
    public final int mPaddingTopField;

    /**
     * Sound for winning alert dialog.
     */
    private MediaPlayer winSound;

    /**
     * Title for winning alert dialog.
     */
    private String mWinMessageTitle;
    /**
     * Text of winning alert dialog.
     */
    private String mWinMessageText;
    /**
     * Title of "Another game" button on winning alert dialog
     */
    private String mChooseAnotherGameButtonTitle;
    /**
     * Title of "Play again" button on winning alert dialog
     */
    private String mPlayAgainButtonTitle;

    private SurfaceHolder mHolder;
    private Context mCurrentContext;

    /**
     * Padding from left side of the screen. It's used for centring the string with information about made game moves.
     */
    private int mPaddingLeftText;
    /**
     * Padding from the top of the screen. Used for positioning of the string with information about made game moves.
     */
    private int mPaddingTopText;

    /**
     * Variable for holding title "Moves:".
     */
    private String mMovesTitle;
    /**
     * Holds amount of moves, made by user during the game.
     */
    private int mAmountOfMoves;

    /**
     * Settings of the brush for writing information about made moves.
     */
    private Paint mPaint;

    public AbstractGameView(Context context) {
        super(context);
        initTitles();
        mAmountOfMoves = 0;
        mCurrentContext = context;
        mMovesTitle = getResources().getString(R.string.amount_of_moves);
        winSound = MediaPlayer.create(context, R.raw.win);
        mHolder = getHolder();
        mHolder.addCallback(this);
        initPaintSettings();
        initActionSounds();
        mPaddingTopField = (int) context.getResources().getDimension(R.dimen.padding_top_field);
    }

    /**
     * Method for initializing fields from string resources.
     */
    private void initTitles() {
        mWinMessageTitle = getResources().getString(R.string.win_title);
        mWinMessageText = getResources().getString(R.string.win_message);
        mChooseAnotherGameButtonTitle = getResources().getString(
                R.string.choose_another_game_title);
        mPlayAgainButtonTitle = getResources().getString(
                R.string.play_again_title);
    }

    /**
     * Initializes brush for writing information about made moves.
     */
    protected void initPaintSettings() {
        mPaint = new Paint();
        mPaint.setColor(Color.WHITE);
        mPaint.setTypeface(getCustomTypeface());
        mPaint.setTextAlign(Paint.Align.CENTER);

        int fontSize = (int) mCurrentContext.getResources().getDimension(R.dimen.moves_text_size);
        mPaint.setTextSize(fontSize);
    }


    /**
     * Depends on the chosen game difficulty, selects width of the field (amount of cells on the field).
     *
     * @param difficulty chosen game difficulty
     * @return width/height of the game field in cells
     */
    protected int calcFieldSize(GameDifficulty difficulty) {
        if (difficulty == GameDifficulty.BEGINNER) {
            return getBeginnerFieldSize();
        }

        if (difficulty == GameDifficulty.EXPERT) {
            return getExpertFieldSize();
        }

        return getAdvancedFieldSize();
    }

    /**
     * Returns custom typeface, called ChalkFont
     *
     * @return custom Typeface
     */
    protected Typeface getCustomTypeface() {
        Activity parentActivity = (Activity) getContext();
        return ChalkFontHolder.getChalkFont(parentActivity);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
        int cellSize = width / getFieldSize();
        cellSize = checkCellSize(cellSize);
        initField(cellSize, width, height);
        calcPaddingForText(width, height, cellSize);
        refreshField();
    }


    /**
     * Calculates paddings for displaying amount of made moves.
     *
     * @param screenWidth  width of the phone screen
     * @param screenHeight height of the phone screen
     * @param cellSize     width/height of one cell on the game field
     */
    protected void calcPaddingForText(int screenWidth, int screenHeight,
                                      int cellSize) {
        mPaddingTopText = (screenHeight - (mPaddingTopField + cellSize
                * getFieldSize()))
                / 2 + mPaddingTopField + cellSize * getFieldSize();
        mPaddingLeftText = screenWidth / 2;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isFieldTouched(event.getY(), event.getX())) {
            int column = calcTouchedColumn(event.getY());
            int row = calcTouchedRow(event.getX());

            if (rebuildField(row, column)) {
                increaseAmountOfMoves();

                if (SoundAndMusicOptionsState.sound == State.ON) {
                    playSound();
                }
                refreshField();
            }
        }

        if (isGameOver()) {
            showWinMessage();
        }

        return super.onTouchEvent(event);
    }

    /**
     * Increases amount of made moves by 1.
     */
    protected void increaseAmountOfMoves() {
        mAmountOfMoves++;
    }

    /**
     * Redraws game field.
     */
    protected void refreshField() {
        Canvas c = mHolder.lockCanvas();
        onDraw(c);
        mHolder.unlockCanvasAndPost(c);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(0, PorterDuff.Mode.CLEAR);
        drawField(canvas);
        canvas.drawText(mMovesTitle + mAmountOfMoves, mPaddingLeftText, mPaddingTopText, mPaint);
    }

    /**
     * Shows winning message to user.
     */
    protected void showWinMessage() {
        mHolder.lockCanvas();

        winSound.start();

        AlertDialog.Builder builder = new AlertDialog.Builder(mCurrentContext);
        builder.setTitle(mWinMessageTitle);
        builder.setMessage(mWinMessageText + "  " + mAmountOfMoves);
        builder.setIcon(R.drawable.cup);
        builder.setCancelable(false);
        builder.setNeutralButton(mChooseAnotherGameButtonTitle, this);
        builder.setPositiveButton(mPlayAgainButtonTitle, this);

        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onClick(DialogInterface dialog, int id) {
        if (id == DialogInterface.BUTTON_POSITIVE) {
            dialog.cancel();
            Activity parentActivity = (Activity) getContext();
            Intent intent = parentActivity.getIntent();
            parentActivity.finish();
            parentActivity.startActivity(intent);
            return;
        }

        if (id == DialogInterface.BUTTON_NEUTRAL) {
            dialog.cancel();
            Activity parentActivity = (Activity) getContext();
            parentActivity.onBackPressed();
            return;
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
    }

    /**
     * Rebuilds game field, based on the information about position of touched field cell and rules of the concrete game.
     *
     * @param row    number of row, in which touched cell is situated
     * @param column number of column, in which touched cell is situated
     * @return {@code true} if game field changed, and {@code false} otherwise.
     */
    protected abstract boolean rebuildField(int row, int column);

    /**
     * Checks if game is over.
     *
     * @return {@code true} if game is finished, and {@code false} otherwise.
     */
    protected abstract boolean isGameOver();

    /**
     * Checks if user touched game field area.
     *
     * @param touchedY Y coordinate of touch point
     * @param touchedX X coordinate of touch point
     * @return {@code true} if field was touched, and {@code false} otherwise.
     */
    protected abstract boolean isFieldTouched(float touchedY, float touchedX);

    /**
     * Calculates row number of touched cell.
     *
     * @param touchedX X coordinate of touch point
     * @return row number of touched cell
     */
    protected abstract int calcTouchedRow(float touchedX);

    /**
     * Calculates column number of touched cell.
     *
     * @param touchedY Y coordinate of touch point
     * @return column number of touched cell
     */
    protected abstract int calcTouchedColumn(float touchedY);

    /**
     * Returns current field width/height.
     *
     * @return current field width/height.
     */
    protected abstract int getFieldSize();

    /**
     * Returns field width/height which appropriates for beginner game difficulty.
     *
     * @return field width/height.
     */
    protected abstract int getBeginnerFieldSize();

    /**
     * Returns field width/height which appropriates for advanced game difficulty.
     *
     * @return field width/height.
     */
    protected abstract int getAdvancedFieldSize();

    /**
     * Returns field width/height which appropriates for expert game difficulty.
     *
     * @return field width/height.
     */
    protected abstract int getExpertFieldSize();

    /**
     * Draws game field on the canvas.
     *
     * @param canvas canvas for drawing on.
     */
    protected abstract void drawField(Canvas canvas);

    /**
     * Initializes game field.
     *
     * @param cellSize     size of one cell of the field
     * @param screenWidth  width of the phone screen
     * @param screenHeight height of the phone screen
     */
    protected abstract void initField(int cellSize, int screenWidth, int screenHeight);

    /**
     * Compares calculated cell width (value based on the phone screen size) to the width of image for cell from resources.
     * And chooses smallest value from them for future use.
     *
     * @param cellSize cell size, based on the phone screen size.
     * @return the final value of the cell width.
     */
    protected abstract int checkCellSize(int cellSize);

    /**
     * Plays game sounds.
     */
    protected abstract void playSound();

    /**
     * Initializes game sounds.
     */
    protected abstract void initActionSounds();
}
