
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
 *  Chessnut mesterséges intelligencia osztálya
 *  <p>
 *  A Negamax (negált maximum) a minimax és az alfa-béta eljárás implementálásának egyszeru formája, a következo egyenloség felhasználásával:
 *  max{min{x1, x2, ...}, min{y1, y2, ...}} = max{-max{-x1, -x2, ...},-max{-y1, -y2, ...}}
 *  Ahol xi és yi egy fa levelei.
 *  <p>
 *  http://homepages.cwi.nl/~paulk/theses/Carolus.pdf
 *  <p>
 *  A sakktábla kiértékelésénél minden bábunak saját értéke van. Ezek az értékek szorzódnak -1, vagy +1-gyel, attól függoen, hogy milyen színrol van szó.
 *  Ezen felül a mobilitási paraméter megmondja, hogy hány lehetséges következo lépése van a bábunak.
 *  A ketto fenti paramétert minden bábura kiértékelve és összeadva, megkapjuk az aktuális állás elojeles értékét.
 *  Jelen esetben a fekete bábuhoz pozitív érték tartozik, fehérhez pedig negaítv.
 *  <p>
 *  https://chessprogramming.wikispaces.com/Evaluation
 *  
 */
public class AI implements IPlayer
{
	/** Ezen a referencián érheti el a GameLogic-ot*/
	ILogic gameLogic;   
	
	/** A mesterséges intelligencia számolásának iterációi*/
	int iterations;
	
	/** A mesterséges intelligencia kiszámolt lépései*/
	Move moveAI;
	
	/** Az elso kattintás indikátora*/
	boolean firstStep;
	
	/** A kapott sakktábla, amelyen a számolások vézendoek*/
	ChessBoard ownChessboard;
	
	
	/**  Konstruktor: létrehozható gameLogic alapján*/
	public AI(ILogic logic)
	{
		this.gameLogic = logic;
	}
	
	
	/** Ezzel beállítható a gamelogic referencia*/
	@Override
	public void setGameLogic(ILogic logic)
	{
		this.gameLogic = logic;
	}
		
	/**
	* Negamax keresési algoritmus, a következo dokumentum alapján:
	* http://homepages.cwi.nl/~paulk/theses/Carolus.pdf  12. oldal
	* @param moves: a kiszámolt lépés
	* @param board: az aktuális sakktábla
	* @param depth: a keresés mélysége
	* @param alpha: az algoritmus alfa paramétere
	* @param beta: az algoritmus béta paramétere
	* @return A legjobb lépés kiértékelésének eredménye
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
		if (depth == 0)	//a keresés a megfelelo mélységu
		{
			value = evaluate(board);	//a sakktábla kiértékelése
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
					
					move = board.getAllPossibleNextMoves().get(i);	//az összes lehetséges következo lépés közül kiválasztjuk a következot
				
					if(move.getStart() != null)
					{
						tempColor = board.getPiece(move.getStart()).getColor();	
					}else
					{
						tempColor = PlayerColor.White;
					}
					
					if (Objects.equals(tempColor, PlayerColor.Black))	//csak a fekete bábukat viizsgáljuk	
					{
						
						nextBoard = board.getPossibleNextBoards().get(i);	//következo lehetséges tábla	
					
						
						value = -alphaBeta(move, nextBoard, depth-1, -beta, -alpha);	//rekurzió 
						//
						
						
					if(value > best && Objects.equals(move, moveNull));// bábu színére vonatkozó ellenorzések	
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
							moveAI = move;	//legjobb lépést eltároljuk
						}
					}
					if(best>value)
						alpha = best;	//az algoritmus új alfa értéke
					if(best >= beta)
						break;
					}
				}
			}
		}
		
		return best;
	}
	
	
	/**
	* A sakktábla aktuális állását adja meg a táblán elhelyezkedo bábuk alapján.
	* Figyelembe veszi, hogy melyik játékosnak milyen bábui vannak a táblán, illetve azok mozgási lehetoségeit.
	* @param board: aktuális sakktábla
	* @return Kiértékelt sakktábla értéke
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
			for(int ranks = 7; ranks>=0; ranks--)	//az összes bábut beleszámoljuk a játékba
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
		
				
		value = valueMobility + 10*valueMaterial; 	// sakktábla értékének számítása
		return value;
	}
	
	
	/**
	* AI szál indítása
	* @param chessboard: aktuális sakktábla
	*/
	@Override	
	public void setChessboard(ChessBoard chessboard)
	{
		System.out.println("AI handles setChessboard.");
		ownChessboard = chessboard;
		Thread aiThread = new Thread(new AiWorkingThread());
		aiThread.start();
	}
	

	/** A mesterséges intelligencia szála */
	private class AiWorkingThread implements Runnable
	{
		@Override
		/**
		* Mielott elkezdodik az AI algoritmusa, idozítési problémák miatt 1 másodpercet várunk.
		* Ha új sakktábla érkezett, és a fekete játékos a soros, elkezdodik a számlálás.
		* Számlálás után az elso kattintás megtörténik.
		* Ha megkaptuk az új táblát, a második kattintás is megtörténik.
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
					gameLogic.click(moveAI.getStart(), PlayerColor.Black);	//az elso kattintás 
					
						
					
					System.out.println("AI ended");
					firstStep = false;
				} else if (ownChessboard.getNextToMove() == PlayerColor.Black && iterations >0)
				{
					gameLogic.click(moveAI.getEnd(), PlayerColor.Black);	//a második kattintás
					
					iterations = 0;
					firstStep = true;
				}
		}
	}
	
	/**
	* Gyalogos eloléptetése
	* @param position: léptetendo bábu pozíciója
	*/
	 @Override
	public void notifyPromotion(Position position)
	{
		 System.out.println("AI handles notifyPromotion.");
		 Piece piece = new Queen(PlayerColor.Black);
		 gameLogic.promote(piece);
	}
}