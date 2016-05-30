package chessnut.network.protocol;

import chessnut.logic.pieces.Piece;

/**
 * promote függvényhívást továbbító hálózati üzenet
 */
public class promoteMess extends ChessnutOverIP
{
	/**  Egyedi magicnumber a sorosításhoz   */
	private static final long serialVersionUID = 7522223295624857149L;
	
	/**  bábu, amelyet a függvényhívás tartalmaz   */
	public Piece piece;
	

	/**
	 * Üzenetet létrehozó konstruktor
	 * @param piece: üzenet tartalma (bábu)
	 */
	public promoteMess(Piece piece)
	{
		this.piece = piece;
	}
}