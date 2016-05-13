/*************************************************
 *  \file     GameLogic.java
 *  \brief    Ez lesz a játéklogika osztály
 *  \note     
 *  \date     2016. máj. 12.
 *************************************************/
package chessnut.logic;

import chessnut.ILogic;
import chessnut.IPlayer;
import chessnut.gui.GUI;
import chessnut.logic.Position;
import chessnut.logic.pieces.Piece;

public class GameLogic implements ILogic
{
	// Játék elemei
	ChessBoard chessboard;     //!< Sakktáblám
	IPlayer gui;               //!< Egyik játékos a helyi GUI
	IPlayer opponent;          //!< Másik játékos: AI / Network
	
	
	//! \brief  Létrehozás GUI alapján
	public GameLogic( GUI gui )
	{
		this.gui = gui;
	}
	
	//! \brief  Ezzel lehet beállítani a túloldali játékosra vonatkozó referenciát (AI / NetworkServer)
	@Override
	public void setPlayer(IPlayer player)
	{
		this.opponent = player;
		// TODO konstruktorba egyéb dolgok, például
	}
	
	//! \brief  Click kezelése
	@Override
	public void click(Position position)
	{
		System.out.println("GameLogic handles click.");
		// TODO Klikk kezelése
		
	}
	
	//! \brief  Gyalog elõléptetés kezelése
	@Override
	public void promote(Piece piece)
	{
		System.out.println("GameLogic handles promote.");
		// TODO Beérkezõ promóció kezelése
		
	}
}
