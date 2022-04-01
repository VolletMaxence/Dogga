package com.example.mangaliste;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;
import static java.util.Objects.requireNonNull;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.content.Context;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SubActivity extends AppCompatActivity {

    private Button connect;
    private EditText mdp;
    private EditText pseudo;
    private TextView error;

    public static final String URL = "jdbc:mysql://mysql-xencev.alwaysdata.net/xencev_site-perso";
    public static final String User = "xencev_root";
    public static final String MDP = "Tallys2001";

    public static final String SHARED_PREFS = "shared_prefs";
    SharedPreferences sharedpreferences;
    public static final String Pseudo_KEY = "pseudo_key";
    String pseudoStock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);

        pseudo = (EditText) findViewById(R.id.pseudo);
        mdp = (EditText) findViewById(R.id.mdp);

        connect = (Button) findViewById(R.id.connect);

        error = (TextView) findViewById(R.id.error);

        sharedpreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        pseudoStock = sharedpreferences.getString(Pseudo_KEY, null);

        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                connection();
            }
        });

        if (Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

    }

    @Override
    protected void onStart(){
        super.onStart();

        checkSession();
    }

    private void checkSession() {
        //Renvoie utilisateur si il est deja connecter
        SessionManagement sessionManagement = new SessionManagement(SubActivity.this);
        int UserId = sessionManagement.getSession();

        System.out.println("User ID Sub : "+UserId);
        if (UserId != -1)
        {
            System.out.println("User connecté");
            //Utilisateur est deja connecter -> va sur la page principal
            NextActivity();
        } else {
            System.out.println("Personne de connecté");
            //rien
        }
    }

    public void login(int ID, String Name, User user) {


        SessionManagement sessionManagement = new SessionManagement(SubActivity.this);
        sessionManagement.saveSession(user);

        int UserId = sessionManagement.getSession();

        System.out.println("Us : "+ UserId);

        NextActivity();
    }

    private void connection() {
        System.out.println("connection début");

        try
        {
            //Erreur dans ces deux ligne
            //Statement st = connectionSQL_BDD();


            String SQLuser = "SELECT username FROM utilisateur WHERE username = '"+pseudo.getText().toString()+"'";

            java.sql.Connection connuser = DriverManager.getConnection("jdbc:mysql://mysql-xencev.alwaysdata.net/xencev_site-perso", "xencev_root", "Tallys2001");
            Statement st = connuser.createStatement();
            ResultSet rsuser = st.executeQuery(SQLuser);
            System.out.println(SQLuser);


            //Si l'utilisateur existe déjà en base :
            if (rsuser.next() != false) {
                //Erreur
                ((TextView) findViewById(R.id.error)).setText("Le compte exise déjà.");
            } else {
                String SQLB = "INSERT INTO `utilisateur`(`username`, `mdp`) VALUES ('"+pseudo.getText().toString()+"','"+mdp.getText().toString()+"')";
                System.out.println(SQLB);

                int ValID = st.executeUpdate(SQLB);

                User user = new User(ValID,pseudo.getText().toString());

                login(ValID, pseudo.getText().toString(), user);
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void NextActivity() {
        Intent intent = new Intent(SubActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    public static Statement connectionSQL_BDD() {
        try
        {
            Class.forName("com.mysql.jdbc.Driver");

            //getConnection bug
            //java.sql.Connection conn = DriverManager.getConnection("jdbc:mysql://mysql-xencev.alwaysdata.net:3306/xencev_site-perso"+"user=xencev_root&password=Tallys2001");
            try (java.sql.Connection conn = DriverManager.getConnection("jdbc:mysql://mysql-xencev.alwaysdata.net/xencev_site-perso", "xencev_root", "Tallys2001"))
            {
                System.out.println("connectionSQL_BDD 1");
                Statement st = conn.createStatement();
                System.out.println(st);
                System.out.println("connectionSQL_BDD good ending");

                return st;
            } catch (SQLException e) {
                System.out.println("connectionSQL_BDD bad ending");

                throw new RuntimeException("Error executing sql:\n"+ e);
            }
        } catch (ClassNotFoundException e)
        {
            e.printStackTrace();
            System.out.println("connectionSQL_BDD bad ending");

            return null;
        }
    }

}