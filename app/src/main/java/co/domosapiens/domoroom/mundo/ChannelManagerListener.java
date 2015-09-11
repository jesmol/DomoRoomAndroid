package co.domosapiens.domoroom.mundo;

/**
 * Created by Christian on 8/25/2015.
 *
 * Contrato que debe implementar el observador del administrador de canales
 */
public interface ChannelManagerListener
{
    /**
     * Activa el servicio bluetooth en el terminal android
     */
    void enableBluetooth( );

    /**
     * Es invocado cada vez que se genra un evento en los canales de comunicacion
     * @param event El evento que se generó
     */
    void onEvent( String event );

    /**
     * Es invocado cada vez que el estado del administrador de canales cambia
     * @param value true cuando el estado es activo, false de lo contrario.
     */
    void onActiveChange( boolean value  );
}