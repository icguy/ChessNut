package chessnut.logic.pieces;

import java.util.ArrayList;
import chessnut.logic.*;

public class Queen extends Piece
{

	public Queen(PlayerColor color)
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
		return color == PlayerColor.White ? "Q" : "q";
	}

	@Override
	public ArrayList<Move> getPossibleMoves(Position pos, ChessBoard board)
	{
		// TODO Auto-generated method stub
		return null;
	}
}
