/*************************************************
 *  \file     NetworkTestClient.java
 *  \brief    Kliens oldali hálózat teszt
 *  \note     
 *  \date     2016. máj. 12.
 *************************************************/
package unittest.networktest;

import chessnut.*;
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

		// Várom a beérkezõ objektumokat
	}
}
