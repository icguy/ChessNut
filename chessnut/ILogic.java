/*************************************************
 *  \file     ILogic.java
 *  \brief    Interfész felfelé, a játéklogika irányába
 *  \note     
 *  \date     2016. máj. 11.
 *************************************************/
package chessnut;

import chessnut.logic.*;
import chessnut.logic.pieces.Piece;

public interface ILogic
{
	/* Ezeket a függvényeket kell mindenkinek magának implementálnia */
	void click(Position position, PlayerColor player);                    //!<  Player/AI általi kattintás ezen jut fel a logikához
	void promote(Piece piece);                        //!<  Player/AI általi gyalogváltás ezen jut fel a logikához
	public abstract void setPlayer( IPlayer player ); //!<  IPlayer interfészû elemre referencia beállítása
}