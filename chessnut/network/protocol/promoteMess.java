/*************************************************
 *  \file     promoteMess.java
 *  \brief    promote-ot �tviv� ChessnutOverIp �zenet
 *  \note     
 *  \date     2016. m�j. 13.
 *************************************************/
package chessnut.network.protocol;

import chessnut.logic.pieces.Piece;

public class promoteMess extends ChessnutOverIP
{
	private static final long serialVersionUID = 7522223295624857149L;  //!< Egyedi magicnumber a soros�t�shoz
	public Piece piece;            //!< El�l�ptetend� b�bu
	
	//! \brief  �zenet l�trehoz� konstruktor
	public promoteMess(Piece piece)
	{
		this.piece = piece;
	}
}