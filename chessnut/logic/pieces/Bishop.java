package chessnut.logic.pieces;

import java.util.ArrayList;
import chessnut.logic.*;

public class Bishop extends Piece
{
	private static final long serialVersionUID = 2758435245622732188L;  //!< Egyedi magicnumber a sorosításhoz
	
	public Bishop(PlayerColor color)
	{
		super(color);
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

	@Override
	public Piece clone()
	{
		return new Bishop(color);
	}
}
