package chessnut.logic;

import util.*;
import chessnut.ILogic;
import chessnut.IPlayer;
import chessnut.logic.Position;
import chessnut.logic.pieces.*;
import chessnut.network.NetworkServer;

/**
 * J�t�klogika oszt�lya
 */
public class GameLogic implements ILogic
{
	// J�t�k elemei
	/**  Sakkt�bl�m   */
	ChessBoard chessboard;
	
	/**  Egyik j�t�kos a helyi GUI   */
	IPlayer gui;
	
	/**  M�sik j�t�kos az AI/Network   */
	IPlayer opponent;

	/**  Azzal kezd�dik a j�t�k, hogy az indul� t�bl�kat kik�ldtem   */
	private boolean gameStarted = false;
	
	/**  Folyamatban l�v� l�p�s kezdete. null, ha nincs semmi kijel�lve   */
	private Position currentMoveStart = null; 

	

	/**
	 * L�trehoz�s GUI alapj�n
	 * @param gui: amire a referencia be�ll�t�dik
	 */
	public GameLogic(IPlayer gui)
	{
		this.gui = gui;
	}


	/**
	 * Ezzel lehet be�ll�tani a t�loldali j�t�kosra vonatkoz� referenci�t (AI / NetworkServer)
	 * @param player: akire a referencia be�ll�t�dik
	 */
	@Override
	public void setPlayer(IPlayer player)
	{
		this.opponent = player;
		chessboard = new ChessBoard();   // Sakkt�bl�t inicializ�lom
		sendInitialBoardToBothPlayers(); // Kik�ld�m a kezdeti sakkt�bl�kat is
	}

	
	/**
	 * J�t�klogika a click-et kezeli
	 */
	@Override
	public void click(Position position, PlayerColor player)
	{
		System.out.println("GameLogic handles " + player.toString() + " player's click: " + position);
		
		// Ha most nem l�p�st v�rok, hanem prom�ci�t, akkor hazamegyek
		if(chessboard.isAwaitingPromotion() == true)
		{
			System.out.println("Click not accepted: promotion is in progress.");
			return;
		}

		// Ha nem � j�n, akkor minek kattintgat?
		if(chessboard.getNextToMove() != player)
		{
			System.out.println("Click not accepted: It's not the player's turn.");
			return;
		}

		// Els� kattint�sa j�n:
		if(currentMoveStart == null)
		{
			firstClick(position);
		}
		else // M�sodik kattint�sa j�n:
		{
			secondClick(position);
		}
	}

	
	/**
	 * Els� click kezel�se
	 */
	private void firstClick(Position position)
	{
		// Ha olyan helyre kattintott, ahol nincs is b�bu
		if(chessboard.getPiece(position) == null)
		{
			System.out.println("Click not accepted: no chesspiece");
			return;
		}
		
		// Ha nem a saj�t sz�n�re kattintott, akkor nem foglalkozok vele
		if(chessboard.getPiece(position).getColor() != chessboard.getNextToMove())
		{
			System.out.println("Click not accepted: selected piece is not his color.");
			return;
		}

		// Elmentem a l�p�s kiindul� mez�j�t
		currentMoveStart = position;

		// Kijel�l�m a sakkt�bl�n ezt a mez�t, �s a lehets�ges c�lmez�ket
		chessboard.selectHighlightSquare(position);

		// Visszak�ld�m az adott j�t�kosnak az �j sakkt�bl�t, amelyen a kijel�l�s m�r szerepel
		SendChessboardToOne(chessboard.getNextToMove());
	}

	/**
	 * M�sodik click kezel�se
	 */
	private void secondClick(Position position)
	{
		PlayerColor playerMakesMoveNow = chessboard.getNextToMove();

		// Ha sikeres l�p�s volt
		if(chessboard.makeMove(new Move(this.currentMoveStart, position)))
		{
			chessboard.clearHighlightSelection();

			// Kik�ld�m a l�p�st megl�p� j�t�kosnak
			SendChessboardToOne(playerMakesMoveNow);

			// Ha gyalogv�lt�s kell, akkor k�ld�k r� k�r�st
			if(chessboard.isAwaitingPromotion())
			{
				sendNotifyPromotionToOne(playerMakesMoveNow);
			}
			else
			{
				// Kik�ld�m a m�siknak is a highlight-olatlan t�bl�t
				SendChessboardToOne(chessboard.getNextToMove());
			}
		}
		else // Ha nem volt sikeres l�p�s
		{
			chessboard.clearHighlightSelection();

			// Kik�ld�m �jra a j�t�kosnak a t�bl�t
			System.out.println("Invalid move! Try again.");
			SendChessboardToOne(chessboard.getNextToMove());
		}

		// �jrakezdem a folyamatot
		currentMoveStart = null;
	}


	/**
	 * Gyalog el�l�ptet�s kezel�se
	 */
	@Override
	public void promote(Piece piece)
	{
		System.out.println("GameLogic handles promote.");

		// Ha nem is v�rok prom�ci�t, akkor nem csin�lok semmit
		if(chessboard.isAwaitingPromotion() == false)
		{
			return;
		}

		// Ha sikeres prom�ci� t�rt�nt
		if(chessboard.Promote(piece))
		{
			chessboard.clearHighlightSelection();
			sendChessboardToBoth();
		}
		else // Ha nem siker�lt, kik�ld�m �jra a k�r�st a gyalogv�lt�sra
		{
			System.out.println("Promotion not successful.");
			sendNotifyPromotionToOne(chessboard.getNextToMove());
		}
	}


	/**
	 * Egy adott j�t�kosnak sakkt�bla kik�ld�se
	 * @param color: a j�t�kos sz�ne, aki t�bl�t kap
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
	 * Mindk�t oldalnak sakkt�bla kik�ld�se
	 */
	private void sendChessboardToBoth()
	{
		gui.setChessboard(chessboard);
		opponent.setChessboard(chessboard);
	}


	/**
	 * Egy adott j�t�kosnak gyalogv�lt�s k�r�s kik�ld�se
	 * @param color: a j�t�kos, aki kap
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
	 * Kezdeti sakkt�bl�t kik�ld�m mindk�t f�lnek - csak egyszer, az elej�n
	 * K�l�n thread-b�l fut, hogy ne fagyja �ssze mag�t a v�rakoz�sba.
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
	 * Ez v�rja, hogy a kliens becsatlakozzon, majd kik�ldi a kezd� j�t�k�ll�st
	 */
	private class ConnectionWaitingThread implements Runnable
	{
		@Override
		public void run()
		{
			wait waitingCycle = new wait();

			// Ha h�l�zatba vagyok
			if(opponent instanceof NetworkServer)
			{
				// V�rok, am�g a kliens becsatlakozik
				while(!((NetworkServer) opponent).isConnected())
				{
					waitingCycle.waitSec(1);
				}
			}

			// Mehet ki a t�bla
			sendChessboardToBoth();
			gameStarted = true;
		}
	}

}
