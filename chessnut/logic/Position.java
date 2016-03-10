package chessnut.logic;
/*
 * Represents a position on the board
 * Ranks (rows) are represented 0 through 7, and marked 1 through 8 on a physical board.
 * Files (columns) are represented 0 through 7, and marked 'a' through 'h' on a physical board.
 */
public class Position
{
	final int rank, file;

	public Position(int rank, int file)
	{
		this.rank = rank;
		this.file = file;
	}

	public int getRank()
	{
		return rank;
	}

	public int getFile()
	{
		return file;
	}
}
