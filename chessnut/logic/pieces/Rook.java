package chessnut.logic.pieces;

import java.util.ArrayList;
import chessnut.logic.*;

public class Rook extends Piece
{

	public Rook(PlayerColor color)
	{
		super(color);
	}

	@Override
	public String toString()
	{
		return color == PlayerColor.White ? "R" : "r";
	}

	@Override
	public ArrayList<Move> getPossibleMoves(Position pos, ChessBoard board)
	{
		ArrayList<Move> moves = new ArrayList<>();
		addMovesInDirection(pos, board, moves, 1, 0); //up
		addMovesInDirection(pos, board, moves, -1, 0); //down
		addMovesInDirection(pos, board, moves, 0, 1); //right
		addMovesInDirection(pos, board, moves, 0, -1); //left
		return moves;
	}
}
