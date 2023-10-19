package com.example.simplegame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class Activity2 extends AppCompatActivity implements View.OnClickListener {

    Button btnTable, btnOtDo, btnTimeTraining;
    ArrayList<String> test_array;
    final static String log_tag = "MYLOG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);

        btnTable = (Button) findViewById(R.id.buttonTable);
        btnOtDo = (Button) findViewById(R.id.buttonOtDo);
        btnTimeTraining = (Button) findViewById(R.id.buttonTimeTraining);
        btnTable.setOnClickListener(this::onClick);
        btnOtDo.setOnClickListener(this::onClick);
        btnTimeTraining.setOnClickListener(this::onClick);
    }

    @Override
    public void onClick(View view) {
        Intent intent2;
        switch (view.getId()){

            // Тренировка на время
            case R.id.buttonTimeTraining:
                intent2 = new Intent(this, TimeActivity.class);
                intent2.putExtra("test_type", 1);
                intent2.putExtra("ot", 1);
                intent2.putExtra("do", 9);
                intent2.putExtra("test_mul_div", 3);
                startActivity(intent2);
                break;

            // Отобразить таблицу умножения
            case R.id.buttonTable:
                intent2 = new Intent(this, TableActivity.class);
                startActivity(intent2);
                break;

            // Тренировка с регулируемым диапазоном чисел
            case R.id.buttonOtDo:
                intent2 = new Intent(this, SimpleActivity.class);
                startActivity(intent2);
                break;

            default:
                break;
        }
    }
}