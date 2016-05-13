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


//! \brief  Szerver oldali hálózatkezelõ osztály
public class NetworkServer extends Network implements IPlayer
{
	// Konstansok
	private static final int port = 10007;     //!< Port
	
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
		if(clientSocket != null)
		{
			return clientSocket.isConnected();
		}
		return false;
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
				// Kapcsolat létrejött
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
					// Üzenet bejön
					ChessnutOverIP received = (ChessnutOverIP) in.readObject();
					
					// Ha click üzenet
					if(received instanceof clickMess)
					{
						System.out.println("click message arrived: \n" + ((clickMess)received).position );
						// Továbbadom a kezelõnek
						if(gameLogic != null)
						{
							gameLogic.click(((clickMess)received).position);
						}
					}
					
					// Ha promote üzenet
					else if(received instanceof promoteMess)
					{
						System.out.println("promote message arrived: \n" + ((promoteMess)received).piece );
						// Továbbadom a kezelõnek
						if(gameLogic != null)
						{
							gameLogic.promote(((promoteMess)received).piece);
						}
					}
					// Ha nem tudom milyen üzenet
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
			// Kiírom az IP-met szövegbe
			String IPAddr = InetAddress.getLocalHost().getHostAddress();
			System.out.println("Starting server on local IP: " + IPAddr );
			
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
	void sendMsgToClient(ChessnutOverIP msgToClient)
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
		ChessnutOverIP msg = new setChessboardMess(chessboard);
		System.out.println("Sending setChessboard: \n" + ((setChessboardMess) msg).chessboard);
		sendMsgToClient(msg);
	}
	
	//! \brief  Promóció felkérés átküldése a kliensnek
	@Override
	public void notifyPromotion(Position position)
	{
		ChessnutOverIP msg = new notifyPromotionMess(position);
		System.out.println("Sending notifyPromotion: \n" + ((notifyPromotionMess) msg).position);
		sendMsgToClient(msg);
	}
	
}
