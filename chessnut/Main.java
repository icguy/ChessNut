/*************************************************
 *  \file     Main.java
 *  \brief    Program main osztálya
 *  \note     
 *  \date     2016. máj. 12.
 *************************************************/
package chessnut;

import chessnut.ai.AI;
import chessnut.gui.*;
import chessnut.logic.*;
import chessnut.network.*;

//PIG Konzolos UI, késõbb kivenni
import chessnut.debug.*;

//! \brief  Program fõ osztálya
public class Main
{	
	private static GUI     GUI;          //!< GUI mindkét oldalon van
	private static IPlayer Opponent;     //!< Szervernél a hálózat/AI, kliensnél nincs
	private static ILogic  Logic;        //!< Szervernél a játéklogika, kliensnél a hálózat
	
	//! \brief  A program futása itt kezdõdik
	public static void main(String[] args)
	{
		GUI = new GUI();                             // GUI létrehozása
	}
	
	
	//! \brief  Szerver oldal felállítása
	public static void setupServer()
	{
		// Ez a rendes futás
		Logic = new GameLogic(GUI);                  // Játéklogikát létrehozom
		Opponent  = new NetworkServer(Logic);        // Szerver oldali hálózatot létrehozom
		GUI.setGameLogic(Logic);                     // Beállítom a GUI gamelogic-ját
		Logic.setPlayer(Opponent);                   // Beállítom a hálózatot ellenfélnek
		((NetworkServer) Opponent).connect("localhost"); // Hálózat nyitása
		
		
		// PIG Konzolos UI-al futás
		/*IPlayer ConsoleUI = new ConsolePlayerInterface();
		Logic = new GameLogic(ConsoleUI);            // Játéklogikát létrehozom
		Opponent  = new NetworkServer(Logic);        // Szerver oldali hálózatot létrehozom
		ConsoleUI.setGameLogic(Logic);               // Beállítom a GUI gamelogic-ját
		Logic.setPlayer(Opponent);                   // Beállítom a hálózatot ellenfélnek
		((NetworkServer) Opponent).connect("localhost"); // Hálózat nyitása
		*/
	}
	
	
	//! \brief  Kliens oldal felállítása
	public static void setupClient(String IP)
	{
		Logic = new NetworkClient();                  // Kliens oldali hálózat jelképezi a logikát
		Logic.setPlayer(GUI);                         // Az õ játékosa a GUI
		GUI.setGameLogic(Logic);                      // GUI logikája a hálózat
		((NetworkClient) Logic).connect(IP); // Csatlakozás
		
		// PIG Konzolos UI-al futás
		/*IPlayer ConsoleUI = new ConsolePlayerInterface();
		Logic = new NetworkClient();                  // Kliens oldali hálózat jelképezi a logikát
		Logic.setPlayer(ConsoleUI);                   // Az õ játékosa a GUI
		ConsoleUI.setGameLogic(Logic);                // GUI logikája a hálózat
		((NetworkClient) Logic).connect(IP); // Csatlakozás
		*/
	}
	
	//! \brief  SinglePlayer játék felállítása
	public static void setupSinglePlayer()
	{
		Logic = new GameLogic(GUI);                  // Játéklogikát létrehozom
		Opponent  = new AI(Logic);                   // AI az ellenfél
		GUI.setGameLogic(Logic);                     // Beállítom a GUI gamelogic-ját
		Logic.setPlayer(Opponent);                   // Beállítom az AI-t ellenfélnek
	}
}
