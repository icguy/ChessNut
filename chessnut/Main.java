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

//PIG teszt, késõbb kivenni
//import unittest.networktest.NetworkTestClient;
//import unittest.networktest.NetworkTestServer;

//! \brief  Program fõ osztálya
public class Main
{	
	// PIG ezek késõbb privátak lesznek, ha már kell rajta teszteknek futni
	public static GUI     GUI;          //!< GUI mindkét oldalon van
	public static IPlayer Opponent;     //!< Szervernél a hálózat/AI, kliensnél nincs
	public static ILogic  Logic;        //!< Szervernél a játéklogika, kliensnél a hálózat
	
	//! \brief  A program futása itt kezdõdik
	public static void main(String[] args)
	{
		GUI = new GUI();                             // GUI létrehozása
	}
	
	
	//! \brief  Szerver oldal felállítása
	public static void setupServer()
	{
		Logic = new GameLogic(GUI);                  // Játéklogikát létrehozom
		Opponent  = new NetworkServer(Logic);        // Szerver oldali hálózatot létrehozom
		GUI.setGameLogic(Logic);                     // Beállítom a GUI gamelogic-ját
		Logic.setPlayer(Opponent);                   // Beállítom a hálózatot ellenfélnek
		((NetworkServer) Opponent).connect("localhost"); // Hálózat nyitása
		
		
		// PIG teszt: késõbb kivenni
		//NetworkTestServer nwTest = new NetworkTestServer();
		//nwTest.start();     // Teszt indítása
	}
	
	
	//! \brief  Kliens oldal felállítása
	public static void setupClient(String IP)
	{
		Logic = new NetworkClient();                  // Kliens oldali hálózat jelképezi a logikát
		Logic.setPlayer(GUI);                         // Az õ játékosa a GUI
		GUI.setGameLogic(Logic);                      // GUI logikája a hálózat
		((NetworkClient) Logic).connect(IP); // Csatlakozás
		
		// PIG tesztek
		//NetworkTestClient nwTest = new NetworkTestClient();
		//nwTest.start();    // Teszt indítása
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
