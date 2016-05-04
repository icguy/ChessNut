package chessnut;

import chessnut.logic.*;

public interface IPlayer
{
	void setChessboard(ChessBoard board);
	void notifyPromotion(Position position);
}
