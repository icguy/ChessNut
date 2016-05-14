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
	IPlayer server;
	
	// Teszt futtat� thread oszt�lya
	private class ServerTestRunnable implements Runnable
	{
		@Override
		public void run()
		{
			// L�trehozom a tesztobjektumokat
			ChessBoard cb = new ChessBoard(); // Teljesen felinicializ�lt
												// sakkt�bla j�n l�tre elvileg
			Position pos = new Position(3, 2);

			// V�rok, am�g a kapcsolat fel�p�l
			while (!((NetworkServer) server).isConnected())
			{
				waitSec(1);
			}

			// K�ld�s v�gtelen�l
			while (true)
			{
				waitSec(2);

				// K�ld�k sakkt�bl�t
				server.setChessboard(cb);

				waitSec(4);

				// K�ld�k gyalogv�lt�st
				server.notifyPromotion(pos);
			}
		}
	}
	
	//! \brief  Teszt ind�t�sa
	@Override
	public void start()
	{
		// Be�ll�tom tesztelend� szervernek a Main szerver�t
		server = Main.Opponent;   // PIG A v�gleges verzi�ban kikommentezni ezt a sort
		
		// K�l�n thread-ben fut a teszt
		Thread testServerThread = new Thread(new ServerTestRunnable());
		testServerThread.start();
	}
}
