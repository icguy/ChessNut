package chessnut.logic;

import java.util.ArrayList;
import chessnut.logic.pieces.*;

/*
 * board indices go from 0 to 7, where the first index is the rank, second is the file of
 * the corresponding position, e.g. [0][0] marks the 'a1' corner square.
 */
public class ChessBoard
{
	Piece[][] board;
	PlayerColor nextMove;
	boolean check;
	ArrayList<Move> allPossibleMoves;

	public ChessBoard()
	{
		board = new Piece[8][8];
		nextMove = PlayerColor.White;
		initBoard();
	}

	public ChessBoard(Position[] pos, Piece[] pieces, PlayerColor nextMove, boolean check)
	{
		assert(pos.length == pieces.length);
		//if(pos.length != pieces.length)
		//	throw new IllegalArgumentException();
		
		board = new Piece[8][8];
		this.nextMove = nextMove;		
		this.check = check;

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
	}

	public boolean move(Move move)
	{
		return false;
		// todo
	}

	ArrayList<Move> getAllPossibleNextMoves()
	{
		if(allPossibleMoves != null)
			return allPossibleMoves;
		
		ArrayList<Move> moves = new ArrayList<>();
		for (int i = 0; i < 8; i++)
		{
			for (int j = 0; j < 8; j++)
			{
				ArrayList<Move> curr = getNextMoves(new Position(i, j));
				if (curr != null)
					moves.addAll(curr);
			}
		}

		allPossibleMoves = moves;
		return moves;
	}

	ArrayList<Move> getNextMoves(Position position)
	{
		Piece piece = board[position.getRank()][position.getFile()];
		if (piece == null || piece.getColor() != nextMove)
			return null;

		ArrayList<Move> moves = piece.getPossibleMoves(position, this);
		return moves; //TODO extra szabályok
	}

	public Piece getPiece(int rank, int file)
	{
		return board[rank][file];
	}
	
	public Piece getPiece(Position pos)
	{
		return getPiece(pos.getRank(),pos.getFile());
	}

	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("  a b c d e f g h\n");
		sb.append("  ---------------\n");
		for (int i = 7; i >= 0; i--)
		{
			sb.append(i+1);
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
			sb.deleteCharAt(sb.length()-1);
			sb.append("|");
			sb.append(i+1);
			sb.append("\n");
		}
		sb.append("  ---------------\n");
		sb.append("  a b c d e f g h\n");
		return sb.toString();
	}
	
	public ChessBoard clone()
	{
		ArrayList<Piece> pieces = new ArrayList<>();
		ArrayList<Position> positions = new ArrayList<>();
		for (int i = 0; i < 8; i++)
		{
			for (int j = 0; j < 8; j++)
			{
				Piece piece = board[i][j];
				if(piece != null)
				{
					pieces.add(piece);
					positions.add(new Position(i, j));
				}
			}
		}
		
		return new ChessBoard(
				positions.toArray(new Position[0]),
				pieces.toArray(new Piece[0]), 
				nextMove,
				check);		
	}
}
