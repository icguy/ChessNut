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
	void click(Position position);                    //!<  Player/AI általi kattintás ezen jut fel a logikához
	void promote(Piece piece);                        //!<  Player/AI általi gyalogváltás ezen jut fel a logikához
	public abstract void setPlayer( IPlayer player ); //!<  IPlayer interfészû elemre referencia beállítása
	
	
	/* A többi csak a Network-re tartozik: */
	
	//! \brief  TCP fölötti ILogic üzenet definíciója
	public abstract class ILogicMsg{}
	
	//! \brief  Ilyen üzenetben megy át a kattintás
	public class ILogicMsg_click extends ILogicMsg
	{
		public Position position;      //!< Klikkelt pozíció
		
		//! \brief  Üzenet létrehozó konstruktor
		public ILogicMsg_click(Position position)
		{
			this.position = position;
		}
	}
	
	// ! \brief Ilyen üzenetben megy át a kattintás
	public class ILogicMsg_promote extends ILogicMsg
	{
		public Piece piece;            //!< Elõléptetendõ bábu

		// ! \brief Üzenet létrehozó konstruktor
		public ILogicMsg_promote(Piece piece)
		{
			this.piece = piece;
		}
	}
}