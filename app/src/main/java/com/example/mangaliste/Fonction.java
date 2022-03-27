package com.example.mangaliste;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

public class Fonction {

    public static final String URL = "jdbc:mysql://mysql-xencev.alwaysdata.net/xencev_site-perso";
    public static final String User = "xencev_root";
    public static final String MDP = "Tallys2001";

    public static Statement connectionSQL_BDD() {
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection(URL, User, MDP);
            Statement st = conn.createStatement();
            return st;
        } catch (ClassNotFoundException e)
        {
            e.printStackTrace();
            return null;
        } catch (SQLException e)
        {
            e.printStackTrace();
            return null;
        }
    }
}
