package com.example.daniel.domoroom;
import android.os.AsyncTask;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Daniel on 27/08/2015.
 */
public class HttpClient extends AsyncTask<String, Void, String>
{
    /**
     * Método que se ejecuta en una forma background
     * @param urls: String con la dirección del servidor
     * @return String: Cadena con la información enviada por el servidor
     *                   En caso de que sea una dirección no válida retorna un mensaje de "URl inválida"
     */
    @Override
    protected String doInBackground(String... urls)
    {
        try {
            return cargarMetodo(urls[0]);
        } catch (IOException e) {
            return "Url inválida!!";
        }
    }

    /**
     * Request con el servidor para iniciar la comunicación Cliente-Servidor
     * @param URL: String de la dirección del servidor
     * @throws IOException: En caso de que aquel URL no exista
     * @return String: Devuelve una cadena de texto con la información que el servidor envió
     */
    public String cargarMetodo(String URL) throws IOException {
        InputStream in = null;
        try
        {
            URL xbmcURL = new URL (URL);
            HttpURLConnection urlConnection = (HttpURLConnection) xbmcURL.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoInput(true);
            // Starts the query
            urlConnection.connect();
            int response = urlConnection.getResponseCode();
            in = urlConnection.getInputStream();

            // Convert the InputStream into a string
            String contentAsString = readIt(in, 500);
            return contentAsString;
        }
        finally {
            if (in != null) {
                in.close();
            }
        }
    }

    /**
     * Lee un InputStream y lo convierte a String
     * @param stream: InputStream
     * @param len: Int con el tamaño a leer
     * @throws IOException: En caso de que la ruta de ese flujo de datos InputStream no exista
     * @return String
     */
    public String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);
    }
}
