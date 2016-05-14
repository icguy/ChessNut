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
	IPlayer server;
	
	// Teszt futtató thread osztálya
	private class ServerTestRunnable implements Runnable
	{
		@Override
		public void run()
		{
			// Létrehozom a tesztobjektumokat
			ChessBoard cb = new ChessBoard(); // Teljesen felinicializált
												// sakktábla jön létre elvileg
			Position pos = new Position(3, 2);

			// Várok, amíg a kapcsolat felépül
			while (!((NetworkServer) server).isConnected())
			{
				waitSec(1);
			}

			// Küldés végtelenül
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
	
	//! \brief  Teszt indítása
	@Override
	public void start()
	{
		// Beállítom tesztelendõ szervernek a Main szerverét
		server = Main.Opponent;   // PIG A végleges verzióban kikommentezni ezt a sort
		
		// Külön thread-ben fut a teszt
		Thread testServerThread = new Thread(new ServerTestRunnable());
		testServerThread.start();
	}
}
