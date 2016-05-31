package chessnut.logic;

import java.io.Serializable;

/**
 * 
 * Egy bábu áthelyezését (lépést) reprezentál
 *
 */
public class Move implements Serializable
{
	/**
	 * Sorosításhoz szükséges azonosító
	 */
	private static final long serialVersionUID = 1239854373685948372L;
	
	/**
	 * A kiinduló pozíció
	 */
	final private Position start;
	
	/**
	 * A végpozíció
	 */
	final private Position end;

	/**
	 * Konstruktor
	 * @param start kezdõpozíció
	 * @param end végpozíció
	 */
	public Move(Position start, Position end)
	{
		if (start == null || end == null)
			throw new NullPointerException();

		this.start = start;
		this.end = end;
	}

	/**
	 * Konstruktor
	 * @param startRank kezdõpozíció sora
	 * @param startFile kezdõpozíció oszlopa
	 * @param endRank végpozíció sora
	 * @param endFile végpozíció oszlopa
	 */
	public Move(int startRank, int startFile, int endRank, int endFile)
	{
		this(new Position(startRank, startFile), new Position(endRank, endFile));
	}

	/**
	 * visszaadja a kezdõpozíciót
	 * @return kezdõpozíció
	 */
	public Position getStart()
	{
		return start;
	}

	/**
	 * visszaadja a végpozíciót
	 * @return végpozíció
	 */
	public Position getEnd()
	{
		return end;
	}

	/**
	 * Visszaadja a lépés nagyságát manhattan-norma szerint
	 * @return a nagyság
	 */
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
