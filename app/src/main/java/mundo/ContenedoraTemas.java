package mundo;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Daniel on 29/08/2015.
 */
public class ContenedoraTemas {

    /**
     * Contenedora de temas del perfil Cine y Series
     */
    private ArrayList<Tema> temasCine;

    /**
     * Contenedora de temas del perfil Deportes
     */
    private ArrayList<Tema> temasDeportes;

    /**
     * Método constructor de la clase que inicializa cada contenedora de temas
     */
    public ContenedoraTemas(){
        temasCine = new ArrayList<Tema>();
        temasDeportes = new ArrayList<Tema>();
    }

    /**
     * add a new Theme into the data structure
     * @param color:  color del tema
     * @param nombreTema: nombre del tema
     * @param tag: indica si es de perfil deportes o, cine
     * @return boolean que indica si se agrego o no el tema
     */
    public boolean agregarNuevoTema(String color, String nombreTema, String tag){
        boolean agrego = false;
        if(!verificarNombreExistente(nombreTema,tag)){
            if(tag.equals("cine")) {
                temasCine.add(new Tema(color, nombreTema));
            }
            else temasDeportes.add(new Tema(color, nombreTema));

            agrego = true;
        }

        return agrego;
    }

    /**
     * Verifica que no exista el nombre del tema dentro del ArrayList
     * @param nombre: Nombre del tema
     * @param tag: Indica cual de los 2 perfiles son: cine o deportes
     * @return boolean que indica si existe el nombre o no
     */
    private boolean verificarNombreExistente(String nombre, String tag){
        boolean existe = false;
        if(tag.equals("cine")){
            for(int i = 0; i< temasCine.size() && !existe; i++){
                if(temasCine.get(i).darNombreTema().equalsIgnoreCase(nombre))
                    existe = true;
            }
        }
        else{
            for(int i = 0; i< temasDeportes.size() && !existe; i++){
                if(temasDeportes.get(i).darNombreTema().equalsIgnoreCase(nombre))
                    existe = true;
            }
        }

        return existe;
    }

    /**
     * @return ArrayList de String con todos los nombres de los temas del perfil Cine
     */
    public ArrayList<String> darTemasCine(){
        ArrayList<String> temas = new ArrayList<String>();

        for(int i = 0; i< temasCine.size();i++){
            temas.add(temasCine.get(i).darNombreTema());
        }

        return temas;
    }

    /**
     * @return ArrayList de String con todos los nombres de los temas del perfil Deportes
     */
    public ArrayList<String> darTemasDeporte(){
        ArrayList<String> temas = new ArrayList<String>();

        for(int i = 0; i< temasDeportes.size();i++){
            temas.add(temasDeportes.get(i).darNombreTema());
        }

        return temas;
    }

    /**
     * Método que retorna una letra que representa el color del tema
     * @param posicion int - Número con la posición que se ha de buscar en la contenedora
     * @param perfil String - Cadena de texto que me indica en cuál perfil he de buscar esa posición
     * @return  char- una letra con el color que se encuentre en la posición de la contenedora de ese perfil
     */
    public char darColorTema(int posicion, String perfil){

        String color = "";
        if(perfil.equals("cine")){
            color = temasCine.get(posicion).darColor();
        }

        else color = temasDeportes.get(posicion).darColor();

        return retornarCharColor(color);
    }

    /**
     *Verifica que color es el que entra por parámetro para retornar una letra.
     * @param color
     * @return char que indica el color para ser enviado al sistema de luces
     */
    public char retornarCharColor(String color){
        char v = 'o';
        if(color.equalsIgnoreCase("Rojo"))
            v = 'r';
        else if(color.equalsIgnoreCase("Azul"))
            v = 'b';
        else if(color.equalsIgnoreCase("Verde"))
            v = 'v';
        else if(color.equalsIgnoreCase("Blanco"))
            v = 'w';
        else if(color.equalsIgnoreCase("Naranja"))
            v = 'n';
        else if(color.equalsIgnoreCase("Amarillo"))
            v = 'a';
        else if(color.equalsIgnoreCase("Morado"))
            v = 'm';
        else if(color.equalsIgnoreCase("Rosado"))
            v = 'p';

            return v;
    }
}
