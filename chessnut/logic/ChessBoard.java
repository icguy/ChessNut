package chessnut.logic;

import java.io.Serializable;
import java.util.ArrayList;
import chessnut.logic.pieces.*;

/**
 * A sakkt�bla �ll�s�t, �s a kijel�l�seket t�rol� oszt�ly.
 * Ismeri a sakk szab�lyait, k�pes konzisztens �llapotban tartani a t�bl�t, nem enged meg szab�lytalan l�p�st.
 * K�v�lr�l lek�rhet� a j�t�k �llapota, a sakkt�bla �llapota, az egyes mez�k�n �ll� b�buk, a kijel�lt mez�k.
 */
public class ChessBoard implements Serializable
{
	/**
	 * Soros�t�shoz sz�ks�ges azonos�t�
	 */
	private static final long serialVersionUID = 1532472298888732188L;
	
	/**
	 * A mez�k aktu�lis kijel�lts�ge
	 */
	private SelectionType[][] selection;
	
	/**
	 * A t�bl�n l�v� b�buk
	 */
	private Piece[][] board;
	
	/**
	 * A k�vetkez� j�t�kos sz�ne
	 */
	private PlayerColor nextMove;
	
	/**
	 * Az �sszes lehets�ges k�vetkez� l�p�s list�ja 
	 */
	private ArrayList<Move> allPossibleMoves;
	
	/**
	 * A s�t�t kir�ly poz�ci�ja
	 */
	private Position blackKingPos;
	
	/**
	 * A vil�gos kir�ly poz�ci�ja
	 */
	private Position whiteKingPos;
	
	/**
	 * True, ha �pp arra v�runk, hogy a k�vetkez� j�t�kos kiv�lassza, hogy mire cser�li le a bevitt gyalogj�t
	 */
	private boolean awaitingPromotion;
	
	/**
	 * A lecser�lt gyalog poz�ci�ja
	 */
	private Position promotionPos;
	
	/**
	 * A j�t�k �llapota
	 */
	private ChessgameState gameState;

	/**
	 * Default konstruktor. �j sakkt�bl�t hoz l�tre a kezd��ll�ssal, vil�gos l�p
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
	 * Copy konstruktor a network sz�m�ra
	 * @param chessboard A m�soland� sakkt�bla
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
	 * Param�teres konstruktor.
	 * @param board A t�bla �ll�sa
	 * @param nextMove K�vetkez� j�t�kos 
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
	 * Param�teres konstruktor. Kiz�r�lag �ll�s elemz�s�re
	 * @param board A t�bla �ll�sa
	 * @param nextMove K�vetkez� j�t�kos 
	 * @param gameState A j�t�k �llapota
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
	 * Be�ll�tja a kezd��llapotot
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
	 * Lecser�li a bevitt gyalogot az �j b�bura
	 * @param newPiece Az �j b�bu. A sz�n�nek meg kell felelnie a k�vetkez� j�t�kos sz�n�nek
	 * @return True, ha sikeres volt a m�velet
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
	 * V�grehajt egy l�p�st a t�bl�n
	 * 
	 * @param move A l�p�s
	 * @return True, ha siker�lt a l�p�st megl�pni
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
	 * A k�vetkez� j�t�kost az ellenkez�j�re �ll�tja
	 */
	private void changeNextMove()
	{
		nextMove = (nextMove == PlayerColor.White) ? PlayerColor.Black : PlayerColor.White;
	}

	/**
	 * Megvizsg�lja, hogy a k�vetkez� j�t�kos sakkban van-e.
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
	 * Friss�ti a j�t�k �llapot�t
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
	 * Friss�ti a kir�lyok poz�ci�j�t
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
	 * Teszt met�dus
	 * @param obj
	 * @return
	 */
	public Object TestMethod(Object obj)
	{
		return getPossibleNextCastlingMoves();
		//return moveEndsUpInCheck((Move) obj);
	}

	/**
	 * Visszaadja a lehets�ges s�ncol� l�p�seket
	 * @return A s�ncol� l�p�sek list�ja
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
	 * Megvizsg�lja, hogy egy l�p�s ut�n a l�p�st v�grehajt� j�t�kos sakkban van-e
	 * @param move A l�p�s
	 * @return True, ha a l�p�s szab�lytalan, mert sakkba ker�l a j�t�kos
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
	 * L�trehozza az adott �ll�sb�l k�vetkez� �sszes tov�bbi lehets�ges �ll�st
	 * @return Az �sszes k�vetkez� j�t�k�ll�shoz tartoz� ChessBoard list�ja
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
	 * Visszat�r az �sszes lehets�ges k�vetkez� l�p�s list�j�val
	 * @return Az �sszes lehets�ges k�vetkez� l�p�s list�ja
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
	 * Visszat�r az �sszes lehets�ges k�vetkez� l�p�s list�j�val, amit egy adott helyen l�v� b�bu l�phet
	 * @param position A b�bu helyzete
	 * @return Az �sszes lehets�ges k�vetkez� l�p�s list�ja
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
	 * Lek�ri az adott helyen tal�lhat� b�bu referenci�j�t
	 * @param pos A poz�ci�
	 * @return A b�bu referenci�ja
	 */
	private Piece getPieceRef(Position pos)
	{
		return getPieceRef(pos.getRank(), pos.getFile());
	}

	/**
	 * Lek�ri az adott helyen tal�lhat� b�bu referenci�j�t
	 * @param rank A b�bu sora
	 * @param file A b�bu oszlopa
	 * @return A b�bu referenci�ja
	 */
	private Piece getPieceRef(int rank, int file)
	{
		return board[rank][file];
	}

	/**
	 * Visszaadja a j�t�kt�bla m�solat�t
	 * @return a j�t�kt�bla m�solata
	 */
	public Piece[][] getBoard()
	{
		return cloneTable(board);
	}

	/**
	 * Lek�ri az adott helyen tal�lhat� b�bu m�solat�t
	 * @param rank A b�bu sora
	 * @param file A b�bu oszlopa
	 * @return A b�bu m�solata
	 */
	public Piece getPiece(int rank, int file)
	{
		if (board[rank][file] == null)
			return null;
		return board[rank][file].clone();
	}

	/**
	 * Lek�ri az adott helyen tal�lhat� b�bu m�solat�t
	 * @param pos A poz�ci�
	 * @return A b�bu m�solata
	 */
	public Piece getPiece(Position pos)
	{
		return getPiece(pos.getRank(), pos.getFile());
	}

	/**
	 * Visszaadja, hogy �ppen b�bu lecser�l�sre v�runk-e
	 * @return True, ha �ppen b�bu lecser�l�sre v�runk
	 */
	public boolean isAwaitingPromotion()
	{
		return awaitingPromotion;
	}

	/**
	 * Visszaadja a lecser�lend� gyalog poz�ci�j�t
	 * @return a lecser�lend� gyalog poz�ci�ja
	 */
	public Position getPromotionPos()
	{
		return promotionPos;
	}

	/**
	 * Visszaadja a j�t�k �llapot�t
	 * @return a j�t�k �llapota
	 */
	public ChessgameState getGameState()
	{
		return gameState;
	}

	/**
	 * Visszaadja a k�vetkez� j�t�kos sz�n�t
	 * @return a k�vetkez� j�t�kos sz�ne
	 */
	public PlayerColor getNextToMove()
	{
		return this.nextMove;
	}

	/**
	 * Visszaadja, hogy a k�vetkez� j�t�kos sakkban van-e
	 * @return True, ha a k�vetkez� j�t�kos sakkban van
	 */
	public boolean isInCheck()
	{
		// ha kell a check-et cache-elni, akkor itt tudod megcsinálni
		return isInCheckInner();
	}

	/**
	 * String konverzi�
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
	 * lem�solja a sakkt�bl�t
	 */
	public ChessBoard clone()
	{
		return new ChessBoard(board, nextMove);
	}

	/**
	 * Lem�sol egy b�bu t�mb�t
	 * @param table a m�soland� t�mb
	 * @return a m�solat
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
	 * Visszaadja a kijel�lt mez�k t�mbj�t
	 * @return a kijel�lt mez�k t�mbje
	 */
	public SelectionType[][] getSelections()
	{
		return selection;
	}

	/**
	 * M�dos�tja a kijel�l�st, �gy, hogy a user a megadott poz�ci�ra klikkelt
	 * @param selectedPos a user �ltal kijel�lt poz�ci�
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
	 * Let�rli a kijel�l�seket 
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
	 * Adott poz�ci�nak megv�ltoztatja a kijel�l�s-t�pus�t
	 * @param pos
	 * @param type
	 */
	public void setHighlight(Position pos, SelectionType type)
	{
		selection[pos.getRank()][pos.getFile()] = type;
	}

	/**
	 * 
	 * A sakkj�tszma �llapot�t jellemz� enum
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
	 * A kijel�l�s t�pus�t jellemz� enum
	 *
	 */
	//no selection is null
	public enum SelectionType
	{
		SourceSelected,
		DestinationSelected
	}
}
