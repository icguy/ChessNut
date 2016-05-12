/*************************************************
 *  \file     GameLogic.java
 *  \brief    Ez lesz a játéklogika osztály
 *  \note     
 *  \date     2016. máj. 12.
 *************************************************/
package chessnut.logic;

import chessnut.ILogic;
import chessnut.IPlayer;
import chessnut.logic.Position;
import chessnut.logic.pieces.Piece;

public class GameLogic implements ILogic
{
	// Játék elemei
	ChessBoard chessboard;     //!< Sakktáblám
	IPlayer gui;               //!< Egyik játékos a helyi GUI
	IPlayer opponent;          //!< Másik játékos: AI / Network
	
	
	//! \brief  Ezzel lehet beállítani a túloldali játékosra vonatkozó referenciát (AI / NetworkServer)
	@Override
	public void setPlayer(IPlayer player)
	{
		this.opponent = player;
	}
	
	//! \brief  Click kezelése
	@Override
	public void click(Position position)
	{
		// TODO Klikk kezelése
		
	}
	
	//! \brief  Gyalog elõléptetés kezelése
	@Override
	public void promote(Piece piece)
	{
		// TODO Beérkezõ promóció kezelése
		
	}
}
