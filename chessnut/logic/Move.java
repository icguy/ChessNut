package chessnut.logic;

import java.io.Serializable;

/**
 * 
 * Egy b�bu �thelyez�s�t (l�p�st) reprezent�l
 *
 */
public class Move implements Serializable
{
	/**
	 * Soros�t�shoz sz�ks�ges azonos�t�
	 */
	private static final long serialVersionUID = 1239854373685948372L;
	
	/**
	 * A kiindul� poz�ci�
	 */
	final private Position start;
	
	/**
	 * A v�gpoz�ci�
	 */
	final private Position end;

	/**
	 * Konstruktor
	 * @param start kezd�poz�ci�
	 * @param end v�gpoz�ci�
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
	 * @param startRank kezd�poz�ci� sora
	 * @param startFile kezd�poz�ci� oszlopa
	 * @param endRank v�gpoz�ci� sora
	 * @param endFile v�gpoz�ci� oszlopa
	 */
	public Move(int startRank, int startFile, int endRank, int endFile)
	{
		this(new Position(startRank, startFile), new Position(endRank, endFile));
	}

	/**
	 * visszaadja a kezd�poz�ci�t
	 * @return kezd�poz�ci�
	 */
	public Position getStart()
	{
		return start;
	}

	/**
	 * visszaadja a v�gpoz�ci�t
	 * @return v�gpoz�ci�
	 */
	public Position getEnd()
	{
		return end;
	}

	/**
	 * Visszaadja a l�p�s nagys�g�t manhattan-norma szerint
	 * @return a nagys�g
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
