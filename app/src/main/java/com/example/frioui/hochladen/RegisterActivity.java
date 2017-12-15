package com.example.frioui.hochladen;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import static android.Manifest.permission.READ_CONTACTS;
import android.content.Intent;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity   implements View.OnClickListener {


    EditText  etPassword2, etUsername, etPassword;
    Button bRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etPassword2 = (EditText) findViewById(R.id.etPassword2);
        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        bRegister = (Button) findViewById(R.id.bRegister);

        bRegister.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bRegister:

                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                String password2 = etPassword2.getText().toString();

                if (equalPassword(password, password2)) {
                    Intent registerIntent = new Intent(this, LoginActivity.class);
                    startActivity(registerIntent);
                } else {
                    Toast.makeText(this, "Passworteingaben sind nicht gleich", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    /**
     * Methode um zu pruefen, ob die eingegebenen Passwoeter gleich sind.
     *
     * @param password1 --> Passwort wie es gesetzt wurde
     * @param password2 --> Wiederholung, zur Pruefung, ob es gleich ist
     * @return --> Antwort, ob die Passworteingaben gleich sind
     */
    public boolean equalPassword(String password1, String password2){
        boolean result = false;
        if (password1.equals(password2)){
            result = true;
        } else {
            result = false;
        }
        return result;
    }

}

