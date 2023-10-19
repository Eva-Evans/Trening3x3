package com.example.simplegame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SimpleActivity extends AppCompatActivity implements View.OnClickListener {

    EditText et_ot, et_do;
    Button btn_begin;
    CheckBox cb_mul, cb_div;
    int number_ot, number_do, mul_div;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple);

        et_ot = (EditText) findViewById(R.id.editTextOt);
        et_do = (EditText) findViewById(R.id.editTextDo);
        btn_begin = (Button) findViewById(R.id.buttonBegin);
        cb_mul = (CheckBox) findViewById(R.id.checkBoxMul);
        cb_div = (CheckBox) findViewById(R.id.checkBoxDiv);

        btn_begin.setOnClickListener(this::onClick);
    }

    // Обработчик нажатия на зелёную галочку
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.buttonBegin)
        {
            Log.d(Constants.log_tag, "btn begin press");
            if (et_ot.getText().length() != 0) {
                number_ot = Integer.parseInt(et_ot.getText().toString());
                if ((number_ot < 1) || (number_ot > 9))
                    number_ot = 1;
            }
            else
                number_ot = 1;

            if (et_do.getText().length() != 0) {
                number_do = Integer.parseInt(et_do.getText().toString());
                if ((number_do < 1) || (number_do > 9))
                    number_do = 9;
            }
            else
                number_do = 9;

            if (number_do < number_ot){
                number_ot = 1;
                number_do = 9;
            }

            mul_div = 0;
            if (cb_mul.isChecked())
                mul_div += 2;
            if (cb_div.isChecked())
                mul_div += 1;

            Intent intent = new Intent(this, TimeActivity.class);
            intent.putExtra("test_type", 2);
            intent.putExtra("ot", number_ot);
            intent.putExtra("do", number_do);
            intent.putExtra("test_mul_div", mul_div);
            startActivity(intent);
        }
    }
}