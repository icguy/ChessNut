package chessnut.network.protocol;

import chessnut.logic.PlayerColor;
import chessnut.logic.Position;

/**
 * click függvényhívást továbbító hálózati üzenet
 */
public class clickMess extends ChessnutOverIP
{
	/**  Egyedi magicnumber a sorosításhoz   */
	private static final long serialVersionUID = 7526163295624857149L;
	
	/**  klikkelt pozíció   */
	public Position position;
	
	/**  küldõ játékos színe   */
	public PlayerColor player;
	

	/**
	 * Üzenetet létrehozó konstruktor
	 * @param position: pozíció
	 * @param player: küldõ játékos színe
	 */
	public clickMess(Position position, PlayerColor player)
	{
		this.position = position;
		this.player = player;
	}
}
