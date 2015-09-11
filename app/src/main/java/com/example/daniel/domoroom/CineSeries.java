package com.example.daniel.domoroom;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import co.domosapiens.domoroom.mundo.ChannelManager;

import static android.widget.AdapterView.*;

/**
 * Created by Daniel on 21/08/2015.
 */
public class CineSeries extends Fragment {

    /**
     * Constante para manejar el codigo de reconocimiento de voz
     */
    private static final int REQUEST_CODE = 1234;

    /**
     * Vista parcial de la aplicación
    */
    View rootView;

    /**
     * Actividad principal del sistema
     */
    private ActividadPrincipal principal;

    /**
     * Instancia del boton que inicia el comando de voz
     */
    Button speakButton;

    /**
     * Boton para cargar la lista de temas del perfil Cines y Series
     */
    private Button butCargarTemas;

    /**
     * Lista para mostrar todos los temas guardados del perfil Cine y Series
     */
    ListView listaResultado;

    /**
     * String donde se guarda la palabra que reconoce el comando de voz
     */
    String palabra="";



    /**
     *Método constructor de la vista donde se instanciará cada elemento y creará la acción de cuando se presione el botón
     * para comenzar el proceso de reconocimiento de voz
     * @param inflater - inflater
     * @param container - ViewGroup
     * @param savedInstanceState - Bundle
     * @return una Vista con el botón de comando de voz(y su acción) y la lista instanciados
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fm_cine, container, false);

        principal = (ActividadPrincipal) getActivity();

        butCargarTemas = (Button) rootView.findViewById(R.id.butTemasC);

        speakButton = (Button) rootView.findViewById(R.id.comandoVCine);
        listaResultado = (ListView) rootView.findViewById(R.id.listView);

        PackageManager pm = getActivity().getPackageManager();
        List<ResolveInfo> activities = pm.queryIntentActivities(new Intent(
                RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
        if (activities.size() == 0) {
            speakButton.setEnabled(false);
            Toast.makeText(getActivity().getApplicationContext(), "Recognizer Not Found", Toast.LENGTH_SHORT).show();
        }

        speakButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startVoiceRecognitionActivity();
            }
        });

        butCargarTemas.setOnClickListener(new View.OnClickListener() {

            /**
             * Called when a view has been clicked.
             *
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {

                cargarTemas();
            }

        });

        listaResultado.setOnItemClickListener(new OnItemClickListener() {
            /**
             * Callback method to be invoked when an item in this AdapterView has
             * been clicked.
             * <p/>
             * Implementers can call getItemAtPosition(position) if they need
             * to access the data associated with the selected item.
             *
             * @param parent   The AdapterView where the click happened.
             * @param view     The view within the AdapterView that was clicked (this
             *                 will be a view provided by the adapter)
             * @param position The position of the view in the adapter.
             * @param id       The row id of the item that was clicked.
             */
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                principal.enviarInfoColor(position, "cine");
            }
        });


        return rootView;
    }

    /**
     * Carga los temas del perfil Cine y Series
    */
    private void cargarTemas(){
        ArrayList<String> lista = principal.darTemasCine();

        listaResultado.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,lista));
    }

    /**
     *Inicializa la actividad de reconocimiento de voz
     */
    private void startVoiceRecognitionActivity() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                "Hable por favor!!!");
        startActivityForResult(intent, REQUEST_CODE);
    }


    /**
     *Descripción:
     * @param requestCode
     * @param resultCode
     * @param data
    */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        ArrayList<String> lista = new ArrayList<String>();

        if (requestCode == REQUEST_CODE & resultCode == getActivity().RESULT_OK) {
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            lista.add(matches.get(0));
            this.palabra = lista.get(0);
//	   resultList.setAdapter(new ArrayAdapter<String>(this,
//	     android.R.layout.simple_list_item_1, matches));
           // listaResultado.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,lista));

        }
        super.onActivityResult(requestCode, resultCode, data);

        TextView consola = (TextView) rootView.findViewById(R.id.textoChequeoCine);
        if(lista.size()== 0){
            AlertDialog.Builder alerta = new AlertDialog.Builder(getActivity());
            alerta.setMessage("No se reconocio la voz por undirle 2 veces click al boton de comando de voz").create();
            alerta.show();
        }
        else{
            procesarMetodo(lista.get(0), consola);
        }
    }

    /**
     * Ejecuta el comando de voz con la URL
     * @param url: cadena de texto en formato HTTP con el request y comando para ejecutar en el servidor
     * @param consola: TextView donde se verá el comando que se ejecutó
     * @param ejecuto: Cadena de texto con el comando a ejecutar
     */
    public void ejecutarURL(String url, TextView consola, String ejecuto)
    {
        ConnectivityManager connMgr = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            new HttpClient().execute(url.toString());
            consola.setText("Console: "+ejecuto);
        } else {
            consola.setText("Ocurrio un error al conectar con XBMC");
        }
    }

    /**
     * Verifica que el string que entra como parámetro sea una de las funciones definidas para enviar el comando por HTTP
     * @param metodo: String con la cadena reconocida por el comando de voz
     *@param consola: TextView donde se podrá verificar qué comando se ejecutó y envió al servidor o si hubo algún error en el
     *              envío de información
     */
    public void procesarMetodo(String metodo, TextView consola)
    {
        String ipXmbc = ChannelManager.OPEN_ELEC_IP;
        String met = "";
        String[] palabras = metodo.split(" ");
        if (metodo.equals("arriba")) {
            met = "http://"+ipXmbc+"/jsonrpc?request={%22jsonrpc%22:%20%222.0%22,%20%22id%22:%201,%20%22method%22:%20%22Input.Up%22}";
            ejecutarURL(met, consola, "Ejecuto Arriba");

        } else if (metodo.equals("abajo")) {
            met = "http://"+ipXmbc+"/jsonrpc?request={%22jsonrpc%22:%20%222.0%22,%20%22id%22:%201,%20%22method%22:%20%22Input.Down%22}";
            ejecutarURL(met, consola, "Ejecuto Abajo");

        } else if (metodo.equals("izquierda")) {
            met = "http://"+ipXmbc+"/jsonrpc?request={%22jsonrpc%22:%20%222.0%22,%20%22id%22:%201,%20%22method%22:%20%22Input.Left%22}";
            ejecutarURL(met, consola, "Ejecuto izquierda");

        } else if (metodo.equals("derecha")) {
            met = "http://"+ipXmbc+"/jsonrpc?request={%22jsonrpc%22:%20%222.0%22,%20%22id%22:%201,%20%22method%22:%20%22Input.Right%22}";
            ejecutarURL(met, consola, "Ejecuto derecha");

        } else if (metodo.equals("aceptar")) {
            met = "http://"+ipXmbc+"/jsonrpc?request={%22jsonrpc%22:%20%222.0%22,%20%22id%22:%201,%20%22method%22:%20%22Input.Select%22}";
            ejecutarURL(met, consola, "Ejecuto aceptar");
        }

        else if (metodo.equals("home") || metodo.equals("inicio") || metodo.equals("salir")) {
            met = "http://"+ipXmbc+"/jsonrpc?request={%22jsonrpc%22:%20%222.0%22,%20%22id%22:%201,%20%22method%22:%20%22Input.Home%22}";
            ejecutarURL(met, consola, "Ejecuto Home");
        }
        else if (metodo.equalsIgnoreCase("regresar") || metodo.equalsIgnoreCase("volver") || metodo.equalsIgnoreCase("atrás")) {
            met = "http://"+ipXmbc+"/jsonrpc?request={%22jsonrpc%22:%20%222.0%22,%20%22id%22:%201,%20%22method%22:%20%22Input.Back%22}";
            ejecutarURL(met, consola, "Ejecuto regresar");
        }
        else if (metodo.equalsIgnoreCase("imágenes")) {
            met = "http://"+ipXmbc+"/jsonrpc?request={%20%22jsonrpc%22:%20%222.0%22,%20%22method%22:%20%22Input.ExecuteAction%22,%20%22params%22:%20{%20%22action%22:%20%22pageup%22%20},%20%22id%22:%201%20}";
            ejecutarURL(met, consola, "Usted dijo: Imágenes");
            //met = "http://"+ipXmbc+"/jsonrpc?request={%22jsonrpc%22:%20%222.0%22,%20%22id%22:%201,%20%22method%22:%20%22Input.Select%22}";
            //ejecutarURL(met, consola, "Ejecuto aceptar");
        }
        else if (metodo.equalsIgnoreCase("música")) {
            met = "http://"+ipXmbc+"/jsonrpc?request={%20%22jsonrpc%22:%20%222.0%22,%20%22method%22:%20%22Input.ExecuteAction%22,%20%22params%22:%20{%20%22action%22:%20%22pageup%22%20},%20%22id%22:%201%20}";
            ejecutarURL(met, consola, "");
            met = "http://"+ipXmbc+"/jsonrpc?request={%22jsonrpc%22:%20%222.0%22,%20%22id%22:%201,%20%22method%22:%20%22Input.Right%22}";
            ejecutarURL(met, consola, "");
            met = "http://"+ipXmbc+"/jsonrpc?request={%22jsonrpc%22:%20%222.0%22,%20%22id%22:%201,%20%22method%22:%20%22Input.Right%22}";
            ejecutarURL(met, consola, "Seleccionó Música");
            //met = "http://"+ipXmbc+"/jsonrpc?request={%22jsonrpc%22:%20%222.0%22,%20%22id%22:%201,%20%22method%22:%20%22Input.Select%22}";
            //ejecutarURL(met, consola, "Ejecuto aceptar");
        }
        else if (metodo.equalsIgnoreCase("videos") || metodo.equalsIgnoreCase("nueva búsqueda")) {
            met = "http://"+ipXmbc+"/jsonrpc?request={%20%22jsonrpc%22:%20%222.0%22,%20%22method%22:%20%22Input.ExecuteAction%22,%20%22params%22:%20{%20%22action%22:%20%22pageup%22%20},%20%22id%22:%201%20}";
            ejecutarURL(met, consola, "");
            met = "http://"+ipXmbc+"/jsonrpc?request={%22jsonrpc%22:%20%222.0%22,%20%22id%22:%201,%20%22method%22:%20%22Input.Right%22}";
            ejecutarURL(met, consola, "Seleccionó Videos");
            //met = "http://"+ipXmbc+"/jsonrpc?request={%22jsonrpc%22:%20%222.0%22,%20%22id%22:%201,%20%22method%22:%20%22Input.Select%22}";
            //ejecutarURL(met, consola, "Ejecuto aceptar");
        }

        else if (metodo.equalsIgnoreCase("programas") || metodo.equalsIgnoreCase("búsqueda")) {
            met = "http://"+ipXmbc+"/jsonrpc?request={%20%22jsonrpc%22:%20%222.0%22,%20%22method%22:%20%22Input.ExecuteAction%22,%20%22params%22:%20{%20%22action%22:%20%22pageup%22%20},%20%22id%22:%201%20}";
            ejecutarURL(met, consola, "");
            met = "http://"+ipXmbc+"/jsonrpc?request={%22jsonrpc%22:%20%222.0%22,%20%22id%22:%201,%20%22method%22:%20%22Input.Right%22}";
            ejecutarURL(met, consola, "");
            met = "http://"+ipXmbc+"/jsonrpc?request={%22jsonrpc%22:%20%222.0%22,%20%22id%22:%201,%20%22method%22:%20%22Input.Right%22}";
            ejecutarURL(met, consola, "");
            met = "http://"+ipXmbc+"/jsonrpc?request={%22jsonrpc%22:%20%222.0%22,%20%22id%22:%201,%20%22method%22:%20%22Input.Right%22}";
            ejecutarURL(met, consola, "Usted dijo: programas");
            //met = "http://"+ipXmbc+"/jsonrpc?request={%22jsonrpc%22:%20%222.0%22,%20%22id%22:%201,%20%22method%22:%20%22Input.Select%22}";
            //ejecutarURL(met, consola, "Ejecuto aceptar");
        }
        else if (metodo.equalsIgnoreCase("sistema")) {
            met = "http://"+ipXmbc+"/jsonrpc?request={%20%22jsonrpc%22:%20%222.0%22,%20%22method%22:%20%22Input.ExecuteAction%22,%20%22params%22:%20{%20%22action%22:%20%22pagedown%22%20},%20%22id%22:%201%20}";
            ejecutarURL(met, consola, "");
            //met = "http://"+ipXmbc+"/jsonrpc?request={%22jsonrpc%22:%20%222.0%22,%20%22id%22:%201,%20%22method%22:%20%22Input.Select%22}";
            //ejecutarURL(met, consola, "Ejecuto aceptar");
        }
        else if (metodo.equalsIgnoreCase("youtube")) {
            met = "http://"+ipXmbc+"/jsonrpc?request={%22jsonrpc%22:%20%222.0%22,%20%22id%22:%201,%20%22method%22:%20%22Input.Down%22}";
            ejecutarURL(met, consola, "");
            met = "http://"+ipXmbc+"/jsonrpc?request={%22jsonrpc%22:%20%222.0%22,%20%22id%22:%201,%20%22method%22:%20%22Input.Down%22}";
            ejecutarURL(met, consola, "Accediendo a Youtube");
            met = "http://"+ipXmbc+"/jsonrpc?request={%20%22jsonrpc%22:%20%222.0%22,%20%22method%22:%20%22Input.ExecuteAction%22,%20%22params%22:%20{%20%22action%22:%20%22pagedown%22%20},%20%22id%22:%201%20}";
            ejecutarURL(met, consola, "");
            //met = "http://"+ipXmbc+"/jsonrpc?request={%22jsonrpc%22:%20%222.0%22,%20%22id%22:%201,%20%22method%22:%20%22Input.Select%22}";
            //ejecutarURL(met, consola, "Ejecuto aceptar");
        }

        else if (metodo.equalsIgnoreCase("pausar") || metodo.equalsIgnoreCase("pausa") || metodo.equalsIgnoreCase("reproducir")) {
            met = "http://"+ipXmbc+"/jsonrpc?request={%22jsonrpc%22:%222.0%22,%22method%22:%22Player.PlayPause%22,%22params%22:{%22playerid%22:%201%20},%22id%22:1}";
            ejecutarURL(met, consola, "");
        }

        else if (metodo.equalsIgnoreCase("detener") || metodo.equalsIgnoreCase("parar" +
                "")|| metodo.equalsIgnoreCase("stop") || metodo.equalsIgnoreCase("sop") ) {
            met = "http://"+ipXmbc+"/jsonrpc?request={%22jsonrpc%22:%222.0%22,%22method%22:%22Player.stop%22,%22params%22:{%22playerid%22:%201%20},%22id%22:1}";
            ejecutarURL(met, consola, "Audio o Video detenido");
        }

        else if (palabras[0].equals("buscar")) {

            String busqueda = "";
            for(int i = 1; i < palabras.length; i++)
            {
                if( i == palabras.length-1 )
                    busqueda += palabras[i];

                else
                    busqueda += busqueda + palabras[i]+"%20";

            }
            busqueda = "%22" + busqueda +"%22";
            met = "http://"+ipXmbc+"/jsonrpc?request={%22jsonrpc%22:%222.0%22,%22id%22:%220%22,%22method%22:%22Input.SendText%22,%22params%22:{%22text%22:"+busqueda+",%22done%22:true}}";

            ejecutarURL(met, consola, "Buscando canción" );


        }
        else
        {
            ejecutarURL(met, consola, "Comando no reconocido");
        }
    }
}
