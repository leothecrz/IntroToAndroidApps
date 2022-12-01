package cpp.concentrationgameapp;

import android.content.Context;
import android.media.MediaPlayer;

public class AudioHandler {

    private MediaPlayer mediaPlayer;

    /**
     * Releases the media player resource.
     * @return True if the mediaplayer was active and it was released. False if it fails.
     */
    public boolean stop(){
        if(mediaPlayer != null){
            mediaPlayer.release();
            mediaPlayer = null;
            return true;
        } else {
            return false;
        }
    }

    public void play(Context c){
        mediaPlayer = MediaPlayer.create(c, R.raw.test8bit);
        mediaPlayer.start();
    }


}
