/*************************************************
 *  \file     NetworkClient.java
 *  \brief    Kliens oldali hálózatkezelés
 *  \note     
 *  \date     2016. máj. 12.
 *************************************************/
package chessnut.network;

//Általános importok
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;

//Projekt specifikus importok
import chessnut.ILogic;
import chessnut.IPlayer;
import chessnut.logic.Position;
import chessnut.logic.pieces.Piece;

public class NetworkClient extends Network implements ILogic
{
	// Konstansok
	private static final int port = 10007;
	
	// Kapcsolódás a játék többi eleméhez
	private IPlayer gui;                    //!< Ezen érjük el a GUI-t

	// Hálózat részei
	private Socket socket = null;     //!< Kliens socket
	private ObjectOutputStream out = null;  //!< Kimenõ stream
	private ObjectInputStream in = null;    //!< Bejövõ stream
	
	
	//! \brief  Default konstruktor
	public NetworkClient(){}
	
	// ! \brief Konstruktor: Létrehozható GUI alapján
	NetworkClient(IPlayer gui)
	{
		this.gui = gui;
	}
	
	//! \brief  GUI beállítható
	public void setPlayer(IPlayer player)
	{
		this.gui = player;
	}
	
	// ! \brief Fogadó thread
	private class ServerNotificaionReceiver implements Runnable
	{
		public void run()
		{
			try
			{
				while (true)
				{
					//Point received = (Point) in.readObject(); TODO fogadás
					//ctrl.clickReceived(received);             TODO fogadott feldolgozása
				}
			} catch (Exception ex)
			{
				System.out.println(ex.getMessage());
				System.err.println("Server disconnected!");
			} finally
			{
				disconnect();
			}
		}
	}
	
	//! \brief  Kapcsolódás szerverhez
	@Override
	void connect(String ip)
	{
		disconnect();
		try
		{
			// Socket létrehozása
			socket = new Socket(ip, port);

			// Objektum sztream-ek létrehozása
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());
			out.flush();

			// Thread indítása
			Thread rec = new Thread(new ServerNotificaionReceiver());
			rec.start();
		} catch (UnknownHostException e)
		{
			System.err.println("Error: host not found.");
		} catch (IOException e)
		{
			System.err.println("Couldn't connect to server: " + e.getMessage());
		}
	}
	
	//! \brief  Kapcsolat bontás
	@Override
	void disconnect()
	{
		try
		{
			// Becsukok mindent, ami még van
			if (out != null)
				out.close();
			if (in != null)
				in.close();
			if (socket != null)
				socket.close();
		} catch (IOException ex)
		{
			System.err.println("Error while disconnecting: " + ex.getMessage());
		}
	}

	// ! \brief Adatküldés szerver oldalra
	void sendMsgToServer(ILogicMsg msgToServer)
	{
		// Ha nincs meg az output stream, akkor gond van
		if (out == null)
		{
			System.out.println("Could not send: output stream is not open.");
			return;
		}
		System.out.println("Sending to server: " + msgToServer);
		// Küldés
		try
		{
			out.writeObject(msgToServer);
			out.flush();
		} catch (IOException ex)
		{
			System.err.println("Error while sending message to server.");
		}
	}
	
	//! \brief  Click átküldése
	@Override
	public void click(Position position)
	{
		ILogicMsg msg = new ILogicMsg_click(position);
		sendMsgToServer(msg);
	}
	
	//! \brief  Promote átküldése
	@Override
	public void promote(Piece piece)
	{
		ILogicMsg msg = new ILogicMsg_promote(piece);
		sendMsgToServer(msg);
	}
	
}
