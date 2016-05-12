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
	
	
	/* A többi csak a Network-re tartozik: */
	
	//! \brief  TCP fölötti IPlayer üzenet definíciója
	public abstract class IPlayerMsg{}
	
	//! \brief  Ilyen üzenetben megy át a sakktábla
	public class IPlayerMsg_setChessboard extends IPlayerMsg
	{
		ChessBoard chessboard;     //!< Küldött sakktábla
		
		//! \brief  Üzenet létrehozó konstruktor
		public IPlayerMsg_setChessboard(ChessBoard chessboard)
		{
			this.chessboard = chessboard;
		}
	}
	
	//! \brief  Ilyen üzenetben megy át a promotion-re kiválasztott mezõ
	public class IPlayerMsg_notifyPromotion extends IPlayerMsg
	{
		Position position;         //!< Küldött pozíció a promotion-höz

		// ! \brief Üzenet létrehozó konstruktor
		public IPlayerMsg_notifyPromotion(Position position)
		{
			this.position = position;
		}
	}
}
