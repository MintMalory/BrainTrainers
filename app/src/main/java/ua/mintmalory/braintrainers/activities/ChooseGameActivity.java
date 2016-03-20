package ua.mintmalory.braintrainers.activities;

import butterknife.Bind;
import butterknife.ButterKnife;
import ua.mintmalory.braintrainers.ChalkFontHolder;
import ua.mintmalory.braintrainers.GameDifficulty;
import ua.mintmalory.braintrainers.R;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

/**
 * Activity for choosing game and game difficulty.
 */
public class ChooseGameActivity extends AppCompatActivity implements
		OnClickListener {

	public static final String GAME_DIFFICULTY = "ua.mintmalory.braintrainers.activities.choosegameactivity.game_difficulty";

	@Bind(R.id.choose_game_textView) TextView mChooseGameTextView;

	@Bind(R.id.choose_game_radioGroup) RadioGroup mChooseGameRadioGroup;
	@Bind(R.id.lights_off_radioButton) RadioButton mLightsOffRadioButton;
	@Bind(R.id.lock_radioButton) RadioButton mUnlockRadioButton;
	@Bind(R.id.n_puzzle_radioButton) RadioButton mNPuzzleRadioButton;

	@Bind(R.id.select_difficulty_textView) TextView mSelectDifficultyTextView;

	@Bind(R.id.select_difficulty_radioGroup) RadioGroup mSelectDifficultyGroup;
	@Bind(R.id.beginner_radioButton) RadioButton mBeginnerRadioButton;
	@Bind(R.id.advanced_radioButton) RadioButton mAdvancedRadioButton;
	@Bind(R.id.expert_radioButton) RadioButton mExpertRadioButton;

	@Bind(R.id.go_button) Button mGoButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_choose_game);

		ButterKnife.bind(this);

		Typeface customFontChalk = ChalkFontHolder.getChalkFont(this);
		setTypeFaceForTitles(customFontChalk);

		mGoButton.setOnClickListener(this);
	}

	/**
	 * Sets custom font to views
	 * @param customTypeFace typeface for setting
	 */
	private void setTypeFaceForTitles(Typeface customTypeFace) {
		mChooseGameTextView.setTypeface(customTypeFace);
		mLightsOffRadioButton.setTypeface(customTypeFace);
		mUnlockRadioButton.setTypeface(customTypeFace);
		mNPuzzleRadioButton.setTypeface(customTypeFace);
		mSelectDifficultyTextView.setTypeface(customTypeFace);
		mBeginnerRadioButton.setTypeface(customTypeFace);
		mAdvancedRadioButton.setTypeface(customTypeFace);
		mExpertRadioButton.setTypeface(customTypeFace);
		mGoButton.setTypeface(customTypeFace);
	}

	/**
	 * Handler for start game button.
	 */
	@Override
	public void onClick(View v) {
		Intent intent = setGame();
		setGameDifficulty(intent);
		startActivity(intent);
	}

	/**
	 * Adds to intent chosen game difficulty
 	 * @param intent an intent for adding information
	 */
	private void setGameDifficulty(Intent intent) {
		int checkedDifficultyButtonID = mSelectDifficultyGroup
				.getCheckedRadioButtonId();

		if (checkedDifficultyButtonID == mBeginnerRadioButton.getId()) {
			intent.putExtra(GAME_DIFFICULTY, GameDifficulty.BEGINNER.ordinal());
			return;
		}

		if (checkedDifficultyButtonID == mAdvancedRadioButton.getId()) {
			intent.putExtra(GAME_DIFFICULTY, GameDifficulty.ADVANCED.ordinal());
			return;
		}

			intent.putExtra(GAME_DIFFICULTY, GameDifficulty.EXPERT.ordinal());
	}

	/**
	 * Adds to intent chosen kind of game
	 * @param intent an intent for adding information
	 */
	private Intent setGame() {
		int checkedGameButtonID = mChooseGameRadioGroup
				.getCheckedRadioButtonId();

		if (checkedGameButtonID == mLightsOffRadioButton.getId()) {
			return new Intent(this, LightsOffGameActivity.class);
		}

		if (checkedGameButtonID == mUnlockRadioButton.getId()) {
			return new Intent(this, UnlockGameActivity.class);
		}

			return new Intent(this, NPuzzleGameActivity.class);

	}
}
