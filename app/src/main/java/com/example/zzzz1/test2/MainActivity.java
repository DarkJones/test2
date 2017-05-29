package com.example.zzzz1.test2;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
public class MainActivity extends AppCompatActivity {
    private final short duration = 100;
    private final short sampleRate = 8000;
    private final int numSamples = duration * sampleRate;
    private final short sample[] = new short[numSamples];
    private final double freqOfTone = 440;
    public short data[] = new short[8000];

public int min = 0,max = 0;
    private final byte generatedSnd[] = new byte[2* numSamples];

    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

//          Запись данных с микрофона в массив data
    public void clickButt(View view) {
        int minBufferSize = AudioRecord.getMinBufferSize(8000, 2,
                AudioFormat.ENCODING_PCM_16BIT);
        AudioRecord ar = new AudioRecord(MediaRecorder.AudioSource.MIC,
                8000, AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT, minBufferSize);
        ar.read(data, 0, 8000);

    }
//           Тут нейкое шаманство и попытки отобразить что получается записать с микрофона
    public void showData(View view){

   // for(int i=0;i<=1000;i++){
     //       if(min>data[i]){min=data[i];}
       //     if(max<data[i]){max=data[i];}
      //  }

        TextView viewDataText1 = (TextView)findViewById(R.id.viewDataText);

       viewDataText1.setText("21");


    }

    @Override
    protected void onResume() {
        super.onResume();

        final Thread thread = new Thread( new Runnable() {
            public void run() {
                genTone();
                handler.post(new Runnable() {

                    public void run() {
                        playSound();
                    }
                });
            }
        });
        thread.start();
    }
//          Генерация сигнала
    void genTone(){

        for (int i = 0; i < numSamples; i+=2) {
            sample[i+0] = (short) (Math.sin(2 * Math.PI * i / (sampleRate / freqOfTone))* 0x7FFF);
            sample[i+1] = (short) (Math.sin(2 * (Math.PI) * i / (sampleRate / freqOfTone)+Math.PI)* 0x7FFF);
        }
    }
//      Воспроизведение
    void playSound(){

        final AudioTrack audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
                sampleRate,AudioFormat.CHANNEL_OUT_STEREO,
                AudioFormat.ENCODING_PCM_16BIT, generatedSnd.length,
                AudioTrack.MODE_STATIC);

        audioTrack.write(sample, 0, sample.length);

        audioTrack.play();
    }
}
