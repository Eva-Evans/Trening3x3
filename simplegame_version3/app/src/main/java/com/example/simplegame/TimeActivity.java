package com.example.simplegame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class TimeActivity extends AppCompatActivity implements View.OnClickListener {

    TextView tvStatus, tvQA;
    Button btn0, btn1, btn2,  btn3, btn4, btn5, btn6, btn7, btn8, btn9, btnClear, btnFinish;
    ImageButton ibCheck;
    static int question_counter,  correct_counter;

    // массив для хранения примеров
    ArrayList<String> examples_list;
    // массив для хранения ошибочно решённых примеров
    ArrayList<String> errors_list;

    static String question, answer, local_answer;
    int start_number, finish_number, test_type, test_mul_div;

    View.OnTouchListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time);

        tvQA = (TextView) findViewById(R.id.textViewQuestionAnswer);
        tvStatus = (TextView) findViewById(R.id.textViewStatus);
        btn0 = (Button) findViewById(R.id.button0);
        btn1 = (Button) findViewById(R.id.button1);
        btn2 = (Button) findViewById(R.id.button2);
        btn3 = (Button) findViewById(R.id.button3);
        btn4 = (Button) findViewById(R.id.button4);
        btn5 = (Button) findViewById(R.id.button5);
        btn6 = (Button) findViewById(R.id.button6);
        btn7 = (Button) findViewById(R.id.button7);
        btn8 = (Button) findViewById(R.id.button8);
        btn9 = (Button) findViewById(R.id.button9);
        btnFinish = (Button) findViewById(R.id.buttonFinish);
        btnClear = (Button) findViewById(R.id.buttonClear);
        ibCheck = (ImageButton) findViewById(R.id.imageButtonCheck);
        btn0.setOnClickListener(this);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
        btn5.setOnClickListener(this);
        btn6.setOnClickListener(this);
        btn7.setOnClickListener(this);
        btn8.setOnClickListener(this);
        btn9.setOnClickListener(this);
        btnFinish.setOnClickListener(this);
        btnClear.setOnClickListener(this);
        ibCheck.setOnClickListener(this);

        errors_list = new ArrayList<>();
        question_counter = correct_counter = 0;

        // Извлекаем нижнюю и верхнюю границу для отображаемых примеров
        Intent intent = getIntent();
        start_number = (intent.getIntExtra("ot", 0) * 10) - 10;
        finish_number = (intent.getIntExtra("do", 0) * 10);
        test_type = (intent.getIntExtra("test_type", 1));
        test_mul_div = (intent.getIntExtra("test_mul_div", 1));

        // массив для хранения примеров
        examples_list = new ArrayList<>();
        // Читаем текстовый файл с примерами и сохраняем их в массиве
        ReadExamplesFile();

        // Показывает текст с примером, на который нужно дать ответ
        ShowQuestion();

        // Если тест на время без ограничения числа попыток
        if (test_type == 1)
        {
            btnFinish.setVisibility(View.GONE);
            Thread t = new Thread(new Runnable() {
                String time;
                @Override
                public void run() {
                    try{
                        // Первые 30 секунд бежит счётчик и отображается на экране
                        tvStatus.setText("00:30");
                        for (int counter = 29; counter >= 0; counter--){
                            TimeUnit.SECONDS.sleep(1);
                            time = String.format("00:%02d", counter);
                            tvStatus.setText(time);
                        }

                        // После 30 секунд нужно вызвать экран с результатами теста
                        Log.d(Constants.log_tag, "FINISH");
                        FinishActivity();
                    } catch (InterruptedException e){
                        ;
                    }
                }
            });
            t.start();
        }
    }
    // -----------------------------------------------------------------------------------

    // -----------------------------------------------------------------------------------
    void ShowQuestion()
    {
        // Генерируем рандомное число и показываем пример из списка с этим индексом
        Random r = new Random();
        if (examples_list.size() > 0) {
            int index = r.nextInt(examples_list.size());
            //String question = examples_list.get(index).split("=")[0];
            question = examples_list.get(index).split("=")[0];
            answer = examples_list.get(index).split("=")[1];
            examples_list.remove(index);    // удаляем из списка выбранный пример чтобы избежать повторений
            local_answer = "";
            // Не отображаем текст если тест на время
            if (test_type == 2)
                tvStatus.setText("Решено: " + question_counter);
            tvQA.setText(question + " = ?");
        }
        else {
            FinishActivity();
        }
    }
    // -----------------------------------------------------------------------------------

    // -----------------------------------------------------------------------------------
    void CheckAnswer()
    {
        // Уходим в эту ветку только если тест на кол-во, а не на время
        if (test_type == 2) {
            question_counter++;
            if (Integer.parseInt(local_answer.trim()) == Integer.parseInt(answer.trim())) {
                Log.d(Constants.log_tag, "OK");
                correct_counter++;
            } else {
                errors_list.add(question + " = " + local_answer);
                Log.d(Constants.log_tag, "ERROR." + " local = " + local_answer + " answer = " + answer);
            }
        }

        // Иначе если тест на время, то после совпадения ответа прибавляется кол-во решённых и отображается новый вопрос
        // Если же ответ не совпадает, то поле ответа очищается для нового ввода ответа
        else if (test_type == 1) {
            if (Integer.parseInt(local_answer.trim()) == Integer.parseInt(answer.trim())) {
                Log.d(Constants.log_tag, "OK");
                correct_counter++;
                ShowQuestion();
            }
            else {
                local_answer = "";
                tvQA.setText(question + " = ?");
                Log.d(Constants.log_tag, "ONE MORE TRY");
            }
        }
    }
    // -----------------------------------------------------------------------------------

    // -----------------------------------------------------------------------------------
    // Читает текстовый файл содержащий все примеры и запоминает их в контейнере для строк
    void ReadExamplesFile()
    {
        int line_number = 0;
        BufferedReader reader = null;
        String mLine;

        if ((test_mul_div & 0x1) != 0) {
            Log.d(Constants.log_tag, "There 1");
            try {
                reader = new BufferedReader(
                        new InputStreamReader(getAssets().open("delenie.txt")));

                // Читаем файл построчно пока не встретим в очередной строке признак конца файла
                while ((mLine = reader.readLine()) != null) {
                    line_number++;  // Число отображает номер строки в фале из которой извлекается текущий пример
                    // Делаем выборку только из тех примеров, которые удовлетворяют условиям От и До
                    if ((line_number > start_number) && (line_number <= finish_number))
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

        line_number = 0;

        if ((test_mul_div & 0x2) != 0) {
            Log.d(Constants.log_tag, "There 2");
            try {
                reader = new BufferedReader(
                        new InputStreamReader(getAssets().open("umnojenie.txt")));

                // Читаем файл построчно пока не встретим в очередной строке признак конца файла
                while ((mLine = reader.readLine()) != null) {
                    line_number++;  // Число отображает номер строки в фале из которой извлекается текущий пример
                    // Делаем выборку только из тех примеров, которые удовлетворяют условиям От и До
                    if ((line_number > start_number) && (line_number <= finish_number))
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
    }
    // -----------------------------------------------------------------------------------

    // -----------------------------------------------------------------------------------
    // Вызывает экран статистики, завершая текущий тест
    void FinishActivity()
    {
        // Вызываем экран статистики, упаковываем сами результаты решения примеров
        Intent intent = new Intent(this, ResultTimeActivity.class);
        intent.putExtra("question", question_counter);
        intent.putExtra("correct", correct_counter);
        intent.putExtra("test_type", test_type);
        intent.putStringArrayListExtra("errors_list", errors_list);
        startActivity(intent);
    }
    // -----------------------------------------------------------------------------------

    // -----------------------------------------------------------------------------------
    @Override
    public void onClick(View view) {
        AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.75F);
        buttonClick.setDuration(100);
        // Проверка введённого ответа на верность
        if (view.getId() == R.id.imageButtonCheck){

            view.startAnimation(buttonClick);

            // Пустой ответ не проверяем
            if (local_answer != ""){
                // Проверяем введённый ответ с тем что записан в файле
                CheckAnswer();

                // Уходим в эту ветку только если тест на кол-во, а не на время
                if (test_type == 2) {
                    // Если кол-во ответов достигло лимита, нужно вывести результаты
                    if (question_counter == 5) {
                        FinishActivity();
                    } else
                        // Отобразить следующий пример
                        ShowQuestion();
                }
            }
        }

        // Очистка введённого ответа до первоначального состояния
        else if (view.getId() == R.id.buttonClear){
            local_answer = "";
            tvQA.setText(question + " = ?");
        }

        // Нажатие на кнопку "Завершить" выводит результаты теста
        else if (view.getId() == R.id.buttonFinish){
            FinishActivity();
        }

        // Нажатие на одну из цифр от 0 до 9
        else {
            switch (view.getId()) {
                case R.id.button0:
                    local_answer += "0";
                    break;
                case R.id.button1:
                    local_answer += "1";
                    break;
                case R.id.button2:
                    local_answer += "2";
                    break;
                case R.id.button3:
                    local_answer += "3";
                    break;
                case R.id.button4:
                    local_answer += "4";
                    break;
                case R.id.button5:
                    local_answer += "5";
                    break;
                case R.id.button6:
                    local_answer += "6";
                    break;
                case R.id.button7:
                    local_answer += "7";
                    break;
                case R.id.button8:
                    local_answer += "8";
                    break;
                case R.id.button9:
                    local_answer += "9";
                    break;

                default:
                    break;
            }
            tvQA.setText(question + " = " + local_answer);
        }
    }
    // -----------------------------------------------------------------------------------
}