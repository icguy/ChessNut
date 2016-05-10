package chessnut.logic.pieces;

import java.util.ArrayList;
import chessnut.logic.*;

public class King extends Piece
{
	private boolean hasMoved;

	public King(PlayerColor color)
	{
		super(color);
		setHasMoved(false);
	}

	@Override
	public String toString()
	{
		return color == PlayerColor.White ? "K" : "k";
	}

	@Override
	public ArrayList<Move> getPossibleMoves(Position pos, ChessBoard board)
	{
		ArrayList<Move> moves = new ArrayList<>();

		for (int i = -1; i <= 1; i++)
		{
			for (int j = -1; j <= 1; j++)
			{
				Position currPos = Position.tryCreate(pos.getRank() + i, pos.getFile() + j);
				if (currPos == null)
					continue;

				Piece currPiece = board.getPiece(currPos);
				if (currPiece == null || currPiece.getColor() != color) //if square is empty or occupied by enemy piece
					moves.add(new Move(pos, currPos));
			}
		}
		return moves;
	}

	@Override
	public Piece clone()
	{
		return new King(color);
	}

	public boolean getHasMoved()
	{
		return hasMoved;
	}

	public void setHasMoved(boolean hasMoved)
	{
		this.hasMoved = hasMoved;
	}
}
