/*************************************************
 *  \file     GameLogic.java
 *  \brief    Ez lesz a j�t�klogika oszt�ly
 *  \note     
 *  \date     2016. m�j. 12.
 *************************************************/
package chessnut.logic;

import chessnut.ILogic;
import chessnut.IPlayer;
import chessnut.gui.GUI;
import chessnut.logic.Position;
import chessnut.logic.pieces.*;

public class GameLogic implements ILogic
{
	// J�t�k elemei
	ChessBoard chessboard;     //!< Sakkt�bl�m
	IPlayer gui;               //!< Egyik j�t�kos a helyi GUI
	IPlayer opponent;          //!< M�sik j�t�kos: AI / Network
	
	
	//! \brief  L�trehoz�s GUI alapj�n
	public GameLogic( IPlayer gui )
	{
		this.gui = gui;
	}
	
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
		System.out.println("GameLogic handles click.");
		// TODO {GameLogic} Klikk kezel�se
		
	}
	
	//! \brief  Gyalog el�l�ptet�s kezel�se
	@Override
	public void promote(Piece piece)
	{		
		System.out.println("GameLogic handles promote.");
		// TODO {GameLogic} Be�rkez� prom�ci� kezel�se
		
	}
}
