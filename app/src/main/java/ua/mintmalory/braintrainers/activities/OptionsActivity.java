package ua.mintmalory.braintrainers.activities;

import butterknife.ButterKnife;
import butterknife.Bind;
import ua.mintmalory.braintrainers.ChalkFontHolder;
import ua.mintmalory.braintrainers.R;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class HelpActivity extends AppCompatActivity {

	@Bind(R.id.lightsoff_title_textView) TextView mLightsOffTitleTextView;
	@Bind(R.id.lock_title_textView) TextView mLockTitleTextView;
	@Bind(R.id.npuzzle_title_textView) TextView mNPuzzleTitleTextView;


	@Override
	protected void onCreate(Bundle savedInstanceState) {//TODO: ИСПРАВИТЬ КОД!!!
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_help);
		
		Typeface type = ChalkFontHolder.getChalkFont(this);
		
		ButterKnife.bind(this);
		setTypeFaces(type);
	}
		
	private void setTypeFaces(Typeface type){
		mLightsOffTitleTextView.setTypeface(type);
		mLockTitleTextView.setTypeface(type);
		mNPuzzleTitleTextView.setTypeface(type);
	}
}
