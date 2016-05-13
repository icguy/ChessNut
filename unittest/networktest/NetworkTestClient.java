/*************************************************
 *  \file     NetworkTestClient.java
 *  \brief    Kliens oldali hálózat teszt
 *  \note     
 *  \date     2016. máj. 12.
 *************************************************/
package unittest.networktest;

import chessnut.*;
import chessnut.logic.*;
import chessnut.logic.pieces.*;
import chessnut.network.*;

public class NetworkTestClient extends NetworkTest
{
	// ! \brief Teszt indítása
	@Override
	public void start()
	{
		// Létrehozom a klienst
		ILogic client = new NetworkClient();
		((NetworkClient) client).connect("localhost");

		// Várok, amíg a kapcsolat biztosan felépül
		while( !((NetworkClient)client).isConnected() )
		{
			waitSec(1);
		}
		
		// Létrehozom a küldeni való objektumokat
		Piece toPromote = new King(PlayerColor.Black);
		Position pos = new Position(5, 7);
		
		while( true )
		{
			// Click-et küldök
			waitSec(3);
			client.click(pos);
			
			// Promote-ot küldök
			waitSec(1);
			client.promote(toPromote);
		}
	}
}
