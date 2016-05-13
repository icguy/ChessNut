/*************************************************
 *  \file     AI.java
 *  \brief    Chessnut mesterséges intelligencia osztálya
 *  \note     
 *  \date     2016. máj. 13.
 *************************************************/
package chessnut.ai;

import chessnut.ILogic;
import chessnut.IPlayer;
import chessnut.logic.ChessBoard;
import chessnut.logic.Position;

public class AI implements IPlayer
{
	ILogic gameLogic;   //!< Ezen a referencián érheti el a GameLogic-ot
	
	
	//! \brief  Konstruktor: létrehozható gameLogic alapján
	public AI(ILogic logic)
	{
		this.gameLogic = logic;
	}
	
	
	//! \brief  Ezzel beállítható a gamelogic referencia
	@Override
	public void setGameLogic(ILogic logic)
	{
		this.gameLogic = logic;
	}
	
	
	//! \brief  Sakktáblát kaptam
	@Override
	public void setChessboard(ChessBoard chessboard)
	{
		System.out.println("AI handles setChessboard.");
		
		// TODO {AI} Ide kell megírni a kapott sakktáblára reakciót
		
		/*
		 * Valami ilyesmi, hogy:
		 * 
		 * Megnézem, hogy én jövök-e
		 * {
		 * 		Ha én jövök, megnézem, hogy ez most az elsõ kattintásom lesz, vagy a második
		 *  	Ha az elsõ kattintásom lesz
		 *  	{
		 *  		valahogy kiszámolom az optimális lépést, (ami igazából maga az AI feladat)
		 *  		aztán megkattintom az elsõt (a kiinduló mezõt)
		 *  	}
		 *  	Ha a második kattintásom lesz
		 *  	{
		 *  		Megkattintom a második kattintást az elõzõleg kiszámolt lépés alapján
		 *  	}
		 * }
		 */
		
	}
	
	
	//! \brief  Megkértek, hogy léptessek elõ egy parasztot
	 @Override
	public void notifyPromotion(Position position)
	{
		 System.out.println("AI handles notifyPromotion.");
		 
		// TODO {AI} Paraszt elõléptetést megcsinálni
		
	}
}
