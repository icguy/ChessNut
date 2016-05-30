package chessnut.network.protocol;

import chessnut.logic.ChessBoard;

/**
 * setChessboard f�ggv�nyh�v�st tov�bb�t� h�l�zati �zenet
 */
public class setChessboardMess extends ChessnutOverIP
{
	/**  Egyedi magicnumber a soros�t�shoz   */
	private static final long serialVersionUID = 6439012656220161412L;
	
	/**  �zenet tartalma: sakkt�bla   */
	public ChessBoard chessboard;
	

	/**
	 * �zenetet l�trehoz� konstruktor
	 * @param chessboard: �zenet tartalma sakkt�bla
	 */
	public setChessboardMess(ChessBoard chessboard)
	{
		this.chessboard = new ChessBoard( chessboard);
	}
}
