package chessnut.logic.pieces;

import java.util.ArrayList;
import chessnut.logic.*;

public class Knight extends Piece
{

	public Knight(PlayerColor color)
	{
		super(color);
	}

	@Override
	public String toString()
	{
		return color == PlayerColor.White ? "N" : "n";
	}

	@Override
	public ArrayList<Move> getPossibleMoves(Position pos, ChessBoard board)
	{
		ArrayList<Move> moves = new ArrayList<>();
		int rank = pos.getRank();
		int file = pos.getFile();

		ArrayList<Position> endPositions = new ArrayList<>();
		endPositions.add(Position.tryCreate(rank + 1, file + 2));
		endPositions.add(Position.tryCreate(rank + 1, file - 2));
		endPositions.add(Position.tryCreate(rank + 2, file + 1));
		endPositions.add(Position.tryCreate(rank + 2, file - 1));
		endPositions.add(Position.tryCreate(rank - 2, file + 1));
		endPositions.add(Position.tryCreate(rank - 2, file - 1));
		endPositions.add(Position.tryCreate(rank - 1, file + 2));
		endPositions.add(Position.tryCreate(rank - 1, file - 2));
		
		for (Position position : endPositions)
		{
			if (position == null)
				continue;

			Piece currPiece = board.getPiece(position);
			if (currPiece == null || currPiece.getColor() != color) //if square is empty or occupied by enemy piece
				moves.add(new Move(pos, position));
		}
		return moves;
	}

	@Override
	public Piece clone()
	{
		return new Knight(color);
	}
}
