/*************************************************
 *  \file     clickMess.java
 *  \brief    Click-et �tsz�ll�t� ChessnutOverIp �zenet
 *  \note     
 *  \date     2016. m�j. 13.
 *************************************************/
package chessnut.network.protocol;

import chessnut.logic.Position;

public class clickMess extends ChessnutOverIP
{
	private static final long serialVersionUID = 7526163295624857149L;  //!< Egyedi magicnumber a soros�t�shoz
	public Position position;      //!< Klikkelt poz�ci�
	
	//! \brief  �zenet l�trehoz� konstruktor
	public clickMess(Position position)
	{
		this.position = position;
	}
}
