/*************************************************
 *  \file     ConsolePlayerInterface.java
 *  \brief    Konzolos interfész a játékosnak
 *  \note     
 *  \date     2016. máj. 16.
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
	ILogic logic;               //!< Játéklogika elérése
	PlayerColor myPlayerColor;  //!< Színem
	
	//! \brief  Default konstruktor
	public ConsolePlayerInterface(){}
	
	//! \brief  Konstruktor
	public ConsolePlayerInterface(ILogic logic)
	{
		this.logic = logic;
		myPlayerColor = (logic instanceof GameLogic) ? PlayerColor.White : PlayerColor.Black;
	}
	
	//! \brief  Lépés bekérése thread
	private class PlayerAskForClickThread implements Runnable
	{
		@Override
		public void run()
		{
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			String s = null;

			try
			{
				while (s == null)
				{
					// Felhasználó itt ír be:
					System.out.println("You click now:");
					s = reader.readLine();
					if (s.equals("quit"))
					{
						System.exit(0);
					}

					// Ha baromságot írt be, megy tovább a ciklus
					if(!handleClick(s))
					{
						System.out.println("Invalid click, try again...");
						s = null;
					}
				}
			} catch (Exception e)
			{
				System.err.println(e.getMessage());
			}
		}
	}
	
	// ! \brief Promóció kérése thread
	private class PlayerAskForPromoteThread implements Runnable
	{
		@Override
		public void run()
		{
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			String s = null;

			try
			{
				while (s == null)
				{
					// Felhasználó itt ír be:
					System.out.println("You promote now:");
					s = reader.readLine();
					if (s.equals("quit"))
					{
						System.exit(0);
					}

					// Ha baromságot írt be, megy tovább a ciklus
					if(!handlePromote(s))
					{
						System.out.println("Invalid promotion, try again...");
						s = null;
					}
				}
			} catch (Exception e)
			{
				System.err.println(e.getMessage());
			}
		}
	}
	

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
		
		return true;
	}

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
	
	
	// Interfész függvényei
	@Override
	public void setChessboard(ChessBoard chessboard)
	{
		System.out.println(chessboard);
		
		if(chessboard.getNextToMove() == myPlayerColor)
		{
			if( !(chessboard.isAwaitingPromotion()) )
			{
				Thread clickthread = new Thread(new PlayerAskForClickThread());
				clickthread.start();
			}
		}
		
	}
	
	
	@Override
	public void notifyPromotion(Position position)
	{
		Thread promotethread = new Thread(new PlayerAskForPromoteThread());
		promotethread.start();
	}
	
	
	@Override
	public void setGameLogic(ILogic logic)
	{
		this.logic = logic;
		myPlayerColor = (logic instanceof GameLogic) ? PlayerColor.White : PlayerColor.Black;
	}
} 
