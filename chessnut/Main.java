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

//PIG Konzolos UI, k�s�bb kivenni
import chessnut.debug.*;

//! \brief  Program f� oszt�lya
public class Main
{	
	private static GUI     GUI;          //!< GUI mindk�t oldalon van
	private static IPlayer Opponent;     //!< Szervern�l a h�l�zat/AI, kliensn�l nincs
	private static ILogic  Logic;        //!< Szervern�l a j�t�klogika, kliensn�l a h�l�zat
	
	//! \brief  A program fut�sa itt kezd�dik
	public static void main(String[] args)
	{
		GUI = new GUI();                             // GUI l�trehoz�sa
	}
	
	
	//! \brief  Szerver oldal fel�ll�t�sa
	public static void setupServer()
	{
		// Ez a rendes fut�s
		Logic = new GameLogic(GUI);                  // J�t�klogik�t l�trehozom
		Opponent  = new NetworkServer(Logic);        // Szerver oldali h�l�zatot l�trehozom
		GUI.setGameLogic(Logic);                     // Be�ll�tom a GUI gamelogic-j�t
		Logic.setPlayer(Opponent);                   // Be�ll�tom a h�l�zatot ellenf�lnek
		((NetworkServer) Opponent).connect("localhost"); // H�l�zat nyit�sa
		
		
		// PIG Konzolos UI-al fut�s
		/*IPlayer ConsoleUI = new ConsolePlayerInterface();
		Logic = new GameLogic(ConsoleUI);            // J�t�klogik�t l�trehozom
		Opponent  = new NetworkServer(Logic);        // Szerver oldali h�l�zatot l�trehozom
		ConsoleUI.setGameLogic(Logic);               // Be�ll�tom a GUI gamelogic-j�t
		Logic.setPlayer(Opponent);                   // Be�ll�tom a h�l�zatot ellenf�lnek
		((NetworkServer) Opponent).connect("localhost"); // H�l�zat nyit�sa
		*/
	}
	
	
	//! \brief  Kliens oldal fel�ll�t�sa
	public static void setupClient(String IP)
	{
		Logic = new NetworkClient();                  // Kliens oldali h�l�zat jelk�pezi a logik�t
		Logic.setPlayer(GUI);                         // Az � j�t�kosa a GUI
		GUI.setGameLogic(Logic);                      // GUI logik�ja a h�l�zat
		((NetworkClient) Logic).connect(IP); // Csatlakoz�s
		
		// PIG Konzolos UI-al fut�s
		/*IPlayer ConsoleUI = new ConsolePlayerInterface();
		Logic = new NetworkClient();                  // Kliens oldali h�l�zat jelk�pezi a logik�t
		Logic.setPlayer(ConsoleUI);                   // Az � j�t�kosa a GUI
		ConsoleUI.setGameLogic(Logic);                // GUI logik�ja a h�l�zat
		((NetworkClient) Logic).connect(IP); // Csatlakoz�s
		*/
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
