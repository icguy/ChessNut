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
	// ! \brief Teszt ind�t�sa
	@Override
	public void start()
	{
		// L�trehozom a klienst
		ILogic client = new NetworkClient();
		((NetworkClient) client).connect("localhost");

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
			client.click(pos);
			
			// Promote-ot k�ld�k
			waitSec(1);
			client.promote(toPromote);
		}
	}
}
