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
import chessnut.logic.*;
import chessnut.logic.pieces.*;

public class ConsolePlayerInterface
{/*
	ILogic logic;   //!< J�t�klogika el�r�se
	
	//! \brief  Konstruktor
	public ConsolePlayerInterface(ILogic logic)
	{
		this.logic = logic;
		Thread play = new Thread(new PlayerActionThread());
		play.start();
	}
	
	//! \brief  K�l�n thread-ben fusson, mert k�l�nben �sszefossa mag�t az eg�sz
	private class PlayerActionThread implements Runnable
	{
		@Override
		public void run()
		{
			while (true)
			{
				// L�p�s bek�r�se
				askPlayer();
			}
		}
	}
	
	//! \brief  Input ezen j�n be
	public static void askPlayer()
	{
		BufferedReader reader = new BufferedReader(
				new InputStreamReader(System.in));
		boolean quit = false;
		
		while (!quit)
		{
			try
			{
				System.out.println(board);
				while (true)
				{
					System.out.println(":");
					String s = reader.readLine();
					if (s.equals("quit"))
					{
						quit = true;
						break;
					}

					try
					{
						if (!board.isAwaitingPromotion())
						{
							if (normal(s, board))
								break;
							else
								continue;
						} else
						{
							if (promotion(s, board))
								break;
							else
								continue;
						}
					} catch (IndexOutOfBoundsException ex)
					{
						continue;
					} catch (IllegalArgumentException ex)
					{
						continue;
					}
				}
				// end input

			} catch (Exception e)
			{
				System.err.println(e.getMessage());
				break;
			}
		}
	}
	public static boolean promotion(String s, ChessBoard board)
	{
		if (s.length() < 1)
			return false;

		switch (s.toLowerCase().charAt(0))
		{
			case 'n' :
				board.Promote(new Knight(board.getNextToMove()));
				break;
			case 'q' :
				board.Promote(new Queen(board.getNextToMove()));
				break;
			case 'b' :
				board.Promote(new Bishop(board.getNextToMove()));
				break;
			case 'r' :
				board.Promote(new Rook(board.getNextToMove()));
				break;
			default :
				return false;
		}
		return true;
	}

	public static boolean normal(String s, ChessBoard board)
	{
		String[] ss = s.split(" ");
		if (ss.length < 2)
			return false;
		int startFile = ss[0].charAt(0) - 'a';
		int startRank = ss[0].charAt(1) - '1';
		int endFile = ss[1].charAt(0) - 'a';
		int endRank = ss[1].charAt(1) - '1';
		Move m = new Move(new Position(startRank, startFile),
				new Position(endRank, endFile));
		return board.makeMove(m);
	} */
} 
