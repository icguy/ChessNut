package chessnut.logic;

import chessnut.logic.pieces.*;

/*
 * board indices go from 0 to 7, where the first index is the rank, second is the file of
 * the corresponding position, e.g. [0][0] marks the 'a1' corner square.
 */
public class ChessBoard
{
	Piece[][] board;
	PieceColor nextMove;

	public ChessBoard()
	{
		board = new Piece[8][8];
		nextMove = PieceColor.White;
		initBoard();
	}

	void initBoard()
	{
		for (int i = 0; i < 8; i++)
		{
			for (int j = 0; j < 8; j++)
			{
				board[i][j] = null;
			}
		}

		for (int i = 0; i < 8; i++)
		{
			board[1][i] = new Pawn(PieceColor.White);
			board[6][i] = new Pawn(PieceColor.Black);
		}

		board[0][0] = new Rook(PieceColor.White);
		board[0][1] = new Knight(PieceColor.White);
		board[0][2] = new Bishop(PieceColor.White);
		board[0][3] = new Queen(PieceColor.White);
		board[0][4] = new King(PieceColor.White);
		board[0][5] = new Bishop(PieceColor.White);
		board[0][6] = new Knight(PieceColor.White);
		board[0][7] = new Rook(PieceColor.White);

		board[7][0] = new Rook(PieceColor.Black);
		board[7][1] = new Knight(PieceColor.Black);
		board[7][2] = new Bishop(PieceColor.Black);
		board[7][3] = new Queen(PieceColor.Black);
		board[7][4] = new King(PieceColor.Black);
		board[7][5] = new Bishop(PieceColor.Black);
		board[7][6] = new Knight(PieceColor.Black);
		board[7][7] = new Rook(PieceColor.Black);
	}

	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		for (int i = 7; i >= 0; i--)
		{
			for (int j = 0; j < 8; j++)
			{
				if (board[i][j] != null)
				{
					sb.append(board[i][j].toString());
				}
				else
				{
					sb.append("-");
				}
				sb.append(" ");
			}
			sb.append("\n");
		}
		return sb.toString();
	}
}
