package kr.hs.emirim.w2034.project3_1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView list1;
    TextView textMusic;
    ProgressBar proBar;
    Button btnStart, btnStop;
    ArrayList<String> arrList;
    String selectedMusic;
    String musicPath = Environment.getExternalStorageDirectory().getPath() +"/";
    MediaPlayer media;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("MP3 Player");
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MODE_PRIVATE);
        arrList = new ArrayList<String>();
        File[] listFiles = new File(musicPath).listFiles();
        String fileName, extName;
        for (File file : listFiles){//(int i=0; i < listFiles.length; i++){    //for (File file : listFiles)
            fileName = file.getName();
            extName = fileName.substring(fileName.length()-3);
            if (extName.equals("mp3"))
                arrList.add(fileName);
        }

        list1 = findViewById(R.id.list1);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice);
        list1.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        list1.setAdapter(adapter);
        list1.setItemChecked(0,true);

        list1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedMusic = arrList.get(position);
            }
        });
        selectedMusic = arrList.get(0);

        btnStart = findViewById(R.id.btn_start);
        btnStop = findViewById(R.id.btn_stop);
        textMusic = findViewById(R.id.text_music);
        proBar = findViewById(R.id.progress);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    media = new MediaPlayer();
                    media.setDataSource(musicPath + selectedMusic);
                    media.prepare();
                    media.start();
                    btnStart.setClickable(false);
                    btnStop.setClickable(true);
                    textMusic.setText(selectedMusic + ":");
                    proBar.setVisibility(View.VISIBLE);
                } catch (IOException e) {
                    e.printStackTrace();
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
                textMusic.setText("실행음악 중지: ");
                proBar.setVisibility(View.INVISIBLE);
            }
        });

        btnStop.setClickable(false);
    }
}