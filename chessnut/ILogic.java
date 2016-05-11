/*************************************************
 *  \file     ILogic.java
 *  \brief    Interf�sz felfel�, a j�t�klogika ir�ny�ba
 *  \note     
 *  \date     2016. m�j. 11.
 *************************************************/
package chessnut;

import chessnut.logic.*;
import chessnut.logic.pieces.Piece;

public interface ILogic
{
	void click(Position position);  //!<  Player/AI �ltali kattint�s ezen jut fel a logik�hoz
	void promote(Piece piece);      //!<  Player/AI �ltali gyalogv�lt�s ezen jut fel a logik�hoz
}