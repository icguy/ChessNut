/*************************************************
 *  \file     Main.java
 *  \brief    Program main oszt�lya
 *  \note     
 *  \date     2016. m�j. 12.
 *************************************************/
package chessnut;

import chessnut.ai.AI;
import chessnut.gui.*;
import chessnut.logic.*;
import chessnut.network.*;

//PIG teszt, k�s�bb kivenni
//import unittest.networktest.NetworkTestClient;
//import unittest.networktest.NetworkTestServer;

//! \brief  Program f� oszt�lya
public class Main
{	
	// PIG ezek k�s�bb priv�tak lesznek, ha m�r kell rajta teszteknek futni
	public static GUI     GUI;          //!< GUI mindk�t oldalon van
	public static IPlayer Opponent;     //!< Szervern�l a h�l�zat/AI, kliensn�l nincs
	public static ILogic  Logic;        //!< Szervern�l a j�t�klogika, kliensn�l a h�l�zat
	
	//! \brief  A program fut�sa itt kezd�dik
	public static void main(String[] args)
	{
		GUI = new GUI();                             // GUI l�trehoz�sa
	}
	
	
	//! \brief  Szerver oldal fel�ll�t�sa
	public static void setupServer()
	{
		Logic = new GameLogic(GUI);                  // J�t�klogik�t l�trehozom
		Opponent  = new NetworkServer(Logic);        // Szerver oldali h�l�zatot l�trehozom
		GUI.setGameLogic(Logic);                     // Be�ll�tom a GUI gamelogic-j�t
		Logic.setPlayer(Opponent);                   // Be�ll�tom a h�l�zatot ellenf�lnek
		((NetworkServer) Opponent).connect("localhost"); // H�l�zat nyit�sa
		
		
		// PIG teszt: k�s�bb kivenni
		//NetworkTestServer nwTest = new NetworkTestServer();
		//nwTest.start();     // Teszt ind�t�sa
	}
	
	
	//! \brief  Kliens oldal fel�ll�t�sa
	public static void setupClient(String IP)
	{
		Logic = new NetworkClient();                  // Kliens oldali h�l�zat jelk�pezi a logik�t
		Logic.setPlayer(GUI);                         // Az � j�t�kosa a GUI
		GUI.setGameLogic(Logic);                      // GUI logik�ja a h�l�zat
		((NetworkClient) Logic).connect(IP); // Csatlakoz�s
		
		// PIG tesztek
		//NetworkTestClient nwTest = new NetworkTestClient();
		//nwTest.start();    // Teszt ind�t�sa
	}
	
	//! \brief  SinglePlayer j�t�k fel�ll�t�sa
	public static void setupSinglePlayer()
	{
		Logic = new GameLogic(GUI);                  // J�t�klogik�t l�trehozom
		Opponent  = new AI(Logic);                   // AI az ellenf�l
		GUI.setGameLogic(Logic);                     // Be�ll�tom a GUI gamelogic-j�t
		Logic.setPlayer(Opponent);                   // Be�ll�tom az AI-t ellenf�lnek
	}
}
