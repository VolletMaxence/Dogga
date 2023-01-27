package com.example.dogga;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Spinner;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_autre);
        Spinner spinner = (Spinner) findViewById(R.id.spinner_utilisateur);

        //User[] employees = UserData.getData();

        //ArrayAdapter<User>adapter = new ArrayAdapter<User>(this,android.R.layout.simple_spinner_item,user);


        //Récupérer les Utilisateur
        String SQLListe = "SELECT `ID`,`username` FROM `utilisateur` ORDER BY `username` ASC";

        java.sql.Connection connliste;
        try {
            connliste = DriverManager.getConnection(ReadConfig.url_bdd, ReadConfig.username_bdd, ReadConfig.mdp_bdd);
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