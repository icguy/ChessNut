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
	/* Ezeket a f�ggv�nyeket kell mindenkinek mag�nak implement�lnia */
	void click(Position position);                    //!<  Player/AI �ltali kattint�s ezen jut fel a logik�hoz
	void promote(Piece piece);                        //!<  Player/AI �ltali gyalogv�lt�s ezen jut fel a logik�hoz
	public abstract void setPlayer( IPlayer player ); //!<  IPlayer interf�sz� elemre referencia be�ll�t�sa
	
	
	/* A t�bbi csak a Network-re tartozik: */
	
	//! \brief  TCP f�l�tti ILogic �zenet defin�ci�ja
	public abstract class ILogicMsg{}
	
	//! \brief  Ilyen �zenetben megy �t a kattint�s
	public class ILogicMsg_click extends ILogicMsg
	{
		public Position position;      //!< Klikkelt poz�ci�
		
		//! \brief  �zenet l�trehoz� konstruktor
		public ILogicMsg_click(Position position)
		{
			this.position = position;
		}
	}
	
	// ! \brief Ilyen �zenetben megy �t a kattint�s
	public class ILogicMsg_promote extends ILogicMsg
	{
		public Piece piece;            //!< El�l�ptetend� b�bu

		// ! \brief �zenet l�trehoz� konstruktor
		public ILogicMsg_promote(Piece piece)
		{
			this.piece = piece;
		}
	}
}