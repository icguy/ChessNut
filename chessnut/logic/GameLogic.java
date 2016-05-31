package chessnut.logic;

import util.*;
import chessnut.ILogic;
import chessnut.IPlayer;
import chessnut.logic.Position;
import chessnut.logic.pieces.*;
import chessnut.network.NetworkServer;

/**
 * Játéklogika osztálya
 */
public class GameLogic implements ILogic
{
	// Játék elemei
	/**  Sakktáblám   */
	ChessBoard chessboard;
	
	/**  Egyik játékos a helyi GUI   */
	IPlayer gui;
	
	/**  Másik játékos az AI/Network   */
	IPlayer opponent;

	/**  Azzal kezdõdik a játék, hogy az induló táblákat kiküldtem   */
	private boolean gameStarted = false;
	
	/**  Folyamatban lévõ lépés kezdete. null, ha nincs semmi kijelölve   */
	private Position currentMoveStart = null; 

	

	/**
	 * Létrehozás GUI alapján
	 * @param gui: amire a referencia beállítódik
	 */
	public GameLogic(IPlayer gui)
	{
		this.gui = gui;
	}


	/**
	 * Ezzel lehet beállítani a túloldali játékosra vonatkozó referenciát (AI / NetworkServer)
	 * @param player: akire a referencia beállítódik
	 */
	@Override
	public void setPlayer(IPlayer player)
	{
		this.opponent = player;
		chessboard = new ChessBoard();   // Sakktáblát inicializálom
		sendInitialBoardToBothPlayers(); // Kiküldöm a kezdeti sakktáblákat is
	}

	
	/**
	 * Játéklogika a click-et kezeli
	 */
	@Override
	public void click(Position position, PlayerColor player)
	{
		System.out.println("GameLogic handles " + player.toString() + " player's click: " + position);
		
		// Ha most nem lépést várok, hanem promóciót, akkor hazamegyek
		if(chessboard.isAwaitingPromotion() == true)
		{
			System.out.println("Click not accepted: promotion is in progress.");
			return;
		}

		// Ha nem õ jön, akkor minek kattintgat?
		if(chessboard.getNextToMove() != player)
		{
			System.out.println("Click not accepted: It's not the player's turn.");
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

	
	/**
	 * Elsõ click kezelése
	 */
	private void firstClick(Position position)
	{
		// Ha olyan helyre kattintott, ahol nincs is bábu
		if(chessboard.getPiece(position) == null)
		{
			System.out.println("Click not accepted: no chesspiece");
			return;
		}
		
		// Ha nem a saját színére kattintott, akkor nem foglalkozok vele
		if(chessboard.getPiece(position).getColor() != chessboard.getNextToMove())
		{
			System.out.println("Click not accepted: selected piece is not his color.");
			return;
		}

		// Elmentem a lépés kiinduló mezõjét
		currentMoveStart = position;

		// Kijelölöm a sakktáblán ezt a mezõt, és a lehetséges célmezõket
		chessboard.selectHighlightSquare(position);

		// Visszaküldöm az adott játékosnak az új sakktáblát, amelyen a kijelölés már szerepel
		SendChessboardToOne(chessboard.getNextToMove());
	}

	/**
	 * Második click kezelése
	 */
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
			}
			else
			{
				// Kiküldöm a másiknak is a highlight-olatlan táblát
				SendChessboardToOne(chessboard.getNextToMove());
			}
		}
		else // Ha nem volt sikeres lépés
		{
			chessboard.clearHighlightSelection();

			// Kiküldöm újra a játékosnak a táblát
			System.out.println("Invalid move! Try again.");
			SendChessboardToOne(chessboard.getNextToMove());
		}

		// Újrakezdem a folyamatot
		currentMoveStart = null;
	}


	/**
	 * Gyalog elõléptetés kezelése
	 */
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


	/**
	 * Egy adott játékosnak sakktábla kiküldése
	 * @param color: a játékos színe, aki táblát kap
	 */
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


	/**
	 * Mindkét oldalnak sakktábla kiküldése
	 */
	private void sendChessboardToBoth()
	{
		gui.setChessboard(chessboard);
		opponent.setChessboard(chessboard);
	}


	/**
	 * Egy adott játékosnak gyalogváltás kérés kiküldése
	 * @param color: a játékos, aki kap
	 */
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


	/**
	 * Kezdeti sakktáblát kiküldöm mindkét félnek - csak egyszer, az elején
	 * Külön thread-bõl fut, hogy ne fagyja össze magát a várakozásba.
	 */
	private void sendInitialBoardToBothPlayers()
	{
		if(!gameStarted) // Csak egyszer lehet
		{
			Thread connectionWait = new Thread(new ConnectionWaitingThread());
			connectionWait.start();
		}
	}


	/**
	 * Ez várja, hogy a kliens becsatlakozzon, majd kiküldi a kezdõ játékállást
	 */
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
