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
	private Position currentMoveStart = null;          //!< Folyamatban lévõ lépés kezdete. null, ha nincs semmi kijelölve

	//! \brief  Létrehozás GUI alapján
	public GameLogic(IPlayer gui)
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

		// Elsõ kattintása jön:
		if(currentMoveStart == null)
		{
			firstClick(position);
		}
		else // Második kattintása jön:
		{
			secondClick(position);
		}
	}

	private void firstClick(Position position)
	{
		// Ha nem a saját színére kattintott, akkor nem foglalkozok vele
		if(chessboard.getPiece(position).getColor() != chessboard.getNextToMove())
		{
			return;
		}

		// Elmentem a lépés kiinduló mezõjét
		currentMoveStart = position;

		// Kijelölöm a sakktáblán ezt a mezõt, és a lehetséges célmezõket
		chessboard.selectHighlightSquare(position);

		// Visszaküldöm az adott játékosnak az új sakktáblát, amelyen a kijelölés már szerepel
		SendChessboardToOne(chessboard.getNextToMove());
	}

	private void secondClick(Position position)
	{
		PlayerColor playerMakesMoveNow = chessboard.getNextToMove();

		// Ha sikeres lépés volt
		if(chessboard.makeMove(new Move(this.currentMoveStart, position)))
		{
			chessboard.clearHighlightSelection();

			// Kiküldöm a lépést meglépõ játékosnak
			SendChessboardToOne(playerMakesMoveNow);

			// Ha gyalogváltás kell, akkor küldök rá kérést
			if(chessboard.isAwaitingPromotion())
			{
				sendNotifyPromotionToOne(playerMakesMoveNow);
				return; // És haza is megyek, mert nincs mit kiküldeni, ameddig ez le nem zajlott.
			}
			else
			{
				// Kiküldöm a másiknak is a highlight-olatlan táblát
				SendChessboardToOne(chessboard.getNextToMove());
				playerMakesMoveNow = chessboard.getNextToMove();
			}
		}
		else // Ha nem volt sikeres lépés
		{
			chessboard.clearHighlightSelection();

			// Kiküldöm újra a játékosnak a táblát
			SendChessboardToOne(chessboard.getNextToMove());
		}

		// Újrakezdem a folyamatot
		currentMoveStart = null;
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
		if(chessboard.Promote(piece))
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
		if(color == PlayerColor.White)
		{
			gui.setChessboard(chessboard);
		}
		else if(color == PlayerColor.Black)
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
		if(color == PlayerColor.White)
		{
			gui.notifyPromotion(chessboard.getPromotionPos());
		}
		else if(color == PlayerColor.Black)
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
				while(!((NetworkServer) opponent).isConnected())
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
