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
	/* Ezeket a függvényeket kell mindenkinek magának implementálnia */
	public abstract void setChessboard(ChessBoard board);     //!<  Ezen tudjuk leküldeni a sakktáblát, annak minden tartozékával együtt
	public abstract void notifyPromotion(Position position);  //!<  Ezen tudunk gyalogváltás kérést leküldeni
	public abstract void setGameLogic( ILogic logic);         //!< ILogic interfészû egységre referencia beállítása
}
