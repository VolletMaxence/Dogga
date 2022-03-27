package com.example.mangaliste;

import static androidx.constraintlayout.motion.utils.Oscillator.TAG;

import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.Buffer;

public class NetworkUtilis {
    private static final String LOG_TAG = NetworkUtilis.class.getSimpleName();
    private static final String BOOK_BASE_URL = "https://api.upcitemdb.com/prod/trial/lookup?";
    private static final String QUERY_PARAM = "upc";
    private static final String MAX_RESULT = "maxResults";
    private static final String PRINT_TYPE = "printType";

    static String getBookInfo(String queryString) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String BookJSONString = null;

        try {

            Uri builtUri = Uri.parse(BOOK_BASE_URL).buildUpon()
                    .appendQueryParameter(QUERY_PARAM, queryString)
                    //.appendQueryParameter(MAX_RESULT, "10")
                    //.appendQueryParameter(PRINT_TYPE, "books")
                    .build();

            System.out.println("XYZA : "+ builtUri);

            URL requestURL = new URL(builtUri.toString());

            urlConnection = (HttpURLConnection) requestURL.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            //Lecture de la réponse :
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if(inputStream == null)
            {
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null)
            {
                buffer.append(line + "\n");
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
            return BookJSONString;
        }
    }


}
