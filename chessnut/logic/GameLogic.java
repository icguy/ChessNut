/*************************************************
 *  \file     GameLogic.java
 *  \brief    Ez lesz a j�t�klogika oszt�ly
 *  \note     
 *  \date     2016. m�j. 12.
 *************************************************/
package chessnut.logic;

import chessnut.ILogic;
import chessnut.IPlayer;
import chessnut.logic.Position;
import chessnut.logic.pieces.Piece;

public class GameLogic implements ILogic
{
	// J�t�k elemei
	ChessBoard chessboard;     //!< Sakkt�bl�m
	IPlayer gui;               //!< Egyik j�t�kos a helyi GUI
	IPlayer opponent;          //!< M�sik j�t�kos: AI / Network
	
	
	//! \brief  Ezzel lehet be�ll�tani a t�loldali j�t�kosra vonatkoz� referenci�t (AI / NetworkServer)
	@Override
	public void setPlayer(IPlayer player)
	{
		this.opponent = player;
	}
	
	//! \brief  Click kezel�se
	@Override
	public void click(Position position)
	{
		// TODO Klikk kezel�se
		
	}
	
	//! \brief  Gyalog el�l�ptet�s kezel�se
	@Override
	public void promote(Piece piece)
	{
		// TODO Be�rkez� prom�ci� kezel�se
		
	}
}
