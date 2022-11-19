package com.example.inztagram;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.inztagram.Models.UserLoginResponse;
import com.example.inztagram.Models.UserRegisterRequest;
import com.example.inztagram.Models.UserRegisterResponse;
import com.example.inztagram.utility.InztaAppCompatActivity;
import com.example.inztagram.viewModels.RegisterViewModel;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

public class RegisterActivity extends InztaAppCompatActivity {

    private TextInputEditText textInputUserName, textInputFullName, textInputEmailID, textInputPassword;
    private Button registerButton;
    private TextView textViewLogin;
    private RelativeLayout parent;

    private RegisterViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initViewModel();

        setElements();
        handleOnClickListeners();
    }

    private void initViewModel() {
        this.viewModel = new ViewModelProvider(this).get(RegisterViewModel.class);
        viewModel.getCreateUserAndLoginObserver().observe(this, new Observer<UserLoginResponse>() {
            @Override
            public void onChanged(UserLoginResponse userLoginResponse) {
                if(userLoginResponse == null) {
                    RegisterActivity.this.makeErrorSnackBar(null, parent);
                } else {
                    if(userLoginResponse.getError() != null) {
                        RegisterActivity.this.makeErrorSnackBar(userLoginResponse.getError(), parent);
                    } else {
                        RegisterActivity.this.onUserRegistrationSuccessful();
                    }
                }
            }
        });
    }

    private void setElements() {
        textInputUserName = findViewById(R.id.textInput_userName);
        textInputFullName = findViewById(R.id.textInput_fullName);
        textInputEmailID = findViewById(R.id.textInput_emailID);
        textInputPassword = findViewById(R.id.textInput_password);
        registerButton = findViewById(R.id.button_register);
        textViewLogin = findViewById(R.id.textView_login);
        parent = findViewById(R.id.parent);
    }

    private void handleOnClickListeners() {
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName = textInputUserName.getText().toString();
                String emailId = textInputEmailID.getText().toString();
                String password = textInputPassword.getText().toString();
                String fullName = textInputFullName.getText().toString();

                UserRegisterRequest userRegisterRequest = new UserRegisterRequest(userName, fullName, emailId, password);

                String registerError = viewModel.getRegisterErrorsIfAvailable(userRegisterRequest);
                if(registerError == null) {
                    viewModel.createNewUser(userRegisterRequest);
                } else {
                    makeErrorSnackBar(registerError, parent);
                }
            }
        });
    }

    private void onUserRegistrationSuccessful() {
        Toast.makeText(this, "Successful", Toast.LENGTH_SHORT).show();
    }
}