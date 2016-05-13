/*************************************************
 *  \file     NetworkServer.java
 *  \brief    Szerver oldali hálózatkezelés
 *  \note     
 *  \date     2016. máj. 11.
 *************************************************/
package chessnut.network;

// Általános importok
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

// Projekt specifikus importok
import chessnut.ILogic;
import chessnut.ILogic.*;
import chessnut.IPlayer;
import chessnut.logic.ChessBoard;
import chessnut.logic.Position;


//! \brief  Szerver oldali hálózatkezelõ osztály
public class NetworkServer extends Network implements IPlayer
{
	// Konstansok
	private static final int port = 10007;     //!< Port
	
	// Él-e a kapcsolat
	private boolean Connected = false;            //!< Él-e a kapcsolat
	
	// Kapcsolódás a játék többi eleméhez
	private ILogic gameLogic;                  //!< Ezen érjük el a GameLogic-ot
	
	// Hálózat részei
	private ServerSocket serverSocket = null;  //!< Szerver socket
	private Socket clientSocket = null;        //!< Kliens socket
	private ObjectOutputStream out = null;     //!< Kimenõ stream
	private ObjectInputStream in = null;       //!< Bejövõ stream
	
	//! \brief  Default konstruktor
	public NetworkServer(){}
	
	//! \brief  Konstruktor: Létrehozható GameLogic alapján
	public NetworkServer(ILogic logic)
	{
		this.gameLogic = logic;
	}
	
	//! \brief  GameLogic beállítása
	public void setGameLogic(ILogic gameLogic)
	{
		this.gameLogic = gameLogic;
	}
		
	
	//! \brief  Kapcsolat állapotát le lehet kérni
	public boolean isConnected()
	{
		return Connected;
	}
	
	
	// ! \brief Fogadó thread
	private class PlayerActionReceiver implements Runnable
	{
		public void run()
		{
			try
			{
				System.out.println("Waiting for Client");
				// Kliensre várakozás (blokkol)
				clientSocket = serverSocket.accept();
				// Kapcsolat létrejött:
				Connected = true;
				System.out.println("Client connected.");
			} catch (IOException e)
			{
				System.err.println("Error while waiting for client connection.");
				disconnect();
				return;
			}

			try
			{
				// Stream-ek létrehozása
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
				// Érkezõ objektumok itt jönnek be
				while (true)
				{
					// Point received = (Point) in.readObject(); TODO Itt fogunk fogadni valamilyen osztályú valamit
					// ctrl.clickReceived(received); TODO lesz egy feldolgozó metódusa ennek
				}
			} catch (Exception ex)
			{
				System.out.println(ex.getMessage());
				System.err.println("Client disconnected!");
			} finally
			{
				// Ha véletlenül kiugrunk ebbõl, akkor bontás
				disconnect();
			}
		}
	}
	
	
	//! \brief  Kapcsolódás klienshez
	@Override
	public void connect(String ipAddr)
	{
		disconnect();
		try
		{
			// Server socket létrehozás
			serverSocket = new ServerSocket(port);
			// Fogadó thread létrehozás és indítás
			Thread rec = new Thread(new PlayerActionReceiver());
			rec.start();
		} catch (IOException e)
		{
			System.err.println("Error while creating PlayerActionReceiver thread.");
		}
	}
	
	//! \brief  Kapcsolat bontása
	@Override
	void disconnect()
	{
		try
		{
			// Becsukok mindent, ami még egyáltalán van
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
	
	//! \brief  Adatküldés kliens oldalra
	void sendMsgToClient(IPlayerMsg msgToClient)
	{		
		// Ha nincs meg az output stream, akkor gond van
		if (out == null)
		{
			System.out.println("Could not send: output stream is not open.");
			return;
		}
		// Küldés
		try
		{
			out.writeObject(msgToClient);
			out.flush();
		} catch (IOException ex)
		{
			System.err.println("Error while sending message to client: " + ex.getMessage());
		}
	}
	
	
	//! \brief  Sakktábla átküldése a kliensnek
	@Override
	public void setChessboard(ChessBoard chessboard)
	{
		IPlayerMsg msg = new IPlayerMsg_setChessboard(chessboard);
		System.out.println("Sending setChessboard: \n" + ((IPlayerMsg_setChessboard) msg).chessboard);
		sendMsgToClient(msg);
	}
	
	//! \brief  Promóció felkérés átküldése a kliensnek
	@Override
	public void notifyPromotion(Position position)
	{
		IPlayerMsg msg = new IPlayerMsg_notifyPromotion(position);
		System.out.println("Sending notifyPromotion: \n" + ((IPlayerMsg_notifyPromotion) msg).position);
		sendMsgToClient(msg);
	}
	
}
