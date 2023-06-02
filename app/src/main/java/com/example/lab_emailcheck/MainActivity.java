package com.example.lab_emailcheck;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText emailEditText;
    private Button checkButton;
    private TextView validationTextView;

    private EditText passwordEditText;
    private Button validatePasswordButton;
    private TextView passwordValidationTextView;

    private Button registerButton;

    private boolean isEmailRegistered = false;
    private String registeredEmail = "";
    private String[] autoCompleteDomains = {
            "@gmail.com",
            "@yahoo.com",
            "@hotmail.com",
            "@outlook.com",
            "@yandex.ru",
            "@mail.ru"
    };

    private List<String> registeredEmails = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        emailEditText = findViewById(R.id.emailEditText);
        checkButton = findViewById(R.id.checkButton);
        validationTextView = findViewById(R.id.validationTextView);

        passwordEditText = findViewById(R.id.passwordEditText);
        validatePasswordButton = findViewById(R.id.validatePasswordButton);
        passwordValidationTextView = findViewById(R.id.passwordValidationTextView);

        registerButton = findViewById(R.id.registerButton);
        registerButton.setEnabled(false);

        AutoCompleteTextView emailEditText = findViewById(R.id.emailEditText);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, autoCompleteDomains);
        emailEditText.setAdapter(adapter);
        emailEditText.setThreshold(1); // Установите минимальное количество символов для показа автозаполнений

        checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString().trim();
                if (isValidEmail(email)) {
                    validationTextView.setText("Valid email address");
                    isEmailRegistered = checkIfEmailRegistered(email);
                    updateRegisterButtonState();
                } else {
                    validationTextView.setText("Invalid email address");
                    isEmailRegistered = false;
                    updateRegisterButtonState();
                }
            }
        });

        validatePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = passwordEditText.getText().toString().trim();
                int minLength = 6;
                int maxLength = 12;

                if (TextUtils.isEmpty(password)) {
                    passwordValidationTextView.setText("Password is required");
                }
                else
                if (password.length() < minLength) {
                    passwordValidationTextView.setText("Password is too short. Minimum length is " + minLength);
                }
                else
                if (password.length() > maxLength) {
                    passwordValidationTextView.setText("Password is too long. Maximum length is " + maxLength);
                }
                else {
                    passwordValidationTextView.setText("Password is Valid");
                }
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isEmailRegistered) {
                    String email = emailEditText.getText().toString().trim();
                    if (isValidEmail(email)) {
                        if (!checkIfEmailRegistered(email)) {
                            // Добавить адрес электронной почты в список зарегистрированных
                            addRegisteredEmail(email);
                            Toast.makeText(MainActivity.this, "Registration successful!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, "Email already registered", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "Invalid email address", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    private boolean isValidEmail(CharSequence email) {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isValidPassword(String password) {
        int minLength = 6;
        int maxLength = 12;

        if (TextUtils.isEmpty(password)) {
            passwordValidationTextView.setText("Password is required");
            return false;
        }

        if (password.length() < minLength) {
            passwordValidationTextView.setText("Password is too short. Minimum length is " + minLength);
            return false;
        }

        if (password.length() > maxLength) {
            passwordValidationTextView.setText("Password is too long. Maximum length is " + maxLength);
            return false;
        }

        // Password is valid
        passwordValidationTextView.setText("");
        return true;
    }


    private boolean checkIfEmailRegistered(String email) {
        List<String> registeredEmails = getRegisteredEmails(); // Получить список зарегистрированных адресов электронной почты

        for (String registeredEmail : registeredEmails) {
            if (registeredEmail.equalsIgnoreCase(email)) {
                return true; // Адрес электронной почты уже зарегистрирован
            }
        }

        return false; // Адрес электронной почты не зарегистрирован
    }


    private void updateRegisterButtonState() {
        if (isEmailRegistered) {
            registerButton.setEnabled(false);
        } else {
            registerButton.setEnabled(true);
        }
    }
    private void addRegisteredEmail(String email) {
        registeredEmails.add(email);
    }
    private List<String> getRegisteredEmails() {
        return registeredEmails;
    }
}

