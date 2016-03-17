package ua.mintmalory.braintrainers.services;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

import ua.mintmalory.braintrainers.Foreground;
import ua.mintmalory.braintrainers.R;

public class BackgroundMusicService extends Service {
    private MediaPlayer[] tracks;
	private int lastTrackIndex;
	private Foreground.Listener myListener;
	
		
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        tracks[] = new MediaPlayer[2];
			
		tracks[0] = MediaPlayer.create(this, R.raw.bg_1);
        tracks[1] = MediaPlayer.create(this, R.raw.bg_2);

		if (tracks.length > 0){
			for (int i = 0; i < tracks.length - 1; i++){
				tracks[i].setNextMediaPlayer(tracks[i+1]);
			}
			
			tracks[length-1].setNextMediaPlayer(tracks[0]);
		}

		myListener = new Foreground.Listener(){
			public void onBecameForeground(){
				tracks[lastTrackIndext].start();
			}
			
			public void onBecameBackground(){
				for (int i =0; i < tracks.length; i++){
					if (tracks[i].sPlaying()){
						tracks[i].pause();
						lastTrackIndex = i;
						break;
					}
				}
			}
		};

		Foreground.get(getApplication()).addListener(listener);
    }

    @Override
    public void onDestroy() {		
        for (int i =0; i < tracks.length; i++){
			if (tracks[i].sPlaying()){
				tracks[i].stop();
				break;
			}
		}
		
		Foreground.get(getApplication()).removeListener(listener);
    }

    @Override
    public void onStart(Intent intent, int startid) {
        if((tracks != null) && (tracks.length > 0)){
			Random r = new Random();
			
			tracks[r.nextInt(tracks.length)].start();
		}
    }
}