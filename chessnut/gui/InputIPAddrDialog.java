/*************************************************
 *  \file     InputIPAddrDialog.java
 *  \brief    IP címet bekérõ szövegdobozos ablak
 *  \note     Csak kliens oldalon.
 *  \date     2016. máj. 14.
 *************************************************/
package chessnut.gui;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

//! \brief  Ez az osztály maga a felugró ablak
public class InputIPAddrDialog extends JFrame
{
	private static final long serialVersionUID = 1532472210101010188L; //!< Egyedi magicnumber a sorositashoz
	String IPAddr;  //!< IP címet ebbe fogjuk kapni
	
	//! \brief  Konstruktor
	public InputIPAddrDialog()
	{
		// Alapvetõ beállítások
		super();                                 // Létrejön az ablak
		setSize(300, 140);                       // Ablakméret beállítása
		setDefaultCloseOperation(EXIT_ON_CLOSE); // Alapértelmezett kilépés
		setLayout(null);                         // Layout
		
		
		// Szövegdoboz beadja a sztringet
		IPAddr = (String) JOptionPane.showInputDialog(this,
				"Server IP:\n",
				"", JOptionPane.PLAIN_MESSAGE);

		setVisible(true);               // Láthatóvá teszem az ablakot
	}
	
	//! \brief  Ezzel kérdezhetjük meg az IP címet
	String getIp()
	{
		// Ha már van mit visszaadni, akkor eltüntetem ezt az ablakot
		if(IPAddr != null)
		{
			this.setVisible(false);
		}
		
		return this.IPAddr;
	}
}
