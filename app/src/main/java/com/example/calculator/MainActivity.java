package com.example.calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView resultField;           // поле для вывода результата
    EditText numberField;           // поле для ввода числа
    TextView operationField;        // поле для вывода знака операции
    Double operand = null;          // операнд операции
    String lastOperation = "=";     // последняя операция

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        resultField =(TextView) findViewById(R.id.resultField);
        numberField = (EditText) findViewById(R.id.numberField); //поле ввода чисел
        operationField = (TextView) findViewById(R.id.operationField);

        numberField.setFocusable(false);
    }
    // сохранение состояния
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("OPERATION", lastOperation);
        if(operand!=null)
            outState.putDouble("OPERAND", operand);
        super.onSaveInstanceState(outState);
    }
    // получение ранее сохраненного состояния
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        lastOperation = savedInstanceState.getString("OPERATION");
        operand= savedInstanceState.getDouble("OPERAND");
        resultField.setText(operand.toString());
        operationField.setText(lastOperation);
    }
    // Нажатие на кнопки с цифрами
    public void onNumberClick(View view){

        Button button = (Button)view;
        numberField.append(button.getText());

        if(lastOperation.equals("=") && operand!=null){
            operand = null;
        }
    }

    // Нажатие на кнопку AC
    public void onSpecialClick1(View view){
            numberField.setText("");
    }

    // Нажатие на кнопку +/-
    public void onSpecialClick2(View view){
        numberField.setText(numberField.getText().toString().replace(',', '.'));
        try{
            if(Double.parseDouble(numberField.getText().toString()) >= 0 && numberField.getText().toString() != null){
                numberField.setText(numberField.getText().toString().replace('.', ','));
                numberField.setText("-" + numberField.getText().toString());
            }
        }catch (Exception ex) {
            numberField.setText(numberField.getText().toString());
        }
        numberField.setText(numberField.getText().toString().replace('.', ','));
    }

    // Нажатие на кнопку математической операции
    public void onOperationClick(View view){

        Button button = (Button)view;
        String operation = button.getText().toString();
        String number = numberField.getText().toString();
        if(number.length()>0){
            number = number.replace(',', '.');
            try{
                Operation(Double.valueOf(number), operation);
            }catch (NumberFormatException ex){
                numberField.setText("");
            }
        }
        lastOperation = operation;
        operationField.setText(lastOperation);
    }

    // Действие для каждой математической операции
    private void Operation(Double number, String operation){

        // если операнд ранее не был установлен (при вводе самой первой операции)
        if(operand ==null){
            operand = number;
        }
        else{
            if(lastOperation.equals("=")){
                lastOperation = operation;
            }
            switch(lastOperation){
                case "=":
                    operand =number;
                    break;
                case "/":
                    if(number==0){
                        operand =0.0;
                    }
                    else{
                        operand /=number;
                    }
                    break;
                case "*":
                    operand *=number;
                    break;
                case "+":
                    operand +=number;
                    break;
                case "-":
                    operand -=number;
                    break;
                case "%":
                    operand = (number * operand) / 100;
                    break;
            }
        }
        resultField.setText(operand.toString().replace('.', ','));
        numberField.setText("");
    }
}