package chessnut.logic.pieces;

import java.util.ArrayList;
import chessnut.logic.PieceColor;
import chessnut.logic.Position;

public class Pawn extends Piece
{	
	public Pawn(PieceColor color)
	{
		super(color);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected boolean canMoveInner(Position start, Position end)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ArrayList<Position> getAllMoves(Position pos)
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String toString()
	{
		return color == PieceColor.White ? "P" : "p";
	}
	
}
