package ua.mintmalory.braintrainers.activities;

import butterknife.ButterKnife;
import butterknife.Bind;
import ua.mintmalory.braintrainers.ChalkFontHolder;
import ua.mintmalory.braintrainers.R;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Activity which displays reference information about games (aim of games, game's rules etc).
 */
public class HelpActivity extends AppCompatActivity {

	@Bind(R.id.lightsoff_title_textView) TextView mLightsOffTitleTextView;
	@Bind(R.id.lock_title_textView) TextView mLockTitleTextView;
	@Bind(R.id.npuzzle_title_textView) TextView mNPuzzleTitleTextView;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_help);
		
		Typeface customTypeFace = ChalkFontHolder.getChalkFont(this);
		
		ButterKnife.bind(this);
		setTypeFaces(customTypeFace);
	}

	/**
	 * Sets custom font to views
	 * @param customTypeFace typeface for setting
	 */
	private void setTypeFaces(Typeface customTypeFace){
		mLightsOffTitleTextView.setTypeface(customTypeFace);
		mLockTitleTextView.setTypeface(customTypeFace);
		mNPuzzleTitleTextView.setTypeface(customTypeFace);
	}
}
