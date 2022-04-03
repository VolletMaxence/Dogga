package com.example.mangaliste;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.budiyev.android.codescanner.ScanMode;
import com.google.zxing.Result;


public class ScanActivity extends AppCompatActivity {

    private CodeScanner codeScanner;
    private TextView codeData;
    private TextView infoLivre;
    private CodeScannerView scannerView;
    private static int UserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        codeData = findViewById(R.id.text_code);
        infoLivre = findViewById(R.id.info_livre);
        scannerView = findViewById(R.id.scanner_view);

        SessionManagement sessionManagement = new SessionManagement(ScanActivity.this);
        UserId = sessionManagement.getSession();
        System.out.println("User ID : "+UserId);

        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {
                Manifest.permission.CAMERA
        };

        if(!hasPermissions(this, PERMISSIONS))
        {
            System.out.println("Pas de permission");
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        } else {
            System.out.println("Permission Camera Accordé");

            runCodeScanner();
        }
    }

    private void runCodeScanner() {
        codeScanner = new CodeScanner(this, scannerView);

        codeScanner.setAutoFocusEnabled(true);
        codeScanner.setFormats(CodeScanner.ALL_FORMATS);
        codeScanner.setScanMode(ScanMode.CONTINUOUS);
        codeScanner.startPreview();

        codeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String data = result.getText();

                        //traiter le code obtenue avec une autre fonction :
                        int TroisPremierCharactere = data.charAt(0) + data.charAt(1) + data.charAt(2);
                        String charTroisPremierCharactere = Integer.toString(TroisPremierCharactere);

                        if (charTroisPremierCharactere.equals("168"))
                        {
                            //Obtenir les infos du manga Scanner
                            codeData.setText("Scan en cour.");
                            try {
                                Thread.sleep(3000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            RechercheLivre(data);
                            codeData.setText("Votre livre : ");
                        } else
                        {
                            codeData.setText("Le code barre scanner n'est pas un code barre de livre.");
                        }
                    }
                });
            }
        });
    }

    public static boolean hasPermissions(Context context, String[] permissions) {
        if(context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }

        return true;
    }

    private void RechercheLivre(String data)
    {
        new FetchBook(infoLivre).execute(data);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public static int getUserId()
    {
        return UserId;
    }
}