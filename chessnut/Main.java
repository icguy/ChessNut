/*************************************************
 *  \file     Main.java
 *  \brief    Program main osztálya
 *  \note     
 *  \date     2016. máj. 12.
 *************************************************/
package chessnut;

import chessnut.logic.*;
import chessnut.network.*;

// PIG Tesztek, a végleges verzióba nem kellenek
import unittest.networktest.*;

public class Main
{
	//! \brief  A program futása itt kezdõdik
	public static void main(String[] args)
	{
		// TODO main függvény
		
		// PIG disznyóság, vedd ki. Csak konstruktor és szetter teszt volt
		//ILogic gameLogic = new GameLogic();
		//IPlayer gui = new GUI();
		//IPlayer networkServer = new NetworkServer(gameLogic);
		//gameLogic.setPlayer(networkServer);
		
		// PIG Network teszt
		NetworkTest test = new NetworkTestServer();
		//NetworkTest test = new NetworkTestClient();
		test.start();
	}
}
