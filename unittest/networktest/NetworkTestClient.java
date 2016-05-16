/*************************************************
 *  \file     NetworkTestClient.java
 *  \brief    Kliens oldali h�l�zat teszt
 *  \note     
 *  \date     2016. m�j. 12.
 *************************************************/
package unittest.networktest;

import chessnut.*;
import chessnut.logic.*;
import chessnut.logic.pieces.*;
import chessnut.network.*;

public class NetworkTestClient extends NetworkTest
{
	ILogic client;
	
	// Teszt futtat� thread
	private class ClientTestRunnable implements Runnable
	{
		@Override
		public void run()
		{
			// V�rok, am�g a kapcsolat biztosan fel�p�l
			while( !((NetworkClient)client).isConnected() )
			{
				waitSec(1);
			}
			
			// L�trehozom a k�ldeni val� objektumokat
			Piece toPromote = new King(PlayerColor.Black);
			Position pos = new Position(5, 7);
			
			while( true )
			{
				// Click-et k�ld�k
				waitSec(3);
				client.click(pos, PlayerColor.Black);
				
				// Promote-ot k�ld�k
				waitSec(1);
				client.promote(toPromote);
			}
		}
	}
	
	// ! \brief Teszt ind�t�sa
	@Override
	public void start()
	{
		// Be�ll�tom kliensnek a main kliensoldali h�l�zat�t, mert azt tesztelem
		client = Main.Logic; // PIG a v�gleges verzi�ban kikommentezni ezt a sort
		
		// Teszt futtat�sa k�l�n thread-ben
		Thread testClientThread = new Thread(new ClientTestRunnable());
		testClientThread.start();
	}
}
