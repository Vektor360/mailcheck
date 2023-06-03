package com.example.lab_emailcheck;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private AutoCompleteTextView emailEditText;
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
    private PopupWindow autoCompletePopup;
    private ListView autoCompleteListView;


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

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, autoCompleteDomains);
        emailEditText.setAdapter(adapter);
        emailEditText.setThreshold(1);

        emailEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                showAutoCompletePopup();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateAutoCompletePopup();
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Не требуется
            }
        });

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
                } else if (password.length() < minLength) {
                    passwordValidationTextView.setText("Password is too short. Minimum length is " + minLength);
                } else if (password.length() > maxLength) {
                    passwordValidationTextView.setText("Password is too long. Maximum length is " + maxLength);
                } else {
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

    private List<String> autoCompleteList = new ArrayList<>();

    private void showAutoCompletePopup() {
        if (autoCompletePopup == null) {
            View popupView = LayoutInflater.from(this).inflate(R.layout.auto_complete_popup, null);
            autoCompletePopup = new PopupWindow(popupView, emailEditText.getWidth(), 300);
            autoCompleteListView = (ListView) popupView.findViewById(R.id.autoCompleteListView);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, autoCompleteList);
            autoCompleteListView.setAdapter(adapter);
            autoCompleteListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String selectedDomain = autoCompleteList.get(position);
                    String currentText = emailEditText.getText().toString();
                    String newText = selectedDomain;
                    emailEditText.setText(newText); // Устанавливаем новый текст в поле ввода
                    emailEditText.setSelection(newText.length()); // Устанавливаем курсор в конец текста
                    autoCompletePopup.dismiss();
                }
            });

            autoCompletePopup.setOutsideTouchable(true);
            autoCompletePopup.setFocusable(false);
            autoCompletePopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    emailEditText.clearFocus();
                }
            });
        }

        updateAutoCompletePopup(); // Обновляем список перед показом попапа

        autoCompletePopup.showAsDropDown(emailEditText);
    }



    private void updateAutoCompletePopup() {
        autoCompleteList.clear();
        String prefix = emailEditText.getText().toString().trim();

        // Проверка, содержит ли префикс уже символ @
        boolean containsAtSymbol = prefix.contains("@");

        for (String domain : autoCompleteDomains) {
            // Проверка каждого символа в префиксе
            boolean isValidPrefix = true;
            int prefixLength = prefix.length();
            int domainLength = domain.length();
            int counter = 0;
            int countermass = 0;
            for (int i = 0; i < prefixLength; i++) {
                if (prefix.charAt(i) == domain.charAt(countermass)) {
                    counter++;
                    countermass++;
                    if (countermass==domainLength)
                    {
                        break;
                    }
                }
                else
                if (countermass>0)
                {
                    isValidPrefix = false;
                }
            }

            if (containsAtSymbol & isValidPrefix == true) {
                autoCompleteList.add(prefix + domain.substring(counter));
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, autoCompleteList);
        autoCompleteListView.setAdapter(adapter);
    }



    private void dismissAutoCompletePopup() {
        if (autoCompletePopup != null && autoCompletePopup.isShowing()) {
            autoCompletePopup.dismiss();
        }
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
