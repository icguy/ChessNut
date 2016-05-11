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
import java.net.ServerSocket;
import java.net.Socket;

// Projekt specifikus importok
import chessnut.ILogic;
import chessnut.IPlayer;
import chessnut.logic.ChessBoard;
import chessnut.logic.Position;
import chessnut.logic.GameLogic;

// Saj�t package importja
import chessnut.network.*;


//! \brief  Szerver oldali h�l�zatkezel� oszt�ly
public class NetworkServer extends Network implements IPlayer
{
	private ServerSocket serverSocket = null;  //!< Szerver socket
	private Socket clientSocket = null;        //!< Kliens socket
	private ObjectOutputStream out = null;     //!< Kimen� stream
	private ObjectInputStream in = null;       //!< Bej�v� stream
	
	private ILogic gameLogic;                  //!< Ezen �rj�k el a GameLogic-ot
	
	//! \brief Konstruktor: L�trehozhat� GameLogic alapj�n
	NetworkServer(GameLogic gameLogic)
	{
		this.gameLogic = gameLogic;
	}
	
	//! \brief  Kapcsol�d�s klienshez
	@Override
	void connect(String ipAddr)
	{
		// TODO Auto-generated method stub
		
	}
	
	//! \brief  Kapcsolat bont�sa
	@Override
	void disconnect()
	{
		// TODO Auto-generated method stub
		
	}
	
	//! \brief  Sakkt�bla �tk�ld�se a kliensnek
	@Override
	public void setChessboard(ChessBoard board)
	{
		// TODO Auto-generated method stub
		
	}
	
	//! \brief  Prom�ci� felk�r�s �tk�ld�se a kliensnek
	@Override
	public void notifyPromotion(Position position)
	{
		// TODO Auto-generated method stub
		
	}
	
}
