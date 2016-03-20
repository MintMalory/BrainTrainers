package ua.mintmalory.braintrainers.activities;

import butterknife.ButterKnife;
import butterknife.Bind;
import ua.mintmalory.braintrainers.ChalkFontHolder;
import ua.mintmalory.braintrainers.R;
import ua.mintmalory.braintrainers.SoundAndMusicOptionsState;
import ua.mintmalory.braintrainers.State;
import ua.mintmalory.braintrainers.services.BackgroundMusicService;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

/**
 * Activity for displaying game options.
 */
public class OptionsActivity extends AppCompatActivity {

    @Bind(R.id.options_textView)
    TextView optionsTextView;
    @Bind(R.id.music_textView)
    TextView musicTextView;
    @Bind(R.id.on_music_radioButton)
    RadioButton onMusicRadioButton;
    @Bind(R.id.off_music_radioButton)
    RadioButton offMusicRadioButton;
    @Bind(R.id.sound_textView)
    TextView soundTextView;
    @Bind(R.id.on_sound_radioButton)
    RadioButton onSoundRadioButton;
    @Bind(R.id.off_sound_radioButton)
    RadioButton offSoundRadioButton;

    @Bind(R.id.on_off_music_radioGroup)
    RadioGroup onOffMusicRadioButton;

    @Bind(R.id.on_off_sound_radioGroup)
    RadioGroup onOffSoundRadioButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        Typeface type = ChalkFontHolder.getChalkFont(this);

        ButterKnife.bind(this);

        setRadioButtonsState();
        onOffMusicRadioButton.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == onMusicRadioButton.getId()) {
                    startService(new Intent(group.getContext(), BackgroundMusicService.class));
                    SoundAndMusicOptionsState.music = State.ON;
                    return;
                }

                if (checkedId == offMusicRadioButton.getId()) {
                    stopService(new Intent(group.getContext(), BackgroundMusicService.class));
                    SoundAndMusicOptionsState.music = State.OFF;
                }
            }
        });

        onOffSoundRadioButton.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == onSoundRadioButton.getId()) {
                    SoundAndMusicOptionsState.sound = State.ON;
                    return;
                }

                if (checkedId == offSoundRadioButton.getId()) {
                    SoundAndMusicOptionsState.sound = State.OFF;
                }
            }
        });

        setTypeFaces(type);
    }

    /**
     * Sets radio buttons states according to the actual information in SoundAndMusicOptionsState class.
     */
    private void setRadioButtonsState() {
        if (SoundAndMusicOptionsState.music == State.ON) {
            onMusicRadioButton.setChecked(true);
        } else {
            offMusicRadioButton.setChecked(true);
        }

        if (SoundAndMusicOptionsState.sound == State.ON) {
            onSoundRadioButton.setChecked(true);
        } else {
            offSoundRadioButton.setChecked(true);
        }
    }

    /**
     * Sets custom font to views
     *
     * @param customTypeFace typeface for setting
     */
    private void setTypeFaces(Typeface customTypeFace) {
        optionsTextView.setTypeface(customTypeFace);
        musicTextView.setTypeface(customTypeFace);
        onMusicRadioButton.setTypeface(customTypeFace);
        offMusicRadioButton.setTypeface(customTypeFace);
        soundTextView.setTypeface(customTypeFace);
        onSoundRadioButton.setTypeface(customTypeFace);
        offSoundRadioButton.setTypeface(customTypeFace);
    }
}
