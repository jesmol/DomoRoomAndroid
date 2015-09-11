package com.example.daniel.domoroom;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import mundo.ContenedoraTemas;
import mundo.Tema;

/**
 * Created by Daniel on 21/08/2015.
 */
public class MenuRGB extends Fragment implements View.OnClickListener {

    /**
     * Vista
     */
    View rootView;

    /**
     * Botones para los colores de las luces LED
     */
   private Button butRojo, butAzul, butVerde, butMorado, butAmarillo, butBlanco, butRosado, butNaranja;

    /**
     * Botones para decidir en qué Pestana se guardara los perfiles
     */
   private ToggleButton butDeporte, butCine;

    /**
     *Campo de texto para el nombre del perfil
     */
    private TextView txtNombre;

    /**
     * Referencia a la clase Principal
     */
    private ActividadPrincipal principal;

    /**
     * Método constructor de la vista donde se instanciará cada elemento y creará la acción de cuando se presione el botón
     * para comenzar el proceso de reconocimiento de voz
     *@param inflater - inflater
     *@param container - ViewGroup
     *@param savedInstanceState - Bundle
     *@return  una Vista con todos los elementos dentro del fragmento instanciados
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.menu_rgb, container, false);

        principal = (ActividadPrincipal) getActivity();

        txtNombre = (TextView) rootView.findViewById(R.id.textTema);

        butAzul = (Button) rootView.findViewById(R.id.butAzul);
        butAzul.setOnClickListener(this);

        butRojo = (Button) rootView.findViewById(R.id.butRojo);
        butRojo.setOnClickListener(this);

        butVerde = (Button) rootView.findViewById(R.id.butVerde);
        butVerde.setOnClickListener(this);

        butMorado = (Button) rootView.findViewById(R.id.butMorado);
        butMorado.setOnClickListener(this);

        butAmarillo = (Button) rootView.findViewById(R.id.butAmar);
        butAmarillo.setOnClickListener(this);

        butBlanco = (Button) rootView.findViewById(R.id.butBlanco);
        butBlanco.setOnClickListener(this);

        butRosado = (Button) rootView.findViewById(R.id.butRosado);
        butRosado.setOnClickListener(this);

        butNaranja = (Button) rootView.findViewById(R.id.butNaranja);
        butNaranja.setOnClickListener(this);

        butDeporte = (ToggleButton) rootView.findViewById(R.id.togDeporte);
        butDeporte.setOnClickListener(this);

        butCine = (ToggleButton) rootView.findViewById(R.id.togCine);
        butCine.setOnClickListener(this);

        return rootView;
    }


    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        if(butAzul == v){
            manejoEvento("Azul",String.valueOf(txtNombre.getText()));
            txtNombre.setText("");
        }

        else if(butRojo == v){
            manejoEvento("Rojo",String.valueOf(txtNombre.getText()));
            txtNombre.setText("");
        }

        else if(butVerde == v){
            manejoEvento("Verde",String.valueOf(txtNombre.getText()));
            txtNombre.setText("");
        }

        else if(butAmarillo == v){
            manejoEvento("Amarillo",String.valueOf(txtNombre.getText()));
            txtNombre.setText("");
        }

        else if(butBlanco == v){
            manejoEvento("Blanco",String.valueOf(txtNombre.getText()));
            txtNombre.setText("");
        }

        else if(butNaranja == v){
            manejoEvento("Naranja",String.valueOf(txtNombre.getText()));
            txtNombre.setText("");
        }

        else if(butRosado == v){
            manejoEvento("Rosado",String.valueOf(txtNombre.getText()));
            txtNombre.setText("");
        }

        else if(butMorado == v){
            manejoEvento("Morado",String.valueOf(txtNombre.getText()));
            txtNombre.setText("");
        }
    }

    /**
     * Verify the conditions to create a perfil, the name is not empty and the user pressed only one of the toggle buttons
     * @param color
     * @param nombreTema
     */
    private void manejoEvento(String color, String nombreTema){

        AlertDialog.Builder alerta = new AlertDialog.Builder(getActivity());

        if(!nombreTema.equals("")){
            if(butCine.isChecked() && !butDeporte.isChecked()){

                principal.agregarTemaCine(color, nombreTema);
            }

            else if(butDeporte.isChecked() && !butCine.isChecked()){
                principal.agregarTemaDeportes(color, nombreTema);
            }

            else{
               alerta.setMessage("No puedes tener activados o desactivados al mismo tiempo los 2 botones").create();
                alerta.show();
            }
        }
        else{
            alerta.setMessage("El nombre del Tema no puede ser vacio").create();
            alerta.show();
        }
    }

}
