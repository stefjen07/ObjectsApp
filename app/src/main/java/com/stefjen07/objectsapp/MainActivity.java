package com.stefjen07.objectsapp;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.cardview.widget.CardView;

public class MainActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener, TextWatcher {
    RadioGroup typeRadioGroup;

    EditText textView1;
    EditText textView2;
    EditText textView3;

    TextView resultsTextView;

    CardView cardView1;
    CardView cardView2;
    CardView cardView3;

    ObjectType currentType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        currentType = ObjectType.circle;
        textView1 = findViewById(R.id.textView1);
        textView2 = findViewById(R.id.textView2);
        textView3 = findViewById(R.id.textView3);

        textView1.addTextChangedListener(this);
        textView2.addTextChangedListener(this);
        textView3.addTextChangedListener(this);

        cardView1 = findViewById(R.id.cardView1);
        cardView2 = findViewById(R.id.cardView2);
        cardView3 = findViewById(R.id.cardView3);

        resultsTextView = findViewById(R.id.resultTextView);

        typeRadioGroup = findViewById(R.id.radioGroup);
        typeRadioGroup.setOnCheckedChangeListener(this);
        onCheckedChanged(typeRadioGroup, 0);
    }

    protected void setupType() {
        switch (currentType) {
            case circle:
                textView1.setHint("радиус");
                cardView2.setVisibility(View.INVISIBLE);
                cardView3.setVisibility(View.INVISIBLE);
                break;
            case rectangle:
                textView1.setHint("сторона 1");
                textView2.setHint("сторона 2");
                cardView2.setVisibility(View.VISIBLE);
                cardView3.setVisibility(View.INVISIBLE);
                break;
            case triangle:
                textView1.setHint("сторона 1");
                textView2.setHint("сторона 2");
                textView3.setHint("сторона 3");
                cardView2.setVisibility(View.VISIBLE);
                cardView3.setVisibility(View.VISIBLE);
                break;
        }

        afterTextChanged(textView1.getText());
    }

    protected void showError() {
        resultsTextView.setText("Введены неправильные данные");
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        int radioButtonID = radioGroup.getCheckedRadioButtonId();
        RadioButton radioButton = radioGroup.findViewById(radioButtonID);

        if(radioButton == null)
            return;

        String selectedText = (String) radioButton.getText();

        switch (selectedText) {
            case "круг":
                currentType = ObjectType.circle;
                break;
            case "прямоугольник":
                currentType = ObjectType.rectangle;
                break;
            case "треугольник":
                currentType = ObjectType.triangle;
                break;
        }

        setupType();
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        Double value1 = 0.0, value2 = 0.0, value3 = 0.0;
        try {
            value1 = Double.parseDouble(textView1.getText().toString());
        } catch (Exception e) {
            showError();
            return;
        }
        try {
            value2 = Double.parseDouble(textView2.getText().toString());
        } catch (Exception e) {
            if (currentType == ObjectType.triangle || currentType == ObjectType.rectangle) {
                showError();
                return;
            }
        }
        try {
            value3 = Double.parseDouble(textView3.getText().toString());
        } catch (Exception e) {
            if (currentType == ObjectType.triangle) {
                showError();
                return;
            }
        }

        if(value1 < 0 || value2 < 0 || value3 < 0) {
            showError();
            return;
        }

        switch (currentType) {
            case circle:
                resultsTextView.setText(Double.toString(Math.PI * Math.pow(value1, 2)));
                break;
            case rectangle:
                resultsTextView.setText(Double.toString(value1 * value2));
                break;
            case triangle:
                Double halfPerimetr = (value1 + value2 + value3) / 2;
                Double result = Math.sqrt(halfPerimetr * (halfPerimetr - value1) * (halfPerimetr - value2) * (halfPerimetr - value3));

                if(result.isNaN() || result.isInfinite()) {
                    showError();
                    return;
                }

                resultsTextView.setText(Double.toString(result));
                break;
        }
    }
}