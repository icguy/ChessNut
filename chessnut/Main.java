/*************************************************
 *  \file     Main.java
 *  \brief    Program main oszt�lya
 *  \note     
 *  \date     2016. m�j. 12.
 *************************************************/
package chessnut;

import chessnut.logic.*;
import chessnut.network.*;

public class Main
{
	//! \brief  A program fut�sa itt kezd�dik
	public static void main(String[] args)
	{
		// TODO main f�ggv�ny
		
		// PIG diszny�s�g, vedd ki. Csak konstruktor �s szetter teszt volt
		ILogic gameLogic = new GameLogic();
		//IPlayer gui = new GUI();
		IPlayer networkServer = new NetworkServer(gameLogic);
		gameLogic.setPlayer(networkServer);
		
	}
}
