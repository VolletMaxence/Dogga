package com.example.dogga;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Instancie les variable de l'objet
        Button mConnectionButton = findViewById(R.id.main_button_connection);
        Button mScanButton = findViewById(R.id.main_button_scan);
        Button mListeButton = findViewById(R.id.main_button_saliste);

        SessionManagement sessionManagement = new SessionManagement(MainActivity.this);
        int UserId = sessionManagement.getSession();
        System.out.println("User ID : "+UserId);

        //Changer de page avec le boutton pour aller au formulaire de connection
        mConnectionButton.setOnClickListener(view -> openDeconnectionPage());
        mScanButton.setOnClickListener(view -> openScannerPage());
        mListeButton.setOnClickListener(view -> openListePage());
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