/*************************************************
 *  \file     NetworkClient.java
 *  \brief    Kliens oldali h�l�zatkezel�s
 *  \note     
 *  \date     2016. m�j. 12.
 *************************************************/
package chessnut.network;

//�ltal�nos importok
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
	
	// Kapcsol�d�s a j�t�k t�bbi elem�hez
	private IPlayer gui;                    //!< Ezen �rj�k el a GUI-t

	// H�l�zat r�szei
	private Socket socket = null;     //!< Kliens socket
	private ObjectOutputStream out = null;  //!< Kimen� stream
	private ObjectInputStream in = null;    //!< Bej�v� stream
	
	
	//! \brief  Default konstruktor
	public NetworkClient(){}
	
	// ! \brief Konstruktor: L�trehozhat� GUI alapj�n
	NetworkClient(IPlayer gui)
	{
		this.gui = gui;
	}
	
	//! \brief  GUI be�ll�that�
	public void setPlayer(IPlayer player)
	{
		this.gui = player;
	}
	
	// ! \brief Fogad� thread
	private class ServerNotificaionReceiver implements Runnable
	{
		public void run()
		{
			try
			{
				while (true)
				{
					//Point received = (Point) in.readObject(); TODO fogad�s
					//ctrl.clickReceived(received);             TODO fogadott feldolgoz�sa
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
	
	//! \brief  Kapcsol�d�s szerverhez
	@Override
	void connect(String ip)
	{
		disconnect();
		try
		{
			// Socket l�trehoz�sa
			socket = new Socket(ip, port);

			// Objektum sztream-ek l�trehoz�sa
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());
			out.flush();

			// Thread ind�t�sa
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
	
	//! \brief  Kapcsolat bont�s
	@Override
	void disconnect()
	{
		try
		{
			// Becsukok mindent, ami m�g van
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

	// ! \brief Adatk�ld�s szerver oldalra
	void sendMsgToServer(ILogicMsg msgToServer)
	{
		// Ha nincs meg az output stream, akkor gond van
		if (out == null)
		{
			System.out.println("Could not send: output stream is not open.");
			return;
		}
		System.out.println("Sending to server: " + msgToServer);
		// K�ld�s
		try
		{
			out.writeObject(msgToServer);
			out.flush();
		} catch (IOException ex)
		{
			System.err.println("Error while sending message to server.");
		}
	}
	
	//! \brief  Click �tk�ld�se
	@Override
	public void click(Position position)
	{
		ILogicMsg msg = new ILogicMsg_click(position);
		sendMsgToServer(msg);
	}
	
	//! \brief  Promote �tk�ld�se
	@Override
	public void promote(Piece piece)
	{
		ILogicMsg msg = new ILogicMsg_promote(piece);
		sendMsgToServer(msg);
	}
	
}
