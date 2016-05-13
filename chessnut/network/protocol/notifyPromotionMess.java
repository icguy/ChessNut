/*************************************************
 *  \file     notifyPromotionMess.java
 *  \brief    Promotion notifik�ci�t �tviv� ChessnutOverIp �zenet
 *  \note     
 *  \date     2016. m�j. 13.
 *************************************************/
package chessnut.network.protocol;

import chessnut.logic.Position;

public class notifyPromotionMess extends ChessnutOverIP
{
	private static final long serialVersionUID = 7526472295622776222L;  //!< Egyedi magicnumber a soros�t�shoz
	public Position position;         //!< K�ld�tt poz�ci� a promotion-h�z

	// ! \brief �zenet l�trehoz� konstruktor
	public notifyPromotionMess(Position position)
	{
		this.position = position;
	}
}
