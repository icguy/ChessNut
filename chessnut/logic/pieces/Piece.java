package chessnut.logic.pieces;

import java.io.Serializable;
import java.util.ArrayList;
import chessnut.logic.*;

/**
 * 
 * Egy sakkbábut reprezentáló absztrakt osztály
 *
 */
public abstract class Piece implements Serializable
{
	/**
	 * Sorosításhoz szükséges azonosító
	 * */
	private static final long serialVersionUID = 2758435245622732188L;  //!< Egyedi magicnumber a sorosításhoz
	
	/**
	 * A bábu színe
	 */
	protected final PlayerColor color;

	/**
	 * Konstruktor
	 * @param color A bábu színe
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
	 * Lemásolja a bábut.
	 * @return A másolat
	 */
	public abstract Piece clone();
	
	/**
	 * Megadja a bábu által megtehetõ következõ lépéseket.
	 * Csak a többi bábu pozícióját veszi figyelembe, és a bábu lépési szabályait.
	 * Nem veszi figyelembe pl. a sakkba lépést 
	 *  
	 * @param pos A bábu pozíciója
	 * @param board A sakktábla, amin a lépést meg kell tennie
	 * @return A lehetséges lépések listája
	 */
	public abstract ArrayList<Move> getPossibleMoves(Position pos, ChessBoard board);
		
	/**
	 * Egy adott irányban megtehetõ lépéseket ad vissza.
	 * Adott pozíciókülönbséggel lépeget, és a tábla széléig, vagy az elsõ bábuig adja vissza a lépéseket.
	 * Ha az elért bábu azonos színû ezzel a bábuval, akkor nincs a lépések között (oda nem léphet),
	 * viszont ha ellenséges bábu, akkor leütheti, tehát szerepel a lépések között.
	 * 
	 * Például a futó átlós lépéséhez a pozíciókülönbség 1 lépés balra/jobbra, és 1 lépés föl/le.
	 * 
	 * @param pos A bábu pozíciója.
	 * @param board A sakktábla
	 * @param moves A lista, amihez hozzáadásra kerülnek a lépések
	 * @param rankDir A pozíciókülönbség sor-komponense
	 * @param fileDir A pozíciókülönbség oszlop-komponense
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
