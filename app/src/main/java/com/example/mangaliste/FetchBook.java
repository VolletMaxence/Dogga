package com.example.mangaliste;

import android.os.AsyncTask;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FetchBook extends AsyncTask<String, Void, String> {
    private TextView mCodeData;
    private int OneShot;
    String Titre = null;
    String title = null;
    String NumeroTomeString;

    public FetchBook(TextView CodeData) {
        this.mCodeData = CodeData;
    }

    @Override
    protected String doInBackground(String... strings) {
        return NetworkUtilis.getBookInfo(strings[0]);
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);


        try {
            //Gerer le JSON :
            JSONObject jsonObject = new JSONObject(s);
            JSONArray itemsArray = jsonObject.getJSONArray("items");

            for (int i = 0; i < itemsArray.length(); i++) {
                JSONObject book = itemsArray.getJSONObject(i);
                String TitrePourNumTome = null;
                //JSONObject volumeInfo = book.getJSONObject("offers");

                try {
                    title = book.getString("title");
                    //TitrePourNumTome = volumeInfo.getString("title");

                } catch (Exception e) {
                    e.printStackTrace();
                }

                //Si on trouve en Titre et un auteur, on fait quelque chose
                if (title != null) {
                    //mCodeData.setText("Titre : "+ title + ", Auteur : "+ auteur);
                    System.out.println("XYZA : " + title);

                    //Cut le nom Entier pour récupérer uniquement le nom du manga
                    Pattern pattern01 = Pattern.compile("^([^\t]+) T(\\d+)");
                    Matcher matcher01 = pattern01.matcher(title);

                    if (matcher01.find()) {
                        System.out.println("Option 01");
                        Titre = matcher01.group(1);
                        NumeroTomeString = matcher01.group(2);
                    } else {
                        Pattern pattern02 = Pattern.compile("^([^\t]+) Tome (\\d+)");
                        Matcher matcher02 = pattern02.matcher(title);

                        if (matcher02.find()) {
                            System.out.println("Option 02");
                            Titre = matcher02.group(1);
                            NumeroTomeString = matcher02.group(2);
                        } else {
                            Pattern pattern03 = Pattern.compile("^([^\t]+) Tome numéro (\\d+)");
                            Matcher matcher03 = pattern03.matcher(title);

                            if (matcher03.find()) {
                                System.out.println("Option 03");
                                Titre = matcher03.group(1);
                                NumeroTomeString = matcher03.group(2);
                            } else {
                                Pattern pattern04 = Pattern.compile("^([^\t]+) Tome numero (\\d+)");
                                Matcher matcher04 = pattern04.matcher(title);
                                if (matcher04.find()) {
                                    System.out.println("Option 04");
                                    Titre = matcher04.group(1);
                                    NumeroTomeString = matcher04.group(2);
                                }
                            }
                        }
                    }
                } else
                {
                    Titre = title;
                    OneShot = 1;
                }
            }
                    String NumeroTome;

                    if (OneShot == 1)
                    {
                        NumeroTome = "1";
                    } else
                    {
                        NumeroTome = NumeroTomeString;
                    }

                    System.out.println("XYZ : "+ NumeroTome);
                    //System.out.println("XYZ : "+ );
                    mCodeData.setText("Titre : "+ Titre + ", Tome numéro "+ NumeroTome);

                    //Ajoute du livre dans la base de donnée
                    //Verifier si il n'y est pas deja :

                    String SQLVerif = "SELECT Nom FROM manga WHERE Nom = '"+ Titre +"'";
                    String SQLNum = "SELECT nbrTome FROM manga WHERE Nom = '"+ Titre +"'";
                    String SQLUpdateTome = "UPDATE manga SET `nbrTome`='"+ NumeroTome +"' WHERE `Nom`='" + Titre + "'";
                    String SQLAjout = "INSERT INTO manga (`Nom`, `nbrTome`) VALUES ('"+ Titre +"','"+ NumeroTome +"')";
                    String SQLID = "SELECT ID FROM manga WHERE Nom = '"+ Titre +"'";

                    java.sql.Connection conn = null;
                    try {
                        conn = DriverManager.getConnection("jdbc:mysql://mysql-xencev.alwaysdata.net/xencev_site-perso", "xencev_root", "Tallys2001");
                        Statement st = conn.createStatement();
                        ResultSet rs = st.executeQuery(SQLVerif);
                        System.out.println(SQLVerif);

                        if (rs.next() != false) {
                            System.out.println("Manga Trouvé :");
                            //Verifier numéro tome
                            conn = DriverManager.getConnection("jdbc:mysql://mysql-xencev.alwaysdata.net/xencev_site-perso", "xencev_root", "Tallys2001");
                            Statement stNum = conn.createStatement();
                            ResultSet rsNum = stNum.executeQuery(SQLNum);
                            System.out.println(SQLNum);
                            rsNum.next();

                            //String to int
                            int ResultRequete = Integer.parseInt(rsNum.getString(1));
                            int NumeroTomeInt = Integer.parseInt(NumeroTome);
                            System.out.println(ResultRequete);
                            System.out.println(NumeroTomeInt);

                            //Si le nbrTome en base est inferieur au nbrTome scanner :
                            if (ResultRequete < NumeroTomeInt) {
                                //Update de la base
                                conn = DriverManager.getConnection("jdbc:mysql://mysql-xencev.alwaysdata.net/xencev_site-perso", "xencev_root", "Tallys2001");
                                Statement stUpdateNum = conn.createStatement();
                                //Erreur
                                System.out.println(SQLUpdateTome);
                                stUpdateNum.executeUpdate(SQLUpdateTome);

                                System.out.println(SQLUpdateTome);
                                //rsUpdateNum.next();
                                System.out.println("XYZ : Update nbrTome");
                            } else
                            {
                                System.out.println("XYZ : Pas d'update de base");
                            }

                        } else {
                            System.out.println("Manga pas en base");
                            //Le livre n'est pas en base : ajout
                            conn = DriverManager.getConnection("jdbc:mysql://mysql-xencev.alwaysdata.net/xencev_site-perso", "xencev_root", "Tallys2001");
                            Statement stAjout = conn.createStatement();
                            int rsAdd = stAjout.executeUpdate(SQLAjout);
                            System.out.println(SQLAjout);
                            //rsAdd.next();
                            System.out.println("XYZ : Ajout du livre en base");
                        }

                        //Ajouter la liaison entre le livre scanner / l'utilisateur
                        //Chercher ID Manga

                        conn = DriverManager.getConnection("jdbc:mysql://mysql-xencev.alwaysdata.net/xencev_site-perso", "xencev_root", "Tallys2001");
                        Statement stID = conn.createStatement();
                        ResultSet rsID = stID.executeQuery(SQLID);
                        System.out.println(SQLID);
                        rsID.next();
                        //String to int
                        int IDManga = Integer.parseInt(rsID.getString(1));

                        //Recup ID utilisatuer (cf Connection) = UserId
                        int UserId = ScanActivity.getUserId();
                        System.out.println("User ID FetchBook : "+ UserId);

                        //Mettre la liaison dans la BDD si elle n'existe pas
                        //Verif
                        String SQLVerifID = "SELECT ID_Manga, nbrTomePosseder FROM mangaUtilisateur WHERE ID_Manga = '"+ IDManga +"' AND ID_Utilisateur = '"+ UserId +"'";
                        conn = DriverManager.getConnection("jdbc:mysql://mysql-xencev.alwaysdata.net/xencev_site-perso", "xencev_root", "Tallys2001");
                        Statement stVerif = conn.createStatement();
                        ResultSet rsVerif = stVerif.executeQuery(SQLVerifID);
                        System.out.println("SQLVerif : "+SQLVerif);

                        //Si la liaison exist
                        if(rsVerif.next() != false)
                        {
                            System.out.println("Liaison en base");
                            //Si problème, changement effectué ici :
                            int NumeroTomeInt = Integer.parseInt(NumeroTome);
                            System.out.println(rsVerif.getString(2));
                            System.out.println(NumeroTomeInt);

                            int ResultSQL = Integer.parseInt(rsVerif.getString(2));
                            //Si le nbrTome en base est inferieur au nbrTome scanner :
                            if (ResultSQL < NumeroTomeInt) {
                                //Update
                                String SQLUpdateNbrMangaLiaison = "UPDATE mangaUtilisateur SET `nbrTomePosseder`='"+ NumeroTome  +"' WHERE ID_Utilisateur = '"+ UserId +"' AND ID_Manga = '"+ IDManga +"'";
                                conn = DriverManager.getConnection("jdbc:mysql://mysql-xencev.alwaysdata.net/xencev_site-perso", "xencev_root", "Tallys2001");
                                Statement stUpdateNbrMangaLiaison = conn.createStatement();
                                int rsUpdateNbrMangaLiaison = stUpdateNbrMangaLiaison.executeUpdate(SQLUpdateNbrMangaLiaison);
                                System.out.println(rsUpdateNbrMangaLiaison);
                            }
                        } else {
                            System.out.println("Pas de liaison en base");
                            //Ajouter la liaison :
                            String SQLAjoutLiaison = "INSERT INTO mangaUtilisateur (`ID_Utilisateur`, `ID_Manga`, `nbrTomePosseder`, `nomManga`) VALUES ('"+ UserId +"','"+ IDManga +"','"+ NumeroTome +"','"+ Titre +"')";
                            conn = DriverManager.getConnection("jdbc:mysql://mysql-xencev.alwaysdata.net/xencev_site-perso", "xencev_root", "Tallys2001");
                            Statement stAjoutLiaison = conn.createStatement();
                            int rsAddLiaison = stAjoutLiaison.executeUpdate(SQLAjoutLiaison);
                            System.out.println(rsAddLiaison);
                        }
                    } catch (SQLException throwables)
                    {
                        throwables.printStackTrace();
                    }

                /* else
                {
                    mCodeData.setText("Erreur dans la requete API.");
                } */

        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}
