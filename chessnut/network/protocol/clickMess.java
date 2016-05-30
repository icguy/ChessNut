package chessnut.network.protocol;

import chessnut.logic.PlayerColor;
import chessnut.logic.Position;

/**
 * click f�ggv�nyh�v�st tov�bb�t� h�l�zati �zenet
 */
public class clickMess extends ChessnutOverIP
{
	/**  Egyedi magicnumber a soros�t�shoz   */
	private static final long serialVersionUID = 7526163295624857149L;
	
	/**  klikkelt poz�ci�   */
	public Position position;
	
	/**  k�ld� j�t�kos sz�ne   */
	public PlayerColor player;
	

	/**
	 * �zenetet l�trehoz� konstruktor
	 * @param position: poz�ci�
	 * @param player: k�ld� j�t�kos sz�ne
	 */
	public clickMess(Position position, PlayerColor player)
	{
		this.position = position;
		this.player = player;
	}
}
