package com.example.dogga;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class ReadConfig {

    static String api_key;

    static String url_bdd;

    static String username_bdd;

    static String mdp_bdd;

    public static void main(String[] args) throws IOException {
        Properties properties = new Properties(); // This class is available in java
        FileInputStream config= new FileInputStream("config.properties");

        properties.load(config);

        api_key = properties.getProperty("key");
        url_bdd = properties.getProperty("url");
        username_bdd = properties.getProperty("name");
        mdp_bdd = properties.getProperty("mdp");
    }
}
