package com.example.android.musicplayer;

import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    //
    private Button fastBackwardButton, playButton, pauseButton, resetButton, fastForwardButton;
    private MediaPlayer songMediaPlayer;

    private double startTime = 0;
    private double finalTime = 0;

    //
    private Handler myHandler = new Handler();
    private int fastForward = 5000;
    private int fastBackward = 5000;

    //
    private SeekBar timeLineSeekBar;

    //
    private TextView currentTimeTextView, totalTimeTextView;

    //
    private static boolean oneTimeOnly = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //----- Initialize variables -----//

        //
        fastBackwardButton = (Button) findViewById(R.id.fast_backward_button);
        playButton = (Button) findViewById(R.id.play_button);
        pauseButton = (Button) findViewById(R.id.pause_button);
        pauseButton.setEnabled(false);
        resetButton = (Button) findViewById(R.id.reset_button);
        fastForwardButton = (Button) findViewById(R.id.fast_forward_button);

        //
        currentTimeTextView = (TextView) findViewById(R.id.current_time_text_view);
        totalTimeTextView = (TextView) findViewById(R.id.total_time_text_view);

        //
        songMediaPlayer = (MediaPlayer) MediaPlayer.create(this, R.raw.summer);

        //
        timeLineSeekBar = (SeekBar) findViewById(R.id.time_line_seek_bar);
        timeLineSeekBar.setClickable(false);

        //
        finalTime = songMediaPlayer.getDuration();
        totalTimeTextView.setText(String.format("%d min, %d sec",
                TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                TimeUnit.MILLISECONDS.toSeconds((long) finalTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) finalTime))));

        Log.d("MainActivity", "startTime: " + startTime);
        Log.d("MainActivity", "finalTime: " + finalTime);

        //----- -----//

        /**
         *
         */
        fastBackwardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (startTime - fastBackward > 0) {
                    //
                    startTime -= fastBackward;
                    songMediaPlayer.seekTo((int) startTime);

                    //
                    Toast toast = Toast.makeText(getApplicationContext(), "Jumped backward 5 seconds", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    //
                    Toast toast = Toast.makeText(getApplicationContext(), "Cannot jump backward 5 seconds", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

        /**
         *
         */
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //
                if (!oneTimeOnly) {
                    timeLineSeekBar.setMax((int) finalTime);
                    oneTimeOnly = true;
                }

                //
                Toast toast = Toast.makeText(getApplicationContext(), "Playing song", Toast.LENGTH_SHORT);
                toast.show();
                songMediaPlayer.start();

                //
                startTime = songMediaPlayer.getCurrentPosition();
                currentTimeTextView.setText(String.format("%d min, %d sec",
                        TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                        TimeUnit.MILLISECONDS.toSeconds((long) startTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) startTime))));

                timeLineSeekBar.setProgress((int) startTime);
                myHandler.postDelayed(UpdateSongTime, 100);

                //
                playButton.setEnabled(false);
                pauseButton.setEnabled(true);
            }
        });

        /**
         *
         */
        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //
                Toast toast = (Toast) Toast.makeText(getApplicationContext(), "Pausing song", Toast.LENGTH_SHORT);
                toast.show();
                songMediaPlayer.pause();

                //
                playButton.setEnabled(true);
                pauseButton.setEnabled(false);
            }
        });

        /**
         *
         */
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //
                startTime = 0;
                songMediaPlayer.seekTo((int) startTime);

                Toast toast = Toast.makeText(getApplicationContext(), "Resetting song", Toast.LENGTH_SHORT);
                toast.show();
            }
        });

        /**
         *
         */
        fastForwardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (startTime + fastForward < finalTime) {
                    //
                    startTime += fastForward;
                    songMediaPlayer.seekTo((int) startTime);

                    //
                    Toast toast = Toast.makeText(getApplicationContext(), "Jumped forward 5 seconds", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    //
                    Toast toast = Toast.makeText(getApplicationContext(), "Cannot jump forward 5 seconds", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

        /**
         *
         */
        songMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                //
                playButton.setEnabled(true);
                pauseButton.setEnabled(false);

                //
                Toast toast = Toast.makeText(getApplicationContext(), "Song is completed", Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }

    /**
     *
     */
    private Runnable UpdateSongTime = new Runnable() {
        @Override
        public void run() {
            startTime = songMediaPlayer.getCurrentPosition();
            currentTimeTextView.setText(String.format("%d min, %d sec",
                    TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                    TimeUnit.MILLISECONDS.toSeconds((long) startTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) startTime))));
            timeLineSeekBar.setProgress((int) startTime);
            myHandler.postDelayed(this, 100);
        }
    };
}
