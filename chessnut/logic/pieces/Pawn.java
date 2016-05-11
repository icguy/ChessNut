package chessnut.logic.pieces;

import java.util.ArrayList;
import chessnut.logic.*;

public class Pawn extends Piece
{
	public Pawn(PlayerColor color)
	{
		super(color);
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

		Position currPos = Position.tryCreate(rank + pawnDir, file);
		Piece currPiece = currPos != null ? board.getPiece(currPos) : null;
		if (currPiece == null)
		{
			moves.add(new Move(pos, currPos));	//step 1 forward

			currPos = Position.tryCreate(rank + 2 * pawnDir, file);
			currPiece = currPos != null ? board.getPiece(currPos) : null;
			if (pos.getRank() == initRank && currPiece == null)
				moves.add(new Move(pos, currPos)); //initial step 2 forward
		}

		currPos = Position.tryCreate(rank + pawnDir, file + 1);
		currPiece = currPos != null ? board.getPiece(currPos) : null;
		if(currPiece != null && currPiece.color != color)
			moves.add(new Move(pos, currPos));	//capture piece
		
		currPos = Position.tryCreate(rank + pawnDir, file - 1);
		currPiece = currPos != null ? board.getPiece(currPos) : null;
		if(currPiece != null && currPiece.color != color)
			moves.add(new Move(pos, currPos));	//capture piece
				
		return moves;
	}

	@Override
	public Piece clone()
	{
		return new Pawn(color);
	}
}
