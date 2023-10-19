package com.example.simplegame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ResultTimeActivity extends AppCompatActivity implements View.OnClickListener {

    TextView tv;
    ListView lv;
    Button btn;
    Button btnErrors;

    ArrayList<String> errors_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_time);

        tv = (TextView) findViewById(R.id.textViewResult);
        btn = (Button) findViewById(R.id.buttonMenu);
        btnErrors = (Button) findViewById(R.id.buttonErrors);
        lv = (ListView) findViewById(R.id.listViewErrors);

        btn.setOnClickListener(this);
        btnErrors.setOnClickListener(this);

        Intent intent = getIntent();
        int questions_counter  = intent.getIntExtra("question", 0);
        int correct_counter  = intent.getIntExtra("correct", 0);
        int test_type = intent.getIntExtra("test_type", 1);
        errors_list = intent.getStringArrayListExtra("errors_list");

        // Если был тест на кол-во правильно решённых примеров
        if (test_type == 2) {
            // Список примеров с ошибками изначально невидимый
            lv.setVisibility(View.GONE);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,
                    errors_list);
            lv.setAdapter(adapter);

            if (questions_counter != correct_counter)
                btnErrors.setVisibility(View.VISIBLE);
            else
                btnErrors.setVisibility(View.GONE);

            // Выводим надпись с общим кол-вом примеров и кол-вом правильных ответов
            tv.setText("Решено: " + correct_counter + " из " + questions_counter);
        }
        // Иначе, если был тест на время
        else if (test_type == 1) {
            btnErrors.setVisibility(View.GONE);

            if (correct_counter > 3)
                tv.setText("Решено: " + correct_counter + "\n\n" + "    Вы молодец!");
            else
                tv.setText("Решено: " + correct_counter + "\n\n" + "    Можно лучше...");
        }
    }

    // Обработчик нажатий на элементы экрана
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            // Если нажать на кнопку Меню, попадаем на экран выбора тестов
            case R.id.buttonMenu:
                Intent intent = new Intent(this, Activity2.class);
                startActivity(intent);
                break;
            case R.id.buttonErrors:
                if (lv.getVisibility() == View.GONE)
                    lv.setVisibility(View.VISIBLE);
                else
                    lv.setVisibility(View.GONE);
                break;

            default:
                break;
        }
    }
}