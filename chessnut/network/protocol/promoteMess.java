package chessnut.network.protocol;

import chessnut.logic.pieces.Piece;

/**
 * promote f�ggv�nyh�v�st tov�bb�t� h�l�zati �zenet
 */
public class promoteMess extends ChessnutOverIP
{
	/**  Egyedi magicnumber a soros�t�shoz   */
	private static final long serialVersionUID = 7522223295624857149L;
	
	/**  b�bu, amelyet a f�ggv�nyh�v�s tartalmaz   */
	public Piece piece;
	

	/**
	 * �zenetet l�trehoz� konstruktor
	 * @param piece: �zenet tartalma (b�bu)
	 */
	public promoteMess(Piece piece)
	{
		this.piece = piece;
	}
}