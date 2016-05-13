/*************************************************
 *  \file     promoteMess.java
 *  \brief    promote-ot átvivõ ChessnutOverIp üzenet
 *  \note     
 *  \date     2016. máj. 13.
 *************************************************/
package chessnut.network.protocol;

import chessnut.logic.pieces.Piece;

public class promoteMess extends ChessnutOverIP
{
	private static final long serialVersionUID = 7522223295624857149L;  //!< Egyedi magicnumber a sorosításhoz
	public Piece piece;            //!< Elõléptetendõ bábu
	
	//! \brief  Üzenet létrehozó konstruktor
	public promoteMess(Piece piece)
	{
		this.piece = piece;
	}
}