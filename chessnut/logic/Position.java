package chessnut.logic;

import java.io.Serializable;

/*
 * Represents a position on the board
 * Ranks (rows) are represented 0 through 7, and marked 1 through 8 on a physical board.
 * Files (columns) are represented 0 through 7, and marked 'a' through 'h' on a physical board.
 */
public class Position implements Serializable
{
	private static final long serialVersionUID = 2758435244468732197L;  //!< Egyedi magicnumber a sorosításhoz
	
	private final int rank, file;

	public Position(int rank, int file)
	{
		this.rank = rank;
		this.file = file;
		if (!inRange(rank, file))
			throw new IllegalArgumentException();
	}

	public int getRank()
	{
		return rank;
	}

	public int getFile()
	{
		return file;
	}

	private static boolean inRange(int rank, int file)
	{
		return rank >= 0 && rank < 8 && file >= 0 && file < 8;
	}

	@Override
	public String toString()
	{
		return String.valueOf((char)('a' + file)) + String.valueOf(1 + rank);
	}

	@Override
	public boolean equals(Object obj)
	{
		if (!(obj instanceof Position))
			return false;
		Position other = (Position) obj;
		return other.getFile() == file && other.getRank() == rank;
	}
	
	@Override
	public int hashCode() 
	{
		return file * 10 + rank;		
	};
	
	static public Position tryCreate(int rank, int file)
	{
		if(!inRange(rank, file))
			return null;
		return new Position(rank, file);
	}
}
