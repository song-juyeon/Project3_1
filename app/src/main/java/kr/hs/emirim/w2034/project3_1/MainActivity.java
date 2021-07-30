package kr.hs.emirim.w2034.project3_1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView list1;
    TextView textMusic, textTime;
    SeekBar seek1;
    ArrayList<String> arrList;
    String selectedMusic;
    String musicPath = Environment.getExternalStorageDirectory().getPath() + "/";
    MediaPlayer media;
    Button btnStart,btnStop,btnPause;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("MP3 Player");
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MODE_PRIVATE);
        arrList = new ArrayList<String>();
        File[] listFiles = new File(musicPath).listFiles();
        String fileName,extName;
        for (File file : listFiles){    //enhanced For (개선된 For 문)
            fileName = file.getName();
            extName = fileName.substring(fileName.length()-3);
            if (extName.equals("mp3"))
                arrList.add(fileName);
        }
        list1.findViewById(R.id.list1);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice,arrList);
        list1.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        list1.setAdapter(adapter);
        list1.setItemChecked(0,true);

        list1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                selectedMusic = arrList.get(position);
            }
        });
        selectedMusic = arrList.get(0); // 리스트를 클릭하지 않았을때 초기값
        btnStart = findViewById(R.id.btn_start);
        btnStop = findViewById(R.id.btn_stop);
        btnPause = findViewById(R.id.btn_pause);
        textMusic = findViewById(R.id.text_music);
        textTime = findViewById(R.id.text_time);
        seek1 = findViewById(R.id.seek1);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                media = new MediaPlayer();
                try {
                    media = new MediaPlayer();
                    media.setDataSource(musicPath + selectedMusic);
                    media.prepare();
                    media.start();
                    btnStart.setClickable(false);
                    btnStop.setClickable(true);
                    textMusic.setText(selectedMusic + ":");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnPause.getText().equals("일시 중지")){
                    media.pause();
                    btnPause.setText("이어듣기");
                }else if(btnPause.getText().equals("이어듣기")){
                    media.start();
                    runOnThread();
                    btnPause.setText("일시중지");
                }
            }
        });
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                media.stop();
                media.reset();
                btnStart.setClickable(true);
                btnStop.setClickable(false);
                textMusic.setText("실행중인 음악 중지 : ");
                textTime.setText(R.string.text_time);
            }
        });
        btnStop.setClickable(false);
    }

    public void  runOnThread(){
        new Thread(){
            SimpleDateFormat dateFormat = new SimpleDateFormat("mm:ss");
            @Override
            public void run() {
                if (media == null)
                    return;
                seek1.setMax(media.getDuration());
                while (media.isPlaying()){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            seek1.setProgress(media.getCurrentPosition());
                            textTime.setText(R.string.text_time);
                            textTime.append(dateFormat.format(media.getCurrentPosition()));
                        }
                    });
                    SystemClock.sleep(200);
                }
            }
        }.start();
    }

}