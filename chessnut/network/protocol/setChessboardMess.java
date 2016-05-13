/*************************************************
 *  \file     setChessboardMess.java
 *  \brief    setChessboard-ot �tk�ld� ChessnutOverIp �zenet
 *  \note     
 *  \date     2016. m�j. 13.
 *************************************************/
package chessnut.network.protocol;

import chessnut.logic.ChessBoard;

public class setChessboardMess extends ChessnutOverIP
{
	private static final long serialVersionUID = 5266901295622776148L;  //!< Egyedi magicnumber a soros�t�shoz
	public ChessBoard chessboard;     //!< K�ld�tt sakkt�bla
	
	//! \brief  �zenet l�trehoz� konstruktor
	public setChessboardMess(ChessBoard chessboard)
	{
		this.chessboard = chessboard;
	}
}
