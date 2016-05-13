/*************************************************
 *  \file     NetworkTestServer.java
 *  \brief    Szerver oldali Network egys�gtesztek
 *  \note     
 *  \date     2016. m�j. 12.
 *************************************************/
package unittest.networktest;

import chessnut.*;
import chessnut.logic.ChessBoard;
import chessnut.logic.Position;
import chessnut.network.*;

public class NetworkTestServer extends NetworkTest
{
	//! \brief  Teszt ind�t�sa
	@Override
	public void start()
	{
		// L�trehozom a szervert
		IPlayer server = new NetworkServer();
		((NetworkServer) server).connect("localhost");
		
		// L�trehozom a tesztobjektumokat
		ChessBoard cb = new ChessBoard(); // Teljesen felinicializ�lt sakkt�bla j�n l�tre elvileg
		Position pos = new Position(3, 2);
		
		// V�rok, am�g a kapcsolat fel�p�l
		while( !((NetworkServer) server).isConnected() );
		
		
		// K�ld�m
		server.setChessboard(cb);
		server.notifyPromotion(pos);
		
	}
}
