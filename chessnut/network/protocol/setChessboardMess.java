package chessnut.network.protocol;

import chessnut.logic.ChessBoard;

/**
 * setChessboard függvényhívást továbbító hálózati üzenet
 */
public class setChessboardMess extends ChessnutOverIP
{
	/**  Egyedi magicnumber a sorosításhoz   */
	private static final long serialVersionUID = 6439012656220161412L;
	
	/**  üzenet tartalma: sakktábla   */
	public ChessBoard chessboard;
	

	/**
	 * Üzenetet létrehozó konstruktor
	 * @param chessboard: üzenet tartalma sakktábla
	 */
	public setChessboardMess(ChessBoard chessboard)
	{
		this.chessboard = new ChessBoard( chessboard);
	}
}
