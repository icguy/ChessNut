package chessnut;

import chessnut.logic.*;
import chessnut.logic.pieces.Piece;

public interface ILogic
{
	void click(Position position);
	void Promote(Piece piece);
}
