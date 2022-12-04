package cpp.concentrationgameapp;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;

public class AudioHandler {

    private static AudioHandler audioHandler;

    private MediaPlayer mediaPlayer;
    static int mediaStopTime;
    private boolean enabled;

    /**
     * Constructor
     */
    private AudioHandler(){

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
     * @return True if the mediaplayer was active AND it was released. False otherwise.
     */
    public boolean stop(){
        if(mediaPlayer != null){

            mediaPlayer.pause();
            mediaStopTime = mediaPlayer.getCurrentPosition();

            Log.i("AudioHandlerInformation", String.valueOf(mediaStopTime));

            mediaPlayer.release();
            mediaPlayer = null;
            return true;
        } else {
            return false;
        }
    }

    /**
     * Default Play the test track in the res/raw.
     * @param c
     */
    public void play(Context c){

        if(mediaPlayer != null){
            mediaPlayer.release();
            mediaPlayer = null;
        }

        mediaPlayer = MediaPlayer.create(c, R.raw.test8bit);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
    }

    /**
     * Play the track with the ID = musicID in res/raw.
     * @param musicID
     * @param c
     */
    public void play(int musicID, Context c){
        if (!enabled)
            return;

        if(mediaPlayer != null){
            mediaPlayer.release();
            mediaPlayer = null;
        }

        mediaPlayer = MediaPlayer.create(c, musicID);
        mediaPlayer.setLooping(true);
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

        if(mediaPlayer != null){
            mediaPlayer.release();
            mediaPlayer = null;
        }

        mediaPlayer = MediaPlayer.create(c, musicID);
        mediaPlayer.setLooping(true);

        Log.i("AudioHandlerInformation", "Position - D: " + String.valueOf(mediaPlayer.getDuration()) + "P: " + String.valueOf(position) );
        if(position <  mediaPlayer.getDuration()) {
            mediaPlayer.seekTo(position);
        }

        mediaPlayer.start();
    }


    /**
     * Default Play the test track in the res/raw.
     * skip to position in the track.
     * @param c
     * @param position
     */
    public void play(Context c, int position){
        if (!enabled)
            return;

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

    /**
     * Get the status of the audioHandler.
     * When running will return true.
     * @return
     */
    public boolean isRunningStatus(){
        return !(mediaPlayer == null);
    }

    /**
     * @return
     */
    public MediaPlayer getMediaPlayer(){
        return this.mediaPlayer;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
