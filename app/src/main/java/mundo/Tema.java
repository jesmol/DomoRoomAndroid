package mundo;

/**
 * Created by Daniel on 29/08/2015.
 */
public class Tema {

    /**
     * Cadena de texto con el color del Tema
     */
    private String color;

    /**
     * Cadena de texto con el nombre del Tema
     */
    private String nombreTema;

    /**
     * Método constructor que inicializa los atributos de la clase
     * @param color String
     * @param nombreTema String
     */
    public Tema(String color, String nombreTema){

        this.color = color;
        this.nombreTema = nombreTema;
    }

    /**
     * @return el nombre del tema
     */
    public String darNombreTema(){
        return nombreTema;
    }

    /**
     * @return el color del tema
     */
    public String darColor(){
        return color;
    }

    /**
     * @return Nombre del tema
     */
    public String toString(){
        return nombreTema;
    }

}
