package chessnut.logic.pieces;

import java.io.Serializable;
import java.util.ArrayList;
import chessnut.logic.*;

/**
 * 
 * Egy sakkb�but reprezent�l� absztrakt oszt�ly
 *
 */
public abstract class Piece implements Serializable
{
	/**
	 * Soros�t�shoz sz�ks�ges azonos�t�
	 * */
	private static final long serialVersionUID = 2758435245622732188L;  //!< Egyedi magicnumber a soros�t�shoz
	
	/**
	 * A b�bu sz�ne
	 */
	protected final PlayerColor color;

	/**
	 * Konstruktor
	 * @param color A b�bu sz�ne
	 */
	public Piece(PlayerColor color)
	{
		this.color = color;
	}

	public PlayerColor getColor()
	{
		return color;
	}

	/**
	 * Lem�solja a b�but.
	 * @return A m�solat
	 */
	public abstract Piece clone();
	
	/**
	 * Megadja a b�bu �ltal megtehet� k�vetkez� l�p�seket.
	 * Csak a t�bbi b�bu poz�ci�j�t veszi figyelembe, �s a b�bu l�p�si szab�lyait.
	 * Nem veszi figyelembe pl. a sakkba l�p�st 
	 *  
	 * @param pos A b�bu poz�ci�ja
	 * @param board A sakkt�bla, amin a l�p�st meg kell tennie
	 * @return A lehets�ges l�p�sek list�ja
	 */
	public abstract ArrayList<Move> getPossibleMoves(Position pos, ChessBoard board);
		
	/**
	 * Egy adott ir�nyban megtehet� l�p�seket ad vissza.
	 * Adott poz�ci�k�l�nbs�ggel l�peget, �s a t�bla sz�l�ig, vagy az els� b�buig adja vissza a l�p�seket.
	 * Ha az el�rt b�bu azonos sz�n� ezzel a b�buval, akkor nincs a l�p�sek k�z�tt (oda nem l�phet),
	 * viszont ha ellens�ges b�bu, akkor le�theti, teh�t szerepel a l�p�sek k�z�tt.
	 * 
	 * P�ld�ul a fut� �tl�s l�p�s�hez a poz�ci�k�l�nbs�g 1 l�p�s balra/jobbra, �s 1 l�p�s f�l/le.
	 * 
	 * @param pos A b�bu poz�ci�ja.
	 * @param board A sakkt�bla
	 * @param moves A lista, amihez hozz�ad�sra ker�lnek a l�p�sek
	 * @param rankDir A poz�ci�k�l�nbs�g sor-komponense
	 * @param fileDir A poz�ci�k�l�nbs�g oszlop-komponense
	 */
	protected final void addMovesInDirection(Position pos, ChessBoard board,
			ArrayList<Move> moves, int rankDir, int fileDir)
	{
		int rank = pos.getRank();
		int file = pos.getFile();
		for (int i = 1;; i++)
		{
			Position currPos = Position.tryCreate(
					rank + i * rankDir,
					file + i * fileDir);
			if (currPos == null)
				break;

			Piece currPiece = board.getPiece(currPos);
			if (currPiece == null)
			{
				moves.add(new Move(pos, currPos));
			}
			else if (currPiece.getColor() != color)
			{
				moves.add(new Move(pos, currPos));
				break;
			}
			else
			{
				break;
			}
		}
	}
}
