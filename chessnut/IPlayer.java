package chessnut;

import chessnut.logic.*;

/**
 * Ezen az interf�szen kereszt�l �rhet�ek el a j�t�kos t�pus� objektumok: GUI �s AI
 */
public interface IPlayer
{	
	/**  Ezen tudjuk lek�ldeni a sakkt�bl�t, annak minden tartoz�k�val egy�tt   */
	public abstract void setChessboard(ChessBoard chessboard);   
	
	/**  Ezen tudunk gyalogv�lt�s k�r�st lek�ldeni   */
	public abstract void notifyPromotion(Position position);
	
	/**  ILogic interf�sz� egys�gre referencia be�ll�t�sa   */
	public abstract void setGameLogic( ILogic logic);          
}
