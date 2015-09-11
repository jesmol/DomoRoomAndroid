package com.example.daniel.domoroom;
import java.util.ArrayList;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import co.domosapiens.domoroom.mundo.ChannelManager;
import co.domosapiens.domoroom.mundo.ChannelManagerListener;
import mundo.ContenedoraTemas;
import mundo.Tema;

public class ActividadPrincipal extends AppCompatActivity  implements ChannelManagerListener{

    /**
     * El administrador para la conexion Wi-fi y Bluetooth
     */
    private ChannelManager manager;

    /**
     * Relacion con el mundo de la aplicacion
     */
    private ContenedoraTemas contenedora;


    /**
     * Método constructor que crea el menú de la aplicación
     * @param savedInstanceState: - Bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vista_principal);

        manager = ChannelManager.getInstance(this);
        contenedora = new ContenedoraTemas();

        // Set up the action bar.
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);


        // Crea las pestanas por cada funcion de la aplicacion
        ActionBar.Tab tab = actionBar.newTab()
                .setText("Menu RGB")
                .setTabListener(new TabsListener(
                        this, "rgb", MenuRGB.class));
        actionBar.addTab(tab);

        ActionBar.Tab tab1 = actionBar.newTab()
                .setText("Deportes")
                .setTabListener(new TabsListener(
                        this, "deportes", Deportes.class));
        actionBar.addTab(tab1);

        ActionBar.Tab tab2 = actionBar.newTab()
                .setText("Cine y Series")
                .setTabListener(new TabsListener(
                        this, "cine", CineSeries.class));
        actionBar.addTab(tab2);


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        manager.destroy();
    }

    public void enableBluetooth() {
        if (!BluetoothAdapter.getDefaultAdapter().isEnabled()) {
            Intent enableBluetooth = new Intent(
                    BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBluetooth, 0);
        }
    }

    public void onEvent(final String event) {
        runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(getApplicationContext(), event,
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onActiveChange(final boolean value) {
        runOnUiThread(new Runnable() {
            public void run() {
                //TODO
            }
        });
    }

    /**
     * Agrema el tema en el perfil de Cines y Series
     * @param color - Color que representa el tema
     * @param nombreTema - Nombre del Tema a guardar
     */
    public void agregarTemaCine(String color, String nombreTema){
         boolean agrego = contenedora.agregarNuevoTema(color,nombreTema, "cine");

       if(agrego)
           Toast.makeText(getApplicationContext(),"Se agrego correctamente al perfil Cine y Deportes",Toast.LENGTH_LONG).show();

       else Toast.makeText(getApplicationContext(),"ya existe un nombre con ese tema dentro de este perfil",Toast.LENGTH_LONG).show();

    }

    /**
     * Agrega el tema en el perfil de Deportes
     * @param color - Color que representa el tema
     * @param nombreTema - Nombre del tema a guardar
     */
    public void agregarTemaDeportes(String color, String nombreTema){
       boolean agrego = contenedora.agregarNuevoTema(color,nombreTema, "deportes");

        if(agrego)
           Toast.makeText(getApplicationContext(),"Se agrego correctamente al perfil de Deportes",Toast.LENGTH_LONG).show();

       else Toast.makeText(getApplicationContext(),"ya existe un nombre con ese tema dentro de este perfil",Toast.LENGTH_LONG).show();
    }

    /**
     * Envia la informacion del color a la arduino por la posicion en que se encuentre la lista y el perfil de búsqueda
     * @param posicion - int que representa la posición en la lista de temas
     * @param perfil - cadena de texto con el perfil a buscar el tema
     */
    public void enviarInfoColor(int posicion, String perfil){
        try {
            manager.EnviarPorBluetoothPapa(contenedora.darColorTema(posicion, perfil));
        }catch(Exception e){
            Toast.makeText(getApplicationContext(),"No se puede enviar la info",Toast.LENGTH_LONG).show();
        }
    }

    /**
     * @return una contenedora de cadenas de texto con todos los temas del perfil Cine y Series
     */
    public ArrayList<String> darTemasCine(){
        return contenedora.darTemasCine();
    }

    /**
     * @return una contenedora de cadenas de texto con todos los temas del perfil Deportes
     */
    public ArrayList<String> darTemasDeportes(){
        return contenedora.darTemasDeporte();
    }

/**
 * Clase Parcial para manejar las acciones del menú
 */
    public class TabsListener  implements ActionBar.TabListener {

    /**
     *  Fragmento de la actividad principal
     */
        private Fragment fragment;

    /**
     * Un identificador, nombre del fragmento
     */
        private final String tag;


    /**
     * Constructor de la clase parcial
     * Instancia el objeto tipo Fragment por la clase que hace referencia y la actividad principal
     @param activity: Parámetro de tipo Activity que hace referencia a la actividad en donde se alojará el fragmento
     @param cls: Parámetro tipo Class que hace referencia a la clase .java que manejará todos los eventos relacionados con el Fragmento .xml
     */
        public TabsListener(Activity activity, String tag, Class cls) {
            this.tag = tag;
            fragment = Fragment.instantiate(activity, cls.getName());
        }


    /**
     * Cambia la pestaña seleccionada por el usuario
     * @param tab: Instancia de la pestaña que el usuario utilizó
     * @param ft: FragmentTransaction
     */
        public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
            ft.replace(android.R.id.content, fragment, tag);
        }

    /**
     *Evento que maneja cuando ya no es seleccionada la pestaña.
     * @param tab: Instancia de la pestaña que el usuario utilizó
     * @param ft: FragmentTransaction
     */
        public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
            ft.remove(fragment);
        }

        public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {}
    }
}
