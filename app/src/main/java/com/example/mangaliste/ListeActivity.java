package com.example.mangaliste;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

public class ListeActivity extends AppCompatActivity {

    private static int UserId;
    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste);

        linearLayout = (LinearLayout)findViewById(R.id.linearLayout);

        SessionManagement sessionManagement = new SessionManagement(ListeActivity.this);
        UserId = sessionManagement.getSession();

        String SQLListe = "SELECT `nomManga`,`nbrTomePosseder` FROM `mangaUtilisateur` WHERE `ID_Utilisateur` = "+ UserId +" ORDER BY `nomManga` ASC";

        java.sql.Connection connliste = null;
        try {
            connliste = DriverManager.getConnection("jdbc:mysql://mysql-xencev.alwaysdata.net/xencev_site-perso", "xencev_root", "Tallys2001");
            Statement stliste = connliste.createStatement();
            ResultSet rsliste = stliste.executeQuery(SQLListe);
            ResultSetMetaData rsmetadataliste = rsliste.getMetaData();
            System.out.println(SQLListe);

            while (rsliste.next())
            {
                int i = 1;
                System.out.println("Valeur 1 : "+rsliste.getString(1));
                System.out.println("Valeur 2 : "+rsliste.getString(2));

                //Créer dynamiquement des TextView :
                //Nom du manga
                TextView Titre = new TextView(this);
                Titre.setText(rsliste.getString(1));
                Titre.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                Titre.setGravity(Gravity.CENTER_HORIZONTAL);
                linearLayout.addView(Titre);

                //nbrTome posséder
                TextView nbrTome = new TextView(this);
                int nbrTomeSQL = Integer.parseInt(rsliste.getString(2));
                if (nbrTomeSQL == 1)
                {
                    nbrTome.setText(rsliste.getString(2)+" tome");
                } else
                {
                    nbrTome.setText(rsliste.getString(2)+" tomes");
                }
                nbrTome.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                nbrTome.setGravity(Gravity.CENTER_HORIZONTAL);
                linearLayout.addView(nbrTome);

                //Délimitation entre 2 Manga
                TextView Delimitation = new TextView(this);
                Delimitation.setText("—————————————————————");
                Delimitation.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                Delimitation.setGravity(Gravity.CENTER_HORIZONTAL);
                linearLayout.addView(Delimitation);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}