package com.example.mangaliste;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private Button mConnectionButton;
    private Button mScanButton;
    private Button mListeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Instancie les variable de l'objet
        mConnectionButton = findViewById(R.id.main_button_connection);
        mScanButton = findViewById(R.id.main_button_scan);
        mListeButton = findViewById(R.id.main_button_saliste);

        SessionManagement sessionManagement = new SessionManagement(MainActivity.this);
        int UserId = sessionManagement.getSession();
        System.out.println("User ID : "+UserId);

        //Changer de page avec le boutton pour aller au formulaire de connection
        mConnectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDeconnectionPage();
            }
        });
        mScanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openScannerPage();
            }
        });
        mListeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openListePage();
            }
        });
    }

    public void openDeconnectionPage() {
        //Se deconnecter -> suppr Session et ouvre login
        SessionManagement sessionManagement = new SessionManagement(MainActivity.this);
        sessionManagement.removeSession();

        openLoginPage();
    }

    private void openLoginPage() {
        Intent intent = new Intent(MainActivity.this, Connection.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void openScannerPage() {
        Intent intent = new Intent(MainActivity.this, ScanActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void openListePage() {
        Intent intent = new Intent(MainActivity.this, ListeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}