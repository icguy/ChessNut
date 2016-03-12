package chessnut.logic.pieces;

import java.util.ArrayList;
import chessnut.logic.*;

public class Bishop extends Piece
{
	public Bishop(PlayerColor color)
	{
		super(color);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected boolean canMoveInner(Move move)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ArrayList<Move> getAllMoves(Position pos)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toString()
	{
		return color == PlayerColor.White ? "B" : "b";
	}

	@Override
	public ArrayList<Move> getPossibleMoves(Position pos, ChessBoard board)
	{
		ArrayList<Move> moves = new ArrayList<>();
		addMovesInDirection(pos, board, moves, 1, -1); //up-left
		addMovesInDirection(pos, board, moves, -1, -1); //down-left
		addMovesInDirection(pos, board, moves, 1, 1); //up-right
		addMovesInDirection(pos, board, moves, -1, 1); //down-right

		return moves;
	}
}