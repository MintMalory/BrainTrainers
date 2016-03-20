package ua.mintmalory.braintrainers.activities;

import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import ua.mintmalory.braintrainers.GameDifficulty;
import ua.mintmalory.braintrainers.R;
import ua.mintmalory.braintrainers.gameviews.UnlockGameView;

/**
 * Activity is responsible for processing Unlock game.
 * Layout consists of two layers: <p/>
 * <ol>
 * <li>ImageView with background image of the activity;</li>
 * <li>Custom view with game field.</li>
 * </ol>
 * <p/>
 * This structure allows redraw only field instead of whole screen.
 */

public class UnlockGameActivity extends AppCompatActivity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        GameDifficulty difficulty = GameDifficulty.values()[getIntent()
                .getIntExtra(ChooseGameActivity.GAME_DIFFICULTY, 1)];

        UnlockGameView unlockView = new UnlockGameView(this, difficulty);
        unlockView.setZOrderOnTop(true);
        unlockView.getHolder().setFormat(PixelFormat.TRANSPARENT);

        ImageView bgImageView = new ImageView(this);
        bgImageView.setBackground(this.getResources().getDrawable(R.drawable.bg));

        RelativeLayout.LayoutParams matchParentLayoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);

        RelativeLayout rootLayout = new RelativeLayout(this);
        rootLayout.setLayoutParams(matchParentLayoutParams);
        rootLayout.addView(unlockView, matchParentLayoutParams);
        rootLayout.addView(bgImageView, matchParentLayoutParams);

        setContentView(rootLayout);
    }
}
