package cl.telematica.android.certamen3.Connections;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by franciscocabezas on 11/18/16.
 */

public class HttpServerConnection {

    public String connectToServer(String myUrl, int timeOut){
        try {
            URL url = new URL(myUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setConnectTimeout(timeOut);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);

            conn.connect();

            InputStream is = conn.getInputStream();
            return readIt(is);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String readIt(InputStream stream) throws IOException {
        Reader reader = null;
        StringBuilder inputStringBuilder = new StringBuilder();

        reader = new InputStreamReader(stream, "UTF-8");
        BufferedReader bufferedReader = new BufferedReader(reader);

        String line = bufferedReader.readLine(); // Cada linea que se lee
        while(line != null){
            inputStringBuilder.append(line);
            inputStringBuilder.append('\n');
            line = bufferedReader.readLine();
        }
        return inputStringBuilder.toString();
    }

}
