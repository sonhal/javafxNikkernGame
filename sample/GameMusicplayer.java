package sample;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.File;
import java.net.URISyntaxException;

public class GameMusicplayer {
    MediaPlayer mediaPlayer;
    Media sound;


    public GameMusicplayer(String filename){
        try {
            String fileUrl = Main.class.getResource("/" + filename).toURI().toString();
            //  String music_file = getClass().getResource(musicFile).toString();
            String music = new File(fileUrl).toURI().toString();
            sound = new Media(fileUrl);
            mediaPlayer = new MediaPlayer(sound);

        }catch (URISyntaxException e){
            System.out.print(e.getStackTrace());
        }
    }


    public void play(){
        mediaPlayer.play();
        mediaPlayer.stop();

    }



    public void stop(){
        mediaPlayer.stop();
    }

    public void toggleAutoPlay(boolean play){
        mediaPlayer.setAutoPlay(play);
    }

    public void setAutoRepeat(){
        mediaPlayer.setOnEndOfMedia(new Runnable() {
            public void run() {
                mediaPlayer.seek(Duration.ZERO);
            }
        });
    }

    public void setRewind(){
        mediaPlayer.setOnEndOfMedia(new Runnable() {
            @Override
            public void run() {
                mediaPlayer.stop();
                mediaPlayer.seek(mediaPlayer.getStartTime());
            }
        });
    }


}