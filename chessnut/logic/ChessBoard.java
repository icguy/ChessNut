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
	private static final long serialVersionUID = 1532472295622732188L; //!< Egyedi magicnumber a sorositashoz

	private Piece[][] board;
	private PlayerColor nextMove;
	private ArrayList<Move> allPossibleMoves;
	private Position blackKingPos;
	private Position whiteKingPos;

	public ChessBoard()
	{
		board = new Piece[8][8];
		nextMove = PlayerColor.White;
		initBoard();
	}

	public ChessBoard(Piece[][] board, PlayerColor nextMove)
	{
		this.board = cloneTable(board);
		this.nextMove = nextMove;
		updateKingPos();
	}

	ChessBoard(Position[] pos, Piece[] pieces, PlayerColor nextMove)
	{
		assert (pos.length == pieces.length);
		//if(pos.length != pieces.length)
		//	throw new IllegalArgumentException();

		board = new Piece[8][8];
		this.nextMove = nextMove;

		for (int i = 0; i < 8; i++)
		{
			for (int j = 0; j < 8; j++)
			{
				board[i][j] = null;
			}
		}

		for (int i = 0; i < pos.length; i++)
		{
			Position currPos = pos[i];
			Piece currPiece = pieces[i];
			board[currPos.getRank()][currPos.getFile()] = currPiece;
		}

		updateKingPos();
	}

	void initBoard()
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

		updateKingPos();
	}

	//TODO pawn promotion

	public boolean makeMove(Move move)
	{
		Position start = move.getStart();
		Position end = move.getEnd();
		Piece moving = getPieceRef(start);

		if (moving == null || moving.getColor() != nextMove)
			return false;

		getAllPossibleNextMoves();
		if (allPossibleMoves.contains(move))
		{
			//TODO check for castling n stuff

			if (moving instanceof King && move.getDelta() == 2)
			{
				//castling

				//				int homeRank = nextMove == PlayerColor.White ? 0 : 7;
				//				if(start.getRank() != homeRank)
				//					return false;
			}

			//move
			board[end.getRank()][end.getFile()] = board[start.getRank()][start.getFile()];
			board[start.getRank()][start.getFile()] = null;

			//update hasMoved, kingpos
			if (moving instanceof King)
			{
				if (nextMove == PlayerColor.White)
					whiteKingPos = end;
				else
					blackKingPos = end;

				((King) moving).setMoved(true);
			}
			else if (moving instanceof Rook)
			{
				((Rook) moving).setMoved(true);
			}

			//clear cache
			allPossibleMoves = null;
			nextMove = (nextMove == PlayerColor.White) ? PlayerColor.Black : PlayerColor.White;
			return true;
		}
		return false;
	}

	public static Piece[][] cloneTable(Piece[][] table)
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

	public boolean isInCheck()
	{
		//TODO kell a check-et cache-elni?
		return isInCheckInner();
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
		return moveEndsUpInCheck((Move) obj);
	}

	// @formatter:off
	private ArrayList<Move> getPossibleNextCastlingMoves()
	{
		//The king and the chosen rook are on the player's first rank.	OK
		//Neither the king nor the chosen rook has previously moved.  	OK
		//There are no pieces between the king and the chosen rook.   	OK
		//The king is not currently in check.							OK
		//The king does not pass through a square that is attacked by an enemy piece.
		//The king does not end up in check. (True of any legal move.)
		
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

		//TODO
		//castling left
		if (leftRook != null && !leftRook.hasMoved())
		{
			boolean piecesInbetween = 
					getPieceRef(homeRank, 1) != null ||
					getPieceRef(homeRank, 2) != null ||
					getPieceRef(homeRank, 3) != null;

			//TODO refactor
			if (!piecesInbetween)
			{
				Piece[][] newboard1 = cloneTable(board);
				newboard1[homeRank][3] = newboard1[homeRank][4];
				newboard1[homeRank][4] = null;
				ChessBoard newcb1 = new ChessBoard(newboard1, nextMove);
				
				Piece[][] newboard2 = cloneTable(board);
				newboard2[homeRank][2] = newboard2[homeRank][4];
				newboard2[homeRank][4] = null;
				ChessBoard newcb2 = new ChessBoard(newboard2, nextMove);
				
				if(!newcb1.isInCheck() && !newcb2.isInCheck())
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
				Piece[][] newboard1 = cloneTable(board);
				newboard1[homeRank][5] = newboard1[homeRank][4];
				newboard1[homeRank][4] = null;
				ChessBoard newcb1 = new ChessBoard(newboard1, nextMove);
				
				Piece[][] newboard2 = cloneTable(board);
				newboard2[homeRank][6] = newboard2[homeRank][4];
				newboard2[homeRank][4] = null;
				ChessBoard newcb2 = new ChessBoard(newboard2, nextMove);
				
				if(!newcb1.isInCheck() && !newcb2.isInCheck())
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
		ChessBoard newChessBoard = new ChessBoard(newBoard, nextMove);
		return newChessBoard.isInCheck();
	}

	private ArrayList<Move> getAllPossibleNextMoves()
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

	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("  a b c d e f g h\n");
		sb.append("  ---------------\n");
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
		sb.append("  ---------------\n");
		sb.append("  a b c d e f g h\n");
		sb.append(nextMove == PlayerColor.White ? "white" : "black");
		sb.append(" to move\n");
		return sb.toString();
	}

	public ChessBoard clone()
	{
		return new ChessBoard(board, nextMove);
	}
}
