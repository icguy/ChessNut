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
	
	
	/* A t�bbi csak a Network-re tartozik: */
	
	//! \brief  TCP f�l�tti IPlayer �zenet defin�ci�ja
	public abstract class IPlayerMsg{}
	
	//! \brief  Ilyen �zenetben megy �t a sakkt�bla
	public class IPlayerMsg_setChessboard extends IPlayerMsg
	{
		ChessBoard chessboard;     //!< K�ld�tt sakkt�bla
		
		//! \brief  �zenet l�trehoz� konstruktor
		public IPlayerMsg_setChessboard(ChessBoard chessboard)
		{
			this.chessboard = chessboard;
		}
	}
	
	//! \brief  Ilyen �zenetben megy �t a promotion-re kiv�lasztott mez�
	public class IPlayerMsg_notifyPromotion extends IPlayerMsg
	{
		Position position;         //!< K�ld�tt poz�ci� a promotion-h�z

		// ! \brief �zenet l�trehoz� konstruktor
		public IPlayerMsg_notifyPromotion(Position position)
		{
			this.position = position;
		}
	}
}
