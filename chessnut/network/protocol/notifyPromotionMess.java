/*************************************************
 *  \file     notifyPromotionMess.java
 *  \brief    Promotion notifikációt átvivõ ChessnutOverIp üzenet
 *  \note     
 *  \date     2016. máj. 13.
 *************************************************/
package chessnut.network.protocol;

import chessnut.logic.Position;

public class notifyPromotionMess extends ChessnutOverIP
{
	private static final long serialVersionUID = 7526472295622776222L;  //!< Egyedi magicnumber a sorosításhoz
	public Position position;         //!< Küldött pozíció a promotion-höz

	// ! \brief Üzenet létrehozó konstruktor
	public notifyPromotionMess(Position position)
	{
		this.position = position;
	}
}
