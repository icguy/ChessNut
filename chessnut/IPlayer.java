package chessnut;

import chessnut.logic.*;

/**
 * Ezen az interfészen keresztül érhetõek el a játékos típusú objektumok: GUI és AI
 */
public interface IPlayer
{	
	/**  Ezen tudjuk leküldeni a sakktáblát, annak minden tartozékával együtt   */
	public abstract void setChessboard(ChessBoard chessboard);   
	
	/**  Ezen tudunk gyalogváltás kérést leküldeni   */
	public abstract void notifyPromotion(Position position);
	
	/**  ILogic interfészû egységre referencia beállítása   */
	public abstract void setGameLogic( ILogic logic);          
}
