package chessnut.logic;

public class Move
{
	final private Position start;
	final private Position end;

	public Move(Position start, Position end)
	{
		if(start == null || end == null)
			throw new NullPointerException();
		
		this.start = start;
		this.end = end;
	}

	public Position getStart()
	{
		return start;
	}

	public Position getEnd()
	{
		return end;
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
	public String toString()
	{
		return start.toString() + "->" + end.toString();
	}
}
