package chessnut.network.protocol;

import chessnut.logic.Position;

/**
 * notifyPromotion függvényhívást továbbító hálózati üzenet
 */
public class notifyPromotionMess extends ChessnutOverIP
{
	/**  Egyedi magicnumber a sorosításhoz   */
	private static final long serialVersionUID = 7526472295622776222L;
	
	/**  Üzenetben szereplõ pozíció   */
	public Position position;


	/**
	 * Üzenetet létrehozó konstruktor
	 * @param position: pozíció
	 */
	public notifyPromotionMess(Position position)
	{
		this.position = position;
	}
}
