package chessnut.logic.pieces;

import java.util.ArrayList;
import chessnut.logic.*;

public abstract class Piece
{
	PieceColor color;

	public boolean canMove(Position start, Position end)
	{
		if (!inRange(end))
			return false;
		return canMoveInner(start, end);
	}

	protected abstract boolean canMoveInner(Position start, Position end);

	public Piece(PieceColor color)
	{
		this.color = color;
	}

	public abstract ArrayList<Position> getAllMoves(Position pos);

	static boolean inRange(Position move)
	{
		int rank = move.getRank();
		int file = move.getFile();
		return rank >= 0 && rank < 8 && file >= 0 && file < 8;
	}
}
