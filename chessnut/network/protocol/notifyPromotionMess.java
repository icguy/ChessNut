package chessnut.network.protocol;

import chessnut.logic.Position;

/**
 * notifyPromotion f�ggv�nyh�v�st tov�bb�t� h�l�zati �zenet
 */
public class notifyPromotionMess extends ChessnutOverIP
{
	/**  Egyedi magicnumber a soros�t�shoz   */
	private static final long serialVersionUID = 7526472295622776222L;
	
	/**  �zenetben szerepl� poz�ci�   */
	public Position position;


	/**
	 * �zenetet l�trehoz� konstruktor
	 * @param position: poz�ci�
	 */
	public notifyPromotionMess(Position position)
	{
		this.position = position;
	}
}
