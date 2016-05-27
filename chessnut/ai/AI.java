/*************************************************
 *  \file     AI.java
 *  \brief    Chessnut mesters�ges intelligencia oszt�lya
 *  \note     
 *  \date     2016. m�j. 13.
 *************************************************/
package chessnut.ai;

import java.util.Objects;

import chessnut.ILogic;
import chessnut.IPlayer;
import chessnut.logic.ChessBoard;
import chessnut.logic.Position;
import chessnut.logic.PlayerColor;
import chessnut.logic.Move;

public class AI implements IPlayer
{
	ILogic gameLogic;   //!< Ezen a referenci�n �rheti el a GameLogic-ot
	int iterations;
	Move moveAI;
	boolean firstStep;
	
	//! \brief  Konstruktor: l�trehozhat� gameLogic alapj�n
	public AI(ILogic logic)
	{
		this.gameLogic = logic;
	}
	private int alphaBeta(Move moves, ChessBoard board, int depth, int alpha, int beta)
	{
		int value = 0;
		int best = -99; // -MATE-1
		int i = 0;
		int rank = 0;
		int file = 0;
		Position posNull = new Position(0,0);
		PlayerColor tempColor, tempColor1;
		if (depth == 0)	//vagy a boardnak v�ge van ??
		{
			value = evaluate(board);
			return value;
		}
		//gotMoveBoard = getPossibleMoves(posTemp, board);
		Move moveNull = new Move(posNull, posNull);
		Move move;
		ChessBoard nextBoard; //=board.getPossibleNextBoards().get(0); 	//????
		i=board.getAllPossibleNextMoves().size()-1;
		while(i > 0 )
		{
			iterations++;		
			i--;
			for(rank = 7; rank>=0; rank--)
			{
				for(file = 7; file>=0; file--)
				{
					
					move = board.getAllPossibleNextMoves().get(i);	
				
					if(move.getStart() != null)
					{
						tempColor = board.getPiece(move.getStart()).getColor();
					}else
					{
						tempColor = PlayerColor.White;
					}
					if (Objects.equals(tempColor, PlayerColor.Black))
					{
						
						nextBoard = board.getPossibleNextBoards().get(i);
					
						
						value = -alphaBeta(move, nextBoard, depth-1, -beta, -alpha); //clicks !!!!
						//
						
						
					if(value > best && Objects.equals(move, moveNull));
					{
						if(board.getPiece(move.getStart()).getColor() != null)
						{
							tempColor1 = board.getPiece(move.getStart()).getColor();
						}else
						{
							tempColor1 = PlayerColor.White;
						}
						
						
						if(Objects.equals(tempColor1, PlayerColor.White))
							value = -1000;
						else{
							
							best = value;
							moveAI = move;
						}
					}
					if(best>value)
						alpha = best;
					if(best >= beta)
						break;
					}
				}
			}
		}
		
		//board = boardTempToOperate;
		return best;
	}
	private int evaluate(ChessBoard board)
	{
		int valueMobility = 0;
		int valueMaterial = 0;
		int value = 0;
		int i=0;
		int j=0;
		String tempChar = "";
		//Piece tempPiece = new Piece()
		Position pos = new Position(0,0);
		
		ChessBoard tempBoard;
		//while(board.getPossibleNextBoards().size() < i)
		{
			//i++;
			tempBoard = board;//.getPossibleNextBoards().get(i);
			for(int ranks = 7; ranks>=0; ranks--)
			{
				for(int  files = 7; files >=0;  files--)
				{
					//pos=Position.tryCreate(ranks, files);
					pos = new Position(ranks, files);
					tempChar = "";
					tempChar += tempBoard.getPiece(ranks, files);
					//.toString();
					if (Objects.equals(tempChar, "K")) //King white
					{
						valueMobility -= tempBoard.getPiece(ranks, files).getPossibleMoves(pos, tempBoard).size();
						valueMaterial -= 200;
					} else if (Objects.equals(tempChar, "k")) //King black
					{
						valueMobility += tempBoard.getPiece(ranks, files).getPossibleMoves(pos, tempBoard).size();
						valueMaterial += 200;

					}
					 else if (Objects.equals(tempChar, "Q")) //Queen black
					{
						 valueMobility -= tempBoard.getPiece(ranks, files).getPossibleMoves(pos, tempBoard).size();
						 valueMaterial -= 9;
					
					}else if (Objects.equals(tempChar, "q"))  //Queen black
					{
						valueMobility += tempBoard.getPiece(ranks, files).getPossibleMoves(pos, tempBoard).size();
						valueMaterial += 9;

					}else if (Objects.equals(tempChar, "B")) //Bishop black
					{
						valueMobility -= tempBoard.getPiece(ranks, files).getPossibleMoves(pos, tempBoard).size();
						valueMaterial -= 3;

					} else if (Objects.equals(tempChar, "b")) //Bishop black
					{
						valueMobility += tempBoard.getPiece(ranks, files).getPossibleMoves(pos, tempBoard).size();
						valueMaterial += 3;

					} else if (Objects.equals(tempChar, "N")) //Knight black
					{
						valueMobility -= tempBoard.getPiece(ranks, files).getPossibleMoves(pos, tempBoard).size();
						valueMaterial -= 3;

					} else if (Objects.equals(tempChar, "n")) //Knight black
					{
						valueMobility += tempBoard.getPiece(ranks, files).getPossibleMoves(pos, tempBoard).size();
						valueMaterial += 3;

					} else if (Objects.equals(tempChar, "P")) //Pawn black
					{
						valueMobility -= tempBoard.getPiece(ranks, files).getPossibleMoves(pos, tempBoard).size();
						valueMaterial -= 1;

					} else if (Objects.equals(tempChar, "p")) //Pawn black
					{
						valueMobility += tempBoard.getPiece(ranks, files).getPossibleMoves(pos, tempBoard).size();
						valueMaterial += 1;

					} else if (Objects.equals(tempChar, "R")) //Rook black
					{
						valueMobility -= tempBoard.getPiece(ranks, files).getPossibleMoves(pos, tempBoard).size();
						valueMaterial -= 5;

					} else if (Objects.equals(tempChar, "r")) //rook black
					{
						valueMobility += tempBoard.getPiece(ranks, files).getPossibleMoves(pos, tempBoard).size();
						valueMaterial += 5;

					}
				}
			}
			
			
		}
		
		
		
		
		
		value = valueMobility + 10*valueMaterial;
		return value;
	}
	//! \brief  Ezzel be�ll�that� a gamelogic referencia
	@Override
	public void setGameLogic(ILogic logic)
	{
		this.gameLogic = logic;
	}
	
	
	//! \brief  Sakkt�bl�t kaptam
	@Override
	public void setChessboard(ChessBoard chessboard)
	{
		System.out.println("AI handles setChessboard.");
		
		// TODO {AI} Ide kell meg�rni a kapott sakkt�bl�ra reakci�t
		int value;
		Position firstClick = new Position(6,0);
		Position secondClick = new Position(0,0);
		Move moves = new Move(firstClick, secondClick);//(firstClick, secondClick);
		//moves.Move;
		/*
		 * Valami ilyesmi, hogy:
		 * 
		 * Megn�zem, hogy �n j�v�k-e
		 * {
		 * 		Ha �n j�v�k, megn�zem, hogy ez most az els� kattint�som lesz, vagy a m�sodik
		 *  	Ha az els� kattint�som lesz
		 *  	{
		 */  		//v�gig kell menni a j�t�kosaimon
					//moves.
			try {
			    Thread.sleep(1000);                 //1000 milliseconds is one second.
			} catch(InterruptedException ex) {
			    Thread.currentThread().interrupt();
			}
			if(chessboard.getNextToMove() == PlayerColor.Black && iterations == 0 )
			{
				
				//if(chessboard.)
				System.out.println("AI started");
				//gameLogic.
				//gameLogic.click(firstClick, PlayerColor.Black);
				//
			
				

				
				
				
				iterations = 0;
				value = alphaBeta(moves, chessboard, 2, -100, 100);
				value = 0;
				this.gameLogic.click(moveAI.getStart(), PlayerColor.Black);
				
					
				
				System.out.println("AI ended");
				firstStep = false;
			} else if (chessboard.getNextToMove() == PlayerColor.Black && iterations >0)
			{
				this.gameLogic.click(moveAI.getEnd(), PlayerColor.Black);
				
				iterations = 0;
				firstStep = true;
			}
		
		
		
		
		
		 /*  		
		 *  		
		 *  		valahogy kisz�molom az optim�lis l�p�st, (ami igaz�b�l maga az AI feladat)
		 *  		azt�n megkattintom az els�t (a kiindul� mez�t)
		 *  	}
		 *  	Ha a m�sodik kattint�som lesz
		 *  	{
		 *  		Megkattintom a m�sodik kattint�st az el�z�leg kisz�molt l�p�s alapj�n
		 *  	}
		 * }
		 */
		
	}
	
	
	//! \brief  Megk�rtek, hogy l�ptessek el� egy parasztot
	 @Override
	public void notifyPromotion(Position position)
	{
		 System.out.println("AI handles notifyPromotion.");
		 
		// TODO {AI} Paraszt el�l�ptet�st megcsin�lni
		
	}
}
