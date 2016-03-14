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
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import ua.mintmalory.braintrainers.ChalkFontHolder;
import ua.mintmalory.braintrainers.GameDifficulty;
import ua.mintmalory.braintrainers.R;

public abstract class AbstractGameView extends SurfaceView implements
        SurfaceHolder.Callback, DialogInterface.OnClickListener {
    public static final int PADDING_TOP_FIELD = 40;

    private String mWinMessageTitle;
    private String mWinMessageText;
    private String mChooseAnotherGameButtonTitle;
    private String mPlayAgainButtonTitle;

    private SurfaceHolder mHolder;
    private Context mCurrentContext;

    private int mPaddingLeftText;
    private int mPaddingTopText;

    private String mMovesTitle;

    private int mAmountOfMoves;
    private Paint mPaint;

    public AbstractGameView(Context context) {
        super(context);
        initTitles();
        mAmountOfMoves = 0;
        mCurrentContext = context;
        mMovesTitle = getResources().getString(R.string.amount_of_moves);
        mHolder = getHolder();
        mHolder.addCallback(this);
        initPaintSettings();
        initActionSounds();
    }

    private void initTitles() {
        mWinMessageTitle = getResources().getString(R.string.win_title);
        mWinMessageText = getResources().getString(R.string.win_message);
        mChooseAnotherGameButtonTitle = getResources().getString(
                R.string.choose_another_game_title);
        mPlayAgainButtonTitle = getResources().getString(
                R.string.play_again_title);
    }

    protected void initPaintSettings() {
        mPaint = new Paint();
        mPaint.setColor(Color.WHITE);
        mPaint.setTypeface(getTypeface());
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setTextSize(30);
    }

	
    protected int calcFieldSize(GameDifficulty difficulty) {
        if (difficulty == GameDifficulty.BEGINNER) {
            return getBeginnerFieldSize();
        }

        if (difficulty == GameDifficulty.EXPERT) {
            return getExpertFieldSize();
        }

        return getAdvancedFieldSize();
    }
	
    protected Typeface getTypeface() {
        Activity parentActivity = (Activity) getContext();
        return ChalkFontHolder.getChalkFont(parentActivity);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
        int cellSize = width / getFieldSize();
        cellSize = checkCellSize(cellSize);
        initField(cellSize);
        calcPaddingForText(width, height, cellSize);
        refreshField();
    }



    protected void calcPaddingForText(int screenWidth, int screenHeight,
                                      int cellSize) {
        mPaddingTopText = (screenHeight - (PADDING_TOP_FIELD + cellSize
                * getFieldSize()))
                / 2 + PADDING_TOP_FIELD + cellSize * getFieldSize();
        mPaddingLeftText = screenWidth / 2;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isFieldTouched(event.getY())) {
            int column = calcTouchedColumn(event.getX());
            int row = calcTouchedRow(event.getY());

            if (rebuildField(row, column)) {
                increaseAmountOfMoves();
                playSound();
                refreshField();
            }
        }

        if (isGameOver()) {
            showWinMessage();
        }

        return super.onTouchEvent(event);
    }

    protected void increaseAmountOfMoves() {
        mAmountOfMoves++;
    }

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

    protected void showWinMessage() {
        mHolder.lockCanvas();

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

    protected abstract boolean rebuildField(int row, int column);

    protected abstract boolean isGameOver();

    protected abstract boolean isFieldTouched(float touchedY);

    protected abstract int calcTouchedRow(float touchedX);

    protected abstract int calcTouchedColumn(float touchedY);

    protected abstract int getFieldSize();

    protected abstract int getBeginnerFieldSize();
	
	protected abstract int getAdvancedFieldSize();
	
	protected abstract int getExpertFieldSize();

    protected abstract void drawField(Canvas canvas);

    protected abstract void initField(int cellSize);

    protected abstract int checkCellSize(int cellSize);

    protected abstract void playSound();

    protected abstract void initActionSounds();
}
