/*************************************************
 *  \file     AI.java
 *  \brief    Chessnut mesters�ges intelligencia oszt�lya
 *  \note     
 *  \date     2016. m�j. 13.
 *************************************************/
package chessnut.ai;

import chessnut.ILogic;
import chessnut.IPlayer;
import chessnut.logic.ChessBoard;
import chessnut.logic.Position;

public class AI implements IPlayer
{
	ILogic gameLogic;   //!< Ezen a referenci�n �rheti el a GameLogic-ot
	
	
	//! \brief  Konstruktor: l�trehozhat� gameLogic alapj�n
	public AI(ILogic logic)
	{
		this.gameLogic = logic;
	}
	
	
	//! \brief  Ezzel be�ll�that� a gamelogic referencia
	@Override
	public void setGameLogic(ILogic logic)
	{
		this.gameLogic = logic;
	}
	
	
	//! \brief  Sakkt�bl�t kaptam
	@Override
	public void setChessboard(ChessBoard chessboard)
	{
		System.out.println("AI handles setChessboard.");
		
		// TODO {AI} Ide kell meg�rni a kapott sakkt�bl�ra reakci�t
		
		/*
		 * Valami ilyesmi, hogy:
		 * 
		 * Megn�zem, hogy �n j�v�k-e
		 * {
		 * 		Ha �n j�v�k, megn�zem, hogy ez most az els� kattint�som lesz, vagy a m�sodik
		 *  	Ha az els� kattint�som lesz
		 *  	{
		 *  		valahogy kisz�molom az optim�lis l�p�st, (ami igaz�b�l maga az AI feladat)
		 *  		azt�n megkattintom az els�t (a kiindul� mez�t)
		 *  	}
		 *  	Ha a m�sodik kattint�som lesz
		 *  	{
		 *  		Megkattintom a m�sodik kattint�st az el�z�leg kisz�molt l�p�s alapj�n
		 *  	}
		 * }
		 */
		
	}
	
	
	//! \brief  Megk�rtek, hogy l�ptessek el� egy parasztot
	 @Override
	public void notifyPromotion(Position position)
	{
		 System.out.println("AI handles notifyPromotion.");
		 
		// TODO {AI} Paraszt el�l�ptet�st megcsin�lni
		
	}
}
