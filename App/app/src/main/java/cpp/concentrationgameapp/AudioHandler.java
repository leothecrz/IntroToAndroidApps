package cpp.concentrationgameapp;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;

import java.io.IOException;

public class AudioHandler {

    private static AudioHandler audioHandler;

    private static MediaPlayer mediaPlayer;
    static int mediaStopTime;
    private boolean enabled;

    /**
     * Constructor
     */
    private AudioHandler(){
        if(mediaPlayer !=null){
            mediaPlayer.release();
        }
        enabled = true;
    }

    /**
     * Singleton instance control
     * @return Singleton instance of AudioHandler
     */
    public static AudioHandler getInstance(){
        if(audioHandler == null){
            audioHandler = new AudioHandler();
        }
        return audioHandler;
    }

    /**
     * Releases the media player resource.
     * @return True if the mediaPlayer was active AND it was released. False otherwise.
     */
    public boolean stop(){
        if(mediaPlayer != null){

            mediaPlayer.pause();
            mediaStopTime = mediaPlayer.getCurrentPosition();

            Log.i("AudioHandlerInformation", String.valueOf(mediaStopTime));

            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
            return true;
        } else {
            return false;
        }
    }

    /**
     * Default Play the test track in the res/raw.
     * @param c Context to play test track
     */
    public void play(Context c){

        releaseMediaPlayer();

        mediaPlayer = MediaPlayer.create(c, R.raw.test8bit);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
    }

    /**
     * Play the track with the ID = musicID in res/raw.
     * @param musicID Resource ID of the raw file
     * @param c Context to play track
     */
    public void play(int musicID, Context c){
        if (!enabled)
            return;

        releaseMediaPlayer();

        mediaPlayer = MediaPlayer.create(c, musicID);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
    }

    /**
     * Default Play the test track in the res/raw.
     * Skip to position in the track.
     * @param c Context to play track
     * @param position Position to start track at
     */
    public void play(Context c, int position){
        if (!enabled)
            return;

        releaseMediaPlayer();
        
        mediaPlayer = MediaPlayer.create(c, R.raw.test8bit);
        mediaPlayer.setLooping(true);

        if(position <  mediaPlayer.getDuration())
            mediaPlayer.seekTo(position);

        mediaPlayer.start();
    }

    /**
     * Play the track with the ID = musicID in res/raw.
     * Skip to position in the track.
     * @param musicID
     * @param c
     * @param position
     */
    public void play(int musicID, Context c, int position){
        if (!enabled)
            return;

        releaseMediaPlayer();

        mediaPlayer = MediaPlayer.create(c, musicID);
        mediaPlayer.setLooping(true);

        if(position < mediaPlayer.getDuration())
            mediaPlayer.seekTo(position);


        mediaPlayer.start();
    }

    private void releaseMediaPlayer(){
        if(mediaPlayer != null){
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }


    /**
     * Get the status of the audioHandler.
     * When running will return true.
     * @return True if AudioHandler is running
     */
    public boolean isRunningStatus(){
        return !(mediaPlayer == null);
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
