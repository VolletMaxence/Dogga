package com.example.dogga;

import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;

public class NetworkUtilis {
    private static final String LOG_TAG = NetworkUtilis.class.getSimpleName();
    private static final String BOOK_BASE_URL = "https://www.googleapis.com/books/v1/";
    private static final String KEY_BASE_URL = "&key=";

    //! A Changer pour mettre dans un dossier de configuration sécuriser !

    private static final String QUERY_PARAM = "volumes?q=isbn:";
    private static final String MAX_RESULT = "maxResults";
    private static final String PRINT_TYPE = "printType";

    public NetworkUtilis() {
    }

    static String getBookInfo(String queryString) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String BookJSONString = null;

        try {

            Uri builtUri = Uri.parse(BOOK_BASE_URL).buildUpon()
                    .appendQueryParameter(QUERY_PARAM, queryString)
                    .appendQueryParameter(KEY_BASE_URL, ReadConfig.api_key)
                    .build();

            System.out.println("XYZA : "+ builtUri);

            URL requestURL = new URL(builtUri.toString());

            urlConnection = (HttpURLConnection) requestURL.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            //Lecture de la réponse :
            InputStream inputStream = urlConnection.getInputStream();
            StringBuilder buffer = new StringBuilder();
            if(inputStream == null)
            {
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null)
            {
                buffer.append(line).append("\n");
            }
            if (buffer.length() == 0)
            {
                return null;
            }
            BookJSONString = buffer.toString();
        } catch (Exception e)
        {
            e.printStackTrace();
            return null;
        } finally {
            if(urlConnection!=null)
            {
                urlConnection.disconnect();
            }
            if (reader != null)
            {
                try
                {
                    reader.close();
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            Log.d(LOG_TAG, BookJSONString);
        }
        return BookJSONString;
    }


}
