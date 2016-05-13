/*************************************************
 *  \file     IPlayer.java
 *  \brief    Interf�sz lefel�, a Player / AI ir�ny�ba
 *  \note     
 *  \date     2016. m�j. 11.
 *************************************************/
package chessnut;

import chessnut.logic.*;

public interface IPlayer
{	
	/* Ezeket a f�ggv�nyeket kell mindenkinek mag�nak implement�lnia */
	public abstract void setChessboard(ChessBoard board);     //!<  Ezen tudjuk lek�ldeni a sakkt�bl�t, annak minden tartoz�k�val egy�tt
	public abstract void notifyPromotion(Position position);  //!<  Ezen tudunk gyalogv�lt�s k�r�st lek�ldeni
	public abstract void setGameLogic( ILogic logic);         //!< ILogic interf�sz� egys�gre referencia be�ll�t�sa
}
