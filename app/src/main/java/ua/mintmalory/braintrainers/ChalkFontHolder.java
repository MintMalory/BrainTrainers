package ua.mintmalory.braintrainers;

import android.app.Activity;
import android.graphics.Typeface;

/**
 * Class for holding custom font.
 */
public class ChalkFontHolder{
	private static Typeface chalkFont;

	private ChalkFontHolder() {

	}

	public static Typeface getChalkFont(Activity activity) {
		if (chalkFont == null) {
			chalkFont = Typeface.createFromAsset(activity.getAssets(),	"fonts/LC Chalk.ttf");
		}

		return chalkFont;
	}
}
