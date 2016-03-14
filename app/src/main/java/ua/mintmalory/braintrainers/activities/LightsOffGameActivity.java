package ua.mintmalory.braintrainers.activities;

import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import ua.mintmalory.braintrainers.GameDifficulty;
import ua.mintmalory.braintrainers.R;
import ua.mintmalory.braintrainers.gameviews.LightsOffGameView;

public class LightsOffGameActivity extends AppCompatActivity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        GameDifficulty difficulty = GameDifficulty.values()[getIntent()
                .getIntExtra(ChooseGameActivity.GAME_DIFFICULTY, 1)];

        LightsOffGameView lightsOffGameView = new LightsOffGameView(this, difficulty);
        lightsOffGameView.setZOrderOnTop(true);
        lightsOffGameView.getHolder().setFormat(PixelFormat.TRANSPARENT);

        ImageView bgImagePanel = new ImageView(this);
        bgImagePanel.setBackground(this.getResources().getDrawable(R.drawable.bg_usual));

        RelativeLayout.LayoutParams fillParentLayout = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        RelativeLayout rootPanel = new RelativeLayout(this);
        rootPanel.setLayoutParams(fillParentLayout);
        rootPanel.addView(lightsOffGameView, fillParentLayout);
        rootPanel.addView(bgImagePanel, fillParentLayout);

        setContentView(rootPanel);
    }
}
