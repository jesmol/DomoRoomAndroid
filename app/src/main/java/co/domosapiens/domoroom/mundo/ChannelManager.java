package co.domosapiens.domoroom.mundo;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import java.io.IOException;
import java.net.Socket;
import java.util.UUID;

import co.atc91.android.utilities.com.Channel;

/**
 * Created by Christian on 8/25/2015.
 *
 * Esta clase es responsable de manejar la comunicacion con OpenELEC y la arduino
 *
 * Esta clase hace uso del patron Singleton y el patron Observer
 */
public class ChannelManager implements Runnable
{
    // -----------
    // Constantes
    // -----------

    /**
     * Constante para la IP en la cual se encuentra OpenELEC
     */
    public static final String OPEN_ELEC_IP = "192.168.1.143";

    private String ARDUINO_MAC = "00:06:66:6A:4D:A6";

    // -----------
    // Atributos
    // -----------

    /**
     * Instancia del administrador de canales de comunicacion
     */
    private static ChannelManager instance;

    /**
     * Instancia del observador del administrador de canales
     */
    private ChannelManagerListener listener;

    /**
     * El canal de comunicacion con la arduino a traves de bluetooth
     */
    private Channel bluetoothChannel;

    /**
     * El canal de comunicacion con la arduino a traves de TCP/IP
     */
    private Channel openELECChannel;

    // -------------
    // Constructor
    // -------------

    /**
     * Construye un nuevo administrador de canales
     * @param listener El observador del administrador de canales
     */
    private ChannelManager( ChannelManagerListener listener )
    {
        this.listener = listener;

        listener.enableBluetooth();
    }

    // -------------
    // Metodos
    // -------------

    /**
     * Devuelve la instancia del administrador de canales.
     * Si no se ha creado una instancia, se crea una nueva con el observador especificaco en los parametros
     * @param listener El observador del administrador de canales.
     * @return
     */
    public static synchronized ChannelManager getInstance( ChannelManagerListener listener )
    {
        if( instance == null )
        {
            instance = new ChannelManager(listener);
            new Thread( instance, "ChannelManagerThread" ).start( );
        }

        return instance;
    }

    /**
     * Crea el canal de comunicacion con OpenELEC
     *
     * Lanza un evento al observador del administrador de canales al momento de crear el canal o si algo inesperado sucede
     *
     * @return true si puede crear el canal o false de lo contrario
     */
    private boolean createOpenELECChannel()
    {
        try
        {
            @SuppressWarnings( "resource" )
            Socket socket = new Socket(OPEN_ELEC_IP, 8045 );
            openELECChannel = new Channel( socket.getInputStream( ), socket.getOutputStream( ) );
            triggerEvent( "Conectado a OpenELEC" );

            return true;
        }
        catch( IOException e )
        {
            triggerException( "Error al conectar a OpenELEC", e );
            return false;
        }
    }

    /**
     * Crea el canal de comunicacion con la arduino
     *
     * Lanza un evento al observador del administrador de canales al momento de crear el canal o si algo inesperado sucede
     *
     * @return true si puede crear el canal o false de lo contrario
     */
    private boolean createBluetoothChannel( )
    {
        long inicio = System.nanoTime();

        while( bluetoothChannel == null )
        {
            try
            {
                BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter( );
                BluetoothDevice bluetoothDevice = bluetoothAdapter.getRemoteDevice( ARDUINO_MAC );

                UUID uuid = UUID.fromString( "00001101-0000-1000-8000-00805F9B34FB" ); // Standard SerialPortService ID

                BluetoothSocket bluetoothSocket;
                bluetoothSocket = bluetoothDevice.createInsecureRfcommSocketToServiceRecord( uuid );
                bluetoothSocket.connect( );

                bluetoothChannel = new Channel( bluetoothSocket.getInputStream( ), bluetoothSocket.getOutputStream( ) );
                triggerEvent( "Conectado a la arduino" );
                return true;
            }
            catch( Exception e )
            {
                if( System.nanoTime( ) - inicio > 10000000000L )
                {
                    triggerException( "Error al conectar a la arduino", e );
                    break;
                }
                else
                    try
                    {
                        Thread.sleep( (long) ( Math.random( ) * 1000 ) );
                    }
                    catch( InterruptedException e1 )
                    {
                        triggerException( "Error al conectar a la arduino", e1 );
                        break;
                    }
            }
        }

        return false;
    }

    /**
     * Crea el canal de comunicación con OpenELECT y Arduino
     * y envia al observador del adminsitrador de canales onActiveChange(true)
     */
    public void run( )
    {
        //createOpenELECChannel();
        createBluetoothChannel();
        listener.onActiveChange(true);
    }

    /**
     * Cierra los canales de comunicación si estos existen
     * Elimina la instancia del administrador de canales
     * Envia al observador del adminsitrador de canales onActiveChange(true)
     */
    public void destroy( )
    {
        if( openELECChannel != null && openELECChannel.isOpen( ) )
            openELECChannel.close( );

        if( bluetoothChannel != null && bluetoothChannel.isOpen( ) )
            bluetoothChannel.close( );

        instance = null;

        listener.onActiveChange( false );
    }

    /**
     * Lanza un evento al observador del administrador de canales
     * @param evento El evento que se quiere lanzar
     */
    public void triggerEvent( String evento )
    {
        listener.onEvent( evento );
    }

    /**
     * Lanza una exception al observador del administrador de canales a traves del servicio onEvent
     * @param message El mensaje de error
     * @param e La excepcion generada
     */
    public void triggerException( String message, Exception e )
    {
        listener.onEvent( message + "\n\n" + e.getMessage( ) );

        destroy( );
    }

    public void EnviarPorBluetoothPapa(char v){
        bluetoothChannel.writeChar(v);
    }
}

