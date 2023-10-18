package com.base.intigratingaudioandvideo;

import androidx.appcompat.app.AppCompatActivity;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements View.OnClickListener , SeekBar.OnSeekBarChangeListener, MediaPlayer.OnCompletionListener {

    private Button mybutton, button , button2;
    private VideoView myVideoView;
    private SeekBar seekBar,moveSeekbar;

    private MediaController mMediaController;
    private MediaPlayer mMediaPlayer;
    private AudioManager audioManager;
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mybutton = findViewById(R.id.mybutton);
        button = findViewById(R.id.button);
        button2 = findViewById(R.id.button2);
        myVideoView = findViewById(R.id.myVideoView);

        seekBar = findViewById(R.id.seekBar);
        moveSeekbar = findViewById(R.id.moveSeekbar);


        mMediaController = new MediaController(MainActivity.this);

        mybutton.setOnClickListener(MainActivity.this);

        mMediaPlayer = MediaPlayer.create(this,R.raw.oopo);
//        try {
//            mMediaPlayer.prepare(); // Prepare the MediaPlayer
//        } catch (IOException e) {
//            e.printStackTrace();
//            Toast.makeText(MainActivity.this, "media error occured",Toast.LENGTH_SHORT).show();;
//
//        }
        audioManager =(AudioManager)getSystemService(AUDIO_SERVICE);

        int maxVolumeofUserdevice= audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

        int currentVolumeOfUserDevice =   audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

        seekBar.setMax(maxVolumeofUserdevice);
        seekBar.setProgress(currentVolumeOfUserDevice);

        moveSeekbar.setOnSeekBarChangeListener(MainActivity.this);
        moveSeekbar.setMax(mMediaPlayer.getDuration());

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(b){
                    Toast.makeText(MainActivity.this, Integer.toString(i),Toast.LENGTH_SHORT).show();
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,i,0);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    public void onClick(View v) {

      if(R.id.mybutton == v.getId()){

          Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/"+ R.raw.video);
          myVideoView.setVideoURI(videoUri);

          myVideoView.setMediaController(mMediaController);
          mMediaController.setAnchorView(myVideoView);
          myVideoView.start();
      }

        if(R.id.button == v.getId()){

            mMediaPlayer.start();
            timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    moveSeekbar.setProgress(mMediaPlayer.getCurrentPosition());
                }
            },0,1000);


        }


        if(R.id.button2 == v.getId()){
            mMediaPlayer.pause();
            timer.cancel();



        }





    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
//        if(b){
//            Toast.makeText(this,Integer.toString(i),Toast.LENGTH_SHORT).show();
//
//        }
        mMediaPlayer.seekTo(i);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        mMediaPlayer.pause();

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        mMediaPlayer.start();

    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        timer.cancel();
        Toast.makeText(MainActivity.this , "music has ended", Toast.LENGTH_SHORT).show();
    }
}