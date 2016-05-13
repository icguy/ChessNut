/*************************************************
 *  \file     clickMess.java
 *  \brief    Click-et átszállító ChessnutOverIp üzenet
 *  \note     
 *  \date     2016. máj. 13.
 *************************************************/
package chessnut.network.protocol;

import chessnut.logic.Position;

public class clickMess extends ChessnutOverIP
{
	private static final long serialVersionUID = 7526163295624857149L;  //!< Egyedi magicnumber a sorosításhoz
	public Position position;      //!< Klikkelt pozíció
	
	//! \brief  Üzenet létrehozó konstruktor
	public clickMess(Position position)
	{
		this.position = position;
	}
}
