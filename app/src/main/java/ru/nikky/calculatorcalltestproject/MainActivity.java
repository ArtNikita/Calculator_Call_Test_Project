package ru.nikky.calculatorcalltestproject;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button multiplicationButton;
    private Button divisionButton;
    private Button minusButton;
    private Button plusButton;
    private Button openParenthesisButton;
    private Button closeParenthesisButton;
    private Button dotButton;

    private EditText expressionEditText;
    private AppCompatButton evaluateExpressionButton;
    private TextView answerTextView;

    private ActivityResultLauncher<Intent> calculatorLauncher;
    private static final String RESULT_KEY = "RESULT_KEY";
    private static final String RESULT_EXPRESSION_KEY = "RESULT_EXPRESSION_KEY";
    private static final String EXPRESSION_KEY = "EXPRESSION_KEY";
    private static final String CALCULATOR_ACTION = "ru.nikky.calculator.calculate";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        setOnClickListeners();
        initCalculatorLauncher();
    }

    private void initViews() {
        multiplicationButton = findViewById(R.id.multiplication_button);
        divisionButton = findViewById(R.id.division_button);
        minusButton = findViewById(R.id.minus_button);
        plusButton = findViewById(R.id.plus_button);
        openParenthesisButton = findViewById(R.id.open_parenthesis_button);
        closeParenthesisButton = findViewById(R.id.close_parenthesis_button);
        dotButton = findViewById(R.id.dot_button);

        expressionEditText = findViewById(R.id.expression_edit_text);
        evaluateExpressionButton = findViewById(R.id.evaluate_expression_button);
        answerTextView = findViewById(R.id.answer_text_view);
    }

    private void setOnClickListeners() {
        multiplicationButton.setOnClickListener(v -> operationButtonPressed((Button) v));
        divisionButton.setOnClickListener(v -> operationButtonPressed((Button) v));
        minusButton.setOnClickListener(v -> operationButtonPressed((Button) v));
        plusButton.setOnClickListener(v -> operationButtonPressed((Button) v));
        openParenthesisButton.setOnClickListener(v -> operationButtonPressed((Button) v));
        closeParenthesisButton.setOnClickListener(v -> operationButtonPressed((Button) v));
        dotButton.setOnClickListener(v -> operationButtonPressed((Button) v));

        evaluateExpressionButton.setOnClickListener(v -> evaluateExpressionButtonPressed());
    }

    private void initCalculatorLauncher() {
        calculatorLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {
                Intent resultIntent = result.getData();
                if (resultIntent != null) {
                    answerTextView.setText(resultIntent.getStringExtra(RESULT_KEY));
                    expressionEditText.setText(resultIntent.getStringExtra(RESULT_EXPRESSION_KEY));
                }
            } else {
                Toast.makeText(MainActivity.this,
                        MainActivity.this.getString(R.string.on_activity_bad_result_toast),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void operationButtonPressed(Button button) {
        expressionEditText.append(button.getText().toString());
    }

    private void evaluateExpressionButtonPressed() {
        answerTextView.setText("");
        Intent calculatorIntent = new Intent();
        calculatorIntent.setAction(CALCULATOR_ACTION);
        calculatorIntent.putExtra(EXPRESSION_KEY, expressionEditText.getText().toString());
        if (calculatorIntent.resolveActivity(getPackageManager()) != null){
            calculatorLauncher.launch(calculatorIntent);
        } else {
            Toast.makeText(this, getString(R.string.no_calculator_app_toast), Toast.LENGTH_SHORT).show();
        }
    }
}