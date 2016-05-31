package chessnut.logic;

import java.io.Serializable;
import java.util.ArrayList;
import chessnut.logic.pieces.*;

/**
 * A sakktábla állását, és a kijelöléseket tároló osztály.
 * Ismeri a sakk szabályait, képes konzisztens állapotban tartani a táblát, nem enged meg szabálytalan lépést.
 * Kívülrõl lekérhetõ a játék állapota, a sakktábla állapota, az egyes mezõkön álló bábuk, a kijelölt mezõk.
 */
public class ChessBoard implements Serializable
{
	/**
	 * Sorosításhoz szükséges azonosító
	 */
	private static final long serialVersionUID = 1532472298888732188L;
	
	/**
	 * A mezõk aktuális kijelöltsége
	 */
	private SelectionType[][] selection;
	
	/**
	 * A táblán lévõ bábuk
	 */
	private Piece[][] board;
	
	/**
	 * A következõ játékos színe
	 */
	private PlayerColor nextMove;
	
	/**
	 * Az összes lehetséges következõ lépés listája 
	 */
	private ArrayList<Move> allPossibleMoves;
	
	/**
	 * A sötét király pozíciója
	 */
	private Position blackKingPos;
	
	/**
	 * A világos király pozíciója
	 */
	private Position whiteKingPos;
	
	/**
	 * True, ha épp arra várunk, hogy a következõ játékos kiválassza, hogy mire cseréli le a bevitt gyalogját
	 */
	private boolean awaitingPromotion;
	
	/**
	 * A lecserélt gyalog pozíciója
	 */
	private Position promotionPos;
	
	/**
	 * A játék állapota
	 */
	private ChessgameState gameState;

	/**
	 * Default konstruktor. Új sakktáblát hoz létre a kezdõállással, világos lép
	 */
	public ChessBoard()
	{
		selection = new SelectionType[8][8];
		board = new Piece[8][8];
		nextMove = PlayerColor.White;
		initBoard();
		updateKingPos();
		updateGameState();
	}
	
	
	/**
	 * Copy konstruktor a network számára
	 * @param chessboard A másolandó sakktábla
	 */
	public ChessBoard(ChessBoard chessboard)
	{
		try
		{
			this.selection = new SelectionType[8][8];
			for(int i = 0; i < 8; i++)
			{
				for(int j = 0; j < 8; j++)
				{
					this.selection[i][j] = chessboard.selection[i][j];
				}
			}
			this.board = new Piece[8][8];
			for(int i = 0; i < 8; i++)
			{
				for(int j = 0; j < 8; j++)
				{
					this.board[i][j] = chessboard.board[i][j];
				}
			}
	
			this.nextMove = chessboard.nextMove;
			this.allPossibleMoves = null;
			this.blackKingPos = null;
			this.whiteKingPos = null;
			this.awaitingPromotion = chessboard.awaitingPromotion;
			this.promotionPos = chessboard.promotionPos;
			this.gameState = chessboard.gameState;
		}catch(Exception e)
		{
			System.err.println("Error at Chessboard copy constructor: " + e.getMessage());
		}
	}

	/**
	 * Paraméteres konstruktor.
	 * @param board A tábla állása
	 * @param nextMove Következõ játékos 
	 */
	public ChessBoard(Piece[][] board, PlayerColor nextMove)
	{
		this.selection = new SelectionType[8][8];
		this.board = cloneTable(board);
		this.nextMove = nextMove;
		updateKingPos();
		updateGameState();
	}

	/**
	 * Paraméteres konstruktor. Kizárólag állás elemzésére
	 * @param board A tábla állása
	 * @param nextMove Következõ játékos 
	 * @param gameState A játék állapota
	 */
	private ChessBoard(Piece[][] board, PlayerColor nextMove, ChessgameState gameState)
	{
		this.selection = new SelectionType[8][8];
		this.board = cloneTable(board);
		this.nextMove = nextMove;
		this.gameState = gameState;
		updateKingPos();
		//no updateGameState(), because that leads to stack overflow.
		//only to be used when constructing boards for analysis (determining check etc.)
	}

	/**
	 * Beállítja a kezdõállapotot
	 */
	private void initBoard()
	{
		for (int i = 0; i < 8; i++)
		{
			for (int j = 0; j < 8; j++)
			{
				board[i][j] = null;
			}
		}

		for (int i = 0; i < 8; i++)
		{
			board[1][i] = new Pawn(PlayerColor.White);
			board[6][i] = new Pawn(PlayerColor.Black);
		}

		board[0][0] = new Rook(PlayerColor.White);
		board[0][1] = new Knight(PlayerColor.White);
		board[0][2] = new Bishop(PlayerColor.White);
		board[0][3] = new Queen(PlayerColor.White);
		board[0][4] = new King(PlayerColor.White);
		board[0][5] = new Bishop(PlayerColor.White);
		board[0][6] = new Knight(PlayerColor.White);
		board[0][7] = new Rook(PlayerColor.White);

		board[7][0] = new Rook(PlayerColor.Black);
		board[7][1] = new Knight(PlayerColor.Black);
		board[7][2] = new Bishop(PlayerColor.Black);
		board[7][3] = new Queen(PlayerColor.Black);
		board[7][4] = new King(PlayerColor.Black);
		board[7][5] = new Bishop(PlayerColor.Black);
		board[7][6] = new Knight(PlayerColor.Black);
		board[7][7] = new Rook(PlayerColor.Black);
	}

	/**
	 * Lecseréli a bevitt gyalogot az új bábura
	 * @param newPiece Az új bábu. A színének meg kell felelnie a következõ játékos színének
	 * @return True, ha sikeres volt a mûvelet
	 */
	public boolean Promote(Piece newPiece)
	{
		if (!awaitingPromotion)
			return false;
		if (newPiece instanceof King || newPiece instanceof Pawn)
			return false;
		if (newPiece.getColor() != nextMove)
			return false;
		if (gameState != ChessgameState.Playing)
			return false;

		int rank = promotionPos.getRank();
		int file = promotionPos.getFile();
		board[rank][file] = newPiece;
		awaitingPromotion = false;
		promotionPos = null;
		allPossibleMoves = null;
		changeNextMove();
		updateGameState();
		return true;
	}

	/**
	 * Végrehajt egy lépést a táblán
	 * 
	 * @param move A lépés
	 * @return True, ha sikerült a lépést meglépni
	 */
	public boolean makeMove(Move move)
	{
		if (awaitingPromotion)
			return false;
		if (gameState != ChessgameState.Playing)
			return false;

		Position start = move.getStart();
		Position end = move.getEnd();
		Piece movingPiece = getPieceRef(start);

		if (movingPiece == null || movingPiece.getColor() != nextMove)
			return false;
		if (end.equals(start))
			return false;

		getAllPossibleNextMoves();
		if (allPossibleMoves.contains(move))
		{
			if (movingPiece instanceof King && move.getDelta() == 2)
			{
				int rank = end.getRank();
				int middleFile = -1, rookFile = -1;
				if (end.getFile() == 6)
				{
					//castling right
					middleFile = 5;
					rookFile = 7;
				}
				else
				{
					//castling left
					middleFile = 3;
					rookFile = 0;
				}

				//moving rook
				board[rank][middleFile] = board[rank][rookFile];
				board[rank][rookFile] = null;
			}

			//move
			board[end.getRank()][end.getFile()] = board[start.getRank()][start.getFile()];
			board[start.getRank()][start.getFile()] = null;

			//update hasMoved, kingpos
			if (movingPiece instanceof King)
			{
				if (nextMove == PlayerColor.White)
					whiteKingPos = end;
				else
					blackKingPos = end;

				((King) movingPiece).setMoved(true);
			}
			else if (movingPiece instanceof Rook)
			{
				((Rook) movingPiece).setMoved(true);
			}

			//clear cache
			allPossibleMoves = null;

			//pawn promotion check
			int pawnFinalRank = (nextMove == PlayerColor.White) ? 7 : 0;
			if (movingPiece instanceof Pawn && end.getRank() == pawnFinalRank)
			{
				//promotion
				awaitingPromotion = true;
				promotionPos = end;
			}
			else
			{
				//if no promotion, next player's turn
				changeNextMove();
			}
			updateGameState();
			return true;
		}
		return false;
	}

	/**
	 * A következõ játékost az ellenkezõjére állítja
	 */
	private void changeNextMove()
	{
		nextMove = (nextMove == PlayerColor.White) ? PlayerColor.Black : PlayerColor.White;
	}

	/**
	 * Megvizsgálja, hogy a következõ játékos sakkban van-e.
	 * @return True, ha sakkban van
	 */
	private boolean isInCheckInner()
	{
		Position kingPos = (nextMove == PlayerColor.White) ? whiteKingPos : blackKingPos;

		//iterate over enemy pieces
		ArrayList<Move> moves = new ArrayList<>();
		for (int i = 0; i < 8; i++)
		{
			for (int j = 0; j < 8; j++)
			{
				Piece currPiece = getPiece(i, j);
				if (currPiece == null)
					continue;

				if (currPiece.getColor() != nextMove)
				{
					moves.addAll(currPiece.getPossibleMoves(new Position(i, j), this));
				}
			}
		}

		//iterate over moves
		for (Move move : moves)
		{
			if (move.getEnd().equals(kingPos))
				return true;
		}

		return false;
	}

	/**
	 * Frissíti a játék állapotát
	 */
	private void updateGameState()
	{
		getAllPossibleNextMoves();
		if (allPossibleMoves.isEmpty())
		{
			if (isInCheck())
				gameState = ChessgameState.Checkmate;
			else
				gameState = ChessgameState.Stalemate;
		}
		else
		{
			gameState = ChessgameState.Playing;
		}
	}

	/**
	 * Frissíti a királyok pozícióját
	 */
	private void updateKingPos()
	{
		for (int i = 0; i < 8; i++)
		{
			for (int j = 0; j < 8; j++)
			{
				Piece curr = board[i][j];
				if (curr instanceof King)
				{
					if (curr.getColor() == PlayerColor.Black)
						blackKingPos = new Position(i, j);
					else
						whiteKingPos = new Position(i, j);
				}
			}
		}
	}

	/**
	 * Teszt metódus
	 * @param obj
	 * @return
	 */
	public Object TestMethod(Object obj)
	{
		return getPossibleNextCastlingMoves();
		//return moveEndsUpInCheck((Move) obj);
	}

	/**
	 * Visszaadja a lehetséges sáncoló lépéseket
	 * @return A sáncoló lépések listája
	 */
	// @formatter:off
	private ArrayList<Move> getPossibleNextCastlingMoves()
	{
		//The king and the chosen rook are on the player's first rank.	OK
		//Neither the king nor the chosen rook has previously moved.  	OK
		//There are no pieces between the king and the chosen rook.   	OK
		//The king is not currently in check.							OK
		//The king does not pass through a square that is attacked by an enemy piece. OK
		//The king does not end up in check. (True of any legal move.)  OK
		
		ArrayList<Move> possibleCastlings = new ArrayList<>();
		int homeRank = nextMove == PlayerColor.White ? 0 : 7;
		Position kingPos = (nextMove == PlayerColor.White) ? whiteKingPos : blackKingPos;
		King king = (King) getPieceRef(kingPos);

		Rook leftRook, rightRook;
		Piece leftPiece, rightPiece;
		leftPiece = getPieceRef(new Position(homeRank, 0));
		rightPiece = getPieceRef(new Position(homeRank, 7));
		leftRook = (leftPiece instanceof Rook) ? (Rook) leftPiece : null;
		rightRook = (rightPiece instanceof Rook) ? (Rook) rightPiece : null;

		if (king.hasMoved())
			return possibleCastlings;
		if (isInCheck())
			return possibleCastlings;

		//castling left
		if (leftRook != null && !leftRook.hasMoved())
		{
			boolean piecesInbetween = 
					getPieceRef(homeRank, 1) != null ||
					getPieceRef(homeRank, 2) != null ||
					getPieceRef(homeRank, 3) != null;

			if (!piecesInbetween)
			{
				boolean move1 = moveEndsUpInCheck(new Move(homeRank, 4, homeRank, 3));
				boolean move2 = moveEndsUpInCheck(new Move(homeRank, 4, homeRank, 2));
				
				if(!move1 && ! move2)
					possibleCastlings.add(new Move(homeRank, 4, homeRank, 2));
			}
		}

		//castling right
		if (rightRook != null && !rightRook.hasMoved())
		{
			boolean piecesInbetween = 
					getPieceRef(homeRank, 5) != null ||
					getPieceRef(homeRank, 6) != null;

			if (!piecesInbetween)
			{
				boolean move1 = moveEndsUpInCheck(new Move(homeRank, 4, homeRank, 5));
				boolean move2 = moveEndsUpInCheck(new Move(homeRank, 4, homeRank, 6));
				
				if(!move1 && ! move2)
					possibleCastlings.add(new Move(homeRank, 4, homeRank, 6));
			}
		}
		
		return possibleCastlings;
	}
	// @formatter:on

	/**
	 * Megvizsgálja, hogy egy lépés után a lépést végrehajtó játékos sakkban van-e
	 * @param move A lépés
	 * @return True, ha a lépés szabálytalan, mert sakkba kerül a játékos
	 */
	private boolean moveEndsUpInCheck(Move move)
	{
		Position start = move.getStart();
		Position end = move.getEnd();
		Piece[][] newBoard = cloneTable(board);
		newBoard[end.getRank()][end.getFile()] = newBoard[start.getRank()][start.getFile()];
		newBoard[start.getRank()][start.getFile()] = null;
		ChessBoard newChessBoard = new ChessBoard(newBoard, nextMove, ChessgameState.Playing);
		return newChessBoard.isInCheck();
	}

	/**
	 * Létrehozza az adott állásból következõ összes további lehetséges állást
	 * @return Az összes következõ játékálláshoz tartozó ChessBoard listája
	 */
	public ArrayList<ChessBoard> getPossibleNextBoards()
	{
		ArrayList<ChessBoard> nextBoards = new ArrayList<>();
		getAllPossibleNextMoves();
		for (Move move : allPossibleMoves)
		{
			ChessBoard newBoard = this.clone();
			newBoard.makeMove(move);
			if (newBoard.isAwaitingPromotion())
			{
				ChessBoard b1 = newBoard.clone();
				b1.Promote(new Rook(nextMove));
				ChessBoard b2 = newBoard.clone();
				b2.Promote(new Knight(nextMove));
				ChessBoard b3 = newBoard.clone();
				b3.Promote(new Bishop(nextMove));
				ChessBoard b4 = newBoard.clone();
				b4.Promote(new Queen(nextMove));
				nextBoards.add(b1);
				nextBoards.add(b2);
				nextBoards.add(b3);
				nextBoards.add(b4);
			}
			else
			{
				nextBoards.add(newBoard);
			}
		}
		return nextBoards;
	}

	/**
	 * Visszatér az összes lehetséges következõ lépés listájával
	 * @return Az összes lehetséges következõ lépés listája
	 */
	public ArrayList<Move> getAllPossibleNextMoves()
	{
		if (allPossibleMoves != null)
			return allPossibleMoves;

		ArrayList<Move> moves = new ArrayList<>();
		for (int i = 0; i < 8; i++)
		{
			for (int j = 0; j < 8; j++)
			{
				ArrayList<Move> currList = getNextMoves(new Position(i, j));

				for (Move move : currList)
				{
					if (!moveEndsUpInCheck(move))
						moves.add(move);
				}
			}
		}

		moves.addAll(getPossibleNextCastlingMoves());

		allPossibleMoves = moves;
		return moves;
	}

	/**
	 * Visszatér az összes lehetséges következõ lépés listájával, amit egy adott helyen lévõ bábu léphet
	 * @param position A bábu helyzete
	 * @return Az összes lehetséges következõ lépés listája
	 */
	private ArrayList<Move> getNextMoves(Position position)
	{
		Piece piece = getPieceRef(position);
		if (piece == null || piece.getColor() != nextMove)
			return new ArrayList<>();

		ArrayList<Move> moves = piece.getPossibleMoves(position, this);
		return moves;
	}

	/**
	 * Lekéri az adott helyen található bábu referenciáját
	 * @param pos A pozíció
	 * @return A bábu referenciája
	 */
	private Piece getPieceRef(Position pos)
	{
		return getPieceRef(pos.getRank(), pos.getFile());
	}

	/**
	 * Lekéri az adott helyen található bábu referenciáját
	 * @param rank A bábu sora
	 * @param file A bábu oszlopa
	 * @return A bábu referenciája
	 */
	private Piece getPieceRef(int rank, int file)
	{
		return board[rank][file];
	}

	/**
	 * Visszaadja a játéktábla másolatát
	 * @return a játéktábla másolata
	 */
	public Piece[][] getBoard()
	{
		return cloneTable(board);
	}

	/**
	 * Lekéri az adott helyen található bábu másolatát
	 * @param rank A bábu sora
	 * @param file A bábu oszlopa
	 * @return A bábu másolata
	 */
	public Piece getPiece(int rank, int file)
	{
		if (board[rank][file] == null)
			return null;
		return board[rank][file].clone();
	}

	/**
	 * Lekéri az adott helyen található bábu másolatát
	 * @param pos A pozíció
	 * @return A bábu másolata
	 */
	public Piece getPiece(Position pos)
	{
		return getPiece(pos.getRank(), pos.getFile());
	}

	/**
	 * Visszaadja, hogy éppen bábu lecserélésre várunk-e
	 * @return True, ha éppen bábu lecserélésre várunk
	 */
	public boolean isAwaitingPromotion()
	{
		return awaitingPromotion;
	}

	/**
	 * Visszaadja a lecserélendõ gyalog pozícióját
	 * @return a lecserélendõ gyalog pozíciója
	 */
	public Position getPromotionPos()
	{
		return promotionPos;
	}

	/**
	 * Visszaadja a játék állapotát
	 * @return a játék állapota
	 */
	public ChessgameState getGameState()
	{
		return gameState;
	}

	/**
	 * Visszaadja a következõ játékos színét
	 * @return a következõ játékos színe
	 */
	public PlayerColor getNextToMove()
	{
		return this.nextMove;
	}

	/**
	 * Visszaadja, hogy a következõ játékos sakkban van-e
	 * @return True, ha a következõ játékos sakkban van
	 */
	public boolean isInCheck()
	{
		// ha kell a check-et cache-elni, akkor itt tudod megcsinÃ¡lni
		return isInCheckInner();
	}

	/**
	 * String konverzió
	 */
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("  a b c d e f g h\n");
		//sb.append("  ---------------\n");
		for (int i = 7; i >= 0; i--)
		{
			sb.append(i + 1);
			sb.append("|");
			for (int j = 0; j < 8; j++)
			{
				if (board[i][j] != null)
				{
					sb.append(board[i][j].toString());
				}
				else
				{
					sb.append("-");
				}
				sb.append(" ");
			}
			sb.deleteCharAt(sb.length() - 1);
			sb.append("|");
			sb.append(i + 1);
			sb.append("\n");
		}
		//sb.append("  ---------------\n");
		sb.append("  a b c d e f g h\n");
		sb.append("game state: ");
		sb.append(gameState);
		sb.append("\n");
		sb.append(awaitingPromotion ? "awaiting promotion\n" : "");
		sb.append(nextMove == PlayerColor.White ? "white" : "black");
		sb.append(" to move");
		sb.append(isInCheck() ? ", in check\n" : "\n");
		return sb.toString();
	}

	/**
	 * lemásolja a sakktáblát
	 */
	public ChessBoard clone()
	{
		return new ChessBoard(board, nextMove);
	}

	/**
	 * Lemásol egy bábu tömböt
	 * @param table a másolandó tömb
	 * @return a másolat
	 */
	public Piece[][] cloneTable(Piece[][] table)
	{
		Piece[][] newtable = new Piece[table.length][];
		for (int i = 0; i < newtable.length; i++)
		{
			newtable[i] = new Piece[table[i].length];
			for (int j = 0; j < newtable[i].length; j++)
			{
				if (table[i][j] == null)
				{
					newtable[i][j] = null;
				}
				else
				{
					newtable[i][j] = table[i][j].clone();
				}
			}
		}
		return newtable;
	}
	
	/*------------------- SELECTION RELATED METHODS -------------------*/

	/**
	 * Visszaadja a kijelölt mezõk tömbjét
	 * @return a kijelölt mezõk tömbje
	 */
	public SelectionType[][] getSelections()
	{
		return selection;
	}

	/**
	 * Módosítja a kijelölést, úgy, hogy a user a megadott pozícióra klikkelt
	 * @param selectedPos a user által kijelölt pozíció
	 */
	public void selectHighlightSquare(Position selectedPos)
	{
		if (getPieceRef(selectedPos) == null)
			return;

		setHighlight(selectedPos, SelectionType.SourceSelected);

		getAllPossibleNextMoves();
		for (Move move : allPossibleMoves)
		{
			if (move.getStart().equals(selectedPos))
			{
				setHighlight(move.getEnd(), SelectionType.DestinationSelected);
			}
		}
	}

	/**
	 * Letörli a kijelöléseket 
	 */
	public void clearHighlightSelection()
	{
		for (int i = 0; i < 8; i++)
		{
			for (int j = 0; j < 8; j++)
			{
				selection[i][j] = null;
			}
		}
	}

	/**
	 * Adott pozíciónak megváltoztatja a kijelölés-típusát
	 * @param pos
	 * @param type
	 */
	public void setHighlight(Position pos, SelectionType type)
	{
		selection[pos.getRank()][pos.getFile()] = type;
	}

	/**
	 * 
	 * A sakkjátszma állapotát jellemzõ enum
	 * 
	 */
	public enum ChessgameState
	{
		Playing,
		Stalemate,
		Checkmate
	}

	/**
	 * 
	 * A kijelölés típusát jellemzõ enum
	 *
	 */
	//no selection is null
	public enum SelectionType
	{
		SourceSelected,
		DestinationSelected
	}
}
