package com.example.ray.game;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Handler;

public class ServerConnection {

    String response;

    public static final String GAME_SERVER = "http://game.nmi.com.ua/gameplay.php";

    public String request(final String params){

        return response;
    }

    public String sendRequestToServer(String params){

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String result = "";

        try {
            URL url = new URL(GAME_SERVER);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(15000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            urlConnection.connect();
            OutputStreamWriter writer = new OutputStreamWriter(urlConnection.getOutputStream());
            writer.write(params);
            writer.flush();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            result = buffer.toString();
            writer.close();
            reader.close();
            urlConnection.disconnect();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.d("GAME_LOGS", "response from server: " + result);

        return result;
    }

    class ServerTask extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... params) {
            for(int i = 1; i<=10; i++) {
                sendRequestToServer(params[0]);
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

    }

}
