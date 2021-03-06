package com.example.frioui.hochladen;

import android.support.v7.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.app.AlertDialog;
import android.widget.Toast;

/**
 * A login screen that offers login via email/password.
 */

    public class LoginActivity extends AppCompatActivity implements View.OnClickListener {



    Button bLogin, bRegister;
    EditText etUsername, etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        bLogin = (Button) findViewById(R.id.bLogin);
        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        bRegister = (Button) findViewById(R.id.bRegister);
        bLogin.setOnClickListener(this);
        bRegister.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bLogin:
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                if(username.equals("") || password.equals(""))
                {
                    Toast.makeText(getApplicationContext(),"Bitte geben Sie das vollständige Feld ein",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    User user = new User();
                    user.setName(username);
                    user.setPassword(password);
                    new AsyncTaskUser(LoginActivity.this,user).execute("http://34.239.181.2:8080/api/users/login");
                }
                break;

            case R.id.bRegister:
                Intent registerIntent = new Intent(this, RegisterActivity.class);
                startActivity(registerIntent);
                break;

        }
    }

































    private void showErrorMessage() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(LoginActivity.this);
        dialogBuilder.setMessage("Incorrect user details");
        dialogBuilder.setPositiveButton("Ok", null);
        dialogBuilder.show();
    }
}

