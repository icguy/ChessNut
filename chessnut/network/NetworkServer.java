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
import chessnut.IPlayer;
import chessnut.logic.ChessBoard;
import chessnut.logic.Position;
import chessnut.logic.GameLogic;

// Saját package importja
import chessnut.network.*;


//! \brief  Szerver oldali hálózatkezelõ osztály
public class NetworkServer extends Network implements IPlayer
{
	private ServerSocket serverSocket = null;  //!< Szerver socket
	private Socket clientSocket = null;        //!< Kliens socket
	private ObjectOutputStream out = null;     //!< Kimenõ stream
	private ObjectInputStream in = null;       //!< Bejövõ stream
	
	private ILogic gameLogic;                  //!< Ezen érjük el a GameLogic-ot
	
	//! \brief Konstruktor: Létrehozható GameLogic alapján
	NetworkServer(GameLogic gameLogic)
	{
		this.gameLogic = gameLogic;
	}
	
	//! \brief  Kapcsolódás klienshez
	@Override
	void connect(String ipAddr)
	{
		// TODO Auto-generated method stub
		
	}
	
	//! \brief  Kapcsolat bontása
	@Override
	void disconnect()
	{
		// TODO Auto-generated method stub
		
	}
	
	//! \brief  Sakktábla átküldése a kliensnek
	@Override
	public void setChessboard(ChessBoard board)
	{
		// TODO Auto-generated method stub
		
	}
	
	//! \brief  Promóció felkérés átküldése a kliensnek
	@Override
	public void notifyPromotion(Position position)
	{
		// TODO Auto-generated method stub
		
	}
	
}
