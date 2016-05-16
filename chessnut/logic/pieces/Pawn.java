package chessnut.logic.pieces;

import java.util.ArrayList;
import chessnut.logic.*;

public class Pawn extends Piece
{
	private static final long serialVersionUID = 2758435245622732188L; //!< Egyedi magicnumber a sorosításhoz

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
		if (currPos != null) //still on board
		{
			Piece currPiece = board.getPiece(currPos);
			if (currPiece == null) //no piece in front
			{
				moves.add(new Move(pos, currPos)); //step 1 forward

				currPos = Position.tryCreate(rank + 2 * pawnDir, file);
				currPiece = currPos != null ? board.getPiece(currPos) : null;
				if (pos.getRank() == initRank && currPiece == null)
					moves.add(new Move(pos, currPos)); //initial step 2 forward
			}
		}

		currPos = Position.tryCreate(rank + pawnDir, file + 1);
		if (currPos != null) //still on board
		{
			Piece currPiece = board.getPiece(currPos);
			if (currPiece != null && currPiece.color != color)
				moves.add(new Move(pos, currPos)); //capture piece
		}

		currPos = Position.tryCreate(rank + pawnDir, file - 1);
		if (currPos != null) //still on board
		{
			Piece currPiece = board.getPiece(currPos);
			if (currPiece != null && currPiece.color != color)
				moves.add(new Move(pos, currPos)); //capture piece
		}

		return moves;
	}

	@Override
	public Piece clone()
	{
		return new Pawn(color);
	}
}
