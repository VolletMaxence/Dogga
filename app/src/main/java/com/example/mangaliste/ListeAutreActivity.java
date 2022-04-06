package com.example.mangaliste;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ListeAutreActivity extends AppCompatActivity {

    private static int UserId;
    LinearLayout linearLayout;
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_autre);
        this.spinner = (Spinner) findViewById(R.id.spinner_utilisateur);

        //User[] employees = UserData.getData();

        //ArrayAdapter<User>adapter = new ArrayAdapter<User>(this,android.R.layout.simple_spinner_item,user);


        //Récupérer les Utilisateur
        String SQLListe = "SELECT `ID`,`username` FROM `utilisateur` ORDER BY `username` ASC";

        java.sql.Connection connliste = null;
        try {
            connliste = DriverManager.getConnection("jdbc:mysql://mysql-xencev.alwaysdata.net/xencev_site-perso", "xencev_root", "Tallys2001");
            Statement stliste = connliste.createStatement();
            ResultSet rsliste = stliste.executeQuery(SQLListe);
            ResultSetMetaData rsmetadataliste = rsliste.getMetaData();
            System.out.println(SQLListe);

            while (rsliste.next())
            {
                System.out.println("Valeur 1 : "+rsliste.getString(1));
                System.out.println("Valeur 2 : "+rsliste.getString(2));


                //Créer dynamiquement des List :
                List<String> list = new ArrayList<String>();
                list.add(rsliste.getString(1));
                list.add(rsliste.getString(1));

            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}