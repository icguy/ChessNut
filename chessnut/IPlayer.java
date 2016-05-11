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
	void setChessboard(ChessBoard board);     //!<  Ezen tudjuk lek�ldeni a sakkt�bl�t, annak minden tartoz�k�val egy�tt
	void notifyPromotion(Position position);  //!<  Ezen tudunk gyalogv�lt�s k�r�st lek�ldeni
}
