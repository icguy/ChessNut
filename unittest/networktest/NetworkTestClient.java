/*************************************************
 *  \file     NetworkTestClient.java
 *  \brief    Kliens oldali h�l�zat teszt
 *  \note     
 *  \date     2016. m�j. 12.
 *************************************************/
package unittest.networktest;

import chessnut.*;
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

		// V�rom a be�rkez� objektumokat
	}
}
