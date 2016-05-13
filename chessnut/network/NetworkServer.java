/*************************************************
 *  \file     NetworkServer.java
 *  \brief    Szerver oldali h�l�zatkezel�s
 *  \note     
 *  \date     2016. m�j. 11.
 *************************************************/
package chessnut.network;

// �ltal�nos importok
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

// Projekt specifikus importok
import chessnut.ILogic;
import chessnut.IPlayer;
import chessnut.logic.ChessBoard;
import chessnut.logic.Position;
import chessnut.network.protocol.ChessnutOverIP;
import chessnut.network.protocol.clickMess;
import chessnut.network.protocol.notifyPromotionMess;
import chessnut.network.protocol.promoteMess;
import chessnut.network.protocol.setChessboardMess;


//! \brief  Szerver oldali h�l�zatkezel� oszt�ly
public class NetworkServer extends Network implements IPlayer
{
	// Konstansok
	private static final int port = 10007;     //!< Port
	
	// Kapcsol�d�s a j�t�k t�bbi elem�hez
	private ILogic gameLogic;                  //!< Ezen �rj�k el a GameLogic-ot
	
	// H�l�zat r�szei
	private ServerSocket serverSocket = null;  //!< Szerver socket
	private Socket clientSocket = null;        //!< Kliens socket
	private ObjectOutputStream out = null;     //!< Kimen� stream
	private ObjectInputStream in = null;       //!< Bej�v� stream
	
	//! \brief  Default konstruktor
	public NetworkServer(){}
	
	//! \brief  Konstruktor: L�trehozhat� GameLogic alapj�n
	public NetworkServer(ILogic logic)
	{
		this.gameLogic = logic;
	}
	
	//! \brief  GameLogic be�ll�t�sa
	public void setGameLogic(ILogic gameLogic)
	{
		this.gameLogic = gameLogic;
	}
		
	
	//! \brief  Kapcsolat �llapot�t le lehet k�rni
	public boolean isConnected()
	{
		if(clientSocket != null)
		{
			return clientSocket.isConnected();
		}
		return false;
	}
	
	
	// ! \brief Fogad� thread
	private class PlayerActionReceiver implements Runnable
	{	
		public void run()
		{
			try
			{
				System.out.println("Waiting for Client");
				// Kliensre v�rakoz�s (blokkol)
				clientSocket = serverSocket.accept();
				// Kapcsolat l�trej�tt
				System.out.println("Client connected.");
			} catch (IOException e)
			{
				System.err.println("Error while waiting for client connection.");
				disconnect();
				return;
			}

			try
			{
				// Stream-ek l�trehoz�sa
				out = new ObjectOutputStream(clientSocket.getOutputStream());
				in = new ObjectInputStream(clientSocket.getInputStream());
				out.flush();
			} catch (IOException e)
			{
				System.err.println("Error while creating streams.");
				disconnect();
				return;
			}

			try
			{
				// �rkez� objektumok itt j�nnek be
				while (true)
				{
					// �zenet bej�n
					ChessnutOverIP received = (ChessnutOverIP) in.readObject();
					
					// Ha click �zenet
					if(received instanceof clickMess)
					{
						System.out.println("click message arrived: \n" + ((clickMess)received).position );
						// Tov�bbadom a kezel�nek
						if(gameLogic != null)
						{
							gameLogic.click(((clickMess)received).position);
						}
					}
					
					// Ha promote �zenet
					else if(received instanceof promoteMess)
					{
						System.out.println("promote message arrived: \n" + ((promoteMess)received).piece );
						// Tov�bbadom a kezel�nek
						if(gameLogic != null)
						{
							gameLogic.promote(((promoteMess)received).piece);
						}
					}
					// Ha nem tudom milyen �zenet
					else
					{
						System.err.println("Error: Unknown object received on network: \n" + received);
					}
				}
			} catch (Exception ex)
			{
				System.out.println(ex.getMessage());
				System.err.println("Client disconnected!");
				System.exit(0);
			} finally
			{
				// Ha v�letlen�l kiugrunk ebb�l, akkor bont�s
				disconnect();
			}
		}
	}
	
	
	//! \brief  Kapcsol�d�s klienshez
	@Override
	public void connect(String ipAddr)
	{
		disconnect();
		try
		{
			// Ki�rom az IP-met sz�vegbe
			String IPAddr = InetAddress.getLocalHost().getHostAddress();
			System.out.println("Starting server on local IP: " + IPAddr );
			
			// Server socket l�trehoz�s
			serverSocket = new ServerSocket(port);
			// Fogad� thread l�trehoz�s �s ind�t�s
			Thread rec = new Thread(new PlayerActionReceiver());
			rec.start();
		} catch (IOException e)
		{
			System.err.println("Error while creating PlayerActionReceiver thread.");
		}
	}
	
	//! \brief  Kapcsolat bont�sa
	@Override
	void disconnect()
	{
		try
		{
			// Becsukok mindent, ami m�g egy�ltal�n van
			if (out != null)
				out.close();
			if (in != null)
				in.close();
			if (clientSocket != null)
				clientSocket.close();
			if (serverSocket != null)
				serverSocket.close();
		} catch (IOException ex)
		{
			System.out.println("Exception at disconnect: " + ex.getMessage());
		}
	}
	
	//! \brief  Adatk�ld�s kliens oldalra
	void sendMsgToClient(ChessnutOverIP msgToClient)
	{		
		// Ha nincs meg az output stream, akkor gond van
		if (out == null)
		{
			System.out.println("Could not send: output stream is not open.");
			return;
		}
		// K�ld�s
		try
		{
			out.writeObject(msgToClient);
			out.flush();
		} catch (IOException ex)
		{
			System.err.println("Error while sending message to client: " + ex.getMessage());
		}
	}
	
	
	//! \brief  Sakkt�bla �tk�ld�se a kliensnek
	@Override
	public void setChessboard(ChessBoard chessboard)
	{
		ChessnutOverIP msg = new setChessboardMess(chessboard);
		System.out.println("Sending setChessboard: \n" + ((setChessboardMess) msg).chessboard);
		sendMsgToClient(msg);
	}
	
	//! \brief  Prom�ci� felk�r�s �tk�ld�se a kliensnek
	@Override
	public void notifyPromotion(Position position)
	{
		ChessnutOverIP msg = new notifyPromotionMess(position);
		System.out.println("Sending notifyPromotion: \n" + ((notifyPromotionMess) msg).position);
		sendMsgToClient(msg);
	}
	
}
