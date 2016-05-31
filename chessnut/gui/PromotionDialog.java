package chessnut.gui;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

/** 
 * Gyalogváltás kérelem oszály.
 */
public class PromotionDialog extends JFrame
{
	private static final long serialVersionUID = 3017906404975522665L;
	
	/** Választott figura */
	String selectedValue;

	/**
	 * Konstruktor
	 */
	public PromotionDialog() 
	{
		/** Ablak alapvetõ beállításai */
		super();
		setSize(400, 140);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(null);
		
		/** Választási lehetõségek felkínálása és kiválaszása */
		String[] possibleValues = { "Bishop", "Knight", "Queen", "Rook" };
		selectedValue = (String) JOptionPane.showInputDialog(null,
				"Choose one", "Promotion Dialog",
				JOptionPane.INFORMATION_MESSAGE, null,
				possibleValues, possibleValues[0]);
		
		setVisible(true);		
	}
	
	/**
	 * Gyalogváltás kérelem eredményének lekérdezése
	 * @return selectedValue: visszaadja a kiválasztott elemet
	 */
	String getChosenOne()
	{
		// Ha már van mit visszaadni, akkor eltüntetem ezt az ablakot
		
		if(selectedValue != null)
		{
			this.setVisible(false);
		}
		
		return this.selectedValue;
	}

}
