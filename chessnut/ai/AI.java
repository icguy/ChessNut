
package chessnut.ai;

import java.util.Objects;

import chessnut.ILogic;
import chessnut.IPlayer;
import chessnut.logic.ChessBoard;
import chessnut.logic.Position;
import chessnut.logic.pieces.Piece;
import chessnut.logic.pieces.Queen;
import chessnut.logic.PlayerColor;
import chessnut.logic.Move;
/**
 *  Chessnut mesters�ges intelligencia oszt�lya
 *  <p>
 *  A Negamax (neg�lt maximum) a minimax �s az alfa-b�ta elj�r�s implement�l�s�nak egyszeru form�ja, a k�vetkezo egyenlos�g felhaszn�l�s�val:
 *  max{min{x1, x2, ...}, min{y1, y2, ...}} = max{-max{-x1, -x2, ...},-max{-y1, -y2, ...}}
 *  Ahol xi �s yi egy fa levelei.
 *  <p>
 *  http://homepages.cwi.nl/~paulk/theses/Carolus.pdf
 *  <p>
 *  A sakkt�bla ki�rt�kel�s�n�l minden b�bunak saj�t �rt�ke van. Ezek az �rt�kek szorz�dnak -1, vagy +1-gyel, att�l f�ggoen, hogy milyen sz�nrol van sz�.
 *  Ezen fel�l a mobilit�si param�ter megmondja, hogy h�ny lehets�ges k�vetkezo l�p�se van a b�bunak.
 *  A ketto fenti param�tert minden b�bura ki�rt�kelve �s �sszeadva, megkapjuk az aktu�lis �ll�s elojeles �rt�k�t.
 *  Jelen esetben a fekete b�buhoz pozit�v �rt�k tartozik, feh�rhez pedig nega�tv.
 *  <p>
 *  https://chessprogramming.wikispaces.com/Evaluation
 *  
 */
public class AI implements IPlayer
{
	/** Ezen a referenci�n �rheti el a GameLogic-ot*/
	ILogic gameLogic;   
	
	/** A mesters�ges intelligencia sz�mol�s�nak iter�ci�i*/
	int iterations;
	
	/** A mesters�ges intelligencia kisz�molt l�p�sei*/
	Move moveAI;
	
	/** Az elso kattint�s indik�tora*/
	boolean firstStep;
	
	/** A kapott sakkt�bla, amelyen a sz�mol�sok v�zendoek*/
	ChessBoard ownChessboard;
	
	
	/**  Konstruktor: l�trehozhat� gameLogic alapj�n*/
	public AI(ILogic logic)
	{
		this.gameLogic = logic;
	}
	
	
	/** Ezzel be�ll�that� a gamelogic referencia*/
	@Override
	public void setGameLogic(ILogic logic)
	{
		this.gameLogic = logic;
	}
		
	/**
	* Negamax keres�si algoritmus, a k�vetkezo dokumentum alapj�n:
	* http://homepages.cwi.nl/~paulk/theses/Carolus.pdf  12. oldal
	* @param moves: a kisz�molt l�p�s
	* @param board: az aktu�lis sakkt�bla
	* @param depth: a keres�s m�lys�ge
	* @param alpha: az algoritmus alfa param�tere
	* @param beta: az algoritmus b�ta param�tere
	* @return A legjobb l�p�s ki�rt�kel�s�nek eredm�nye
	*/
	private int alphaBeta(Move moves, ChessBoard board, int depth, int alpha, int beta)
	{
		int value = 0;
		int best = -99; // -MATE-1
		int i = 0;
		int rank = 0;
		int file = 0;
		Position posNull = new Position(0,0);
		PlayerColor tempColor, tempColor1;
		if (depth == 0)	//a keres�s a megfelelo m�lys�gu
		{
			value = evaluate(board);	//a sakkt�bla ki�rt�kel�se
			return value;
		}
		Move moveNull = new Move(posNull, posNull);
		Move move;
		ChessBoard nextBoard; 
		i=board.getAllPossibleNextMoves().size()-1;
		while(i > 0 )
		{
			iterations++;		
			i--;
			for(rank = 7; rank>=0; rank--)	
			{
				for(file = 7; file>=0; file--)
				{
					
					move = board.getAllPossibleNextMoves().get(i);	//az �sszes lehets�ges k�vetkezo l�p�s k�z�l kiv�lasztjuk a k�vetkezot
				
					if(move.getStart() != null)
					{
						tempColor = board.getPiece(move.getStart()).getColor();	
					}else
					{
						tempColor = PlayerColor.White;
					}
					
					if (Objects.equals(tempColor, PlayerColor.Black))	//csak a fekete b�bukat viizsg�ljuk	
					{
						
						nextBoard = board.getPossibleNextBoards().get(i);	//k�vetkezo lehets�ges t�bla	
					
						
						value = -alphaBeta(move, nextBoard, depth-1, -beta, -alpha);	//rekurzi� 
						//
						
						
					if(value > best && Objects.equals(move, moveNull));// b�bu sz�n�re vonatkoz� ellenorz�sek	
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
							moveAI = move;	//legjobb l�p�st elt�roljuk
						}
					}
					if(best>value)
						alpha = best;	//az algoritmus �j alfa �rt�ke
					if(best >= beta)
						break;
					}
				}
			}
		}
		
		return best;
	}
	
	
	/**
	* A sakkt�bla aktu�lis �ll�s�t adja meg a t�bl�n elhelyezkedo b�buk alapj�n.
	* Figyelembe veszi, hogy melyik j�t�kosnak milyen b�bui vannak a t�bl�n, illetve azok mozg�si lehetos�geit.
	* @param board: aktu�lis sakkt�bla
	* @return Ki�rt�kelt sakkt�bla �rt�ke
	*/
	private int evaluate(ChessBoard board)
	{
		int valueMobility = 0;
		int valueMaterial = 0;
		int value = 0;
		String tempChar = "";
		Position pos = new Position(0,0);
		
		ChessBoard tempBoard;
		{
			tempBoard = board;
			for(int ranks = 7; ranks>=0; ranks--)	//az �sszes b�but belesz�moljuk a j�t�kba
			{
				for(int  files = 7; files >=0;  files--)
				{
					pos = new Position(ranks, files);
					tempChar = "";
					tempChar += tempBoard.getPiece(ranks, files);
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
		
				
		value = valueMobility + 10*valueMaterial; 	// sakkt�bla �rt�k�nek sz�m�t�sa
		return value;
	}
	
	
	/**
	* AI sz�l ind�t�sa
	* @param chessboard: aktu�lis sakkt�bla
	*/
	@Override	
	public void setChessboard(ChessBoard chessboard)
	{
		System.out.println("AI handles setChessboard.");
		ownChessboard = chessboard;
		Thread aiThread = new Thread(new AiWorkingThread());
		aiThread.start();
	}
	

	/** A mesters�ges intelligencia sz�la */
	private class AiWorkingThread implements Runnable
	{
		@Override
		/**
		* Mielott elkezdodik az AI algoritmusa, idoz�t�si probl�m�k miatt 1 m�sodpercet v�runk.
		* Ha �j sakkt�bla �rkezett, �s a fekete j�t�kos a soros, elkezdodik a sz�ml�l�s.
		* Sz�ml�l�s ut�n az elso kattint�s megt�rt�nik.
		* Ha megkaptuk az �j t�bl�t, a m�sodik kattint�s is megt�rt�nik.
		*/
		public void run()
		{
			Position firstClick = new Position(6,0);
			Position secondClick = new Position(0,0);
			Move moves = new Move(firstClick, secondClick); 
				try {
				    Thread.sleep(1000);                 //1000 milliseconds is one second.
				} catch(InterruptedException ex) {
				    Thread.currentThread().interrupt();
				}
				if(ownChessboard.getNextToMove() == PlayerColor.Black && iterations == 0 )
				{
					System.out.println("AI started");

					
					
					iterations = 0;
					alphaBeta(moves, ownChessboard, 2, -100, 100);		// kereso algoritmus
					gameLogic.click(moveAI.getStart(), PlayerColor.Black);	//az elso kattint�s 
					
						
					
					System.out.println("AI ended");
					firstStep = false;
				} else if (ownChessboard.getNextToMove() == PlayerColor.Black && iterations >0)
				{
					gameLogic.click(moveAI.getEnd(), PlayerColor.Black);	//a m�sodik kattint�s
					
					iterations = 0;
					firstStep = true;
				}
		}
	}
	
	/**
	* Gyalogos elol�ptet�se
	* @param position: l�ptetendo b�bu poz�ci�ja
	*/
	 @Override
	public void notifyPromotion(Position position)
	{
		 System.out.println("AI handles notifyPromotion.");
		 Piece piece = new Queen(PlayerColor.Black);
		 gameLogic.promote(piece);
	}
}