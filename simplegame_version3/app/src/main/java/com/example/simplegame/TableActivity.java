package com.example.simplegame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class TableActivity extends AppCompatActivity implements View.OnClickListener {

    TextView tv;

    // массив для хранения примеров
    ArrayList<String> examples_list;

    ListView lv;

    Button btnDown, btnUp, btnBack;

    Integer current_index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);

        current_index = 3;  // Текущее отображаемое число из таблицы умножения
        // массив для хранения примеров
        examples_list = new ArrayList<>();

        tv = (TextView) findViewById(R.id.textViewMultiplyBy);
        lv = (ListView) findViewById(R.id.listViewTable);
        btnUp = (Button)findViewById(R.id.buttonUp);
        btnDown = (Button)findViewById(R.id.buttonDown);
        btnBack = (Button)findViewById(R.id.buttonBack);
        btnUp.setOnClickListener(this);
        btnDown.setOnClickListener(this);
        btnBack.setOnClickListener(this);

        // Читаем файл с таблицей умножения для всех чисел от 1 до 9
        ReadExamplesFile();
        ShowTable(current_index);
    }

    // -----------------------------------------------------------------------------------
    // Читает текстовый файл содержащий примеры с умножением
    void ReadExamplesFile() {
        BufferedReader reader = null;
        String mLine;

            try {
                reader = new BufferedReader(
                        new InputStreamReader(getAssets().open("umnojenie.txt")));

                // Читаем файл построчно пока не встретим в очередной строке признак конца файла
                while ((mLine = reader.readLine()) != null) {
                        examples_list.add(mLine);
                }
            } catch (IOException e) {
                Log.d(Constants.log_tag, e.getMessage());
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        Log.d(Constants.log_tag, e.getMessage());
                    }
                }
            }
    }
    // -----------------------------------------------------------------------------------


    // -----------------------------------------------------------------------------------
    // Отображает таблицу умножения для выбранного числа
    void ShowTable(int index)
    {
        List<String> sublist = examples_list.subList((index * 10 - 10), (index * 10 - 1));
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,
                sublist);
        lv.setAdapter(adapter);
        tv.setText(String.format("Умножение на %d", current_index));

        if (current_index == 1)
            btnDown.setVisibility(View.GONE);
        else
            btnDown.setVisibility(View.VISIBLE);

        if (current_index == 9)
            btnUp.setVisibility(View.GONE);
        else
            btnUp.setVisibility(View.VISIBLE);
    }
    // -----------------------------------------------------------------------------------


    // -----------------------------------------------------------------------------------
    // Обработчик нажатий на кнопки
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            // Если нажать на кнопку вперёд, отображается таблица для следующего числа
            case R.id.buttonUp:
                if (current_index < 9){
                    current_index++;
                    ShowTable(current_index);
                }
                break;
            // Если нажать на кнопку назад
            case R.id.buttonDown:
                if (current_index > 1){
                    current_index--;
                    ShowTable(current_index);
                }
                break;
            // Если нажать на кнопку вернуться
            case R.id.buttonBack:
                this.finish();
                break;

            default:
                break;
        }
    }
    // -----------------------------------------------------------------------------------

}