/*************************************************
 *  \file     IPlayer.java
 *  \brief    Interfész lefelé, a Player / AI irányába
 *  \note     
 *  \date     2016. máj. 11.
 *************************************************/
package chessnut;

import java.io.Serializable;

import chessnut.logic.*;

public interface IPlayer
{	
	/* Ezeket a függvényeket kell mindenkinek magának implementálnia */
	public abstract void setChessboard(ChessBoard board);     //!<  Ezen tudjuk leküldeni a sakktáblát, annak minden tartozékával együtt
	public abstract void notifyPromotion(Position position);  //!<  Ezen tudunk gyalogváltás kérést leküldeni
	public abstract void setGameLogic( ILogic logic);         //!< ILogic interfészû egységre referencia beállítása
	
	
	/* A többi csak a Network-re tartozik: */
	
	//! \brief  TCP fölötti IPlayer üzenet definíciója
	public class IPlayerMsg implements Serializable
	{
		// Üzenet tartalma
		private static final long serialVersionUID = 7526472295622776147L;  //!< Egyedi magicnumber a sorosításhoz
	}
	
	//! \brief  Ilyen üzenetben megy át a sakktábla
	public class IPlayerMsg_setChessboard extends IPlayerMsg
	{
		private static final long serialVersionUID = 7526472295622776148L;  //!< Egyedi magicnumber a sorosításhoz
		public ChessBoard chessboard;     //!< Küldött sakktábla
		
		//! \brief  Üzenet létrehozó konstruktor
		public IPlayerMsg_setChessboard(ChessBoard chessboard)
		{
			this.chessboard = chessboard;
		}
	}
	
	//! \brief  Ilyen üzenetben megy át a promotion-re kiválasztott mezõ
	public class IPlayerMsg_notifyPromotion extends IPlayerMsg
	{
		private static final long serialVersionUID = 7526472295622776149L;  //!< Egyedi magicnumber a sorosításhoz
		public Position position;         //!< Küldött pozíció a promotion-höz

		// ! \brief Üzenet létrehozó konstruktor
		public IPlayerMsg_notifyPromotion(Position position)
		{
			this.position = position;
		}
	}
}
