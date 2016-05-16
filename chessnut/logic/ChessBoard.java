package chessnut.logic;

import java.io.Serializable;
import java.util.ArrayList;
import chessnut.logic.pieces.*;

/*
 * board indices go from 0 to 7, where the first index is the rank, second is the file of
 * the corresponding position, e.g. [0][0] marks the 'a1' corner square.
 */
public class ChessBoard implements Serializable
{
	private static final long serialVersionUID = 1532472298888732188L; //!< Egyedi magicnumber a sorositashoz

	private SelectionType[][] selection;
	private Piece[][] board;
	private PlayerColor nextMove;
	private ArrayList<Move> allPossibleMoves;
	private Position blackKingPos;
	private Position whiteKingPos;
	private boolean awaitingPromotion;
	private Position promotionPos;
	private ChessgameState gameState;

	public ChessBoard()
	{
		selection = new SelectionType[8][8];
		board = new Piece[8][8];
		nextMove = PlayerColor.White;
		initBoard();
		updateKingPos();
		updateGameState();
	}
	
	
	//! \brief  Copy konstruktor a h�l�zaton �tk�ld�shez
	//! \note   Nem mindent m�sol �t, csak ami a t�loldalt is l�nyeges
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

	public ChessBoard(Piece[][] board, PlayerColor nextMove)
	{
		this.selection = new SelectionType[8][8];
		this.board = cloneTable(board);
		this.nextMove = nextMove;
		updateKingPos();
		updateGameState();
	}

	private ChessBoard(Piece[][] board, PlayerColor nextMove, ChessgameState gameState)
	{
		this.selection = new SelectionType[8][8];
		this.board = cloneTable(board);
		this.nextMove = nextMove;
		this.gameState = gameState;
		updateKingPos();
		//no updateGameState(), because that leads to stack overflow.
		//only to be used when constructing boards for analyzis (determining check etc.)
	}

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
	 * If possible, makes a move on the chessboard
	 * 
	 * @param move
	 *            the move to be made
	 * @return true if the move could be made, false otherwise
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

	private void changeNextMove()
	{
		nextMove = (nextMove == PlayerColor.White) ? PlayerColor.Black : PlayerColor.White;
	}

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

	public Object TestMethod(Object obj)
	{
		return getPossibleNextCastlingMoves();
		//return moveEndsUpInCheck((Move) obj);
	}

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

	private ArrayList<Move> getNextMoves(Position position)
	{
		Piece piece = getPieceRef(position);
		if (piece == null || piece.getColor() != nextMove)
			return new ArrayList<>();

		ArrayList<Move> moves = piece.getPossibleMoves(position, this);
		return moves;
	}

	private Piece getPieceRef(Position pos)
	{
		return getPieceRef(pos.getRank(), pos.getFile());
	}

	private Piece getPieceRef(int rank, int file)
	{
		return board[rank][file];
	}

	public Piece[][] getBoard()
	{
		return cloneTable(board);
	}

	public Piece getPiece(int rank, int file)
	{
		if (board[rank][file] == null)
			return null;
		return board[rank][file].clone();
	}

	public Piece getPiece(Position pos)
	{
		return getPiece(pos.getRank(), pos.getFile());
	}

	public boolean isAwaitingPromotion()
	{
		return awaitingPromotion;
	}

	public Position getPromotionPos()
	{
		return promotionPos;
	}

	public ChessgameState getGameState()
	{
		return gameState;
	}

	public PlayerColor getNextToMove()
	{
		return this.nextMove;
	}

	public boolean isInCheck()
	{
		// ha kell a check-et cache-elni, akkor itt tudod megcsinálni
		return isInCheckInner();
	}

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

	public ChessBoard clone()
	{
		return new ChessBoard(board, nextMove);
	}

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

	public SelectionType[][] getSelections()
	{
		return selection;
	}

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

	public void setHighlight(Position pos, SelectionType type)
	{
		selection[pos.getRank()][pos.getFile()] = type;
	}

	public enum ChessgameState
	{
		Playing,
		Stalemate,
		Checkmate
	}

	//no selection is null
	public enum SelectionType
	{
		SourceSelected,
		DestinationSelected
	}
}
