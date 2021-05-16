package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;

public class MusicPlayerActivity extends AppCompatActivity implements View.OnClickListener {

    //declarations
    TextView tvTime, tvDuration,tvTitle,tvArtist;
    SeekBar seekBarTime , seekBarVolume;
    Button btnPlay;
    MediaPlayer musicPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Song song=(Song) getIntent().getSerializableExtra("song");
        tvTime=findViewById(R.id.tvTime);
        tvDuration=findViewById(R.id.tvDuration);
        seekBarTime=findViewById(R.id.seekBarTime);
        seekBarVolume=findViewById(R.id.seekBarVolume);
        btnPlay=findViewById(R.id.btnPlay);
        tvTitle=findViewById(R.id.tvTitle);
        tvArtist=findViewById(R.id.tvArtist);

        tvTitle.setText(song.getTitle());
        tvArtist.setText(song.getArtist());

        musicPlayer = new MediaPlayer();
        try {
            musicPlayer.setDataSource(song.getPath());
            musicPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        musicPlayer.setLooping(true);
        musicPlayer.setVolume(0.5f,0.5f);
        musicPlayer.seekTo(0);

        String duration = millisecondsToString(musicPlayer.getDuration());
        tvDuration.setText(duration);




         btnPlay.setOnClickListener(this);
         seekBarVolume.setProgress(50);
         seekBarVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
             @Override
             public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                 float volume = progress / 100f;
                 musicPlayer.setVolume(volume,volume);
             }

             @Override
             public void onStartTrackingTouch(SeekBar seekBar) {

             }

             @Override
             public void onStopTrackingTouch(SeekBar seekBar) {

             }
         });
         seekBarTime.setMax(musicPlayer.getDuration());
         seekBarTime.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
             @Override
             public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                 if(fromUser)
                 {
                     musicPlayer.seekTo(progress);
                     seekBar.setProgress(progress);
                 }
             }

             @Override
             public void onStartTrackingTouch(SeekBar seekBar) {

             }

             @Override
             public void onStopTrackingTouch(SeekBar seekBar) {

             }
         });
         new Thread(new Runnable() {
             @Override
             public void run() {
                 while (musicPlayer !=null)
                 {try {

                     final double current = musicPlayer.getCurrentPosition();
                     final String elapsedTime = millisecondsToString((int) current);
                     runOnUiThread(new Runnable() {
                         @Override
                         public void run() {
                             tvTime.setText((elapsedTime));
                             seekBarTime.setProgress((int) current);
                         }
                     });

                     Thread.sleep(1000);
                 }catch (InterruptedException e ){}
                 }
             }
         }).start();
    }
public String millisecondsToString(int time)
{
    String elapsedTime="";
    int minutes=time/1000/60;
    int seconds= time/1000%60;
    elapsedTime= minutes+":";
    if(seconds<10)
    {
        elapsedTime+="0";
    }
    elapsedTime +=seconds;
    return elapsedTime;
}

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btnPlay)
        {
            if(musicPlayer.isPlaying())
            {
                musicPlayer.pause();
                btnPlay.setBackgroundResource(R.drawable.ic_play);
            }else
            {
                musicPlayer.start();
                btnPlay.setBackgroundResource(R.drawable.ic_pause);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home)
        {
            finish();
            if(musicPlayer.isPlaying())
            {
                musicPlayer.stop();
            }
        }
        return super.onOptionsItemSelected(item);
    }
}