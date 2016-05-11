package chessnut.logic;

public class Move
{
	final private Position start;
	final private Position end;

	public Move(Position start, Position end)
	{
		if (start == null || end == null)
			throw new NullPointerException();

		this.start = start;
		this.end = end;
	}

	public Move(int startRank, int startFile, int endRank, int endFile)
	{
		this(new Position(startRank, startFile), new Position(endRank, endFile));
	}

	public Position getStart()
	{
		return start;
	}

	public Position getEnd()
	{
		return end;
	}

	public int getDelta()
	{
		return Math.abs(start.getRank() - end.getRank())
				+ Math.abs(start.getFile() - end.getFile());
	}

	@Override
	public boolean equals(Object obj)
	{
		if (!(obj instanceof Move))
			return false;
		Move other = (Move) obj;
		return start.equals(other.getStart()) && end.equals(other.getEnd());
	}

	@Override
	public int hashCode()
	{
		return start.hashCode() * 100 + end.hashCode();
	};

	@Override
	public String toString()
	{
		return start.toString() + "->" + end.toString();
	}
}
