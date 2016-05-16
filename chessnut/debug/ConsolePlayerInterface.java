/*************************************************
 *  \file     ConsolePlayerInterface.java
 *  \brief    Konzolos interf�sz a j�t�kosnak
 *  \note     
 *  \date     2016. m�j. 16.
 *************************************************/
package chessnut.debug;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import chessnut.ILogic;
import chessnut.IPlayer;
import chessnut.logic.*;
import chessnut.logic.pieces.*;

public class ConsolePlayerInterface implements IPlayer
{
	ILogic logic = null;                   //!< J�t�klogika el�r�se
	PlayerColor myPlayerColor = null;      //!< Sz�nem
	boolean isPromotionOn = false;         //!< Lesz-e mostan�ban promotion?
	
	//! \brief  Default konstruktor
	public ConsolePlayerInterface()
	{
		// Thread nyit�sa
		Thread playerInputThread = new Thread(new PlayerInputThread());
		playerInputThread.start();
	}
	
	//! \brief  Konstruktor
	public ConsolePlayerInterface(ILogic logic)
	{
		this.logic = logic;
		myPlayerColor = (logic instanceof GameLogic) ? PlayerColor.White : PlayerColor.Black;
	}
	
	//! \brief  GameLogic referencia be�ll�t�sa
	@Override
	public void setGameLogic(ILogic logic)
	{
		this.logic = logic;
		myPlayerColor = (logic instanceof GameLogic)
				? PlayerColor.White
				: PlayerColor.Black;
	}
	
	
	//! \brief  Felhaszn�l�i input thread
	private class PlayerInputThread implements Runnable
	{
		@Override
		public void run()
		{
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			String s = null;

			try
			{
				while (true)
				{
					// Felhaszn�l� itt �r be:
					s = reader.readLine();
					if (s.equals("quit"))
					{
						System.exit(0);
					}

					// Ha van logic, ami fogadja a beavatkoz�st
					if(logic != null)
					{
						// Rendes l�p�st v�rok
						if(isPromotionOn == false)
						{
							if(!handleClick(s))
							{
								System.out.println("Invalid click, try again...");
							}
						}
						else
						{
							if(!handlePromote(s))
							{
								System.out.println("Invalid promotion, try again...");
							}
						}
					}
				}
			} catch (Exception e)
			{
				System.err.println(e.getMessage());
			}
		}
	}
	
	
	//! \brief  Promote inputot kezeli
	public boolean handlePromote(String s)
	{
		Piece piece = null;
		
		if (s.length() != 1)
		{
			return false;
		}

		switch (s.toLowerCase().charAt(0))
		{
			case 'n' :
				piece = new Knight(myPlayerColor);
				break;
			case 'q' :
				piece = new Queen(myPlayerColor);
				break;
			case 'b' :
				piece = new Bishop(myPlayerColor);
				break;
			case 'r' :
				piece = new Rook(myPlayerColor);
				break;
			default :
				return false;
		}
		
		logic.promote(piece);
		
		isPromotionOn = false;
		
		return true;
	}

	
	//! \brief  Click inputot kezeli
	private boolean handleClick(String s)
	{		
		if(s.length() != 2)
		{
			return false;
		}
		
		int File = s.charAt(0) - 'a';
		int Rank = s.charAt(1) - '1';

		Position pos = new Position(Rank, File);
		
		logic.click(pos, myPlayerColor);
		
		return true;
	}
	
	
	//! \brief  Be�rkez� sakkt�bla kezel�se
	@Override
	public void setChessboard(ChessBoard chessboard)
	{
		System.out.println(chessboard);
		
		if(chessboard.getNextToMove() == myPlayerColor)
		{
			if( !(chessboard.isAwaitingPromotion()) )
			{
				System.out.println("You click now:");
			}
		}
		
	}
	
	
	//! \brief  Be�rkez� prom�ci� k�r�s kezel�se
	@Override
	public void notifyPromotion(Position position)
	{
		System.out.println("You promote now:");
		
		isPromotionOn = true;
	}
} 
