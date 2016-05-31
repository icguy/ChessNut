package chessnut.gui;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

/** 
 * Gyalogv�lt�s k�relem osz�ly.
 */
public class PromotionDialog extends JFrame
{
	private static final long serialVersionUID = 3017906404975522665L;
	
	/** V�lasztott figura */
	String selectedValue;

	/**
	 * Konstruktor
	 */
	public PromotionDialog() 
	{
		/** Ablak alapvet� be�ll�t�sai */
		super();
		setSize(400, 140);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(null);
		
		/** V�laszt�si lehet�s�gek felk�n�l�sa �s kiv�lasz�sa */
		String[] possibleValues = { "Bishop", "Knight", "Queen", "Rook" };
		selectedValue = (String) JOptionPane.showInputDialog(null,
				"Choose one", "Promotion Dialog",
				JOptionPane.INFORMATION_MESSAGE, null,
				possibleValues, possibleValues[0]);
		
		setVisible(true);		
	}
	
	/**
	 * Gyalogv�lt�s k�relem eredm�ny�nek lek�rdez�se
	 * @return selectedValue: visszaadja a kiv�lasztott elemet
	 */
	String getChosenOne()
	{
		// Ha m�r van mit visszaadni, akkor elt�ntetem ezt az ablakot
		
		if(selectedValue != null)
		{
			this.setVisible(false);
		}
		
		return this.selectedValue;
	}

}
