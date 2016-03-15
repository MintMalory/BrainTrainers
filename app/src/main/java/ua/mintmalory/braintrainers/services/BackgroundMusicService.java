package ua.mintmalory.braintrainers.services;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

import ua.mintmalory.braintrainers.R;

public class BackgroundMusicService extends Service {
    MediaPlayer player_1;
    MediaPlayer player_2;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        player_1 = MediaPlayer.create(this, R.raw.bg_1);
        player_2 = MediaPlayer.create(this, R.raw.bg_2);

        player_1.setNextMediaPlayer(player_2);
        player_2.setNextMediaPlayer(player_1);
    }

    @Override
    public void onDestroy() {
        if (player_1.isPlaying()) {
            player_1.stop();
            return;
        }
        if (player_2.isPlaying()){
            player_2.stop();
        }

    }

    @Override
    public void onStart(Intent intent, int startid) {
        player_1.start();
    }
}