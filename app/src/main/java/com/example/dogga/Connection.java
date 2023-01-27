package com.example.dogga;

import static java.util.Objects.requireNonNull;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
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

public class Connection extends AppCompatActivity {

    private EditText mdp;
    private EditText pseudo;

    public static final String SHARED_PREFS = "shared_prefs";
    SharedPreferences sharedpreferences;
    public static final String Pseudo_KEY = "pseudo_key";
    String pseudoStock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection);

        pseudo = (EditText) findViewById(R.id.pseudo);
        mdp = (EditText) findViewById(R.id.mdp);

        Button connect = (Button) findViewById(R.id.connect);
        Button mSubButton = (Button) findViewById(R.id.Sub);

        TextView error = (TextView) findViewById(R.id.error);

        sharedpreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        pseudoStock = sharedpreferences.getString(Pseudo_KEY, null);

        connect.setOnClickListener(view -> connection());

        mSubButton.setOnClickListener(view -> openSubPage());

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

    }

    private void openSubPage() {
        Intent intent = new Intent(Connection.this, SubActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onStart(){
        super.onStart();

        checkSession();
    }

    private void checkSession() {
        //Renvoie utilisateur si il est deja connecter
        SessionManagement sessionManagement = new SessionManagement(Connection.this);
        int UserId = sessionManagement.getSession();

        System.out.println("User ID : "+UserId);
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


        SessionManagement sessionManagement = new SessionManagement(Connection.this);
        sessionManagement.saveSession(user);

        int UserId = sessionManagement.getSession();

        System.out.println("Us : "+ UserId);

        NextActivity();
    }

    @SuppressLint("SetTextI18n")
    private void connection() {
        System.out.println("connection début");

         try
         {
             //Erreur dans ces deux ligne
             //Statement st = connectionSQL_BDD();

             String SQL = "SELECT mdp FROM utilisateur WHERE username = '"+pseudo.getText().toString()+"'";

             java.sql.Connection conn = DriverManager.getConnection("jdbc:mysql://mysql-xencev.alwaysdata.net/xencev_site-perso", "xencev_root", "Tallys2001");
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(SQL);
             System.out.println(SQL);

             rs.next();

             if (rs.getString(1).equals(mdp.getText().toString())) {
                 //Afficher message d'erreur
                 String SQLB = "SELECT ID FROM utilisateur WHERE username = '"+pseudo.getText().toString()+"' AND mdp = '"+mdp.getText().toString()+"'";
                 System.out.println(SQLB);

                 final ResultSet rsb = st.executeQuery(SQLB);
                 rsb.next();

                 int ValID = ((Number) rsb.getObject(1)).intValue();

                 User user = new User(ValID,pseudo.getText().toString());

                 login(ValID, pseudo.getText().toString(), user);
             } else {
                 ((TextView) findViewById(R.id.error)).setText("Le Pseudo ou le Mot de Passe entré est incorrecte.");
             }
         } catch (Exception e)
         {
             e.printStackTrace();
         }
    }

    private void NextActivity() {
        Intent intent = new Intent(Connection.this, MainActivity.class);
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