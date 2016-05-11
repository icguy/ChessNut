/*************************************************
 *  \file     IPlayer.java
 *  \brief    Interfész lefelé, a Player / AI irányába
 *  \note     
 *  \date     2016. máj. 11.
 *************************************************/
package chessnut;

import chessnut.logic.*;

public interface IPlayer
{
	void setChessboard(ChessBoard board);     //!<  Ezen tudjuk leküldeni a sakktáblát, annak minden tartozékával együtt
	void notifyPromotion(Position position);  //!<  Ezen tudunk gyalogváltás kérést leküldeni
}
