package chessnut;

import chessnut.logic.*;
import chessnut.logic.pieces.Piece;

/**
 * Ezen az interf�szen kereszt�l el�rhet� a j�t�klogika.
 */
public interface ILogic
{
	/**  Player/AI �ltali kattint�s ezen jut fel a logik�hoz   */
	void click(Position position, PlayerColor player);     
	
	/**  Player/AI �ltali gyalogv�lt�s ezen jut fel a logik�hoz   */
	void promote(Piece piece);                             
	
	/**  IPlayer interf�sz� elemre referencia be�ll�t�sa   */
	public abstract void setPlayer( IPlayer player );      
}