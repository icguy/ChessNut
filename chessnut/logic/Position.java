package chessnut.logic;

import java.io.Serializable;

/**
 * A sakkt�bla egy poz�ci�j�t reprezent�lja
 * A sorok (rank) 0-t�l 7-ig sz�moz�dnak, a val�di t�bl�n 1-t�l 8-ig vannak jel�lve.
 * Az oszlopok (file) 0-t�l 7-ig sz�moz�dnak, a val�di t�bl�n a-t�l h-ig vannak jel�lve.
 */
public class Position implements Serializable
{
	/**
	 * Soros�t�shoz sz�ks�ges azonos�t�
	 */
	private static final long serialVersionUID = 2758435244468732197L;
	
	/**
	 * A poz�ci� sora
	 */
	private final int rank;
	
	/**
	 * A poz�ci� oszlopa
	 */
	private final int file;

	/**
	 * Konstruktor
	 * @param rank A poz�ci� sora
	 * @param file A poz�ci� oszlopa
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
	 * Megvizsg�lja, hogy az adott sor/oszlop kombin�ci� egy sakkt�bla tartom�ny�n bel�lre esik el
	 * @param rank a poz�ci� sora
	 * @param file a poz�ci� oszlopa
	 * @return True, ha a megadott param�terek l�tez� poz�ci�ra mutatnak
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
	 * Megrp�b�l l�trehozni egy position objektumot a megadott adatokb�l
	 * @param rank A poz�ci� sora
	 * @param file A poz�ci� oszlopa
	 * @return A l�trehozott poz�ci�, vagy null, ha az adatok kimutatnak a sakkt�bl�n k�v�lre
	 */
	static public Position tryCreate(int rank, int file)
	{
		if(!inRange(rank, file))
			return null;
		return new Position(rank, file);
	}
}
