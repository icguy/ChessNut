/*************************************************
 *  \file     GameLogic.java
 *  \brief    Ez lesz a j�t�klogika oszt�ly
 *  \note     
 *  \date     2016. m�j. 12.
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
	// J�t�k elemei
	ChessBoard chessboard;     //!< Sakkt�bl�m
	IPlayer gui;               //!< Egyik j�t�kos a helyi GUI
	IPlayer opponent;          //!< M�sik j�t�kos: AI / Network
	
	private boolean gameStarted = false;        //!< Azzal kezd�dik a j�t�k, hogy az indul� t�bl�kat kik�ldtem
	
	private PlayerColor playerMakesMoveNow;     //!< Aki most kattintgat. Ezt az�rt kell itt is megjegyeznem, mert a l�p�s ut�n a chessboard-ban m�r �t fog �llni, �s �gy nem tudn�m, hogy kinek kell visszak�ldeni 
	private Position currentMoveStart = null;          //!< Folyamatban l�v� l�p�s kezdete. null, ha nincs semmi kijel�lve
	
	
	//! \brief  L�trehoz�s GUI alapj�n
	public GameLogic( IPlayer gui )
	{
		this.gui = gui;
	}
	
	//! \brief  Ezzel lehet be�ll�tani a t�loldali j�t�kosra vonatkoz� referenci�t (AI / NetworkServer)
	@Override
	public void setPlayer(IPlayer player)
	{
		this.opponent = player;
		chessboard = new ChessBoard();   // Sakkt�bl�t inicializ�lom
		sendInitialBoardToBothPlayers(); // Kik�ld�m a kezdeti sakkt�bl�kat is
	}
	
	//! \brief  Click kezel�se
	@Override
	public void click(Position position, PlayerColor player)
	{
		System.out.println("GameLogic handles click.");
		
		// Ha most nem l�p�st v�rok, hanem prom�ci�t, akkor hazamegyek
		if(chessboard.isAwaitingPromotion() == true)
		{
			return;
		}
		
		// Ha nem � j�n, akkor minek kattintgat?
		if(chessboard.getNextToMove() != player)
		{
			return;
		}
		
		// Ha nem a saj�t sz�n�re kattintott, akkor nem foglalkozok vele
		if (chessboard.getPiece(position).getColor() != chessboard.getNextToMove() )
		{
			return;
		}
		
		// Els� kattint�sa j�n:
		if( currentMoveStart == null )
		{
			// Elmentem a l�p�s kiindul� mez�j�t
			currentMoveStart = position;
			
			// Kijel�l�m a sakkt�bl�n ezt a mez�t a l�p�s kezdet�nek,
			/*
			 *  TODO - {Chessboard} hogyan? k�ne r� egy f�ggv�ny a chessboard-ban,
			 *  ami egy position-nel megadott mez�t highlightol kezdeti mez�nek az adott t�bl�n
			 */
			
			// Visszak�ld�m az adott j�t�kosnak az �j sakkt�bl�t, amelyen a kijel�l�s m�r szerepel
			SendChessboardToOne(chessboard.getNextToMove());
			
			// Els� kattint�s sikeresen megvolt, v�rom a m�sodikat
			playerMakesMoveNow = chessboard.getNextToMove();
		}
		// M�sodik kattint�sa j�n:
		{
			// Ha sikeres l�p�s volt
			if( chessboard.makeMove(new Move(this.currentMoveStart, position)))
			{
				// Kijel�l�m a sakkt�bl�n a c�l mez�t
				/*
				 * TODO - {Chessboard} - a c�l mez�t Position alapj�n kijel�l� f�ggv�ny k�ne
				 */
				
				// Kik�ld�m a l�p�st megl�p� j�t�kosnak, ahol sz�pen vil�g�tani fog, hogy mit l�pett
				SendChessboardToOne(playerMakesMoveNow);
				
				// Ha gyalogv�lt�s kell, akkor k�ld�k r� k�r�st
				if(chessboard.isAwaitingPromotion())
				{
					sendNotifyPromotionToOne(playerMakesMoveNow);
					return; // �s haza is megyek, mert nincs mit kik�ldeni, ameddig ez le nem zajlott.
				}
				
				// T�rl�m a highlight-okat a t�bl�r�l, azt a m�sik j�t�kosnak nem k�ld�m ki
				/*
				 * TODO {Chessboard} - osszes highlight-ot t�rl� f�ggv�ny megint
				 */
				
				// Kik�ld�m a m�siknak is a highlight-olatlan t�bl�t
				SendChessboardToOne(chessboard.getNextToMove());
			}
			// Ha nem volt sikeres l�p�s
			else
			{
				// �sszes highlight-ot t�rl�m a t�bl�r�l
				/*
				 * TODO {Chessboard} - osszes highlight-ot t�rl� f�ggv�ny megint megint
				 */
				
				// Kik�ld�m �jra mindk�t j�t�kosnak a t�bl�t
				sendChessboardToBoth();
			}
			
			// �jrakezdem a folyamatot
			currentMoveStart = null;
		}
		
		
	}
	
	//! \brief  Gyalog el�l�ptet�s kezel�se
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
		if (chessboard.Promote(piece))
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
	
	
	//! \brief  Egy adott j�t�kosnak t�bla kik�ld�se
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
	
	
	//! \brief  Mindk�t oldalnak kik�ld�m a sakkt�bl�t
	private void sendChessboardToBoth()
	{
		gui.setChessboard(chessboard);
		opponent.setChessboard(chessboard);
	}
	
	
	//! \brief  Egy adott j�t�kosnak gyalogv�lt�s k�r�s kik�ld�se
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
	
	
	//! \brief  Kezdeti sakkt�bl�t kik�ld�m mindk�t f�lnek - csak egyszer, az elej�n
	//! \note   K�l�n thread-b�l fut, hogy ne fagyja �ssze mag�t a v�rakoz�sba
	private void sendInitialBoardToBothPlayers()
	{
		if(!gameStarted) // Csak egyszer lehet
		{
			Thread connectionWait = new Thread(new ConnectionWaitingThread());
			connectionWait.start();
		}
	}
	
	
	//! \brief  Ez v�rja, hogy a kliens becsatlakozzon, majd kik�ldi a kezd� j�t�k�ll�st
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
				while( !((NetworkServer)opponent).isConnected() )
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
