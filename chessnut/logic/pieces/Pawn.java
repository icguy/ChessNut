package chessnut.logic.pieces;

import java.util.ArrayList;
import chessnut.logic.*;

public class Pawn extends Piece
{
	public Pawn(PlayerColor color)
	{
		super(color);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected boolean canMoveInner(Move move)
	{
		return true; //unused
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
		return color == PlayerColor.White ? "P" : "p";
	}

	@Override
	public ArrayList<Move> getPossibleMoves(Position pos, ChessBoard board)
	{
		ArrayList<Move> moves = new ArrayList<>();
		int pawnDir = color == PlayerColor.White ? 1 : -1;
		int initRank = color == PlayerColor.White ? 1 : 6;
		int rank = pos.getRank();
		int file = pos.getFile();

		Position currPos = new Position(rank + pawnDir, file);
		Piece currPiece = board.getPiece(currPos);
		if (currPiece == null)
		{
			moves.add(new Move(pos, currPos));	//step 1 forward

			currPos = new Position(rank + 2 * pawnDir, file);
			currPiece = board.getPiece(currPos);
			if (pos.getRank() == initRank && currPiece == null)
				moves.add(new Move(pos, currPos)); //initial step 2 forward
		}

		currPos = new Position(rank + pawnDir, file + 1);
		currPiece = board.getPiece(currPos);
		if(currPiece != null && currPiece.color != color)
			moves.add(new Move(pos, currPos));	//capture piece
		
		currPos = new Position(rank + pawnDir, file - 1);
		currPiece = board.getPiece(currPos);
		if(currPiece != null && currPiece.color != color)
			moves.add(new Move(pos, currPos));	//capture piece
				
		return moves;
	}

}
