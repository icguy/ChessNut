/*************************************************
 *  \file     setChessboardMess.java
 *  \brief    setChessboard-ot átküldõ ChessnutOverIp üzenet
 *  \note     
 *  \date     2016. máj. 13.
 *************************************************/
package chessnut.network.protocol;

import chessnut.logic.ChessBoard;

public class setChessboardMess extends ChessnutOverIP
{
	private static final long serialVersionUID = 6439012656220161412L;  //!< Egyedi magicnumber a sorosításhoz
	public ChessBoard chessboard;     //!< Küldött sakktábla
	
	//! \brief  Üzenet létrehozó konstruktor
	public setChessboardMess(ChessBoard chessboard)
	{
		this.chessboard = new ChessBoard( chessboard);
	}
}
