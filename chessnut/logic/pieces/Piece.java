package chessnut.logic.pieces;

import java.util.ArrayList;
import chessnut.logic.*;

public abstract class Piece
{
	final PlayerColor color;

	public Piece(PlayerColor color)
	{
		this.color = color;
	}

	public boolean canMove(Move move)
	{
		return canMoveInner(move);
	}

	protected abstract boolean canMoveInner(Move move);

	public PlayerColor getColor()
	{
		return color;
	}

	public abstract ArrayList<Move> getAllMoves(Position pos);
	
	public abstract ArrayList<Move> getPossibleMoves(Position pos, ChessBoard board);
}
