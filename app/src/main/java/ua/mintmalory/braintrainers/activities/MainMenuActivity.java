package ua.mintmalory.braintrainers.activities;

import butterknife.ButterKnife;
import butterknife.Bind;
import ua.mintmalory.braintrainers.ChalkFontHolder;
import ua.mintmalory.braintrainers.R;
import ua.mintmalory.braintrainers.SoundAndMusicOptionsState;
import ua.mintmalory.braintrainers.State;
import ua.mintmalory.braintrainers.services.BackgroundMusicService;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;

public class MainMenuActivity extends AppCompatActivity implements
        OnClickListener, DialogInterface.OnClickListener {

    @Bind(R.id.play_button)
    Button mPlayButton;
    @Bind(R.id.options_button)
    Button mOptionsButton;
    @Bind(R.id.help_button)
    Button mHelpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main_menu);
        Typeface chalkFont = ChalkFontHolder.getChalkFont(this);
        ButterKnife.bind(this);
        setTypeFaces(chalkFont);

        mPlayButton.setOnClickListener(this);
        mHelpButton.setOnClickListener(this);
        mOptionsButton.setOnClickListener(this);

        AudioManager audio = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);


        if (audio.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
            showMuteMessage();
        } else {
            startService();
        }
    }

    /**
     * Shows message which gives user opportunity to turn on or off music and sounds in silent mode.
     */
    private void showMuteMessage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.mute_msg_text);
        builder.setCancelable(false);
        builder.setNegativeButton(R.string.mute_msg_no, this);
        builder.setPositiveButton(R.string.mute_msg_yes, this);

        AlertDialog alert = builder.create();
        alert.show();
    }

    /**
     * Starts background music service.
     */
    private void startService() {
        startService(new Intent(this, BackgroundMusicService.class));
    }

    /**
     * Handler for AlertDialog.
      * @param dialog instance of the AlertDialog, whose buttons are needed to be handled.
     * @param id id of clicked button
     */
    @Override
    public void onClick(DialogInterface dialog, int id) {
        if (id == DialogInterface.BUTTON_POSITIVE) {
            SoundAndMusicOptionsState.sound = State.ON;
            SoundAndMusicOptionsState.music = State.ON;
            startService();
            return;
        }

        if (id == DialogInterface.BUTTON_NEGATIVE) {
            dialog.cancel();
            SoundAndMusicOptionsState.sound = State.OFF;
            SoundAndMusicOptionsState.music = State.OFF;
            return;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        stopService(new Intent(this, BackgroundMusicService.class));
        super.onBackPressed();
    }

    /**
     * Sets custom font to views
     * @param customTypeFace typeface for setting
     */
    private void setTypeFaces(Typeface customTypeFace) {
        mPlayButton.setTypeface(customTypeFace);
        mOptionsButton.setTypeface(customTypeFace);
        mHelpButton.setTypeface(customTypeFace);
    }


    /**
     * Handler for clicking on main menu buttons.
     */
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
