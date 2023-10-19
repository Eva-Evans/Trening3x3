package com.example.simplegame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView tv;
    Button btn;

    // Тут будет комментарий для проверки системы контроля версий
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = (TextView) findViewById(R.id.tvAbout);
        btn = (Button) findViewById(R.id.btnBegin);

        btn.setOnClickListener(this::onClick);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnBegin:
                tv.setText("Новый текст\nТаблицы умножения");
                Intent intent = new Intent(this, Activity2.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}