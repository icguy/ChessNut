/*************************************************
 *  \file     GameLogic.java
 *  \brief    Ez lesz a játéklogika osztály
 *  \note     
 *  \date     2016. máj. 12.
 *************************************************/
package chessnut.logic;

import util.*;
import chessnut.ILogic;
import chessnut.IPlayer;
import chessnut.logic.Position;
import chessnut.logic.pieces.*;
import chessnut.network.NetworkServer;

public class GameLogic implements ILogic
{
	// Játék elemei
	ChessBoard chessboard;     //!< Sakktáblám
	IPlayer gui;               //!< Egyik játékos a helyi GUI
	IPlayer opponent;          //!< Másik játékos: AI / Network
	
	private boolean gameStarted = false;        //!< Azzal kezdõdik a játék, hogy az induló táblákat kiküldtem
	
	private PlayerColor playerMakesMoveNow;     //!< Aki most kattintgat. Ezt azért kell itt is megjegyeznem, mert a lépés után a chessboard-ban már át fog állni, és így nem tudnám, hogy kinek kell visszaküldeni 
	private Position currentMoveStart = null;          //!< Folyamatban lévõ lépés kezdete. null, ha nincs semmi kijelölve
	
	
	//! \brief  Létrehozás GUI alapján
	public GameLogic( IPlayer gui )
	{
		this.gui = gui;
	}
	
	//! \brief  Ezzel lehet beállítani a túloldali játékosra vonatkozó referenciát (AI / NetworkServer)
	@Override
	public void setPlayer(IPlayer player)
	{
		this.opponent = player;
		chessboard = new ChessBoard();   // Sakktáblát inicializálom
		sendInitialBoardToBothPlayers(); // Kiküldöm a kezdeti sakktáblákat is
	}
	
	//! \brief  Click kezelése
	@Override
	public void click(Position position, PlayerColor player)
	{
		System.out.println("GameLogic handles click.");
		
		// Ha most nem lépést várok, hanem promóciót, akkor hazamegyek
		if(chessboard.isAwaitingPromotion() == true)
		{
			return;
		}
		
		// Ha nem õ jön, akkor minek kattintgat?
		if(chessboard.getNextToMove() != player)
		{
			return;
		}
		
		// Ha nem a saját színére kattintott, akkor nem foglalkozok vele
		if (chessboard.getPiece(position).getColor() != chessboard.getNextToMove() )
		{
			return;
		}
		
		// Elsõ kattintása jön:
		if( currentMoveStart == null )
		{
			// Elmentem a lépés kiinduló mezõjét
			currentMoveStart = position;
			
			// Kijelölöm a sakktáblán ezt a mezõt a lépés kezdetének,
			/*
			 *  TODO - {Chessboard} hogyan? kéne rá egy függvény a chessboard-ban,
			 *  ami egy position-nel megadott mezõt highlightol kezdeti mezõnek az adott táblán
			 */
			
			// Visszaküldöm az adott játékosnak az új sakktáblát, amelyen a kijelölés már szerepel
			SendChessboardToOne(chessboard.getNextToMove());
			
			// Elsõ kattintás sikeresen megvolt, várom a másodikat
			playerMakesMoveNow = chessboard.getNextToMove();
		}
		// Második kattintása jön:
		{
			// Ha sikeres lépés volt
			if( chessboard.makeMove(new Move(this.currentMoveStart, position)))
			{
				// Kijelölöm a sakktáblán a cél mezõt
				/*
				 * TODO - {Chessboard} - a cél mezõt Position alapján kijelölõ függvény kéne
				 */
				
				// Kiküldöm a lépést meglépõ játékosnak, ahol szépen világítani fog, hogy mit lépett
				SendChessboardToOne(playerMakesMoveNow);
				
				// Ha gyalogváltás kell, akkor küldök rá kérést
				if(chessboard.isAwaitingPromotion())
				{
					sendNotifyPromotionToOne(playerMakesMoveNow);
					return; // És haza is megyek, mert nincs mit kiküldeni, ameddig ez le nem zajlott.
				}
				
				// Törlöm a highlight-okat a tábláról, azt a másik játékosnak nem küldöm ki
				/*
				 * TODO {Chessboard} - osszes highlight-ot törlõ függvény megint
				 */
				
				// Kiküldöm a másiknak is a highlight-olatlan táblát
				SendChessboardToOne(chessboard.getNextToMove());
			}
			// Ha nem volt sikeres lépés
			else
			{
				// Összes highlight-ot törlöm a tábláról
				/*
				 * TODO {Chessboard} - osszes highlight-ot törlõ függvény megint megint
				 */
				
				// Kiküldöm újra mindkét játékosnak a táblát
				sendChessboardToBoth();
			}
			
			// Újrakezdem a folyamatot
			currentMoveStart = null;
		}
		
		
	}
	
	//! \brief  Gyalog elõléptetés kezelése
	@Override
	public void promote(Piece piece)
	{		
		System.out.println("GameLogic handles promote.");
		
		// Ha nem is várok promóciót, akkor nem csinálok semmit
		if(chessboard.isAwaitingPromotion() == false)
		{
			return;
		}
		
		// Ha sikeres promóció történt
		if (chessboard.Promote(piece))
		{
			chessboard.clearHighlightSelection();			
			sendChessboardToBoth();
		}
		else // Ha nem sikerült, kiküldöm újra a kérést a gyalogváltásra
		{
			System.out.println("Promotion not successful.");
			sendNotifyPromotionToOne(chessboard.getNextToMove());
		}
	}
	
	
	//! \brief  Egy adott játékosnak tábla kiküldése
	private void SendChessboardToOne(PlayerColor color)
	{
		if( color == PlayerColor.White )
		{
			gui.setChessboard(chessboard);
		}
		else if( color == PlayerColor.Black )
		{
			opponent.setChessboard(chessboard);
		}
		else
		{
			System.err.println("I don't even know who comes next.");
		}
	}
	
	
	//! \brief  Mindkét oldalnak kiküldöm a sakktáblát
	private void sendChessboardToBoth()
	{
		gui.setChessboard(chessboard);
		opponent.setChessboard(chessboard);
	}
	
	
	//! \brief  Egy adott játékosnak gyalogváltás kérés kiküldése
	private void sendNotifyPromotionToOne(PlayerColor color)
	{
		if( color == PlayerColor.White )
		{
			gui.notifyPromotion(chessboard.getPromotionPos());
		}
		else if( color == PlayerColor.Black )
		{
			opponent.notifyPromotion(chessboard.getPromotionPos());
		}
		else
		{
			System.err.println("I don't even know who comes next.");
		}
	}
	
	
	//! \brief  Kezdeti sakktáblát kiküldöm mindkét félnek - csak egyszer, az elején
	//! \note   Külön thread-bõl fut, hogy ne fagyja össze magát a várakozásba
	private void sendInitialBoardToBothPlayers()
	{
		if(!gameStarted) // Csak egyszer lehet
		{
			Thread connectionWait = new Thread(new ConnectionWaitingThread());
			connectionWait.start();
		}
	}
	
	
	//! \brief  Ez várja, hogy a kliens becsatlakozzon, majd kiküldi a kezdõ játékállást
	private class ConnectionWaitingThread implements Runnable
	{
		@Override
		public void run()
		{
			wait waitingCycle = new wait();
			
			// Ha hálózatba vagyok
			if(opponent instanceof NetworkServer)
			{
				// Várok, amíg a kliens becsatlakozik
				while( !((NetworkServer)opponent).isConnected() )
				{
					waitingCycle.waitSec(1);
				}
			}
			
			// Mehet ki a tábla
			sendChessboardToBoth();
			gameStarted = true;
		}
	}
	
}
