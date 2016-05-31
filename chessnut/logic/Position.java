package chessnut.logic;

import java.io.Serializable;

/**
 * A sakktábla egy pozícióját reprezentálja
 * A sorok (rank) 0-tól 7-ig számozódnak, a valódi táblán 1-tõl 8-ig vannak jelölve.
 * Az oszlopok (file) 0-tól 7-ig számozódnak, a valódi táblán a-tól h-ig vannak jelölve.
 */
public class Position implements Serializable
{
	/**
	 * Sorosításhoz szükséges azonosító
	 */
	private static final long serialVersionUID = 2758435244468732197L;
	
	/**
	 * A pozíció sora
	 */
	private final int rank;
	
	/**
	 * A pozíció oszlopa
	 */
	private final int file;

	/**
	 * Konstruktor
	 * @param rank A pozíció sora
	 * @param file A pozíció oszlopa
	 */
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

	/**
	 * Megvizsgálja, hogy az adott sor/oszlop kombináció egy sakktábla tartományán belülre esik el
	 * @param rank a pozíció sora
	 * @param file a pozíció oszlopa
	 * @return True, ha a megadott paraméterek létezõ pozícióra mutatnak
	 */
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
	
	/**
	 * Megrpóbál létrehozni egy position objektumot a megadott adatokból
	 * @param rank A pozíció sora
	 * @param file A pozíció oszlopa
	 * @return A létrehozott pozíció, vagy null, ha az adatok kimutatnak a sakktáblán kívülre
	 */
	static public Position tryCreate(int rank, int file)
	{
		if(!inRange(rank, file))
			return null;
		return new Position(rank, file);
	}
}
