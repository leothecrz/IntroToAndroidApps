package cpp.concentrationgameapp;

import android.content.Context;
import android.media.MediaPlayer;

public class AudioHandler {

    private static AudioHandler audioHandler;

    private MediaPlayer mediaPlayer;
    int mediaStopTime;

    /**
     * Constructor
     */
    private AudioHandler(){
        int mediaStopTime = 0;
    }

    /**
     * Singleton instance control
     * @return
     */
    public static AudioHandler getInstance(){
        if(audioHandler == null){
            audioHandler = new AudioHandler();
        }
        return audioHandler;
    }

    /**
     * Releases the media player resource.
     * @return True if the mediaplayer was active and it was released. False if it fails.
     */
    public boolean stop(){
        if(mediaPlayer != null){

            mediaPlayer.pause();
            mediaStopTime = mediaPlayer.getCurrentPosition();

            mediaPlayer.release();
            mediaPlayer = null;
            return true;
        } else {
            return false;
        }
    }

    public void play(Context c){

        if(mediaPlayer != null){
            mediaPlayer.release();
            mediaPlayer = null;
        }

        mediaPlayer = MediaPlayer.create(c, R.raw.test8bit);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
    }

    public void play(Context c, int position){

        if(mediaPlayer != null){
            mediaPlayer.release();
            mediaPlayer = null;
        }
        
        mediaPlayer = MediaPlayer.create(c, R.raw.test8bit);
        mediaPlayer.setLooping(true);

        if(position <  mediaPlayer.getDuration())
            mediaPlayer.seekTo(position);

        mediaPlayer.start();
    }

    public boolean isRunningStatus(){
        return !(mediaPlayer == null);
    }

    public MediaPlayer getMediaPlayer(){
        return this.mediaPlayer;
    }

}
