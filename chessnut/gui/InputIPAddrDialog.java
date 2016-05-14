/*************************************************
 *  \file     InputIPAddrDialog.java
 *  \brief    IP c�met bek�r� sz�vegdobozos ablak
 *  \note     Csak kliens oldalon.
 *  \date     2016. m�j. 14.
 *************************************************/
package chessnut.gui;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

//! \brief  Ez az oszt�ly maga a felugr� ablak
public class InputIPAddrDialog extends JFrame
{
	private static final long serialVersionUID = 1532472210101010188L; //!< Egyedi magicnumber a sorositashoz
	String IPAddr;  //!< IP c�met ebbe fogjuk kapni
	
	//! \brief  Konstruktor
	public InputIPAddrDialog()
	{
		// Alapvet� be�ll�t�sok
		super();                                 // L�trej�n az ablak
		setSize(300, 140);                       // Ablakm�ret be�ll�t�sa
		setDefaultCloseOperation(EXIT_ON_CLOSE); // Alap�rtelmezett kil�p�s
		setLayout(null);                         // Layout
		
		
		// Sz�vegdoboz beadja a sztringet
		IPAddr = (String) JOptionPane.showInputDialog(this,
				"Server IP:\n",
				"", JOptionPane.PLAIN_MESSAGE);

		setVisible(true);               // L�that�v� teszem az ablakot
	}
	
	//! \brief  Ezzel k�rdezhetj�k meg az IP c�met
	String getIp()
	{
		// Ha m�r van mit visszaadni, akkor elt�ntetem ezt az ablakot
		if(IPAddr != null)
		{
			this.setVisible(false);
		}
		
		return this.IPAddr;
	}
}
