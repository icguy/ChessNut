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

	public PlayerColor getColor()
	{
		return color;
	}

	public abstract ArrayList<Move> getPossibleMoves(Position pos, ChessBoard board);
		
	protected final void addMovesInDirection(Position pos, ChessBoard board,
			ArrayList<Move> moves, int rankDir, int fileDir)
	{
		int rank = pos.getRank();
		int file = pos.getFile();
		for (int i = 1;; i++)
		{
			Position currPos = Position.tryCreate(
					rank + i * rankDir,
					file + i * fileDir);
			if (currPos == null)
				break;

			Piece currPiece = board.getPiece(currPos);
			if (currPiece == null)
			{
				moves.add(new Move(pos, currPos));
			}
			else if (currPiece.getColor() != color)
			{
				moves.add(new Move(pos, currPos));
				break;
			}
			else
			{
				break;
			}
		}
	}
}
