package chessnut.network;

/**
 * Hálózatkezelés absztrakt osztálya.
 * Ebből származik le a kliens és a szerver oldali hálózatkezelő osztály
 */
public abstract class Network
{
    /**  Kapcsolódás   */
	abstract void connect(String ipAddr);
	
	/**  Kapcsolat bontás  */
	abstract void disconnect();
}
