package com.ynov.tbu.schoolexplorer.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.ynov.tbu.schoolexplorer.R;
import com.ynov.tbu.schoolexplorer.body.AuthentificationBody;
import com.ynov.tbu.schoolexplorer.controller.ApiRequest;
import com.ynov.tbu.schoolexplorer.controller.AuthorizationController;
import com.ynov.tbu.schoolexplorer.response.AuthentificationResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText login;
    private EditText password;
    private CheckBox remember;
    private Button send;
    private Button reset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // On récupère nos champs
        login = findViewById(R.id.editLogin);
        password = findViewById(R.id.editPassword);
        remember = findViewById(R.id.checkRemember);
        send = findViewById(R.id.btnSend);
        reset = findViewById(R.id.btnReset);

        // On applique leurs fonctions
        send.setOnClickListener(genericOnClickListener);
        reset.setOnClickListener(genericOnClickListener);

        RestoreUserData();
    }

    private void RestoreUserData() {
        // SI on avait checké "Se souvenir" on remet les valeurs
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
        if( prefs.getBoolean("remember", false)) {
            login.setText(prefs.getString("login", ""));
            password.setText(prefs.getString("password", ""));
            remember.setChecked(prefs.getBoolean("remember", false));
        }
    }

    private void Login() {
        EnabledForm(false);
        if(TextUtils.isEmpty(login.getText())) {
            EnabledForm(true);
            login.setError(getText(R.string.field_required));
        }
        if(TextUtils.isEmpty(password.getText())) {
            EnabledForm(true);
            password.setError(getText(R.string.field_required));
        }
        if(!TextUtils.isEmpty(login.getText()) && !TextUtils.isEmpty(password.getText())) {
            AuthorizationController authorizationController = ApiRequest.getController(AuthorizationController.class);

            authorizationController.getAuthorization(new AuthentificationBody(login.getText().toString(), password.getText().toString())).enqueue(new Callback<AuthentificationResponse>() {
                @Override
                public void onResponse(Call<AuthentificationResponse> call, Response<AuthentificationResponse> response) {
                    AuthentificationResponse authentificationResponse = response.body();
                    if(authentificationResponse != null && authentificationResponse.getSuccess()) {
                        // On definit le token
                        ApiRequest.getInstance().setToken(authentificationResponse.getAuth_token());

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        RememberLogin();

                        // On lance l'activité
                        LoginActivity.this.startActivity(intent);
                        LoginActivity.this.finish();

                    } else {
                        EnabledForm(true);
                        password.setError(LoginActivity.this.getText(R.string.login_error));
                    }
                }

                @Override
                public void onFailure(Call<AuthentificationResponse> call, Throwable t) {
                    EnabledForm(true);
                    password.setError(LoginActivity.this.getText(R.string.login_error));
                }
            });
        }
    }

    private void Reset() {
        login.setText(null);
        password.setText(null);
        remember.setChecked(false);
    }

    private final View.OnClickListener genericOnClickListener = new View.OnClickListener(){
        @Override
        public void onClick(final View view) {
            switch (view.getId()){
                case R.id.btnSend:
                    Login();
                    break;
                case R.id.btnReset:
                    Reset();
                    break;
            }
        }
    };

    private void RememberLogin() {
        // On stocke les données si on a checké "Se souvenir"
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (remember.isChecked()) {
            prefs.edit()
                    .putString("login", login.getText().toString())
                    .putString("password", password.getText().toString())
                    .putBoolean("remember", remember.isChecked())
                    .apply();
        } else {
            prefs.edit()
                    .remove("login")
                    .remove("password")
                    .putBoolean("remember", remember.isChecked())
                    .apply();
        }
    }

    private void EnabledForm(boolean enabled) {
        login.setEnabled(enabled);
        password.setEnabled(enabled);
        send.setEnabled(enabled);
        reset.setEnabled(enabled);

        send.setClickable(enabled);
        reset.setClickable(enabled);
    }
}
