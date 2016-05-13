/*************************************************
 *  \file     NetworkTestServer.java
 *  \brief    Szerver oldali Network egységtesztek
 *  \note     
 *  \date     2016. máj. 12.
 *************************************************/
package unittest.networktest;

import chessnut.*;
import chessnut.logic.ChessBoard;
import chessnut.logic.Position;
import chessnut.network.*;

public class NetworkTestServer extends NetworkTest
{
	//! \brief  Teszt indítása
	@Override
	public void start()
	{
		// Létrehozom a szervert
		IPlayer server = new NetworkServer();
		((NetworkServer) server).connect("localhost");
		
		// Létrehozom a tesztobjektumokat
		ChessBoard cb = new ChessBoard(); // Teljesen felinicializált sakktábla jön létre elvileg
		Position pos = new Position(3, 2);
		
		// Várok, amíg a kapcsolat felépül
		while( !((NetworkServer) server).isConnected() )
		{
			waitSec(1);
		}
		
		while (true)
		{
			waitSec(2);
			
			// Küldök sakktáblát
			server.setChessboard(cb);
			
			waitSec(4);
			
			// Küldök gyalogváltást
			server.notifyPromotion(pos);
		}
	}
}
