package chessnut;

import chessnut.logic.*;
import chessnut.logic.pieces.Piece;

/**
 * Ezen az interfészen keresztül elérhetõ a játéklogika.
 */
public interface ILogic
{
	/**  Player/AI általi kattintás ezen jut fel a logikához   */
	void click(Position position, PlayerColor player);     
	
	/**  Player/AI általi gyalogváltás ezen jut fel a logikához   */
	void promote(Piece piece);                             
	
	/**  IPlayer interfészû elemre referencia beállítása   */
	public abstract void setPlayer( IPlayer player );      
}