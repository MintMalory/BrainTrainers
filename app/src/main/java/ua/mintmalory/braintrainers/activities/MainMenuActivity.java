package ua.mintmalory.braintrainers.activities;

import butterknife.ButterKnife;
import butterknife.Bind;
import ua.mintmalory.braintrainers.ChalkFontHolder;
import ua.mintmalory.braintrainers.R;
import ua.mintmalory.braintrainers.services.BackgroundMusicService;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;

public class MainMenuActivity extends AppCompatActivity implements
        OnClickListener {

    @Bind(R.id.play_button)Button mPlayButton;
    @Bind(R.id.options_button)Button mOptionsButton;
    @Bind(R.id.help_button)Button mHelpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main_menu);
        Typeface chalkFont = ChalkFontHolder.getChalkFont(this);
        ButterKnife.bind(this);
        setTypeFaces(chalkFont);
        startService(new Intent(this, BackgroundMusicService.class));
        mPlayButton.setOnClickListener(this);
        mHelpButton.setOnClickListener(this);
        mOptionsButton.setOnClickListener(this);


    }

    @Override
    public void onBackPressed() {
        stopService(new Intent(this, BackgroundMusicService.class));
        super.onBackPressed();
    }

    private void setTypeFaces(Typeface type) {
        mPlayButton.setTypeface(type);
        mOptionsButton.setTypeface(type);
        mHelpButton.setTypeface(type);
    }



    @Override
    public void onClick(View v) {
        Intent intent;

        if (v == mPlayButton) {
            intent = new Intent(this, ChooseGameActivity.class);
            startActivity(intent);
        }

        if (v == mHelpButton) {
            intent = new Intent(this, HelpActivity.class);
            startActivity(intent);
        }

        if (v == mOptionsButton) {
            intent = new Intent(this, OptionsActivity.class);
            startActivity(intent);
        }
    }
}
